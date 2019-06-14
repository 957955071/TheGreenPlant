package com.example.a95795.thegreenplant.side;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.a95795.thegreenplant.HomeActivity;
import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.bean.EnvironmentInfoDay;
import com.example.a95795.thegreenplant.bean.SetValue;
import com.example.a95795.thegreenplant.tools.OpenApiManager;
import com.example.a95795.thegreenplant.tools.OpenApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.yokeyword.fragmentation.SupportFragment;
import retrofit2.Call;
import retrofit2.Callback;

public class SetMaxVauleFragment extends SupportFragment {
    private Button button;
    private String TAG = "volley";
    private OpenApiService openApiService = null;
    private int pm_min,pm_max,pm_diff;
    private int tmp_min,tmp_max,tmp_diff;
    private int hum_min,hum_max,hum_diff;
    private EditText ed_min_pm,ed_max_pm,ed_diff_pm;
    private EditText ed_min_hum,ed_max_hum,ed_diff_hum;
    private EditText ed_min_tmp,ed_max_tmp,ed_diff_tmp;
    private View view;
    public static SetMaxVauleFragment newInstance() {
        return new SetMaxVauleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_set_maxvaule, container, false);
        initView();
        getData();
        return view;
    }

    private void initView(){
        ed_min_pm = view.findViewById(R.id.ed_min_pm);
        ed_max_pm = view.findViewById(R.id.ed_max_pm);
        ed_diff_pm = view.findViewById(R.id.ed_diff_pm);

        ed_min_hum = view.findViewById(R.id.ed_min_hum);
        ed_max_hum = view.findViewById(R.id.ed_max_hum);
        ed_diff_hum = view.findViewById(R.id.ed_diff_hum);

        ed_min_tmp = view.findViewById(R.id.ed_min_tmp);
        ed_max_tmp = view.findViewById(R.id.ed_max_tmp);
        ed_diff_tmp = view.findViewById(R.id.ed_diff_tmp);

        button = view.findViewById(R.id.btn_value_bc);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(SetMaxVauleFragment.this.getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                        .setContentText("您的更改已经保存！")
                        .setConfirmText("确定")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });
    }

    public void getData() {
        openApiService = OpenApiManager.createOpenApiService();
        Call<SetValue> einfo= openApiService.getValue();

        //step4:通过异步获取数据
        einfo.enqueue(new Callback<SetValue>() {
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
                ed_min_pm.setText(pm_min+"");
                ed_max_pm.setText(pm_max+"");
                ed_diff_pm.setText(pm_diff+"");
                ed_min_tmp.setText(tmp_min+"");
                ed_max_tmp.setText(tmp_max+"");
                ed_diff_tmp.setText(tmp_diff+"");
                ed_min_hum.setText(hum_min+"");
                ed_max_hum.setText(hum_max+"");
                ed_diff_hum.setText(hum_diff+"");
            }
            @Override
            public void onFailure(Call<SetValue> call, Throwable t) {
                Log.e(TAG, "onFailure: 请求失败！！！！~~~~~" );
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });
    }

}


