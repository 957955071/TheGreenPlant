package com.example.a95795.thegreenplant.dynamicLineChart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.bean.EnvironmentInfoDay;
import com.example.a95795.thegreenplant.tools.OpenApiManager;
import com.example.a95795.thegreenplant.tools.OpenApiService;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;

public class DayWorkShop2ChartFragment extends Fragment {
    private View view;
    private LineChart chart;
    private DayDynamicLineChartManager dynamicLineChartManager;
    private List<Integer> listData = new ArrayList<>(); //数据集合
    private List<String> names = new ArrayList<>(); //折线名字集合
    private List<Integer> colour = new ArrayList<>();//折线颜色集合
    private String TAG = "volley";
    private int tmp,pm,hum;
    private OpenApiService openApiService = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_workshop_linechart, container, false);
        initView();
        getEnvironmentInfoList();
        return view;
    }

    public void initView(){
        chart = view.findViewById(R.id.chart_workshop);
        chart.getDescription().setText("");//去掉图标题
        //折线名字
        names.add("PM2.5");
        names.add("温度");
        names.add("相对湿度");
        //折线颜色
        colour.add(Color.CYAN);
        colour.add(Color.GREEN);
        colour.add(Color.BLUE);
        dynamicLineChartManager = new DayDynamicLineChartManager(chart, names, colour);
        dynamicLineChartManager.setYAxis(100, 0, 10);

        openApiService= OpenApiManager.createOpenApiService();
    }

    public void getEnvironmentInfoList() {

        Call<EnvironmentInfoDay> einfo= openApiService.queryEnvironmentInfoDayByType2();

        //step4:通过异步获取数据
        einfo.enqueue(new Callback<EnvironmentInfoDay>() {
            @Override
            public void onResponse(Call<EnvironmentInfoDay> call, retrofit2.Response<EnvironmentInfoDay> response) {
                Log.e(TAG, "onResponse: 请求成功！！！" );
                EnvironmentInfoDay ef=response.body();

                for(int j=0;j<ef.getEnvironmentInfoList().size();j++){
                    tmp = ef.getEnvironmentInfoList().get(j).getTmp();
                    hum = ef.getEnvironmentInfoList().get(j).getHum();
                    pm = ef.getEnvironmentInfoList().get(j).getPm();
                    listData.add(pm);
                    listData.add(tmp);
                    listData.add(hum);

                    dynamicLineChartManager.addEntry2(listData);
                    listData.clear();
                }
            }
            @Override
            public void onFailure(Call<EnvironmentInfoDay> call, Throwable t) {
                Log.e(TAG, "onFailure: 请求失败！！！！~~~~~" );
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });
    }
}
