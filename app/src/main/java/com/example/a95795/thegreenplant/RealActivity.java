package com.example.a95795.thegreenplant;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.a95795.thegreenplant.HomeFragment.EnvironmentFragment;
import com.example.a95795.thegreenplant.bean.SetValue;
import com.example.a95795.thegreenplant.real.RealBean;
import com.example.a95795.thegreenplant.real.RealItemAdaper;
import com.example.a95795.thegreenplant.side.SetMaxVauleFragment;
import com.example.a95795.thegreenplant.tools.CircleImageView;
import com.example.a95795.thegreenplant.tools.MyBroadcastReceiver;
import com.example.a95795.thegreenplant.tools.OpenApiManager;
import com.example.a95795.thegreenplant.tools.OpenApiService;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import retrofit2.Call;
import retrofit2.Callback;

public class RealActivity extends SwipeBackActivity {
    private String json;
    private JSONArray HeWeather6;
    private JSONObject now;
    private TextView tv_pm1,tv_tmp1,tv_hum1,tv_pm2,tv_tmp2,tv_hum2;
    private TextView tv_pm3,tv_tmp3,tv_hum3,tv_pm4,tv_tmp4,tv_hum4;
    private TextView tv_now_pm,tv_now_tmp,tv_now_hum;
    private int pm1,tmp1,hum1,pm2,tmp2,hum2;
    private int pm3,tmp3,hum3,pm4,tmp4,hum4;
    private String TAG = "volley";
    private ImageView bgPm,bgTmp,bgHum,bgPm2,bgTmp2,bgHum2;
    private ImageView bgPm3,bgTmp3,bgHum3,bgPm4,bgTmp4,bgHum4;
    private ImageView back;
    private int pm_min,pm_max,pm_diff;
    private int tmp_min,tmp_max,tmp_diff;
    private int hum_min,hum_max,hum_diff;
    private OpenApiService openApiService = OpenApiManager.createOpenApiService();
    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_real_shopwork);
        initView();
        startThread();
    }

    private void initView(){
        back = (ImageView) findViewById(R.id.iv_back);
        bgPm = (ImageView) findViewById(R.id.bg_pm);
        bgHum = (ImageView) findViewById(R.id.bg_hum);
        bgTmp = (ImageView) findViewById(R.id.bg_tmp);
        tv_pm1 = (TextView) findViewById(R.id.tv_pm_value);
        tv_tmp1 = (TextView) findViewById(R.id.tv_tmp_value);
        tv_hum1 = (TextView) findViewById(R.id.tv_hum_value);

        bgPm2 = (ImageView) findViewById(R.id.bg_pm2);
        bgHum2 = (ImageView) findViewById(R.id.bg_hum2);
        bgTmp2 = (ImageView) findViewById(R.id.bg_tmp2);
        tv_pm2 = (TextView) findViewById(R.id.tv_pm_value2);
        tv_tmp2 = (TextView) findViewById(R.id.tv_tmp_value2);
        tv_hum2 = (TextView) findViewById(R.id.tv_hum_value2);

        bgPm3 = (ImageView) findViewById(R.id.bg_pm3);
        bgHum3 = (ImageView) findViewById(R.id.bg_hum3);
        bgTmp3 = (ImageView) findViewById(R.id.bg_tmp3);
        tv_pm3 = (TextView) findViewById(R.id.tv_pm_value3);
        tv_tmp3 = (TextView) findViewById(R.id.tv_tmp_value3);
        tv_hum3 = (TextView) findViewById(R.id.tv_hum_value3);

        bgPm4 = (ImageView) findViewById(R.id.bg_pm4);
        bgHum4 = (ImageView) findViewById(R.id.bg_hum4);
        bgTmp4 = (ImageView) findViewById(R.id.bg_tmp4);
        tv_pm4 = (TextView) findViewById(R.id.tv_pm_value4);
        tv_tmp4 = (TextView) findViewById(R.id.tv_tmp_value4);
        tv_hum4 = (TextView) findViewById(R.id.tv_hum_value4);

        tv_now_hum = (TextView) findViewById(R.id.tv_now_hum);
        tv_now_tmp = (TextView) findViewById(R.id.tv_now_tmp);
        tv_now_pm = (TextView) findViewById(R.id.tv_now_pm);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e(TAG, "onBackPressed: 按下了返回键");
    }

    public void getData() {
        String url = "https://free-api.heweather.net/s6/weather//now?location=chengde&key="+getString(R.string.key);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                "{}",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        json = response.toString();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            HeWeather6  = jsonObject.getJSONArray("HeWeather6");

                            for(int i=0;i<HeWeather6.length();i++){
                                now = HeWeather6.getJSONObject(i).getJSONObject("now");

                            }
                            hum1 = now.getInt("hum");//湿度
                            tmp1 = now.getInt("tmp");//温度
                            pm1 = now.getInt("cloud");//pm2.5

                            tv_pm1.setText(pm1+"");
                            tv_tmp1.setText(tmp1+"");
                            tv_hum1.setText(hum1+"");
                            isEvent(pm1,tmp1,hum1,bgPm,bgTmp,bgHum);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });
        RequestQueue v = Volley.newRequestQueue(this);
        v.add(request);
    }

    public void getData2() {
        String url = "https://free-api.heweather.net/s6/weather//now?location=shanghai&key="+getString(R.string.key);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                "{}",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        json = response.toString();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            HeWeather6  = jsonObject.getJSONArray("HeWeather6");

                            for(int i=0;i<HeWeather6.length();i++){
                                now = HeWeather6.getJSONObject(i).getJSONObject("now");

                            }
                            hum2 = now.getInt("hum");//湿度
                            tmp2 = now.getInt("tmp");//温度
                            pm2 = now.getInt("cloud");//pm2.5
                            tv_pm2.setText(pm2+"");
                            tv_tmp2.setText(tmp2+"");
                            tv_hum2.setText(hum2+"");
                            isEvent(pm2,tmp2,hum2,bgPm2,bgTmp2,bgHum2);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });
        RequestQueue v = Volley.newRequestQueue(this);
        v.add(request);
    }
    public void getData3() {
        String url = "https://free-api.heweather.net/s6/weather//now?location=guilin&key="+getString(R.string.key);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                "{}",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        json = response.toString();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            HeWeather6  = jsonObject.getJSONArray("HeWeather6");

                            for(int i=0;i<HeWeather6.length();i++){
                                now = HeWeather6.getJSONObject(i).getJSONObject("now");

                            }
                            hum3 = now.getInt("hum");//湿度
                            tmp3 = now.getInt("tmp");//温度
                            pm3 = now.getInt("cloud");//pm2.5
                            tv_pm3.setText(pm3+"");
                            tv_tmp3.setText(tmp3+"");
                            tv_hum3.setText(hum3+"");
                            isEvent(pm3,tmp3,hum3,bgPm3,bgTmp3,bgHum3);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });
        RequestQueue v = Volley.newRequestQueue(this);
        v.add(request);
    }

    public void getData4() {
        String url = "https://free-api.heweather.net/s6/weather//now?location=nanjing&key="+getString(R.string.key);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                "{}",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        json = response.toString();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            HeWeather6  = jsonObject.getJSONArray("HeWeather6");

                            for(int i=0;i<HeWeather6.length();i++){
                                now = HeWeather6.getJSONObject(i).getJSONObject("now");

                            }
                            hum4 = now.getInt("hum");//湿度
                            tmp4 = now.getInt("tmp");//温度
                            pm4 = now.getInt("cloud");//pm2.5
                            tv_pm4.setText(pm4+"");
                            tv_tmp4.setText(tmp4+"");
                            tv_hum4.setText(hum4+"");
                            isEvent(pm4,tmp4,hum4,bgPm4,bgTmp4,bgHum4);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });
        RequestQueue v = Volley.newRequestQueue(this);
        v.add(request);
    }

    public void startThread(){
        new Thread(new Runnable(){
            public void run() {
                while (true) {
                    try {
                        getData();
                        getData2();
                        getData3();
                        getData4();
                        Thread.sleep(800000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    public void isEvent(final int p, final int t, final int h, final ImageView img1, final ImageView img2, final ImageView img3){
        Call<SetValue> call=openApiService.getValue();

        call.enqueue(new Callback<SetValue>() {
            @Override
            public void onResponse(Call<SetValue> call, retrofit2.Response<SetValue> response) {
                Log.e(TAG, "onResponse: 请求成功！！！" );
                SetValue sv=response.body();
                for(int j=0;j<sv.getGetValue().size();j++){
                    pm_min = sv.getGetValue().get(j).getPmMin();
                    pm_max = sv.getGetValue().get(j).getPmMax();
                    pm_diff = sv.getGetValue().get(j).getPmDiff();
                    tmp_min = sv.getGetValue().get(j).getTmpMim();
                    tmp_max = sv.getGetValue().get(j).getTmpMax();
                    tmp_diff = sv.getGetValue().get(j).getTmpDiff();
                    hum_min = sv.getGetValue().get(j).getHumMin();
                    hum_max = sv.getGetValue().get(j).getHumMax();
                    hum_diff = sv.getGetValue().get(j).getHumDiff();
                }
                tv_now_hum.setText("当前湿度指标："+hum_min+"RH～"+hum_max+"RH，偏差值:"+hum_diff);
                tv_now_tmp.setText("当前温度指标："+tmp_min+"℃～"+tmp_max+"℃，偏差值:"+tmp_diff);
                tv_now_pm.setText("当前PM2.5指标："+pm_min+"ug/m3～"+pm_max+"ug/m3，偏差值:"+pm_diff);

                if(p>=pm_min&&p<=pm_max){
                    img1.setImageResource(R.drawable.green);
                }else if((p>pm_min-pm_diff&&t<=pm_min)||(p>pm_max&&p<=pm_max+pm_diff)){
                    img1.setImageResource(R.drawable.yellow);

                }else{
                    img1.setImageResource(R.drawable.red);

                }

                if(t>tmp_min&&t<=tmp_max){
                    img2.setImageResource(R.drawable.green);
                }else if((t>tmp_min-tmp_diff&&t<=tmp_min)||(t>tmp_max&&t<=tmp_max+tmp_diff)){
                    img2.setImageResource(R.drawable.yellow);
                }else{
                    img2.setImageResource(R.drawable.red);
                }

                if(h>hum_min&&h<=hum_max){
                    img3.setImageResource(R.drawable.green);
                }else if((h>hum_min-hum_diff&&h<=hum_min)||(h>hum_max&&h<=hum_max+hum_diff)){
                    img3.setImageResource(R.drawable.yellow);
                }else{
                    img3.setImageResource(R.drawable.red);
                }
            }

            @Override
            public void onFailure(Call<SetValue> call, Throwable t) {
                Log.e(TAG, "onFailure: 请求失败！！！！~~~~~" );
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });
    }


}
