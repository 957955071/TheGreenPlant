package com.example.a95795.thegreenplant.HomeFragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.transition.Fade;
import android.support.transition.Slide;
import android.support.v4.app.Fragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.a95795.thegreenplant.HomeActivity;
import com.example.a95795.thegreenplant.MainActivity;
import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.adapter.MyPagerAdapter;
import com.example.a95795.thegreenplant.custom.Id;
import com.example.a95795.thegreenplant.custom.MyApplication;
import com.example.a95795.thegreenplant.custom.StatusBarCompat;
import com.example.a95795.thegreenplant.custom.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends SupportFragment {

    private BottomBar mBottomBar;
    private EnvironmentFragment environmentFragment;
    private EquipmentFragment equipmentFragment;
    private Information information;
    private ViewPager viewPager;
    private CoordinatorLayout mCoordinator;
    private List<Fragment> fragmentList;



    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //沉浸式状态栏
        StatusBarCompat.compat(HomeFragment.this.getActivity(), Color.parseColor("#FF16A295"));
        mCoordinator = (CoordinatorLayout) view.findViewById(R.id.mCoordinator);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        initViewPager();
        mBottomBar = BottomBar.attachShy(mCoordinator, viewPager, savedInstanceState);
        mBottomBar.setItems(R.menu.navigation);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                //单击事件 menuItemId 是 R.menu.bottombar_menu 中 item 的 id
                switch (menuItemId) {

                    case R.id.navigation_home:
                        viewPager.setCurrentItem(0);
                        ((HomeActivity ) getActivity()).test(1);
                        break;
                    case R.id.navigation_dashboard:
                        viewPager.setCurrentItem(1);
                        ((HomeActivity ) getActivity()).test(2);
                        break;
                    case R.id.navigation_notifications:
                        viewPager.setCurrentItem(2);
                        ((HomeActivity ) getActivity()).test(3);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });
        return view;

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //保存BottomBar的状态
        mBottomBar.onSaveInstanceState(outState);
    }


    private void initViewPager() {
        fragmentList = new ArrayList<>();
        fragmentList.add(null == equipmentFragment ? equipmentFragment = EquipmentFragment.newInstance() : equipmentFragment);
        fragmentList.add(null == environmentFragment ? environmentFragment = EnvironmentFragment.newInstance() : environmentFragment);
        fragmentList.add(null == information ? information = Information.newInstance() : information);
        viewPager.setAdapter(new MyPagerAdapter(getFragmentManager(), fragmentList));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomBar.selectTabAtPosition(position, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }



}
