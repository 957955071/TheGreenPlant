package com.example.a95795.thegreenplant.register;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.a95795.thegreenplant.Login.LoginFragment;
import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.custom.Message;
import com.example.a95795.thegreenplant.custom.Message_all;
import com.example.a95795.thegreenplant.custom.MyApplication;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.yokeyword.fragmentation.SupportFragment;


public class EndMessageFragment extends SupportFragment {

    private EditText password, passwordAffirm, userId;
    private String Password, PasswordAffirm, ID, Job_Number, Phone, UserId,Mac;
    private Button button;

    public static EndMessageFragment newInstance() {
        return new EndMessageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_end_message, container, false);
        init(view);
        endButton();
        EventBus.getDefault().register(this);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(Message message) {
        ID = message.getId();
        Job_Number = message.getJob_Number();
        Phone = message.getPhone();
        Mac = message.getMac();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }

    //提交按钮
    public void endButton() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password = password.getText().toString();
                PasswordAffirm = passwordAffirm.getText().toString();
                UserId = userId.getText().toString();
                if (UserId.equals("")) {
                    userId.setError("账号不能为空");
                } else if (UserId.length() < 8) {
                    userId.setError("请输入不低于8位数的账号");
                } else if (!rexCheckPassword(Password)) {
                    password.setError("你输入的密码不合法，请重新输入");
                } else if (Password.equals("")) {
                    password.setError("密码不能为空");
                } else if (PasswordAffirm.equals("")) {
                    passwordAffirm.setError("密码不能为空");
                } else if (Password.equals(PasswordAffirm)) {
                    register();
                } else {
                    passwordAffirm.setError("两次密码不相同");
                }
            }
        });
    }

    /**
     * 正则表达式验证密码
     *
     * @param input
     * @return
     */
    public static boolean rexCheckPassword(String input) {
// 6-20 位，字母、数字、字符
//String reg = "^([A-Z]|[a-z]|[0-9]|[`-=[];,./~!@#$%^*()_+}{:?]){6,20}$";
        String regStr = "^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）――+|{}【】‘；：”“'。，、？]){6,20}$";
        return input.matches(regStr);
    }

    //初始化
    public void init(View view) {
        password = (EditText) view.findViewById(R.id.pressword);
        passwordAffirm = (EditText) view.findViewById(R.id.presswordTwo);
        userId = (EditText) view.findViewById(R.id.userid);
        button = (Button) view.findViewById(R.id.end);
    }

    //注册
    public void register() {
        String url = getString(R.string.ip) + "user/useradd";
        Gson gson = new Gson();
        Message_all message_all = new Message_all();
        message_all.setUserCall(Phone);
        message_all.setUserId(Job_Number);
        message_all.setUserIdentity(ID);
        message_all.setUserMac(Mac);
        message_all.setUserNum(UserId);
        message_all.setUserPassword(Password);
      String json=  gson.toJson(message_all);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String UserList;
                            UserList = response.getString("UserList");
                            if (UserList.equals("1")) {
                                userId.setError("用户名重复，请重新输入");
                            } else {
                                new SweetAlertDialog(EndMessageFragment.this.getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setContentText("您已注册成功，请妥善保管好自己的账号，点击 [确定] 按钮后将为您跳转到登录页面")
                                        .setConfirmText("确定")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        popTo(PhoneFragment.class, true);
                                                    }
                                                }, 1000);    //延时1s执行
                                            }
                                        })
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", error.toString());
                    }
                }
        );
        MyApplication.addRequest(jsonObjectRequest, "MainActivity");
    }
}
