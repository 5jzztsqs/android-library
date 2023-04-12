package com.android.library.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.library.R;
import com.android.library.base.BaseActivity;
import com.android.library.widget.ViewHelper;
import com.blankj.utilcode.util.LogUtils;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.webview.QMUIWebViewContainer;


public class DefaultWebViewActivity extends BaseActivity implements IBridge {

    private static final String TAG = DefaultWebViewActivity.class.getSimpleName();
    private QMUITopBarLayout topBarLayout;
    private QMUIWebViewContainer webViewContainer;
    private DefaultWebView defaultWebView;
    private ProgressBar progressBar;
    private View holderView;
    private TextView errorText;
    private WindowHolder windowHolder;
    private static final String KET_WINDOW_HOLDER = "windowHolder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        parseParams();
        initViews();
        loadWeb();
    }

    private void initViews() {
        topBarLayout = findViewById(R.id.topBarLayout);
        webViewContainer = findViewById(R.id.webViewContainer);
        defaultWebView = new DefaultWebView(this);
        defaultWebView.setWebViewClient(new DefaultWebViewClient(defaultWebView));
        defaultWebView.setWebChromeClient(new DefaultWebChromeClient(this));
        webViewContainer.addWebView(defaultWebView, true);

        if(windowHolder.isHideTopBar()){
            topBarLayout.setFitsSystemWindows(false);
            topBarLayout.setVisibility(View.GONE);
        }else{
            topBarLayout.setFitsSystemWindows(true);
            topBarLayout.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams containerLp = (FrameLayout.LayoutParams) webViewContainer.getLayoutParams();
            webViewContainer.setFitsSystemWindows(true);
            containerLp.topMargin = QMUIResHelper.getAttrDimen(this, com.qmuiteam.qmui.R.attr.qmui_topbar_height);
            webViewContainer.setLayoutParams(containerLp);
        }

        holderView = getLayoutInflater().inflate(R.layout.web_holder,null);
        errorText = holderView.findViewById(R.id.errorText);
        holderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String failingUrl = (String) errorText.getTag();
                LogUtils.iTag(TAG,failingUrl);
                defaultWebView.loadUrl(failingUrl);
            }
        });
        webViewContainer.addView(holderView);
        progressBar = new ProgressBar(this);
        FrameLayout.LayoutParams lp = ViewHelper.generateWrapContentFrameLayoutLayoutParams();
        lp.gravity = Gravity.CENTER;
        webViewContainer.addView(progressBar,lp);
    }

    private void parseParams() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(KET_WINDOW_HOLDER)) {
            String holderJSON = intent.getStringExtra(KET_WINDOW_HOLDER);
            if (holderJSON != null) {
                WebLog.json(holderJSON);
                windowHolder = JSON.parseObject(holderJSON, WindowHolder.class);
            } else {
                windowHolder = new WindowHolder();
            }
        }


    }

    private void loadWeb() {
        if (!windowHolder.isAutoTitle()) {
            windowHolder.setTitle(windowHolder.getTitle());
        }
        defaultWebView.loadUrl(windowHolder.getUrl());
    }

    @Override
    public void updateTitle(String title) {
        WebLog.i("updateTitle:"+title);
        if(windowHolder.isAutoTitle()){
            topBarLayout.setTitle(title);
        }
    }

    @Override
    public void onProgress(int progress) {
        if(progress >= 100){
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageStarted() {
        WebLog.i("onPageStarted");
        holderView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished() {
        WebLog.i("onPageFinished");

    }

    @Override
    public void onReceivedError(int errorCode, String description, String failingUrl) {
        WebLog.i("onReceivedError:"+failingUrl);
        holderView.setVisibility(View.VISIBLE);
        errorText.setTag(failingUrl);
        errorText.setText(description);
    }


    public static void start(Context context, String windowHolderJSON) {
        Intent intent = new Intent(context, DefaultWebViewActivity.class);
        intent.putExtra(KET_WINDOW_HOLDER, windowHolderJSON);
        context.startActivity(intent);
    }

    public static void startWidthUrl(Context context, String url) {
        Intent intent = new Intent(context, DefaultWebViewActivity.class);
        WindowHolder windowHolder = new WindowHolder();
        windowHolder.setUrl(url);
        windowHolder.setAutoTitle(true);
        intent.putExtra(KET_WINDOW_HOLDER, windowHolder.toJSONString());
        context.startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(defaultWebView.canGoBack()){
                defaultWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
