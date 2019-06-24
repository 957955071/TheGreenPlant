package com.example.a95795.thegreenplant.HomeFragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.eminayar.panter.DialogType;
import com.eminayar.panter.PanterDialog;
import com.eminayar.panter.interfaces.OnTextInputConfirmListener;
import com.example.a95795.thegreenplant.MainActivity;
import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.custom.MyApplication;
import com.example.a95795.thegreenplant.custom.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.yokeyword.fragmentation.SupportFragment;
import rm.com.longpresspopup.LongPressPopup;
import rm.com.longpresspopup.LongPressPopupBuilder;
import rm.com.longpresspopup.PopupInflaterListener;
import rm.com.longpresspopup.PopupOnHoverListener;
import rm.com.longpresspopup.PopupStateListener;
import ru.katso.livebutton.LiveButton;

public class Information extends SupportFragment implements PopupInflaterListener,
        PopupStateListener, PopupOnHoverListener, View.OnClickListener{

    private ImageView mHBack;
    private ImageView mHHead;

    private ItemView mNickName;
    private ItemView mSex;
    private ItemView mTelephone;
    private ItemView mJob;
    private ItemView mWorkshop;
    private LiveButton exit;
    private TextView info;
    private TextView info2;
    private TextView info3;
    private TextView info4;
    private int ID;
    String name,userid,id,time,num;
    private AlertDialog.Builder pwdBuilder;
    private AlertDialog pwdDialog;
    Button count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_information, null);
    }
    public static Information newInstance() {
        return new Information();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        set();
        setData();
        initView();
        pop();

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder localBuilder = new AlertDialog.Builder(getContext());
                localBuilder.setMessage("是否退出？");
                localBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
                    {
                        Intent intent = new Intent(Information.this.getActivity(), MainActivity.class);
                               startActivity(intent);
                           getActivity().finish();
                    }
                });
                localBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
                    {

                    }
                });


                localBuilder.setCancelable(true).create();

                localBuilder.show();
            }
        });

    }

    public void set(){
        mHBack = (ImageView) getActivity().findViewById(R.id.h_back);
        mHHead = (ImageView) getActivity().findViewById(R.id.h_head);
        //下面item控件
        mNickName = (ItemView) getActivity().findViewById(R.id.nickName);
        mSex = (ItemView) getActivity().findViewById(R.id.sex);
        mTelephone = (ItemView) getActivity().findViewById(R.id.telephone);
        mJob = (ItemView) getActivity().findViewById(R.id.job);
        mWorkshop = (ItemView) getActivity().findViewById(R.id.workshop);
        exit=getActivity().findViewById(R.id.ex);
    }

    private void setData() {
        //设置背景磨砂效果
        Glide.with(this).load(R.drawable.head)
                .bitmapTransform(new BlurTransformation(getContext(), 25), new CenterCrop(getContext()))
                .into(mHBack);
        //设置圆形图像
        Glide.with(this).load(R.drawable.head)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(mHHead);

        //设置用户名整个item的点击事件
        mNickName.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
        mSex.setShowRightArrow(false);
        mJob.setShowRightArrow(false);
        mWorkshop.setShowRightArrow(false);
        //修改用户名item的左侧图标
      /* mNickName.setLeftIcon(R.drawable.ic_phone);
        //
        mNickName.setLeftTitle("修改后的用户名");
        mNickName.setRightDesc("名字修改");
        mNickName.setShowRightArrow(false);
        mNickName.setShowBottomLine(false);

        //设置用户名整个item的点击事件
        mNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "我是onclick事件显示的", Toast.LENGTH_SHORT).show();
            }
        });
*/

        mTelephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialgoView = View.inflate(getContext(), R.layout.phone, null); //初始化输入对话框的布局
                //   设置登录密码
                pwdBuilder = new AlertDialog.Builder(getContext());
                pwdBuilder.setTitle("请输入想要修改的手机号码");
                pwdBuilder.setView(dialgoView);
                //因为点击取消关闭dialog，我直接这样写
                pwdBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                //现在builder中这样写确定按钮
                pwdBuilder.setPositiveButton("确定",null);
                //创建dialog
                pwdDialog=pwdBuilder.create();
                //dialog点击其他地方不关闭
                pwdDialog.setCancelable(false);
                pwdDialog.show();
                //创建dialog点击监听OnClickListener
                pwdDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText edit_text = dialgoView.findViewById(R.id.editText);
                        String text = edit_text.getText().toString();
                        if (text.equals("")){
                            Toast.makeText(getContext(),"请输入手机号",Toast.LENGTH_LONG).show();
                        }else if(text.length()!=11) {
                            Toast.makeText(getContext(),"手机长度不合法",Toast.LENGTH_LONG).show();
                        }else if(!validatePhoneNumber(text)){
                            Toast.makeText(getContext(),"请输入正确的手机号",Toast.LENGTH_LONG).show();
                        }else{
                            pwdDialog.cancel();
                            final View dialgoView = View.inflate(getContext(), R.layout.checknum, null); //初始化输入对话框的布局
                            //   设置登录密码
                            count=dialgoView.findViewById(R.id.button2);
                            count.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CountDownTimer timer = new CountDownTimer(60000, 1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            count.setEnabled(false);
                                            count.setText("已发送(" + millisUntilFinished / 1000 + ")");

                                        }
                                        @Override
                                        public void onFinish() {
                                            count.setEnabled(true);
                                            count.setText("重新获取");

                                        }
                                    }.start();
                                }
                            });
                            pwdBuilder = new AlertDialog.Builder(getContext());
                            pwdBuilder.setTitle("请输入短信验证码");
                            pwdBuilder.setView(dialgoView);
                            //因为点击取消关闭dialog，我直接这样写
                            pwdBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            //现在builder中这样写确定按钮
                            pwdBuilder.setPositiveButton("确定",null);
                            //创建dialog
                            pwdDialog=pwdBuilder.create();
                            //dialog点击其他地方不关闭
                            pwdDialog.setCancelable(false);
                            pwdDialog.show();
                            //创建dialog点击监听OnClickListener
                            pwdDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                        }
                    }
                }
                );
            }
        });



    }

    private void initView() {
        Context ctx = Information.this.getActivity();
        SharedPreferences sp = ctx.getSharedPreferences("SP", Context.MODE_PRIVATE);
        ID = sp.getInt("STRING_KEY", 0);
        String url = getString(R.string.ip)+"user/userfindid";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                "{\n" +
                        "\t \"id\": "+ID+"\n" +
                        "}",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("volley", response.toString());
                        try {
                            Gson gson = new Gson();
                            List<User> users =  gson.fromJson(response.getString("UserList"), new TypeToken<List<User>>() {
                            }.getType());
                            mNickName.setRightDesc(users.get(0).getUserName());
                            Log.d("11111111", "initView: "+users.get(0).getUserName());
                            mTelephone.setRightDesc(users.get(0).getUserCall().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                            if(users.get(0).getUserWork()==0){
                                mJob.setRightDesc("生产员");
                                //mWorkshop.setRightDesc("生产员无管理车间");
                                mWorkshop.setLeftTitle("工作车间");
                                mWorkshop.setRightDesc(users.get(0).getUserWorkshop()+"号车间");
                            }else{
                                mJob.setRightDesc("管理员");
                                mWorkshop.setRightDesc(users.get(0).getUserWorkshop()+"号车间");
                            }
                            if (users.get(0).getUserSex()==0) {
                                mSex.setRightDesc("男");
                                Glide.with(getContext()).load(R.drawable.man)
                                        .bitmapTransform(new BlurTransformation(getContext(), 25), new CenterCrop(getContext()))
                                        .into(mHBack);
                                //设置圆形图像
                                Glide.with(getContext()).load(R.drawable.man)
                                        .bitmapTransform(new CropCircleTransformation(getContext()))
                                        .into(mHHead);
                            }else{
                                mSex.setRightDesc("女");
                                Glide.with(getContext()).load(R.drawable.head)
                                        .bitmapTransform(new BlurTransformation(getContext(), 25), new CenterCrop(getContext()))
                                        .into(mHBack);
                                //设置圆形图像
                                Glide.with(getContext()).load(R.drawable.head)
                                        .bitmapTransform(new CropCircleTransformation(getContext()))
                                        .into(mHHead);
                            }
                            name=users.get(0).getUserName();
                            userid=users.get(0).getUserId();
                            id=users.get(0).getId()+"";
                            num=users.get(0).getUserNum();
                            Date date=users.get(0).getUserFirstjob();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            time=sdf.format(date);


//                           HocellAdapter myAdapter=new HocellAdapter(producttype,R.layout.hocell);
//                           LinearLayoutManager manager=new LinearLayoutManager(getActivity());
//                           manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            //time.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.HORIZONTAL));
//                           time.setLayoutManager(manager);
//                           time.setAdapter(myAdapter);
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

    public void pop(){
        LongPressPopup popup = new LongPressPopupBuilder(getContext())
                .setTarget(mNickName)
                //.setPopupView(textView)// Not using this time
                .setPopupView(R.layout.detail, this)
                .setLongPressDuration(400)
                .setDismissOnLongPressStop(false)
                .setDismissOnTouchOutside(true)
                .setDismissOnBackPressed(true)
                .setCancelTouchOnDragOutsideView(true)
                .setLongPressReleaseListener(this)
                .setOnHoverListener(this)
                .setPopupListener(this)
                .setTag("PopupFoo")
                .setAnimationType(LongPressPopup.ANIMATION_TYPE_FROM_CENTER)
                .build();
        popup.register();



    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onViewInflated(@Nullable String popupTag, View root) {
        info=root.findViewById(R.id.info);
        info2=root.findViewById(R.id.info2);
        info3=root.findViewById(R.id.info3);
        info4=root.findViewById(R.id.info4);
        info.setText(name);
        info2.setText(userid);
        info3.setText(id);
        info4.setText(time);
    }

    @Override
    public void onHoverChanged(View view, boolean isHovered) {

    }

    @Override
    public void onPopupShow(@Nullable String popupTag) {

    }

    @Override
    public void onPopupDismiss(@Nullable String popupTag) {

    }
    //正则判断手机号码正确与否
    public static boolean validatePhoneNumber(String mobiles) {
        String telRegex = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$";
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }
}
