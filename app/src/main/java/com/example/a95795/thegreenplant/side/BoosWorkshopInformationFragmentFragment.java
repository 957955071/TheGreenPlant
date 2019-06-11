package com.example.a95795.thegreenplant.side;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.adapter.BoosWorkshopAdapter;
import com.example.a95795.thegreenplant.adapter.WorkshopAdapter;
import com.example.a95795.thegreenplant.custom.CodeUtils;
import com.example.a95795.thegreenplant.custom.MyApplication;
import com.example.a95795.thegreenplant.custom.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.yokeyword.fragmentation.SupportFragment;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


public class BoosWorkshopInformationFragmentFragment extends SupportFragment {

 private int interpretation = 0 ;
 private LinearLayout linearLayout;
    private Bitmap bitmap;
    private String code;
    private Button button;
    private EditText editText;

    public static BoosWorkshopInformationFragmentFragment newInstance() {

        return new BoosWorkshopInformationFragmentFragment();

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_boos_workshop_information, container, false);
        linearLayout = (LinearLayout) view.findViewById(R.id.workshopcodo);
        editText = (EditText) view.findViewById(R.id.et_phoneCodes);
        //获取需要展示图片验证码的ImageView
        final ImageView image = (ImageView) view.findViewById(R.id.image);
        //获取工具类生成的图片验证码对象
        bitmap = CodeUtils.getInstance().createBitmap();
        //获取当前图片验证码的对应内容用于校验
        code = CodeUtils.getInstance().getCode();

        button = (Button) view.findViewById(R.id.but_forgetpass_toSetCodes);
        image.setImageBitmap(bitmap);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmap = CodeUtils.getInstance().createBitmap();
                code = CodeUtils.getInstance().getCode();
                image.setImageBitmap(bitmap);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String CODE = editText.getText().toString();
                if (CODE.equals("")){
                    new SweetAlertDialog(view.getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("警告")
                            .setContentText("输入为空")
                            .setConfirmText("确定")
                            .show();

                }else if(CODE.equals(code)){
                    interpretation = 1;
                    linearLayout.setVisibility(View.GONE);
                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setContentText("验证码正确！")
                            .setConfirmText("确定")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }else {
                    new SweetAlertDialog(view.getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("警告")
                            .setContentText("验证码输入错误！")
                            .setConfirmText("确定")
                            .show();
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });


        String url = getString(R.string.ip) + "user/userall";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Gson gson = new Gson();
                            final List<User> subjectList = gson.fromJson(response.getJSONArray("UserList").toString(),new TypeToken<List<User>>(){}.getType());
                            SwipeRecyclerView recyclerView  = (SwipeRecyclerView ) view.findViewById(R.id.recycler_view);
                            // 设置监听器。
                            recyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
                            // 菜单点击监听。
                            recyclerView.setOnItemMenuClickListener(new OnItemMenuClickListener() {
                                @Override
                                public void onItemClick(SwipeMenuBridge menuBridge, int position) {
                                    // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                                    menuBridge.closeMenu();
                                    if (interpretation == 0){
                                        linearLayout.setVisibility(View.VISIBLE);
                                        new SweetAlertDialog(view.getContext(), SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText("警告")
                                                .setContentText("你将使用本软件最高权限，请按步骤操作，或与管理员联系永久开启最高权限")
                                                .setConfirmText("确定")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(final SweetAlertDialog sDialog) {
                                                        sDialog
                                                                .setTitleText("提示")
                                                                .setContentText("第一步：输入验证码")
                                                                .setConfirmText("我明白了")
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                        sDialog
                                                                                .setTitleText("提示")
                                                                                .setContentText("第二步：输入最高权限密码")
                                                                                .setConfirmText("我明白了")
                                                                                .setConfirmClickListener(null)
                                                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                                                    }
                                                                })
                                                                .changeAlertType(SweetAlertDialog.WARNING_TYPE);
                                                    }
                                                })
                                                .show();
                                    }else if (interpretation == 1){
                                        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                .setContentText("您好，您暂时获得最高权限，请务必确认您正在进行的操作！")
                                                .setConfirmText("确定")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                    }
                                                })
                                                .show();
                                        AlertDialog.Builder customizeDialog = new AlertDialog.Builder(getContext());
                                        final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog,null);
                                        customizeDialog.setView(dialogView);
                                        customizeDialog.setPositiveButton("确定",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // 获取EditView中的输入内容
                                                        EditText edit_text = (EditText) dialogView.findViewById(R.id.et_forgetPass_PhoneNum);
                                                        String test = edit_text.getText().toString();
                                                        String url = getString(R.string.ip) + "user/verification?id="+test;
                                                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                                                Request.Method.POST,
                                                                url,
                                                                "",
                                                                new Response.Listener<JSONObject>(){
                                                                    @Override
                                                                    public void onResponse(JSONObject response) {
                                                                        try {
                                                                            Gson gson = new Gson();
                                                                            String UserList;
                                                                            UserList = response.getString("UserList");
                                                                            if (UserList.equals("0")) {
                                                                                new SweetAlertDialog(view.getContext(), SweetAlertDialog.WARNING_TYPE)
                                                                                        .setTitleText("警告")
                                                                                        .setContentText("密码输入错误，您共有3次机会输入，错误3次之后将无法使用此功能！")
                                                                                        .setConfirmText("确定")
                                                                                        .show();
                                                                            } else {
                                                                                interpretation = 3;
                                                                                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                                                        .setContentText("您已暂时开启超级用户权限！请谨慎使用！")
                                                                                        .setConfirmText("确定")
                                                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                            @Override
                                                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                                                sDialog.dismissWithAnimation();
                                                                                            }
                                                                                        })
                                                                                        .show();
                                                                            }
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }


                                                                    }
                                                                },
                                                                new Response.ErrorListener(){
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {

                                                                    }
                                                                }
                                                        );
                                                        MyApplication.addRequest(jsonObjectRequest,"MainActivity");



                                                    }
                                                });
                                        customizeDialog.show();
                                    }else if( interpretation ==3){
                                        Toast.makeText(getContext(),"123",Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(linearLayoutManager);
                            BoosWorkshopAdapter boosWorkshopAdapter = new BoosWorkshopAdapter(subjectList);
                            recyclerView.setAdapter(boosWorkshopAdapter);

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

        return view;
    }
    // 创建菜单：
    SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {


            SwipeMenuItem deleteItem = new SwipeMenuItem(BoosWorkshopInformationFragmentFragment.this.getActivity())
                    .setBackground(R.color.red)
                    .setText("删除") // 文字。
                    .setImage(R.mipmap.delete)
                    .setTextColor(Color.WHITE) // 文字颜色。
                    .setTextSize(16) // 文字大小。
                    .setWidth(300)
                    .setHeight(MATCH_PARENT);
            rightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。.

        }
    };


}
