package com.example.a95795.thegreenplant.register;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.a95795.thegreenplant.MainActivity;
import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.custom.Mac;
import com.example.a95795.thegreenplant.custom.Message;
import com.example.a95795.thegreenplant.custom.MyApplication;
import com.example.a95795.thegreenplant.custom.Phone;
import com.example.a95795.thegreenplant.custom.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.swipebackfragment.SwipeBackFragment;


public class MessageFragment extends SupportFragment {

    private EditText name,ID,job_number;
    private String Id,Job_Number,Phone,Mac;
    private Button button;
    private int id;
    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

       init(view);messageButton();
        EventBus.getDefault().register(this);
        return view;
    }
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(Phone phone) {
       Phone = phone.getNumber();
       Mac = phone.getMac();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }
    //提交按钮
    public void messageButton(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Id = ID.getText().toString();
                Job_Number = job_number.getText().toString();
                if (Job_Number.equals("")){
                    job_number.setError("工号为空");
                }else if (Id.equals("")){
                    ID.setError("身份证为空");
                }else if(Id.length() != 18){
                    ID.setError("身份证长度不合法");
                }else if(!isIDNumber(Id)) {
                    ID.setError("身份证不合法");
                }else {
                    acquire();
                }
            }
        });
    }
    //初始化
    public void init(View view){
        ID = (EditText) view.findViewById(R.id.ID_EditText);
        job_number = (EditText) view.findViewById(R.id.phonenumber_test);
        button = (Button) view.findViewById(R.id.next);
    }
    //验证工号是否存在
    public void acquire(){

        String url = getString(R.string.ip)+"user/userfindnumber";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                "{\n" +
                        "\t\"userId\":"+Job_Number+"\n" +
                        "}",
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Gson gson = new Gson();
                            String UserList;
                            UserList = response.getString("UserList");
                            if(UserList.equals("0")){
                                job_number.setError("工号不存在");
                            }else {
                                List<User> subjectList = gson.fromJson(response.getJSONArray("UserList").toString(),new TypeToken<List<User>>(){}.getType());
                                id = subjectList.get(0).getId();
                                Log.d("11111111", "onResponse: "+Id+Job_Number+Phone);
                                EventBus.getDefault().postSticky(new Message(Id,id,Job_Number,Phone,Mac));
                                start(EndMessageFragment.newInstance());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley",error.toString());
                    }
                }
        );
        MyApplication.addRequest(jsonObjectRequest,"MainActivity");
    }
    //正则验证是否为合法身份证
    public static boolean isIDNumber(String IDNumber) {
        if (IDNumber == null || "".equals(IDNumber)) {
            return false;
        }
        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
        String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";

        boolean matches = IDNumber.matches(regularExpression);

        if (matches) {

            if (IDNumber.length() == 18) {
                try {
                    char[] charArray = IDNumber.toCharArray();
                    //前十七位加权因子
                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                    //这是除以11后，可能产生的11位余数对应的验证码
                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                    int sum = 0;
                    for (int i = 0; i < idCardWi.length; i++) {
                        int current = Integer.parseInt(String.valueOf(charArray[i]));
                        int count = current * idCardWi[i];
                        sum += count;
                    }
                    char idCardLast = charArray[17];
                    int idCardMod = sum % 11;
                    if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("异常:" + IDNumber);
                    return false;
                }
            }

        }
        return matches;
    }
    //正则验证是否为汉字
    public static boolean isChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find())
            flg = true;

        return flg;
    }

}
