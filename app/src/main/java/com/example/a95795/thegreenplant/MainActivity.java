package com.example.a95795.thegreenplant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.a95795.thegreenplant.custom.AndroidWorkaround;
import com.example.a95795.thegreenplant.custom.Id;
import com.example.a95795.thegreenplant.custom.MyApplication;
import com.example.a95795.thegreenplant.register.EndMessageFragment;
import com.example.a95795.thegreenplant.Login.LoginFragment;
import com.example.a95795.thegreenplant.register.MessageFragment;
import com.example.a95795.thegreenplant.register.PhoneFragment;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends SupportActivity  {
    private FragmentTransaction mFragmentTransaction;//fragment事务
    private FragmentManager mFragmentManager;//fragment管理者
    private MessageFragment messageFragment;
    private PhoneFragment phonefragment;
    private EndMessageFragment endMessageFragment;
    private LoginFragment loginFragment;
    private String num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        web();
        loadRootFragment(R.id.Home, LoginFragment.newInstance()); //activity初始加载LogonFragment


        //适配三大虚拟键 避免遮盖控件
        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
        }




    }
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向动画
        return new DefaultHorizontalAnimator();
    }

    //简单的更新fragment
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.Home, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    //网络是否正确
    public void web(){
        num = "0";
        String url = getString(R.string.ip) + "user/Web";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String UserList;
                            UserList = response.getString("UserList");
                            if (UserList.equals("1")) {
                                SharedPreferences rememberUser = getSharedPreferences("login", MODE_PRIVATE);//获取模式
                                SharedPreferences.Editor edit = rememberUser.edit();
                                edit.putString("name", "1");
                                edit.commit();

                            } else {
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
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        String name_str = sharedPreferences.getString("name", "");
        num = name_str;
        if(num.equals("1")){
        }else {
            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("提示")
                    .setContentText("您的网络出现问题，请检查网络设置！")
                    .setConfirmText("确定")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Intent intent =  new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .show();
        }
    }
    //显示和隐藏fragment
    public void setClick(int type) {
        //开启事务
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //显示之前将所有的fragment都隐藏起来,在去显示我们想要显示的fragment
        hideFragment(transaction);
        switch (type) {
            case 0:
                if (phonefragment == null) {
                    phonefragment = new PhoneFragment();
                    //加入事务
                    transaction.add(R.id.Home, phonefragment);
                } else {
                    transaction.show(messageFragment);
                }
                break;
            case 1:
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    //加入事务
                    transaction.add(R.id.Home, messageFragment);
                } else {
                    transaction.show(messageFragment);
                }
                break;
            case 2:
                if (endMessageFragment == null) {
                    endMessageFragment = new EndMessageFragment();
                    //加入事务
                    transaction.add(R.id.Home, endMessageFragment);
                } else {
                    transaction.show(endMessageFragment);
                }
                break;
            case 3:
                if (loginFragment == null) {
                    loginFragment = new LoginFragment();
                    //加入事务
                    transaction.add(R.id.Home, loginFragment);
                } else {
                    transaction.show(loginFragment);
                }
                break;
        }
        //提交事务
        transaction.commit();
    }

    public void hideFragment(FragmentTransaction fragmentTransaction) {
        //如果此fragment不为空的话就隐藏起来
        if (messageFragment != null) {
            fragmentTransaction.hide(messageFragment);
        }
        if (phonefragment != null) {
            fragmentTransaction.hide(phonefragment);
        }
        if (endMessageFragment != null) {
            fragmentTransaction.hide(endMessageFragment);
        }
        if (loginFragment != null) {
            fragmentTransaction.hide(loginFragment);
        }
    }
    @Override
    public void onBackPressedSupport() {
        if(getSupportFragmentManager().getBackStackEntryCount() <= 1){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示：");
            builder.setMessage("您确定退出？");
            //设置确定按钮
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            //设置取消按钮
            builder.setPositiveButton("取消", null);
            //显示提示框
            builder.show();
        } else
            getSupportFragmentManager().popBackStack();

    }


}
