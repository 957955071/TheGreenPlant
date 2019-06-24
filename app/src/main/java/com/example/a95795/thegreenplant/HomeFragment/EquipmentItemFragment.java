package com.example.a95795.thegreenplant.HomeFragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.DashPathEffect;
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
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daivd.chart.component.axis.BaseAxis;
import com.daivd.chart.component.base.IAxis;
import com.daivd.chart.component.base.IComponent;
import com.daivd.chart.core.LineChart;
import com.daivd.chart.data.ChartData;
import com.daivd.chart.data.LineData;
import com.daivd.chart.data.style.PointStyle;
import com.daivd.chart.provider.component.cross.VerticalCross;
import com.daivd.chart.provider.component.level.LevelLine;
import com.daivd.chart.provider.component.mark.BubbleMarkView;
import com.daivd.chart.provider.component.point.Point;
import com.example.a95795.thegreenplant.Login.LoginFragment;
import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.adapter.EnvironmentAdapter;
import com.example.a95795.thegreenplant.adapter.EquipmentAdapter;
import com.example.a95795.thegreenplant.bean.Machinerecord;
import com.example.a95795.thegreenplant.bean.SetValue;
import com.example.a95795.thegreenplant.custom.Equipment_Dianji;
import com.example.a95795.thegreenplant.custom.Machine;
import com.example.a95795.thegreenplant.custom.MyApplication;
import com.example.a95795.thegreenplant.custom.Phone;
import com.example.a95795.thegreenplant.custom.SecretTextView;
import com.example.a95795.thegreenplant.custom.User;
import com.example.a95795.thegreenplant.custom.WorkShopJudge;
import com.example.a95795.thegreenplant.custom.Workshop;
import com.example.a95795.thegreenplant.table.BaseDialog;
import com.example.a95795.thegreenplant.tools.OpenApiManager;
import com.example.a95795.thegreenplant.tools.OpenApiService;
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

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.yokeyword.fragmentation.SupportFragment;
import retrofit2.Call;
import retrofit2.Callback;
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
    private SecretTextView secretTextView;
    private int workshop;
    private int id;
    private ListView listView_big, listView_small;
    private LinearLayout linearLayout;
    private int work;
    private String TAG = "retrofit";
    private List<Double> machineFenergys = new ArrayList<>();
    private List<Date> dates = new ArrayList<>();
    private OpenApiService openApiService = OpenApiManager.createOpenApiService();
    private String chatName;

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
        work = sp.getInt("STRING_KEY2", 0);
        workshop = sp.getInt("STRING_KEY5", 0);

        EventBus.getDefault().register(this);

        list();
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
                            expandableListView.setAdapter(new EquipmentAdapter(subjectList, work));
                            //定义临时数组
                            final ArrayList list = new ArrayList();
                            //遍历找到出现问题的设备
                            for (int i = 0; i < subjectList.size(); i++) {
                                if (subjectList.get(i).getMachineFs() == 1) {
                                    list.add(subjectList.get(i).getMachineType() + subjectList.get(i).getMachineId() + "号");
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
                                    int count = new EquipmentAdapter(subjectList, work).getGroupCount();
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
                                        smasher.with(listView_small).setStyle(SmashAnimator.STYLE_DROP)
                                                .setDuration(1000)
                                                .start();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                listView_big.setVisibility(View.VISIBLE);
                                                listView_small.setVisibility(View.GONE);
                                            }
                                        }, 1000);    //延时3s执行
                                    } else {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                ParticleSmasher smasher = new ParticleSmasher(EquipmentItemFragment.this.getActivity());
                                                smasher.reShowView(listView_small);
                                                smasher.with(listView_big).setStyle(SmashAnimator.STYLE_DROP)
                                                        .setDuration(1000)
                                                        .start();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        listView_big.setVisibility(View.GONE);
                                                        listView_small.setVisibility(View.VISIBLE);
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
            id = MachineId;
        }
    }

    private void showChartDialog(String tableName, List<Date> chartYDataList, List<Double> list) {
        View chartView = View.inflate(this.getContext(), R.layout.dialog_chart, null);
        LineChart lineChart = (LineChart) chartView.findViewById(R.id.lineChart);
        lineChart.setLineModel(LineChart.CURVE_MODEL);
        Resources res = getResources();
        com.daivd.chart.data.style.FontStyle.setDefaultTextSpSize(this.getContext(), 12);
        List<LineData> ColumnDatas = new ArrayList<>();
        ArrayList<Double> tempList1 = new ArrayList<>();
        ArrayList<String> ydataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Date d = chartYDataList.get(i);
            ydataList.add(dateToStr(d));
        }
        for (int i = 0; i < 10; i++) {
            double value = list.get(i);
            tempList1.add(value);
        }
        LineData columnData1 = new LineData(tableName, "", IAxis.AxisDirection.LEFT, getResources().getColor(R.color.arc1), tempList1);
        ColumnDatas.add(columnData1);
        ChartData<LineData> chartData2 = new ChartData<>(chatName, ydataList, ColumnDatas);
        lineChart.getChartTitle().setDirection(IComponent.TOP);
        lineChart.getLegend().setDirection(IComponent.BOTTOM);
        lineChart.setLineModel(LineChart.CURVE_MODEL);
        BaseAxis verticalAxis = lineChart.getLeftVerticalAxis();
        BaseAxis horizontalAxis = lineChart.getHorizontalAxis();
        //设置竖轴方向
        verticalAxis.setAxisDirection(IAxis.AxisDirection.LEFT);
        //设置网格
        verticalAxis.setDrawGrid(true);
        //设置横轴方向
        horizontalAxis.setAxisDirection(IAxis.AxisDirection.BOTTOM);
        horizontalAxis.setDrawGrid(true);
        //设置线条样式
        verticalAxis.getAxisStyle().setWidth(this.getContext(), 1);
        DashPathEffect effects = new DashPathEffect(new float[]{1, 2, 4, 8}, 1);
        verticalAxis.getGridStyle().setWidth(this.getContext(), 1).setColor(res.getColor(R.color.arc_text)).setEffect(effects);
        horizontalAxis.getGridStyle().setWidth(this.getContext(), 1).setColor(res.getColor(R.color.arc_text)).setEffect(effects);
        lineChart.setZoom(true);
        //开启十字架
        lineChart.getProvider().setOpenCross(true);
        lineChart.getProvider().setCross(new VerticalCross());
        lineChart.getProvider().setShowText(true);
        //开启MarkView
        lineChart.getProvider().setOpenMark(true);
        //设置MarkView
        lineChart.getProvider().setMarkView(new BubbleMarkView(this.getContext()));

        //设置显示标题
        lineChart.setShowChartName(true);
        //设置标题样式
        com.daivd.chart.data.style.FontStyle fontStyle = lineChart.getChartTitle().getFontStyle();
        fontStyle.setTextColor(res.getColor(R.color.arc_temp));
        fontStyle.setTextSpSize(this.getContext(), 15);
        LevelLine levelLine = new LevelLine(30);
        DashPathEffect effects2 = new DashPathEffect(new float[]{1, 2, 2, 4}, 1);
        levelLine.getLineStyle().setWidth(this.getContext(), 1).setColor(res.getColor(R.color.arc23)).setEffect(effects);
        levelLine.getLineStyle().setEffect(effects2);
        lineChart.getProvider().addLevelLine(levelLine);
        Point legendPoint = (Point) lineChart.getLegend().getPoint();
        PointStyle style = legendPoint.getPointStyle();
        style.setShape(PointStyle.SQUARE);
        lineChart.getProvider().setArea(true);
        lineChart.getHorizontalAxis().setRotateAngle(90);
        lineChart.setChartData(chartData2);
        lineChart.startChartAnim(400);
        BaseDialog dialog = new BaseDialog.Builder(this.getContext()).setFillWidth(true).setContentView(chartView).create();
        dialog.show();
    }

    public void getFenergy(int id) {
        Call<Machinerecord> call = null;
        machineFenergys = new ArrayList<>();
        dates = new ArrayList<>();
        switch (workshop) {
            case 1:
                if (id == 1 || id == 4) {
                    call = openApiService.getFenergy(1, "EPM1");
                } else if (id == 2 || id == 5) {
                    call = openApiService.getFenergy(1, "ETEM1");
                } else if (id == 3 || id == 6) {
                    call = openApiService.getFenergy(1, "EHUM1");
                }
                break;

            case 2:
                if (id == 7 || id == 10) {
                    call = openApiService.getFenergy(2, "EPM2");
                } else if (id == 8 || id == 11) {
                    call = openApiService.getFenergy(2, "ETEM2");
                } else if (id == 9 || id == 12) {
                    call = openApiService.getFenergy(2, "EHUM2");
                }
                break;

            case 3:
                if (id == 13 || id == 16) {
                    call = openApiService.getFenergy(3, "EPM3");
                } else if (id == 14 || id == 17) {
                    call = openApiService.getFenergy(3, "ETEM3");
                } else if (id == 15 || id == 18) {
                    call = openApiService.getFenergy(3, "EHUM3");
                }
                break;

            case 4:
                if (id == 19 || id == 22) {
                    call = openApiService.getFenergy(4, "EPM4");
                } else if (id == 20 || id == 23) {
                    call = openApiService.getFenergy(4, "ETEM4");
                } else if (id == 21 || id == 24) {
                    call = openApiService.getFenergy(4, "EHUM4");
                }
                break;

            default:
                call = openApiService.getFenergy(1, "EPM1");
                break;
        }
        call.enqueue(new Callback<Machinerecord>() {
            @Override
            public void onResponse(Call<Machinerecord> call, retrofit2.Response<Machinerecord> response) {
                Log.e(TAG, "onResponse: 请求成功！！！");
                Machinerecord mc = response.body();

                for (int i = 0; i < mc.getGetFenergy().size(); i++) {
                    double f = mc.getGetFenergy().get(i).getMachineFenergy();
                    String d = mc.getGetFenergy().get(i).getDate();
                    Log.e(TAG, "onResponse: f：" + f);
                    Log.e(TAG, "onResponse: d：" + d);

                    machineFenergys.add(f);
                    dates.add(strToDate(d));
                }
                secretTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(EquipmentItemFragment.this.getActivity(), "点击了", Toast.LENGTH_LONG).show();
                        showChartDialog("机器能耗量(千瓦时/天)", dates, machineFenergys);
                    }
                });

                Log.e(TAG, "请求成功》machineFenergys:  " + machineFenergys + "，dates：" + dates);
            }

            @Override
            public void onFailure(Call<Machinerecord> call, Throwable t) {
                Log.e(TAG, "onFailure: 请求失败,哈哈哈哈！！！！~~~~~");
                Log.e(TAG, "onFailure: " + t.getMessage());
                Log.e(TAG, "machineFenergys:  " + machineFenergys + "，dates：" + dates);

            }
        });
    }


    public void init(View view) {
        secretTextView = (SecretTextView) view.findViewById(R.id.detail);
        secretTextView.show();

        //开关状态
        final TextView textView1 = (TextView) view.findViewById(R.id.on_off_state_data);
        //负责人
        final TextView textView2 = (TextView) view.findViewById(R.id.principal_data);
        //机器编号
        final TextView textView3 = (TextView) view.findViewById(R.id.machine_identification_number_data);
        //使用频次
        final TextView textView4 = (TextView) view.findViewById(R.id.frequency_of_usage_data);
        //使用时间
        final TextView textView5 = (TextView) view.findViewById(R.id.hours_of_use_data);
        //故障状态
        final TextView textView6 = (TextView) view.findViewById(R.id.malfunction_data);
        //车间编号
        final TextView textView7 = (TextView) view.findViewById(R.id.local_name_data);
        //能耗
        final TextView textView8 = (TextView) view.findViewById(R.id.energy_consumption_data);
        //机器型号
        final TextView textView9 = (TextView) view.findViewById(R.id.machine_model_data);
        //采购时间
        final TextView textView10 = (TextView) view.findViewById(R.id.time_to_purchase_data);
        String url = getString(R.string.ip) + "user/userfindMachineId";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                "{\n" +
                        "\t\"machineId\":" + id + "\n" +
                        "}",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Gson gson = new Gson();
                            final ArrayList<Machine> subjectList = gson.fromJson(response.getJSONArray("Machine").toString(), new TypeToken<List<Machine>>() {
                            }.getType());
                            if (subjectList.get(0).getMachineSwitch().equals(0)) {
                                textView1.setText("关");
                            } else if (subjectList.get(0).getMachineSwitch().equals(1)) {
                                textView1.setText("开");
                            }
                            textView2.setText(subjectList.get(0).getMachineLeading());
                            textView3.setText(subjectList.get(0).getMachineId() + "");
                            Date date = subjectList.get(0).getMachineUsetime();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            textView5.setText((sdf.format(date)));
                            if (subjectList.get(0).getMachineFs() == 1) {
                                textView6.setText("设备故障");
                            } else {
                                textView6.setText("设备正常");
                            }
                            textView7.setText("车间" + subjectList.get(0).getMachineWorkshop());

                            switch (subjectList.get(0).getMachineType()) {
                                case "EPM":
                                    textView9.setText("PM2.5装置" + "（EPM）");
                                    break;
                                case "EHUM":
                                    textView9.setText("湿度装置" + "（EHUM）");
                                    break;
                                case "ETEM":
                                    textView9.setText("温度装置" + "（ETEM）");
                                    break;
                                case "PRO":
                                    textView9.setText("生产装置" + "（PRO）");
                                    break;
                                default:
                                    break;
                            }
                            Date dates = subjectList.get(0).getMachineBuytime();
                            SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
                            textView10.setText((sdfs.format(dates)));
                            String mstr = "";
                            switch (subjectList.get(0).getMachineId()) {
                                case 1:
                                case 7:
                                case 13:
                                case 19:
                                    mstr = "EPM" + subjectList.get(0).getMachineId();
                                    break;
                                case 2:
                                case 8:
                                case 14:
                                case 20:
                                    mstr = "ETEM" + subjectList.get(0).getMachineId();
                                    break;
                                case 3:
                                case 9:
                                case 15:
                                case 21:
                                    mstr = "EHUM" + subjectList.get(0).getMachineId();
                                    break;
                                default:
                                    mstr = "PRO" + subjectList.get(0).getMachineId();
                                    break;
                            }
                            chatName = subjectList.get(0).getMachineWorkshop() + "号车间" + mstr + "机器能耗图";
                            getFenergy(subjectList.get(0).getMachineId());

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

    public void list() {
        String url = getString(R.string.ip) + "user/environmental_unit";
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
                            final List<Machine> subjectList = gson.fromJson(response.getJSONArray("Machine").toString(), new TypeToken<List<Machine>>() {
                            }.getType());
                            EnvironmentAdapter adapter_big = new EnvironmentAdapter(EquipmentItemFragment.this.getActivity(), R.layout.listview, subjectList, work);
                            listView_big = (ListView) getView().findViewById(R.id.listviewbig);
                            listView_big.setAdapter(adapter_big);
                            listView_big.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    EquipmentItemFragment.GetMachineId getMachineId = new EquipmentItemFragment.GetMachineId(subjectList.get(i).getMachineId());
                                    getMachineId.setMachineId();
                                    //点击子控件弹出框框
                                    LongPressPopup popup = new LongPressPopupBuilder(EquipmentItemFragment.this.getActivity())
                                            .setTarget(view)
                                            .setPopupView(R.layout.particulars_abbreviate, EquipmentItemFragment.this)
                                            .setLongPressDuration(50)
                                            .build();
                                    // You can also chain it to the .build() mehod call above without declaring the "popup" variable before
                                    popup.register();
                                }
                            });
                            EnvironmentAdapter adapter_small = new EnvironmentAdapter(EquipmentItemFragment.this.getActivity(), R.layout.listview_small, subjectList, work);
                            listView_small = (ListView) getView().findViewById(R.id.listview_little);
                            listView_small.setAdapter(adapter_small);
                            listView_small.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    EquipmentItemFragment.GetMachineId getMachineId = new EquipmentItemFragment.GetMachineId(subjectList.get(i).getMachineId());
                                    getMachineId.setMachineId();
                                    //点击子控件弹出框框
                                    LongPressPopup popup = new LongPressPopupBuilder(EquipmentItemFragment.this.getActivity())
                                            .setTarget(view)
                                            .setPopupView(R.layout.particulars_abbreviate, EquipmentItemFragment.this)
                                            .setLongPressDuration(50)
                                            .build();
                                    // You can also chain it to the .build() mehod call above without declaring the "popup" variable before
                                    popup.register();
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

    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public static String dateToStr(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }
}
