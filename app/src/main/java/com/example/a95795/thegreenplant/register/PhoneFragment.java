package com.example.a95795.thegreenplant.register;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.custom.Mac;
import com.example.a95795.thegreenplant.custom.MyApplication;
import com.example.a95795.thegreenplant.custom.Phone;
import com.google.gson.Gson;
import com.mob.MobSDK;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import me.yokeyword.fragmentation.SupportFragment;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

public class PhoneFragment extends SupportFragment {

    private Button mSend,button;
    private EditText editText,editText2;
    private String phone,code,Mac;
    public static PhoneFragment newInstance() {
        return new PhoneFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_phone, container, false);
        Mac = getMac();
        init(view);   send();next();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new SweetAlertDialog(PhoneFragment.this.getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("提示")
                        .setContentText("手机号和本手机将会与您的身份进行绑定，请务必确认此手机为您的常用手机")
                        .setConfirmText("我明白了")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog
                                        .setTitleText("提示")
                                        .setContentText("一个手机号加一台手机只能注册一个账号，更多规则请查看【使用条款】")
                                        .setConfirmText("我明白了")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        })
                        .show();
            }
        }, 1000);    //延时2s执行


        return view;
    }
    //初始化
    public  void init(View view){
        MobSDK.init(PhoneFragment.this.getActivity());
        editText = (EditText) view.findViewById(R.id.phonenumber_test);
        editText2 = (EditText) view.findViewById(R.id.codeeditText);
        button = (Button) view.findViewById(R.id.next);
        mSend = (Button) view.findViewById(R.id.btn);
        button.setEnabled(false);
        button.setBackground(getResources().getDrawable(R.drawable.code_no));
    }
    //按下  下一步按钮
    public void next(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = editText2.getText().toString();
                phone = editText.getText().toString();
                SMSSDK.submitVerificationCode("86", phone, code);

            }
        });
    }
    public void mac(){
        phone = editText.getText().toString();
        String url = getString(R.string.ip) + "user/FindUserMac";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                "{\n" +
                        "\t\"userCall\": \""+phone+"\",\n" +
                        "\t \"userMac\": \""+Mac+"\"\n" +
                        "\t\n" +
                        "}",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String UserList;
                            UserList = response.getString("UserList");
                            if (UserList.equals("0")) {
                                jt_code(); code();

                            } else {
                                Toast.makeText(PhoneFragment.this.getActivity(),"本手机已经被注册过，如有疑问请于管理员联系",Toast.LENGTH_LONG).show();

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
    //判断手机号是否存在于本公司
    public void judge() {
        phone = editText.getText().toString();
        String url = getString(R.string.ip) + "user/UserFindPhone";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                "{\n" +
                        "\t \"userCall\": \"" + phone + "\"\n" +
                        "\t\n" +
                        "}",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String UserList;
                            UserList = response.getString("UserList");
                            if (UserList.equals("0")) {
                                mac();
                            } else {
                                Toast.makeText(PhoneFragment.this.getActivity(),"此手机号已经注册过，请勿重复注册",Toast.LENGTH_LONG).show();

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
    //发送验证码按钮
    private void send() {
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = editText.getText().toString();
              if (phone.equals("")){
                  editText.setError("请输入手机号");
              }else if(phone.length()!=11) {
                  editText.setError("手机长度不合法");
              }else if(!validatePhoneNumber(phone)){
                  editText.setError("请输入正确的手机号");
              }else {
                  judge();
                  EventBus.getDefault().postSticky(new Phone(phone,Mac));
              }
            }
        });
    }
    //正则判断手机号码正确与否
    public static boolean validatePhoneNumber(String mobiles) {
        String telRegex = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$";
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }
    /*开启验证码倒计时*/
    private void code(){
    final int count = 60;
    Observable.interval(0, 1, TimeUnit.SECONDS)//设置0延迟，每隔一秒发送一条数据
            .take(count+1)
            .map(new Func1<Long, Long>() {
                @Override
                public Long call(Long aLong) {
                    return count-aLong; //
                }
            })
            .doOnSubscribe(new Action0() {
                @Override
                public void call() {
                    mSend.setEnabled(false);//在发送数据的时候设置为不能点击
                    mSend.setBackground(getResources().getDrawable(R.drawable.code_no));
                    SMSSDK.registerEventHandler(eventHandler);
                    phone = editText.getText().toString();
                    SMSSDK.getVerificationCode("86", phone);
                }
            })

            .observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
            .subscribe(new Observer<Long>() {
                @Override
                public void onCompleted() {
                    mSend.setText("获取验证码");//数据发送完后设置为原来的文字
                    mSend.setTextColor(Color.BLACK);
                    mSend.setEnabled(true);
                    mSend.setBackground(getResources().getDrawable(R.drawable.code));//数据发送完后设置为原来背景色
                }
                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }
                @Override
                public void onNext(Long aLong) { //接受到一条就是会操作一次UI
                    mSend.setText("剩余时间 "+aLong+" 秒");
                    mSend.setTextColor(Color.WHITE);

                }
            });

}
    // 使用完EventHandler需注销，否则可能出现内存泄漏
    public void onDestroy()
    {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }



    //监听验证码反馈下一步
    public void jt_code(){
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 4) {
                    button.setEnabled(true);
                    button.setBackground(getResources().getDrawable(R.drawable.code));
                } else {
                    button.setEnabled(false);
                    button.setBackground(getResources().getDrawable(R.drawable.code_no));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    //验证码规则
    EventHandler eventHandler = new EventHandler() {
        public void afterEvent(int event, int result, Object data) {
            // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            new Handler(Looper.getMainLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            // TODO 处理成功得到验证码的结果
                            Toast.makeText(PhoneFragment.this.getActivity(),"已发送了4位数验证码，请注意查收",Toast.LENGTH_LONG).show();
                        } else {
                            // TODO 处理错误的结果
                            ((Throwable) data).printStackTrace();
                            Toast.makeText(PhoneFragment.this.getActivity(),"服务端出现问题，请稍后再试",Toast.LENGTH_LONG).show();
                        }
                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            // TODO 处理验证码验证通过的结果
                            start(MessageFragment.newInstance());
                        } else {
                            // TODO 处理错误的结果
                            Toast.makeText(PhoneFragment.this.getActivity(),"验证码错误",Toast.LENGTH_LONG).show();
                            ((Throwable) data).printStackTrace();
                        }
                    }
                    // TODO 其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                    return false;
                }
            }).sendMessage(msg);
        }
    };
    public static String getMac() {
        String macSerial = "";
        try {
            LineNumberReader input = new LineNumberReader(new InputStreamReader(Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address").getInputStream()));
            while (true) {
                String line = input.readLine();
                if (line == null) {
                    break;
                }
                macSerial = macSerial + line.trim();
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(macSerial)) {
            return getMacAddr();
        }
        return macSerial;
    }
    public static String getMacAddr() {
        try {
            for (NetworkInterface nif : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }
                    StringBuilder res1 = new StringBuilder();
                    int length = macBytes.length;
                    for (int i = 0; i < length; i++) {
                        res1.append(String.format("%02X:", new Object[]{Byte.valueOf(macBytes[i])}));
                    }
                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }
        } catch (Exception e) {
        }
        return getMacFinal();
    }
    public static String getMacFinal() {
        String str = "";
        String macSerial = "";
        try {
            LineNumberReader input = new LineNumberReader(new InputStreamReader(Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ").getInputStream()));
            while (str != null) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if ("".equals(macSerial)) {
            try {
                macSerial = loadFileAsString("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return macSerial;
    }
    public static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }
    public static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }


}
