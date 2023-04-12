package com.android.demo;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import com.android.library.base.BaseActivity;
import com.android.library.web.DefaultWebViewActivity;
import com.android.library.web.WindowHolder;


public class LaunchActivity extends BaseActivity {

    private static final long MILLIS_IN_FUTURE = 3000;
    private static final long COUNT_DOWN_INTERVAL = 1000;
    private CountDownTimer countDownTimer;
    private String url = "http://192.168.1.81:8080/#/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        countDownTimer = new CountDownTimer(MILLIS_IN_FUTURE,COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                WindowHolder windowHolder = new WindowHolder();
                windowHolder.setAutoTitle(true);
                windowHolder.setHideTopBar(true);
                windowHolder.setUrl(url);
                DefaultWebViewActivity.start(LaunchActivity.this,windowHolder.toJSONString());
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }
}
