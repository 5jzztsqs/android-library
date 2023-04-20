package com.android.demo;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alibaba.fastjson.JSON;
import com.android.library.base.BaseActivity;
import com.android.library.web.DefaultWebViewActivity;
import com.android.library.web.WebLog;
import com.android.library.web.WindowHolder;


public class LaunchActivity extends BaseActivity {

    private static final long MILLIS_IN_FUTURE = 2000;
    private static final long COUNT_DOWN_INTERVAL = 1000;
    private CountDownTimer countDownTimer;
    private String url = "http://192.168.1.81:8080/#/";
//    private String url = "https://nutui.jd.com/demo.html";
//    private String url = "https://m.baidu.com";
//   private String url = "http://test.imydao.cn:15888/standardapp/#/pages/login/login";
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
//                windowHolder.setShowStatusBar(true);
//                windowHolder.setStatusBarColor("#1677ff");
//                windowHolder.setAutoTitle(true);
                windowHolder.setHideTopBar(true);
                windowHolder.setUrl(url);
                DefaultWebViewActivity.start(LaunchActivity.this,windowHolder.toJSONString());
                finish();
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
