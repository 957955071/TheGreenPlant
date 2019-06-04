package com.example.a95795.thegreenplant.HomeFragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.dynamicLineChart.DayDynamicLineChartFrament;
import com.example.a95795.thegreenplant.dynamicLineChart.MonDynamicLineChartFrament;
import com.example.a95795.thegreenplant.dynamicLineChart.WeekDynamicLineChartFrament;
import com.example.a95795.thegreenplant.table.DayTableFragment;
import com.example.a95795.thegreenplant.table.MonTableFragment;
import com.example.a95795.thegreenplant.table.WeekTableFragment;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnvironmentFragment extends SupportFragment implements View.OnClickListener {
    private int i = 1;
    private LinearLayout ll_chart_table;
    private ImageView imageView_ct;
    private TextView tv_ct;
    /** TextView选择框 */
    private TextView mSelectTv;
    /** popup窗口里的ListView */
    private ListView mTypeLv;
    /** popup窗口 */
    private PopupWindow typeSelectPopup;
    /** 模拟的假数据 */
    private List<String> testData;
    /** 数据适配器 */
    private ArrayAdapter<String> testDataAdapter;
    // SpringView
    SpringView springView;

    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";
    private FragmentManager fragmentManager;
    private Fragment currentFragment = new Fragment();
    private List<Fragment> fragments = new ArrayList<>();
    private int currentIndex = 0;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_environment, container, false);
        initView();
        initEvnet();
        fragmentManager = getFragmentManager();
        if (savedInstanceState != null) { // “内存重启”时调用
            //获取“内存重启”时保存的索引下标
            currentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT,0);

            fragments.removeAll(fragments);
            fragments.add(fragmentManager.findFragmentByTag(0+""));
            fragments.add(fragmentManager.findFragmentByTag(1+""));
            fragments.add(fragmentManager.findFragmentByTag(2+""));
            fragments.add(fragmentManager.findFragmentByTag(3+""));
            fragments.add(fragmentManager.findFragmentByTag(4+""));
            fragments.add(fragmentManager.findFragmentByTag(5+""));

            //恢复fragment页面
            restoreFragment();
        }else{      //正常启动时调用
            fragments.add(new DayDynamicLineChartFrament());//日图  0
            fragments.add(new WeekDynamicLineChartFrament());//周图  1
            fragments.add(new MonDynamicLineChartFrament());//月图  2
            fragments.add(new DayTableFragment());//日表  3
            fragments.add(new WeekTableFragment());//周表  4
            fragments.add(new MonTableFragment());//月表  5
            showFragment();
        }
        return view;
    }

    public static EnvironmentFragment newInstance() {
        return new EnvironmentFragment();
    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    //结束刷新动作
                    springView.onFinishFreshAndLoad();
                    //更新adapter
                    Toast.makeText(EnvironmentFragment.this.getActivity(),"刷新成功",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };




    private void initView(){
        ll_chart_table = view.findViewById(R.id.ll_chart_table);
        ll_chart_table.setOnClickListener(this);
        tv_ct = view.findViewById(R.id.tv_ct);
        imageView_ct = view.findViewById(R.id.img_ct);
        mSelectTv = (TextView) view.findViewById(R.id.tv_cycle);
        mSelectTv.setOnClickListener(this);

        springView = (SpringView) view.findViewById(R.id.Spview_spbranchList);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cycle:
                // 点击控件后显示popup窗口
                initSelectPopup();
                // 使用isShowing()检查popup窗口是否在显示状态
                if (typeSelectPopup != null && !typeSelectPopup.isShowing()) {
                    typeSelectPopup.showAsDropDown(mSelectTv, 0, 10);
                }
                break;
            case R.id.ll_chart_table:
                if(i==1){//在表格中
                    imageView_ct.setImageResource(R.drawable.tb);
                    tv_ct.setText("图表");
                    i=2;
                    currentIndex = 3;
                }else if(i==2){//在图表中
                    imageView_ct.setImageResource(R.drawable.line_chat);
                    tv_ct.setText("表格");
                    i=1;
                    currentIndex = 0;
                }
                break;
        }
        showFragment();
    }

    /**
     * 初始化popup窗口
     */
    private void initSelectPopup() {
        mTypeLv = new ListView(this.getContext());
        TestData();
        // 设置适配器
        testDataAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.popup_text_item, testData);
        mTypeLv.setAdapter(testDataAdapter);

        // 设置ListView点击事件监听
        mTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentIndex = position;
                switch (position){
                    case 0:
                        if(i==2) {
                            currentIndex = 3;
                        }else if(i==1){
                            currentIndex = 0;
                        }
                        break;
                    case 1:
                        if(i==2) {
                            currentIndex = 4;
                        }else if(i==1){
                            currentIndex = 1;
                        }
                        break;
                    case 2:
                        if(i==2) {
                            currentIndex = 5;
                        }else if(i==1){
                            currentIndex = 2;
                        }
                        break;
                }
                showFragment();
                // 在这里获取item数据
                String value = testData.get(position);
                // 把选择的数据展示对应的TextView上
                mSelectTv.setText(value);
                // 选择完后关闭popup窗口
                typeSelectPopup.dismiss();
            }
        });
        typeSelectPopup = new PopupWindow(mTypeLv, mSelectTv.getWidth(), ActionBar.LayoutParams.WRAP_CONTENT, true);
        // 取得popup窗口的背景图片
        Drawable drawable = ContextCompat.getDrawable(this.getContext(), R.drawable.bg_corner);
        typeSelectPopup.setBackgroundDrawable(drawable);
        typeSelectPopup.setFocusable(true);
        typeSelectPopup.setOutsideTouchable(true);
        typeSelectPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 关闭popup窗口
                typeSelectPopup.dismiss();
            }
        });
    }

    /**
     * 模拟假数据
     */
    private void TestData() {
        testData = new ArrayList<>();
        testData.add("日报表");
        testData.add("周报表");
        testData.add("月报表");
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
                    .add(R.id.fra_linechart,fragments.get(currentIndex),""+currentIndex);  //第三个参数为添加当前的fragment时绑定一个tag

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


}
