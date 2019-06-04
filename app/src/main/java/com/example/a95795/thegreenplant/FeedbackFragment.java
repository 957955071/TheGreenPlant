package com.example.a95795.thegreenplant;


import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
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


import com.unstoppable.submitbuttonview.SubmitButton;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;



/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends SwipeBackFragment implements ISupportFragment, SubmitButton.OnResultEndListener {

    private ImageView iv;
    private LinearLayout ll;
    private TextView remaining, tv;
    private EditText et;
    private Button submit;
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
