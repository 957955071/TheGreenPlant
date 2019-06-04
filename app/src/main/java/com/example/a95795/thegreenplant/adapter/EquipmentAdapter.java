package com.example.a95795.thegreenplant.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.custom.Equipment_Dianji;
import com.example.a95795.thegreenplant.custom.Machine;
import com.example.a95795.thegreenplant.custom.MyApplication;
import com.example.a95795.thegreenplant.custom.Workshop;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nightonke.jellytogglebutton.JellyToggleButton;
import com.nightonke.jellytogglebutton.JellyTypes.Jelly;
import com.nightonke.jellytogglebutton.State;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;
import static com.mob.MobSDK.getContext;
import static com.mob.tools.utils.Strings.getString;
import static com.nightonke.jellytogglebutton.JellyTypes.Jelly.ACTIVE_TREMBLE_BODY_SLIM_JIM;
import static com.nightonke.jellytogglebutton.JellyTypes.Jelly.LAZY_TREMBLE_TAIL_SLIM_JIM;

/**
 * Created by 95795 on 2019-05-17.
 */

/**
 * 一个 BaseExpandableListA的适配器
 */
public class EquipmentAdapter extends BaseExpandableListAdapter {
    public String[] groupString = {"电机（PRO）"};
    public String[][] childString = new String[1][3];
    public List<Machine> list;
    public int workid;
    

    //重写构造方法
    public EquipmentAdapter(List<Machine> objects,int work) {
        this.list = objects;
        this.workid = work;
        for (int i = 0; i < 3; i++) {
            childString[0][i] = list.get(i).getMachineType()+list.get(i).getMachineId();
        }
    }

    @Override
    // 获取分组的个数
    public int getGroupCount() {
        return groupString.length;
    }

    //获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return childString[groupPosition].length;
    }

    // 获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return groupString[groupPosition];
    }

    //获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childString[groupPosition][childPosition];
    }

    //获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们
    @Override
    public boolean hasStableIds() {
        return true;
    }



    /**
     * 获取显示指定组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded    该组是展开状态还是伸缩状态
     * @param convertView   重用已有的视图对象
     * @param parent        返回的视图对象始终依附于的视图组
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.partent_item, parent, false);
        TextView textView = (TextView) convertView.findViewById(R.id.label_group_normal);
        textView.setText(groupString[groupPosition]);
        return convertView;
    }

    /**
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild   子元素是否处于组中的最后一个
     * @param convertView   重用已有的视图(View)对象
     * @param parent        返回的视图(View)对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     * android.view.ViewGroup)
     */

    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.equipment, parent, false);
        final EquipmentAdapter.Init init = new EquipmentAdapter.Init(groupPosition, childPosition);
        init.init(convertView);
        return convertView;
    }
    //子选项是否可以点击
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    //点击类——> 监听设备点击位置
    class MyListener {
        int group, child,MachineId;
        String IP;

        public MyListener(int groupPosition, int childPosition,String ip,int machineId) {
            group = groupPosition;
            child = childPosition;
            IP = ip;
            MachineId = machineId;
        }
//同listview方法
        public void left() {
            String url = IP+"user/MachineUpdata";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    "{\n" +
                            "\t\"machineSwitch\":0,\n" +
                            "\t\"machineId\":"+MachineId+"\n" +
                            "\n" +
                            "}",
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

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

        public void right() {
            String url =  IP+"user/MachineUpdata";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    "{\n" +
                            "\t\"machineSwitch\":1,\n" +
                            "\t\"machineId\":"+MachineId+"\n" +
                            "\n" +
                            "}",
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
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

    //初始化——> 子列表类
    class Init {
        int group, child;

        public Init(int groupPosition, int childPosition) {
            group = groupPosition;
            child = childPosition;
        }
        public void init(View convertView) {
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView2);
            JellyToggleButton jellyToggleButton = (JellyToggleButton) convertView.findViewById(R.id.JellyToggleButton);
            ImageView image = (ImageView) convertView.findViewById(R.id.imageView);
            textView.setText(childString[group][child]);

            //设备图片
            ImageLoader.getInstance().displayImage(convertView.getContext().getString(R.string.ip) + "img/dianji.png", image, MyApplication.getLoaderOptions());
            //将位置传给Mylistener,EquipmentNow
            EquipmentAdapter.EquipmentNow equipmentNow = new EquipmentAdapter.EquipmentNow(group, child);
            final EquipmentAdapter.MyListener myListener = new EquipmentAdapter.MyListener(group, child,convertView.getContext().getString(R.string.ip),list.get(child).getMachineId());

            //判断设备状况
            if ((list.get(child).getMachineFs()==1)) {
                jellyToggleButton.setCheckedImmediately( false, false );
                //设置按钮不可以点击
                jellyToggleButton.setEnabled(false);
                imageView.setOnClickListener(equipmentNow);
            } else if ((list.get(child).getMachineSwitch()==0)) {
                if((list.get(child).getMachineFs()==0)){
                    //隐藏警告按钮
                    imageView.setVisibility(View.INVISIBLE);
                    jellyToggleButton.setChecked(false, false);
                    if(workid==0){
                        jellyToggleButton.setEnabled(false);
                    }
                }
            } else if (list.get(child).getMachineSwitch()==1) {
                //同上
                if((list.get(child).getMachineFs()==0)){
                    //隐藏警告按钮
                    imageView.setVisibility(View.INVISIBLE);
                    jellyToggleButton.setChecked(true, false);
                    if(workid==0){
                        jellyToggleButton.setEnabled(false);
                    }
                }

        }

            //启动果冻滑动按钮
            jellyToggleButton.setJelly(Jelly.ITSELF);
            //背景为白色
            jellyToggleButton.setBackgroundColorRes(R.color.white);
            //关闭为红色按钮
            jellyToggleButton.setLeftThumbColorRes(R.color.red);
            //打开为绿色按钮
            jellyToggleButton.setRightThumbColorRes(R.color.green);
            //粘稠果冻效果
            jellyToggleButton.setJelly(ACTIVE_TREMBLE_BODY_SLIM_JIM);
            jellyToggleButton.setOnStateChangeListener(new JellyToggleButton.OnStateChangeListener() {
                @Override
                public void onStateChange(float process, State state, JellyToggleButton jtb) {
                    if (state.equals(State.LEFT)) {
                        myListener.left();
                    }
                    if (state.equals(State.RIGHT)) {
                        myListener.right();
                    }
                }
            });
        }
    }

    //点击类——> 监听设备警告
    class EquipmentNow implements View.OnClickListener {
        int GroupPosition, ChildPosition;

        public EquipmentNow(int groupPosition, int childPosition) {
            GroupPosition = groupPosition;
            ChildPosition = childPosition;
        }


        @Override
        public void onClick(View v) {
            new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("警告")
                    .setContentText("[" + childString[GroupPosition][ChildPosition] + "]机器出现未知故障，请立即检查！")
                    .setConfirmText("确定")
                    .show();
        }

    }


}
