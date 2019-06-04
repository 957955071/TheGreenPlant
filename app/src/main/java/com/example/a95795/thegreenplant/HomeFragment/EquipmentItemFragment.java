package com.example.a95795.thegreenplant.HomeFragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.a95795.thegreenplant.Login.LoginFragment;
import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.adapter.EnvironmentAdapter;
import com.example.a95795.thegreenplant.adapter.EquipmentAdapter;
import com.example.a95795.thegreenplant.custom.Equipment_Dianji;
import com.example.a95795.thegreenplant.custom.Machine;
import com.example.a95795.thegreenplant.custom.MyApplication;
import com.example.a95795.thegreenplant.custom.Phone;
import com.example.a95795.thegreenplant.custom.SecretTextView;
import com.example.a95795.thegreenplant.custom.User;
import com.example.a95795.thegreenplant.custom.WorkShopJudge;
import com.example.a95795.thegreenplant.custom.Workshop;
import com.fadai.particlesmasher.ParticleSmasher;
import com.fadai.particlesmasher.SmashAnimator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sbingo.guide.GuideView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.yokeyword.fragmentation.SupportFragment;
import rm.com.longpresspopup.LongPressPopup;
import rm.com.longpresspopup.LongPressPopupBuilder;
import rm.com.longpresspopup.PopupInflaterListener;
import rm.com.longpresspopup.PopupStateListener;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class EquipmentItemFragment extends SupportFragment implements PopupInflaterListener {

    private ExpandableListView expandableListView;

    private int workshop = 1;
    private int Id;
    private ListView listView_big,listView_small;
    private LinearLayout linearLayout;
    private int ID,Workshop;

    public static EquipmentItemFragment newInstance() {
        return new EquipmentItemFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equipment_item, container, false);
        expandableListView = (ExpandableListView) view.findViewById(R.id.expend_list);
        linearLayout = (LinearLayout) view.findViewById(R.id.equipmentAgain);
        Context ctx = EquipmentItemFragment.this.getActivity();
        SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
        ID = sp.getInt("STRING_KEY", 0);
        Workshop = sp.getInt("STRING_KEY2",0);
        list();
        EventBus.getDefault().register(this);
        adapter();

        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(WorkShopJudge judge) {
        workshop = judge.getWorkshop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }

    //构造适配器
    public void adapter() {
        String url = getString(R.string.ip) + "user/Machine";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                "{\n" +
                        "\t\"machineWorkshop\":" + workshop + "\n" +
                        "}",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Gson gson = new Gson();
                            final ArrayList<Machine> subjectList = gson.fromJson(response.getJSONArray("Machine").toString(), new TypeToken<List<Machine>>() {
                            }.getType());
                            expandableListView.setAdapter(new EquipmentAdapter(subjectList,Workshop));
                            //定义临时数组
                            final ArrayList list = new ArrayList();
                            Log.d("2222222", "onResponse: "+list);
                            //遍历找到出现问题的设备
                            for (int i = 0; i < subjectList.size(); i++) {
                                if (subjectList.get(i).getMachineFs()==1) {
                                    list.add(subjectList.get(i).getMachineType() + subjectList.get(i).getMachineId() + "号");
                                    Log.d("2222222", "onResponse: "+list);
                                }
                            }
                            if (list.size() > 0) {
                                //提示出现故障位置
                                new SweetAlertDialog(EquipmentItemFragment.this.getActivity(), SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("警告")
                                        .setContentText(list + "机器出现故障！")
                                        .setConfirmText("确定")
                                        .show();
                            }
                            //控制只能打开一个组
                            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                                @Override
                                public void onGroupExpand(int groupPosition) {
                                    int count = new EquipmentAdapter(subjectList,Workshop).getGroupCount();
                                    for (int i = 0; i < count; i++) {
                                        if (i != groupPosition) {
                                            expandableListView.collapseGroup(i);
                                        }
                                    }
                                }
                            });
                            //设置子项布局监听
                            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                                @Override
                                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                                    EquipmentItemFragment.GetMachineId getMachineId = new EquipmentItemFragment.GetMachineId(subjectList.get(childPosition).getMachineId());
                                    getMachineId.setMachineId();

                                    //点击子控件弹出框框
                                    LongPressPopup popup = new LongPressPopupBuilder(EquipmentItemFragment.this.getActivity())
                                            .setTarget(v)
                                            .setPopupView(R.layout.particulars_abbreviate, EquipmentItemFragment.this)
                                            .setLongPressDuration(50)
                                            .build();
                                    // You can also chain it to the .build() mehod call above without declaring the "popup" variable before
                                    popup.register();
                                    return true;
                                }
                            });

                            expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                                @Override
                                public boolean onGroupClick(final ExpandableListView expandableListView, View view, int groupPosition, long l) {
                                   //设置列表不可以快速点击
                                    expandableListView.setEnabled(false);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            expandableListView.setEnabled(true);
                                        }
                                    }, 3000);    //延时3s执行
                                    if (expandableListView.isGroupExpanded(groupPosition)) {
                                        ParticleSmasher smasher = new ParticleSmasher(EquipmentItemFragment.this.getActivity());
                                        smasher.reShowView(listView_big);
                                        smasher.with(listView_small) .setStyle(SmashAnimator.STYLE_DROP)
                                                .setDuration(1000)
                                                .start();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                listView_big.setVisibility(View.VISIBLE);
                                                listView_small.setVisibility(View.GONE );
                                            }
                                        }, 1000);    //延时3s执行
                                    } else {

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                ParticleSmasher smasher = new ParticleSmasher(EquipmentItemFragment.this.getActivity());
                                                smasher.reShowView(listView_small);
                                                smasher.with(listView_big) .setStyle(SmashAnimator.STYLE_DROP)
                                                        .setDuration(1000)
                                                        .start();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        listView_big.setVisibility(View.GONE);
                                                        listView_small.setVisibility(View.VISIBLE );
                                                    }
                                                }, 1000);    //延时3s执行
                                            }
                                        }, 200);    //延时3s执行


                                    }
                                    return false;
                                }
                            });
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

    //定义弹出小框内容
    @Override
    public void onViewInflated(@Nullable String popupTag, View root) {
        init(root);
    }


    public void margin(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }


    //数据单例——> 数值
    class GetMachineId {
        int MachineId;

        public GetMachineId(int machineId) {
            MachineId = machineId;
        }

        public void setMachineId() {
            Id = MachineId;
        }


    }


    public void init(View view){
        final SecretTextView secretTextView = (SecretTextView) view.findViewById(R.id.detail);
        secretTextView.show();
        secretTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EquipmentItemFragment.this.getActivity(),"点击了",Toast.LENGTH_LONG).show();
            }
        });

        //开关状态
        final TextView textView1 = (TextView) view.findViewById(R.id.on_off_state_data);
        //负责人
        final TextView textView2 = (TextView) view.findViewById(R.id.principal_data);
        //机器编号
        final  TextView textView3 = (TextView) view.findViewById(R.id.machine_identification_number_data);
        //使用频次
        final  TextView textView4 = (TextView) view.findViewById(R.id.frequency_of_usage_data);
        //使用时间
        final   TextView textView5 = (TextView) view.findViewById(R.id.hours_of_use_data);
        //故障状态
        final  TextView textView6 = (TextView) view.findViewById(R.id.malfunction_data);
        //车间编号
        final  TextView textView7 = (TextView) view.findViewById(R.id.local_name_data);
        //能耗
        final  TextView textView8 = (TextView) view.findViewById(R.id.energy_consumption_data);
        //机器型号
        final   TextView textView9 = (TextView) view.findViewById(R.id.machine_model_data);
        //采购时间
        final    TextView textView10 = (TextView) view.findViewById(R.id.time_to_purchase_data);
        String url = getString(R.string.ip) + "user/userfindMachineId";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                "{\n" +
                        "\t\"machineId\":" + Id + "\n" +
                        "}",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Gson gson = new Gson();
                            final ArrayList<Machine> subjectList = gson.fromJson(response.getJSONArray("Machine").toString(), new TypeToken<List<Machine>>() {
                            }.getType());
                            if (subjectList.get(0).getMachineSwitch().equals(0)){
                                textView1.setText("关");
                            }else if(subjectList.get(0).getMachineSwitch().equals(1)){
                                textView1.setText("开");
                            }
                            textView2.setText(subjectList.get(0).getMachineLeading());
                            textView3.setText(subjectList.get(0).getMachineId()+"");
                            Date date=subjectList.get(0).getMachineUsetime();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            textView5.setText((sdf.format(date)));
                            if (subjectList.get(0).getMachineFs()==1){
                                textView6.setText("设备故障");
                            }else {
                                textView6.setText("设备正常");
                            }
                            textView7.setText("车间"+subjectList.get(0).getMachineWorkshop());

                            switch (subjectList.get(0).getMachineType()){
                                case "EPM":
                                    textView9.setText("PM2.5装置"+"（EPM）");
                                    break;
                                case "EHUM":
                                    textView9.setText("湿度装置"+"（EHUM）");
                                    break;
                                case "ETEM":
                                    textView9.setText("温度装置"+"（ETEM）");
                                    break;
                                case "PRO":
                                    textView9.setText("生产装置"+"（PRO）");
                                    break;
                                    default:
                                        break;
                            }
                            Date dates = subjectList.get(0).getMachineBuytime();
                            SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
                            textView10.setText((sdfs.format(dates)));
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

    public void list(){
        String url = getString(R.string.ip) + "user/environmental_unit";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                "{\n" +
                        "\t\"machineWorkshop\":"+workshop+"\n" +
                        "}",
                new Response.Listener< JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Gson gson = new Gson();
                            List<Machine> subjectList = gson.fromJson(response.getJSONArray("Machine").toString(),new TypeToken<List<Machine>>(){}.getType());

                            EnvironmentAdapter adapter_big = new EnvironmentAdapter(EquipmentItemFragment.this.getActivity(), R.layout.listview,subjectList,Workshop);
                            listView_big = (ListView) getView().findViewById(R.id.listviewbig);
                            listView_big.setAdapter(adapter_big);
                            EnvironmentAdapter adapter_small = new EnvironmentAdapter(EquipmentItemFragment.this.getActivity(), R.layout.listview_small,subjectList,Workshop);
                            listView_small = (ListView) getView().findViewById(R.id.listview_little);
                            listView_small.setAdapter(adapter_small);

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

}
