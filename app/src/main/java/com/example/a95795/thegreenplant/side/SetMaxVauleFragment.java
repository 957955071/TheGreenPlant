package com.example.a95795.thegreenplant.side;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.a95795.thegreenplant.HomeActivity;
import com.example.a95795.thegreenplant.R;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.yokeyword.fragmentation.SupportFragment;

public class SetMaxVauleFragment extends SupportFragment {
    private Button button;
    public static SetMaxVauleFragment newInstance() {
        return new SetMaxVauleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_maxvaule, container, false);
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
        return view;
    }
}
