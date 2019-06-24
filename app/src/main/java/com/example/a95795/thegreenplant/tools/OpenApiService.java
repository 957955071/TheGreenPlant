package com.example.a95795.thegreenplant.tools;


import com.example.a95795.thegreenplant.bean.EnvironmentInfoDay;
import com.example.a95795.thegreenplant.bean.EnvironmentInfoMon;
import com.example.a95795.thegreenplant.bean.EnvironmentInfoWeek;
import com.example.a95795.thegreenplant.bean.Machinerecord;
import com.example.a95795.thegreenplant.bean.SetValue;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OpenApiService {
//    public static final  String BASE_URL="http://192.168.43.26:8080/";
    public static final  String BASE_URL="http://nanhai655.cn:8080/";

    //查询全部环境详情 日
    @GET("user/queryEnvironmentInfoDay")
    Call<EnvironmentInfoDay> queryEnvironmentInfoDay();

    //查询环境详情根据车间类型
    @GET("user/queryEnvironmentInfoDayByType/1")
    Call<EnvironmentInfoDay> queryEnvironmentInfoDayByType1();

    @GET("user/queryEnvironmentInfoDayByType/2")
    Call<EnvironmentInfoDay> queryEnvironmentInfoDayByType2();

    @GET("user/queryEnvironmentInfoDayByType/3")
    Call<EnvironmentInfoDay> queryEnvironmentInfoDayByType3();

    @GET("user/queryEnvironmentInfoDayByType/4")
    Call<EnvironmentInfoDay> queryEnvironmentInfoDayByType4();

    //查询全部环境详情 周
    @GET("user/queryEnvironmentInfoWeek")
    Call<EnvironmentInfoWeek> queryEnvironmentInfoWeek();

    //查询环境详情根据车间类型
    @GET("user/queryEnvironmentInfoWeekByType/1")
    Call<EnvironmentInfoWeek> queryEnvironmentInfoWeekByType1();

    @GET("user/queryEnvironmentInfoWeekByType/2")
    Call<EnvironmentInfoWeek> queryEnvironmentInfoWeekByType2();

    @GET("user/queryEnvironmentInfoWeekByType/3")
    Call<EnvironmentInfoWeek> queryEnvironmentInfoWeekByType3();

    @GET("user/queryEnvironmentInfoWeekByType/4")
    Call<EnvironmentInfoWeek> queryEnvironmentInfoWeekByType4();

    //查询全部环境详情 月
    @GET("user/queryEnvironmentInfoMon")
    Call<EnvironmentInfoMon> queryEnvironmentInfoMon();

    //查询环境详情根据车间类型
    @GET("user/queryEnvironmentInfoMonByType/1")
    Call<EnvironmentInfoMon> queryEnvironmentInfoMonByType1();

    @GET("user/queryEnvironmentInfoMonByType/2")
    Call<EnvironmentInfoMon> queryEnvironmentInfoMonByType2();

    @GET("user/queryEnvironmentInfoMonByType/3")
    Call<EnvironmentInfoMon> queryEnvironmentInfoMonByType3();

    @GET("user/queryEnvironmentInfoMonByType/4")
    Call<EnvironmentInfoMon> queryEnvironmentInfoMonByType4();

    //获取监测的阈值
    @GET("user/getValue")
    Call<SetValue> getValue();


//    @GET("user/getFenergy")
//    Call<Machinerecord> getFenergy();
////    //保存设置
//    @GET("user/save")
//    Call<SetValue> save(SetValue.GetValueBean bean);

    @POST("user/save")
    Call<SetValue> save(@Body SetValue.GetValueBean bean);

    @GET("user/workShop/{workShop}/mname/{mname}")
    Call<Machinerecord> getFenergy(@Path("workShop") int workshop,
                                   @Path("mname") String mname);



}
