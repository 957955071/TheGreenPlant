package com.example.a95795.thegreenplant.HomeFragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a95795.thegreenplant.R;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonageFragment extends Fragment {

    // SpringView
    SpringView springView;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    //结束刷新动作
                    springView.onFinishFreshAndLoad();
                    //更新adapter
                    Toast.makeText(PersonageFragment.this.getActivity(),"刷新成功",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    public static PersonageFragment newInstance() {
        return new PersonageFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     View view = inflater.inflate(R.layout.fragment_personage, container, false);
        springView = (SpringView) view.findViewById(R.id.Spview_spbranchList);
        initData();
        initEvnet();
        return view;
    }
    private void initData() {

        springView.setHeader(new DefaultHeader(this.getActivity()));

    }

    private void initEvnet() {

        springView.setType(SpringView.Type.FOLLOW);

//        刷新和载入很多其它的事件处理
//        假设须要处理的话，仅仅需在代码中加入监听：
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                handler.sendEmptyMessageDelayed(0,1000);
            }

            @Override
            public void onLoadmore() {

            }
        });

    }


}
