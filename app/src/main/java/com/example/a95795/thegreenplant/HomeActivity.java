package com.example.a95795.thegreenplant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.example.a95795.thegreenplant.HomeFragment.FeedbackFragment;
import com.example.a95795.thegreenplant.HomeFragment.HomeFragment;
import com.example.a95795.thegreenplant.HomeFragment.OperationLogFragment;
import com.example.a95795.thegreenplant.adapter.BoosWorkshopAdapter;
import com.example.a95795.thegreenplant.custom.LocationService;
import com.example.a95795.thegreenplant.custom.SecretTextView;
import com.example.a95795.thegreenplant.custom.StatusBarCompat;
import com.example.a95795.thegreenplant.side.AboutFragment;
import com.example.a95795.thegreenplant.side.BoosWorkshopInformationFragmentFragment;
import com.example.a95795.thegreenplant.side.SetMaxVauleFragment;
import com.example.a95795.thegreenplant.side.WorkshopInformationFragment;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;

import static com.mob.MobSDK.getContext;

public class HomeActivity extends SupportActivity

        implements NavigationView.OnNavigationItemSelectedListener {
    SecretTextView secretTextView;
    ImageView imageView;

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRDLY = 2;
    public static final int FOURTHLY = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;

    private LocationService locationService;
    private TextView mTextView;
    private int postion = 0;
    private TextView textView;


    private SupportFragment[] mFragments = new SupportFragment[7];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Context ctx = getContext();
        SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
        String name = sp.getString("STRING_KEY3","");

        imageView = findViewById(R.id.imageView3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        //沉浸式状态栏
        StatusBarCompat.compat(this, Color.parseColor("#FF16A295"));
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //这里基本都是侧滑菜单
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        locationService = new LocationService(this);

//        多个activity
//        locationService = ((App) getApplication()).locationService;
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();

        //设置顶部姓名
        View headerview = navigationView.getHeaderView(0);
        textView = headerview.findViewById(R.id.username);
        textView.setText(name);

        View headerview2 = navigationView.getHeaderView(0);
        mTextView = headerview2.findViewById(R.id.price);

        SupportFragment firstFragment = findFragment(HomeFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = HomeFragment.newInstance();
            mFragments[SECOND] = AboutFragment.newInstance();
            mFragments[THIRDLY] = WorkshopInformationFragment.newInstance();
            mFragments[FOURTHLY] = SetMaxVauleFragment.newInstance();
            mFragments[FOUR] = FeedbackFragment.newInstance();
            mFragments[FIVE] = OperationLogFragment.newInstance();
            mFragments[SIX] = BoosWorkshopInformationFragmentFragment.newInstance();
            loadMultipleRootFragment(R.id.home, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRDLY],
                    mFragments[FOURTHLY],
                    mFragments[FOUR],
                    mFragments[FIVE],
                    mFragments[SIX]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = findFragment(AboutFragment.class);
            mFragments[THIRDLY] = findFragment(WorkshopInformationFragment.class);
            mFragments[FOURTHLY] = findFragment(SetMaxVauleFragment.class);
            mFragments[FOUR] = findFragment(FeedbackFragment.class);
            mFragments[FIVE] = findFragment(OperationLogFragment.class);
            mFragments[SIX] = findFragment(BoosWorkshopInformationFragmentFragment.class);
        }



        //使用开源控件SecretTextView实现文字的渐变
        secretTextView = (SecretTextView)findViewById(R.id.textView);
        secretTextView.setDuration(1000);
        secretTextView.setIsVisible(true);
        //实时视图的按钮
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, RealActivity.class);
                startActivity(intent);
            }
        });



    }

//一个简单的退出软件提示
    @Override
    public void onBackPressedSupport() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(postion!=0) {
            showHideFragment(mFragments[0], mFragments[postion]);
            postion = 0;
            secretTextView.hide();
            secretTextView.setText("首页");
            secretTextView.show();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示：");
            builder.setMessage("您确定退出？");
            //设置确定按钮
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            //设置取消按钮
            builder.setPositiveButton("取消", null);
            //显示提示框
            builder.show();
        }
    }
    //侧滑菜单的点击事件

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Context ctx = getContext();
        SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
        int Workshop = sp.getInt("STRING_KEY2",0);
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id==R.id.homeitem){
            showHideFragment(mFragments[0], mFragments[postion]);
            postion = 0;
            secretTextView.hide();
            secretTextView.setText("首页");
            secretTextView.show();

        } else if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {
            if(Workshop==0){
                new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("警告")
                        .setContentText("抱歉，你没有权利使用此功能")
                        .setConfirmText("确定")
                        .show();
            }else {
                showHideFragment(mFragments[2], mFragments[postion]);
                test(4);
                postion = 2;
            }
        } else if (id == R.id.nav_slideshow) {//设置

            showHideFragment(mFragments[6], mFragments[postion]);
            test(6);
            postion = 6;

        } else if (id == R.id.nav_manage) {//用户反馈
            showHideFragment(mFragments[4], mFragments[postion]);
            test(7);
            postion = 4;

        } else if (id == R.id.nav_share) {
            showHideFragment(mFragments[1], mFragments[postion]);
            postion = 1;
            test(5);
        } else if (id == R.id.nav_send) {
            new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setContentText("您的版本已经是最新版2.0")
                    .setConfirmText("确定")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }else if (id == R.id.feed) {
            if(Workshop==0){
                new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("警告")
                        .setContentText("抱歉，你没有权利使用此功能")
                        .setConfirmText("确定")
                        .show();
            }else {

                showHideFragment(mFragments[5], mFragments[postion]);
                test(8);
                postion = 5;

            }

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //设置顶部标题的修改
    public void test(int number) {
        switch (number) {
            case 1:
                imageView.setVisibility(View.GONE);
                secretTextView.hide();
                secretTextView.setText("设备监测");
                secretTextView.show();

                break;
            case 2:

                imageView.setVisibility(View.VISIBLE);
                secretTextView.hide();
                secretTextView.setText("环境监测");
                secretTextView.show();

                break;
            case 3:
                imageView.setVisibility(View.GONE);
                secretTextView.hide();
                secretTextView.setText("个人中心");
                secretTextView.show();
                break;
            case 4:
                imageView.setVisibility(View.GONE);
                secretTextView.hide();
                secretTextView.setText("车间人员信息");
                secretTextView.show();
                break;
            case 5:
                imageView.setVisibility(View.GONE);
                secretTextView.hide();
                secretTextView.setText("关于车间");
                secretTextView.show();
                break;
            case 6:
                imageView.setVisibility(View.GONE);
                secretTextView.hide();
                secretTextView.setText("监测设置");
                secretTextView.show();
                break;
            case 7:
                imageView.setVisibility(View.GONE);
                secretTextView.hide();
                secretTextView.setText("用户反馈");
                secretTextView.show();
                break;
            case 8:
                imageView.setVisibility(View.GONE);
                secretTextView.hide();
                secretTextView.setText("操作日志");
                secretTextView.show();
                break;
            default:
                break;
        }
    }

//一个简单的fragment跳转 使用replace
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.home, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                mTextView.setText(location.getAddrStr());
                //超级精确！！！！！以下方法--》周边信息
             /*   if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }*/
            }
        }

    };



}
