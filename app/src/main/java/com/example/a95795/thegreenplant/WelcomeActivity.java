package com.example.a95795.thegreenplant;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class WelcomeActivity extends AppIntro {

    @Override
    public void init(@Nullable Bundle savedInstanceState) {


        addSlide(AppIntroFragment.newInstance("数据分析", "图表显示，分析更精确", R.drawable.data,(getResources().getColor(R.color.colo))));
        addSlide(AppIntroFragment.newInstance("状态操作", "实时状态，查看更方便", R.drawable.status,(getResources().getColor(R.color.colo))));
        addSlide(AppIntroFragment.newInstance("状态切换", "一键切换，操作更便捷", R.drawable.switchs,(getResources().getColor(R.color.colo))));
        addSlide(AppIntroFragment.newInstance("开始吧", "进入智能绿色工厂时代", R.drawable.intelligent,(getResources().getColor(R.color.colo))));
        //setSeparatorColor(getResources().getColor(R.color.colorAccent));
        setVibrateIntensity(30);
        setSkipText("跳过");
        setDoneText("进入");
        // setFadeAnimation();
        setZoomAnimation();
        //setFlowAnimation();
        // setSlideOverAnimation();
        // setDepthAnimation();
        boolean isFirstOpen = SpUtils.getBoolean(this, AppConstants.FIRST_OPEN);
        Log.d("111111", "onCreate: " + isFirstOpen);
        // 如果是第一次启动，则先进入功能引导页
        if (isFirstOpen) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
    }

    @Override
    public void onSkipPressed() {
        //当执行跳过动作时触发
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        SpUtils.putBoolean(WelcomeActivity.this, AppConstants.FIRST_OPEN, true);
        finish();
    }

    @Override
    public void onNextPressed() {
        //当执行下一步动作时触发
    }

    @Override
    public void onDonePressed() {
        //当执行完成动作时触发
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        SpUtils.putBoolean(WelcomeActivity.this, AppConstants.FIRST_OPEN, true);
        finish();
    }

    @Override
    public void onSlideChanged() {
        //当执行变化动作时触发
    }
}
