package com.example.a95795.thegreenplant.HomeFragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.adapter.FeedbackAdapter;
import com.example.a95795.thegreenplant.custom.Feedback;
import com.example.a95795.thegreenplant.custom.MyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unstoppable.submitbuttonview.SubmitButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends SwipeBackFragment implements ISupportFragment, SubmitButton.OnResultEndListener {

    private ImageView iv;
    private LinearLayout ll;
    private TextView remaining, tv;
    private RelativeLayout relativeLayout;
    private EditText et;
    private Button submit;
    private String feed;
    private boolean isback;
    private ListView listView;
    SubmitButton sBtnLoading,sBtnLoading2,sBtnLoading3;
    private int MAX=100;
    public static FeedbackFragment newInstance() {
        return new FeedbackFragment();
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        et=view.findViewById(R.id.useropinion_et);
        relativeLayout=view.findViewById(R.id.feedbackRelativeLayout);
        sBtnLoading=view.findViewById(R.id.submitbutton);
        sBtnLoading2 = view.findViewById(R.id.button_feed_true);
        sBtnLoading3 = view.findViewById(R.id.button_feed_false);
        listView = view.findViewById(R.id.feedlistview);
        relativeLayout = view.findViewById(R.id.feedbackRelativeLayout);
        sBtnLoading3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sBtnLoading3.doResult(true);
                sBtnLoading.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                sBtnLoading3.setVisibility(View.GONE);
                sBtnLoading2.setVisibility(View.VISIBLE);
            }
        });
        sBtnLoading2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sBtnLoading2.doResult(true);
                        sBtnLoading.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.GONE);
                        sBtnLoading3.setVisibility(View.VISIBLE);
                        sBtnLoading2.setVisibility(View.GONE);
                        String url = getString(R.string.ip) + "user/FeedbackAll";
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                Request.Method.POST,
                                url,
                                "",
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            Gson gson = new Gson();
                                            List<Feedback> subjectList = gson.fromJson(response.getJSONArray("Feedback").toString(),new TypeToken<List<Feedback>>(){}.getType());
                                            FeedbackAdapter feedbackAdapter = new FeedbackAdapter(FeedbackFragment.this.getActivity(), R.layout.feedback,subjectList);
                                            listView = (ListView) getView().findViewById(R.id.listfeed);
                                            listView.setAdapter(feedbackAdapter);
                                            listView.setVisibility(View.VISIBLE);
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
                }, 2000);


            }
        });
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    remaining.setText("你还可以输入"+(MAX-s.toString().length())+"个字");


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sBtnLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (et.getText().toString().isEmpty()) {
                    sBtnLoading.doResult(false);
                    new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("警告")
                            .setContentText("反馈栏为空，请重新输入并提交")
                            .setConfirmText("确定")
                            .show();
                } else {
                    feed = et.getText().toString();
                    new Handler().postDelayed(new Runnable() {
                        Context ctx = getContext();
                        SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
                        String name = sp.getString("STRING_KEY3","");
                        String userid = sp.getString("STRING_KEY4","");
                        @Override
                        public void run() {

                            sBtnLoading.doResult(true);
                            new SweetAlertDialog(FeedbackFragment.this.getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setContentText("您的反馈已提交完成，我们尽快处理问题")
                                    .setConfirmText("确定")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                            SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
                            String ee = dff.format(new Date());
                            String url = getString(R.string.ip) + "user/FeedbackAdd";
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                    Request.Method.POST,
                                    url,
                                    "{\n" +
                                            "\t \"userId\": \""+userid+"\",\n" +
                                            "            \"userName\": \""+name+"\",\n" +
                                            "            \"date\": \""+ee+"\",\n" +
                                            "            \"userFeedback\": \""+feed+"\"\n" +
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
                    }, 2000);
                }
            }
        });
        sBtnLoading.setOnResultEndListener(this);
        sBtnLoading2.setOnResultEndListener(this);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback, container, false);
    }


    @Override
    public void onResultEnd() {
        sBtnLoading.reset();
        sBtnLoading2.reset();
        sBtnLoading3.reset();

    }
}
