package com.example.a95795.thegreenplant.dynamicLineChart;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.a95795.thegreenplant.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class WeekDynamicLineChartFrament extends Fragment implements View.OnClickListener{
    private LinearLayout ll_wshop1,ll_wshop2,ll_wshop3,ll_wshop4;
    private View view_wshop1,view_wshop2,view_wshop3,view_wshop4;
    private View view;

    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW_WEEK";
    private FragmentManager fragmentManager;
    private Fragment currentFragment = new Fragment();
    private List<Fragment> fragments = new ArrayList<>();
    private int currentIndex = 0;
    private int work, workShop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dynamic_linechart_week, container, false);
        initView();
        fragmentManager = getFragmentManager();
        if (savedInstanceState != null) { // “内存重启”时调用
            //获取“内存重启”时保存的索引下标
            currentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT,0);

            fragments.removeAll(fragments);
            fragments.add(fragmentManager.findFragmentByTag(0+""));
            fragments.add(fragmentManager.findFragmentByTag(1+""));
            fragments.add(fragmentManager.findFragmentByTag(2+""));
            fragments.add(fragmentManager.findFragmentByTag(3+""));

            //恢复fragment页面
            restoreFragment();
        }else{      //正常启动时调用
            fragments.add(new WeekWorkShop1ChartFragment());
            fragments.add(new WeekWorkShop2ChartFragment());
            fragments.add(new WeekWorkShop3ChartFragment());
            fragments.add(new WeekWorkShop4ChartFragment());
            isWork();
            showFragment();
        }
        return view;
    }

    public void initView(){
        ll_wshop1 = view.findViewById(R.id.ll_wshop1);
        view_wshop1 = view.findViewById(R.id.view_wshop1);
        ll_wshop1.setOnClickListener(this);

        ll_wshop2 = view.findViewById(R.id.ll_wshop2);
        view_wshop2 = view.findViewById(R.id.view_wshop2);
        ll_wshop2.setOnClickListener(this);

        ll_wshop3 = view.findViewById(R.id.ll_wshop3);
        view_wshop3 = view.findViewById(R.id.view_wshop3);
        ll_wshop3.setOnClickListener(this);

        ll_wshop4 = view.findViewById(R.id.ll_wshop4);
        view_wshop4 = view.findViewById(R.id.view_wshop4);
        ll_wshop4.setOnClickListener(this);

        //可以通过缓存数据  来使得全局软件获得数据
        Context ctx = WeekDynamicLineChartFrament.this.getActivity();
        SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
        workShop = sp.getInt("STRING_KEY5", 0);//获取车间
        work = sp.getInt("STRING_KEY2", 0);//获取用户管理权限
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        //“内存重启”时保存当前的fragment名字
        outState.putInt(CURRENT_FRAGMENT,currentIndex);
        super.onSaveInstanceState(outState);
    }

    /**
     * 使用show() hide()切换页面
     * 显示fragment
     */
    private void showFragment(){

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //如果之前没有添加过
        if(!fragments.get(currentIndex).isAdded()){
            transaction
                    .hide(currentFragment)
                    .add(R.id.fra_chart_workshop_week,fragments.get(currentIndex),""+currentIndex);  //第三个参数为添加当前的fragment时绑定一个tag

        }else{
            transaction
                    .hide(currentFragment)
                    .show(fragments.get(currentIndex));
        }

        currentFragment = fragments.get(currentIndex);

        transaction.commit();

    }

    /**
     * 恢复fragment
     */
    private void restoreFragment(){

        FragmentTransaction mBeginTreansaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {

            if(i == currentIndex){
                mBeginTreansaction.show(fragments.get(i));
            }else{
                mBeginTreansaction.hide(fragments.get(i));
            }
        }

        mBeginTreansaction.commit();

        //把当前显示的fragment记录下来
        currentFragment = fragments.get(currentIndex);

    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_wshop1:
                if (work==1) {
                    view_wshop1.setBackground(getResources().getDrawable(R.drawable.shape_round_orange));
                    view_wshop2.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    view_wshop3.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    view_wshop4.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    currentIndex = 0;
                }else if(work==0&&workShop!=1){
                    Toast.makeText(WeekDynamicLineChartFrament.this.getActivity(), getString(R.string.power), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ll_wshop2:
                if (work==1){
                    view_wshop1.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    view_wshop2.setBackground(getResources().getDrawable(R.drawable.shape_round_orange));
                    view_wshop3.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    view_wshop4.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    currentIndex = 1;
                }else if (work==0&&workShop!=2){
                    Toast.makeText(WeekDynamicLineChartFrament.this.getActivity(), getString(R.string.power), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ll_wshop3:
                if (work==1){
                    view_wshop1.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    view_wshop2.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    view_wshop3.setBackground(getResources().getDrawable(R.drawable.shape_round_orange));
                    view_wshop4.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    currentIndex = 2;
                }else if (work==0&&workShop!=3){
                    Toast.makeText(WeekDynamicLineChartFrament.this.getActivity(), getString(R.string.power), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ll_wshop4:
                if (work==1){
                    view_wshop1.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    view_wshop2.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    view_wshop3.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    view_wshop4.setBackground(getResources().getDrawable(R.drawable.shape_round_orange));
                    currentIndex = 3;
                }else if (work==0&&workShop!=4){
                    Toast.makeText(WeekDynamicLineChartFrament.this.getActivity(), getString(R.string.power), Toast.LENGTH_LONG).show();
                }
                break;
        }
        showFragment();
    }
    @SuppressWarnings("deprecation")
    //权限设定
    public void isWork(){
        //非管理员权限
        if(work==0){
            switch (workShop){
                case 1:
                    currentIndex=0;
                    view_wshop1.setBackground(getResources().getDrawable(R.drawable.shape_round_orange));
                    view_wshop2.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    view_wshop3.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    view_wshop4.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    break;
                case 2:
                    currentIndex=1;
                    view_wshop1.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    view_wshop2.setBackground(getResources().getDrawable(R.drawable.shape_round_orange));
                    view_wshop3.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    view_wshop4.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    break;
                case 3:
                    currentIndex=2;
                    view_wshop1.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    view_wshop2.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    view_wshop3.setBackground(getResources().getDrawable(R.drawable.shape_round_orange));
                    view_wshop4.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    break;
                case 4:
                    currentIndex=3;
                    view_wshop1.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    view_wshop2.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    view_wshop3.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    view_wshop4.setBackground(getResources().getDrawable(R.drawable.shape_round_orange));
                    break;
            }
        }
    }

}
