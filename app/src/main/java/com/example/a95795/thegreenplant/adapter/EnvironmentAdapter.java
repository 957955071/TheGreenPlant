package com.example.a95795.thegreenplant.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.custom.Machine;
import com.example.a95795.thegreenplant.custom.MyApplication;
import com.nightonke.jellytogglebutton.JellyToggleButton;
import com.nightonke.jellytogglebutton.JellyTypes.Jelly;
import com.nightonke.jellytogglebutton.State;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;
import static com.nightonke.jellytogglebutton.JellyTypes.Jelly.ACTIVE_TREMBLE_BODY_SLIM_JIM;

/**
 * listview的适配器
 */
public class EnvironmentAdapter extends ArrayAdapter<Machine> {
    public List<Machine> list;
    public int workid;
    private int resourceId;
//重写适配器
    public EnvironmentAdapter(Context context, int textViewResourceId, List<Machine> objects,int work) {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
        this.list = objects;
        this.workid = work;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Machine machine = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        JellyToggleButton jellyToggleButton = (JellyToggleButton) view.findViewById(R.id.JellyToggleButton);
        ImageView imageView1 = (ImageView) view.findViewById(R.id.imageView2);
        textView.setText(machine.getMachineType()+machine.getMachineId());
        //简单设置图片
        switch (machine.getMachineType()) {
            case "EHUM":
                //设备图片
                ImageLoader.getInstance().displayImage(getContext().getString(R.string.ip) + "img/humidity.png", imageView, MyApplication.getLoaderOptions());
                break;
            case "EPM":
                ImageLoader.getInstance().displayImage(getContext().getString(R.string.ip) + "img/particulate.png", imageView, MyApplication.getLoaderOptions());
                break;
            case "ETEM":
                ImageLoader.getInstance().displayImage(getContext().getString(R.string.ip) + "img/temperature.png", imageView, MyApplication.getLoaderOptions());
                break;
            default:
                break;
        }

        //将位置传给Mylistener
        EnvironmentAdapter.EquipmentNow equipmentNow = new EnvironmentAdapter.EquipmentNow(position);
        final EnvironmentAdapter.MyListener myListener = new EnvironmentAdapter.MyListener(position, getContext().getString(R.string.ip), list.get(position).getMachineId());

        //使用jellyToggleButton开源控件 实现果冻化按钮
        //判断设备状况
        if ((list.get(position).getMachineFs() == 1)) {
            jellyToggleButton.setCheckedImmediately(false, false);
            //设置按钮不可以点击
            jellyToggleButton.setEnabled(false);
            imageView1.setOnClickListener(equipmentNow);
        } else if ((list.get(position).getMachineSwitch() == 0)) {
            if ((list.get(position).getMachineFs() == 0)) {
                //隐藏警告按钮
                imageView1.setVisibility(View.INVISIBLE);
                jellyToggleButton.setChecked(false, false);

            }
        } else if (list.get(position).getMachineSwitch() == 1) {
            //同上
            if ((list.get(position).getMachineFs() == 0)) {
                //隐藏警告按钮
                imageView1.setVisibility(View.INVISIBLE);
                jellyToggleButton.setChecked(true, false);

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


        return view;
    }
    //设置子单元可以点击
    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    //点击类——> 监听设备点击位置
    class MyListener {
        int position, MachineId;
        String IP;
        boolean status = false;

        Context ctx = getContext();
        SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
        String name = sp.getString("STRING_KEY3","");
        String userid = sp.getString("STRING_KEY4","");
        public MyListener(int Position, String ip, int machineId) {
            position = Position;
            IP = ip;
            MachineId = machineId;
        }
//设置按钮在左边需要发送的事件
        public void left() {
            if (workid==0){

                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("警告")
                            .setContentText("您好，您没有权限使用此功能！请下拉刷新查看设备最新情况！")
                            .setConfirmText("确定")
                            .show();


            }else {
                String url = IP + "user/MachineUpdata";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        "{\n" +
                                "\t\"machineSwitch\":0,\n" +
                                "\t\"machineId\":" + MachineId + "\n" +
                                "\n" +
                                "}",
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                addlogLeft();
                                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setContentText("设备已关闭")
                                        .setConfirmText("确定")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();
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
//控件在右边的事件
        public void right() {
            if (workid==0){

                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("警告")
                            .setContentText("您好，您没有权限使用此功能！请下拉刷新查看设备最新情况！")
                            .setConfirmText("确定")
                            .show();

            }else {
                String url = IP + "user/MachineUpdata";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        "{\n" +
                                "\t\"machineSwitch\":1,\n" +
                                "\t\"machineId\":" + MachineId + "\n" +
                                "\n" +
                                "}",
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                addlogRight();
                                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setContentText("设备已启动")
                                        .setConfirmText("确定")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();
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
        public void addlogRight(){
            SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
            String ee = dff.format(new Date());
            String url = IP + "user/LogAdd";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    "{\n" +
                            "\t\n" +
                            "            \"userId\": \""+userid+"\",\n" +
                            "            \"userName\": \""+name+"\",\n" +
                            "            \"date\": \""+ee+"\",\n" +
                            "            \"log\": \"打开了"+MachineId+"号设备\",\n" +
                            "            \"logType\": 3\n" +
                            "}",
                    new Response.Listener<JSONObject>(){
                        @Override
                        public void onResponse(JSONObject response) {

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
        public void addlogLeft(){
            SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
            String ee = dff.format(new Date());

            String url = IP + "user/LogAdd";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    "{\n" +
                            "\t\n" +
                            "            \"userId\": \""+userid+"\",\n" +
                            "            \"userName\": \""+name+"\",\n" +
                            "            \"date\": \""+ee+"\",\n" +
                            "            \"log\": \"关闭了"+MachineId+"号设备\",\n" +
                            "            \"logType\": 3\n" +
                            "}",
                    new Response.Listener<JSONObject>(){
                        @Override
                        public void onResponse(JSONObject response) {

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

    }


    //点击类——> 监听设备警告
    class EquipmentNow implements View.OnClickListener {
        int position;

        public EquipmentNow(int Position) {
            position = Position;
        }

        @Override
        public void onClick(View v) {
            new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("警告")
                    .setContentText("[" + list.get(position).getMachineType() + "]机器出现未知故障，请立即检查！")
                    .setConfirmText("确定")
                    .show();
        }

    }



}
