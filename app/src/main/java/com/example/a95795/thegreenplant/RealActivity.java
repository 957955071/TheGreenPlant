package com.example.a95795.thegreenplant;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.a95795.thegreenplant.HomeFragment.EnvironmentFragment;
import com.example.a95795.thegreenplant.real.RealBean;
import com.example.a95795.thegreenplant.real.RealItemAdaper;
import com.example.a95795.thegreenplant.tools.CircleImageView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class RealActivity extends SwipeBackActivity {
    private String city = "chengde";
    private String json;
    private JSONArray HeWeather6;
    private JSONObject now;
    private TextView tv_pm1,tv_tmp1,tv_hum1,tv_pm2,tv_tmp2,tv_hum2;
    private TextView tv_pm3,tv_tmp3,tv_hum3,tv_pm4,tv_tmp4,tv_hum4;
    private int pm1,tmp1,hum1,pm2,tmp2,hum2;
    private int pm3,tmp3,hum3,pm4,tmp4,hum4;
    private String TAG = "volley";
    private RecyclerView recyclerView;
    private List<RealBean> realList = new ArrayList<>();
    private ImageView bgPm,bgTmp,bgHum,bgPm2,bgTmp2,bgHum2;
    private ImageView bgPm3,bgTmp3,bgHum3,bgPm4,bgTmp4,bgHum4;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_real_shopwork);
        initView();
        startThread();
    }

    private void initView(){
        recyclerView = (RecyclerView) findViewById(R.id.rview_real);
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
        String url = "https://free-api.heweather.net/s6/weather//now?location="+city+"&key="+getString(R.string.key);
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
        String url = "https://free-api.heweather.net/s6/weather//now?location=beijing&key="+getString(R.string.key);
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

    public void isEvent(int p,int t,int h,ImageView img1,ImageView img2,ImageView img3){
        if(p>=0&&p<=50){
            img1.setImageResource(R.drawable.green);
        }else if(p>50&&p<=100){
            img1.setImageResource(R.drawable.yellow);
        }else{
            img1.setImageResource(R.drawable.red);
        }

        if(t>18&&t<=25){
            img2.setImageResource(R.drawable.green);
        }else if((t>13&&t<=18)||(t>25&&t<=30)){
            img2.setImageResource(R.drawable.yellow);
        }else{
            img2.setImageResource(R.drawable.red);
        }

        if(h>30&&h<=60){
            img3.setImageResource(R.drawable.green);
        }else if((h>10&&h<=30)||(h>60&&h<=80)){
            img3.setImageResource(R.drawable.yellow);
        }else{
            img3.setImageResource(R.drawable.red);
        }
    }




}
