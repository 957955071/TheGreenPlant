package com.example.a95795.thegreenplant.HomeFragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.custom.MyApplication;
import com.unstoppable.submitbuttonview.SubmitButton;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    private EditText et;
    private Button submit;
    private String feed;
    private boolean isback;
    SubmitButton sBtnLoading;
    private int MAX=1000;
    public static FeedbackFragment newInstance() {
        return new FeedbackFragment();
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        et=view.findViewById(R.id.useropinion_et);
        remaining=view.findViewById(R.id.useropinion_remaining);
        sBtnLoading=view.findViewById(R.id.submitbutton);
        Context ctx = getContext();
        SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
        final String name = sp.getString("STRING_KEY3","");
        final String userid = sp.getString("STRING_KEY4","");
        //submit=view.findViewById(R.id.useropinion_submit);
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
                            String url = getString(R.string.ip) + "user/LogAdd";
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


//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


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

    }
}
