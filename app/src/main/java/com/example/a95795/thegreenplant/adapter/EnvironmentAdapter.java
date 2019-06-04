package com.example.a95795.thegreenplant.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.a95795.thegreenplant.HomeFragment.EnvironmentFragment;
import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.custom.Machine;
import com.example.a95795.thegreenplant.custom.MyApplication;
import com.nightonke.jellytogglebutton.JellyToggleButton;
import com.nightonke.jellytogglebutton.JellyTypes.Jelly;
import com.nightonke.jellytogglebutton.State;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

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
        textView.setText(machine.getMachineType());
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
                if(workid==0){
                    jellyToggleButton.setEnabled(false);
                }
            }
        } else if (list.get(position).getMachineSwitch() == 1) {
            //同上
            if ((list.get(position).getMachineFs() == 0)) {
                //隐藏警告按钮
                imageView1.setVisibility(View.INVISIBLE);
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


        return view;
    }

    //点击类——> 监听设备点击位置
    class MyListener {
        int position, MachineId;
        String IP;

        public MyListener(int Position, String ip, int machineId) {
            position = Position;
            IP = ip;
            MachineId = machineId;
        }
//设置按钮在左边需要发送的事件
        public void left() {
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
//控件在右边的事件
        public void right() {
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
