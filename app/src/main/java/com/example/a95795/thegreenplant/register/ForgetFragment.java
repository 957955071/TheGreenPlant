package com.example.a95795.thegreenplant.register;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.custom.MyApplication;
import com.example.a95795.thegreenplant.custom.Phone;
import com.example.a95795.thegreenplant.custom.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mob.MobSDK;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import me.yokeyword.fragmentation.SupportFragment;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgetFragment extends SupportFragment {

    private Button mSend, button, confirm;
    private EditText editText, editText2, password, passwordTwo;
    private String phone, code, Password, PasswordTwo;
    private RelativeLayout relativeLayout1, relativeLayout2, relativeLayout3, relativeLayout4;

    public static ForgetFragment newInstance() {
        return new ForgetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget, container, false);
        init(view);
        return view;
    }

    //初始化
    public void init(View view) {
        MobSDK.init(ForgetFragment.this.getActivity());
        editText = (EditText) view.findViewById(R.id.phonenumber_test);
        editText2 = (EditText) view.findViewById(R.id.codeeditText);
        button = (Button) view.findViewById(R.id.next);
        mSend = (Button) view.findViewById(R.id.btn);
        password = (EditText) view.findViewById(R.id.pressword);
        passwordTwo = (EditText) view.findViewById(R.id.presswordTwo);
        confirm = (Button) view.findViewById(R.id.confirm);
        confirm.setVisibility(View.GONE);
        relativeLayout1 = (RelativeLayout) view.findViewById(R.id.relativeLayout1);
        relativeLayout2 = (RelativeLayout) view.findViewById(R.id.relativeLayout2);
        relativeLayout3 = (RelativeLayout) view.findViewById(R.id.relativeLayout3);
        relativeLayout4 = (RelativeLayout) view.findViewById(R.id.relativeLayout4);
        relativeLayout3.setVisibility(View.GONE);
        relativeLayout4.setVisibility(View.GONE);
        button.setEnabled(false);
        button.setBackground(getResources().getDrawable(R.drawable.code_no));

        send();
        next();
        confirm();
    }

    //按下  下一步按钮
    public void next() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = editText2.getText().toString();
                phone = editText.getText().toString();
                SMSSDK.submitVerificationCode("86", phone, code);

            }
        });
    }

    public void confirm() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password = password.getText().toString();
                PasswordTwo = passwordTwo.getText().toString();
                if (!rexCheckPassword(Password)) {
                    password.setError("你输入的密码不合法，请重新输入");
                } else if (Password.equals("")) {
                    password.setError("密码不能为空");
                } else if (PasswordTwo.equals("")) {
                    passwordTwo.setError("密码不能为空");
                } else if (Password.equals(PasswordTwo)) {
                    forget();
                } else {
                    passwordTwo.setError("两次密码不相同");
                }
            }
        });
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
                            Gson gson = new Gson();
                            String UserList;
                            UserList = response.getString("UserList");
                            if (UserList.equals("0")) {
                                editText.setError("此手机号不存在本系统，请检查后输入");
                            } else {
                                relativeLayout1.setVisibility(View.GONE);
                                relativeLayout2.setVisibility(View.GONE);
                                relativeLayout3.setVisibility(View.VISIBLE);
                                relativeLayout4.setVisibility(View.VISIBLE);
                                button.setVisibility(View.GONE);
                                confirm.setVisibility(View.VISIBLE);
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

    public void forget() {
        phone = editText.getText().toString();
        Password = password.getText().toString();
        String url = getString(R.string.ip) + "user/UserPasswordUpdata";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                "{\n" +
                        "\t \"userCall\": \""+phone+"\",\n" +
                        "\t \"userPassword\": \""+Password+"\"\n" +
                        "}}",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ForgetFragment.this.getActivity(),"修改密码成功，稍后为您跳转到登录页面",Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                             pop();
                            }
                        }, 3000);    //延时3s执行
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
                if (phone.equals("")) {
                    editText.setError("请输入手机号");
                } else if (phone.length() != 11) {
                    editText.setError("手机长度不合法");
                } else if (!validatePhoneNumber(phone)) {
                    editText.setError("请输入正确的手机号");
                } else {
                    jt_code();
                    code();
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
    private void code() {
        final int count = 60;
        Observable.interval(0, 1, TimeUnit.SECONDS)//设置0延迟，每隔一秒发送一条数据
                .take(count + 1)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return count - aLong; //
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
                        mSend.setText("剩余时间 " + aLong + " 秒");
                        mSend.setTextColor(Color.WHITE);

                    }
                });

    }

    // 使用完EventHandler需注销，否则可能出现内存泄漏
    public void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    //监听验证码反馈下一步
    public void jt_code() {
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
                            Toast.makeText(ForgetFragment.this.getActivity(), "已发送了4位数验证码，请注意查收", Toast.LENGTH_LONG).show();
                        } else {
                            // TODO 处理错误的结果
                            ((Throwable) data).printStackTrace();
                            Log.d("11111111", "handleMessage: "+data);
                            Toast.makeText(ForgetFragment.this.getActivity(), "服务端出现问题，请稍后再试", Toast.LENGTH_LONG).show();
                        }
                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            // TODO 处理验证码验证通过的结果

                            judge();
                        } else {
                            // TODO 处理错误的结果
                            Toast.makeText(ForgetFragment.this.getActivity(), "验证码错误", Toast.LENGTH_LONG).show();
                            ((Throwable) data).printStackTrace();
                        }
                    }
                    // TODO 其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                    return false;
                }
            }).sendMessage(msg);
        }
    };

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

}
