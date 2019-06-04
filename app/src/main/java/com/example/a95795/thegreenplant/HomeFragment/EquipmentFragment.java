package com.example.a95795.thegreenplant.HomeFragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.a95795.thegreenplant.HomeActivity;
import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.adapter.EnvironmentAdapter;
import com.example.a95795.thegreenplant.adapter.EquipmentAdapter;
import com.example.a95795.thegreenplant.custom.Equipment_Dianji;
import com.example.a95795.thegreenplant.custom.MyApplication;
import com.example.a95795.thegreenplant.custom.Phone;
import com.example.a95795.thegreenplant.custom.User;
import com.example.a95795.thegreenplant.custom.WorkShopJudge;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.sbingo.guide.GuideView;

import org.angmarch.views.NiceSpinner;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.yokeyword.fragmentation.SupportFragment;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class EquipmentFragment extends SupportFragment {

    private NiceSpinner niceSpinner;
    private int ID;
    public static EquipmentFragment newInstance() {
        return new EquipmentFragment();
    }
    // SpringView
   public SpringView springView;

//下拉刷新
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    //结束刷新动作
                    springView.onFinishFreshAndLoad();
                    //更新adapter
                    replaceFragment(new EquipmentItemFragment());
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_equipment, container, false);


       //可以通过缓存数据  来使得全局软件获得数据
        Context ctx = EquipmentFragment.this.getActivity();
        SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
        ID = sp.getInt("STRING_KEY", 0);//获取用户id
        int work = sp.getInt("STRING_KEY2", 0);//获取用户是否为管理


        judge();

        judge2();
        springView = (SpringView) view.findViewById(R.id.Spview_spbranchList);
        //下拉列表
        niceSpinner =  view.findViewById(R.id.nice_spinner);
        initData();initEvnet();replaceFragment(new EquipmentItemFragment());
        List<String> spinnerData = new LinkedList<>(Arrays.asList("一号车间", "二号车间","三号车间","四号车间"));
        niceSpinner.attachDataSource(spinnerData);
        niceSpinner.setTextSize(13);
        niceSpinner.setArrowDrawable(R.drawable.jiantou);

//监听下拉列表
        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(EquipmentFragment.this.getActivity(),"点击了第"+(1 + position)+"个",Toast.LENGTH_LONG).show();
                EventBus.getDefault().postSticky(new WorkShopJudge((1+position)));
                replaceFragment(new EquipmentItemFragment());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        return view;
    }

    private void initData() {

        springView.setHeader(new DefaultHeader(this.getActivity()));

    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.equipment, fragment);
        transaction.commit();
    }
    private void initEvnet() {

        springView.setType(SpringView.Type.FOLLOW);

//        刷新和载入很多其它的事件处理
//        假设须要处理的话，仅仅需在代码中加入监听：
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {

                handler.sendEmptyMessageDelayed(0,1000);
            }
            @Override
            public void onLoadmore() {

            }
        });

    }
    //通过id获得用户的全部资料
    public void judge(){
        String url = getString(R.string.ip) + "user/userfindid";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                "{\n" +
                        "\t\"id\": "+ID+"\n" +
                        "}",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Gson gson = new Gson();
                            List<User> subjectList = gson.fromJson(response.getJSONArray("UserList").toString(),new TypeToken<List<User>>(){}.getType());
                            if (subjectList.get(0).getUserWork()==0){
                                niceSpinner.setClickable(false);

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
    //对生产者或者管理员进行区分
    public void judge2(){
        String url = getString(R.string.ip) + "user/userfindid";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                "{\n" +
                        "\t\"id\": "+ID+"\n" +
                        "}",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Gson gson = new Gson();
                            List<User> subjectList = gson.fromJson(response.getJSONArray("UserList").toString(),new TypeToken<List<User>>(){}.getType());
                            if (subjectList.get(0).getUserWork()==0){
                                new SweetAlertDialog(EquipmentFragment.this.getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                        .setTitleText("提醒")
                                        .setContentText("你好，"+subjectList.get(0).getUserName()+"生产员，你以登录系统，你可以查看自己车间的相应信息，没有更改的权限")
                                        .setCustomImage(R.drawable.ren)
                                        .show();
                            }else {
                                new SweetAlertDialog(EquipmentFragment.this.getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                        .setTitleText("提醒")
                                        .setContentText("您好，尊敬的"+subjectList.get(0).getUserName()+"管理员，您已获得软件的全部权限")
                                        .setCustomImage(R.drawable.vip)
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
