package com.android.library.web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSON;
import com.android.library.R;
import com.android.library.base.BaseActivity;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.webview.QMUIWebViewContainer;


public class DefaultWebViewActivity extends BaseActivity implements IBridge {

    private QMUITopBarLayout topBarLayout;
    private QMUIWebViewContainer webViewContainer;
    private DefaultWebView defaultWebView;
    private ProgressBar progressBar;
    private LinearLayout errorView;
    private WindowHolder windowHolder;
    private static final String KET_WINDOW_HOLDER = "windowHolder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initViews();
        parseParams();
        loadWeb();
    }

    private void initViews() {
        topBarLayout = findViewById(R.id.topBarLayout);
        webViewContainer = findViewById(R.id.webViewContainer);
        progressBar = findViewById(R.id.progressBar);
        errorView = findViewById(R.id.errorView);
        defaultWebView = new DefaultWebView(this);
        defaultWebView.setWebViewClient(new DefaultWebViewClient(defaultWebView));
        defaultWebView.setWebChromeClient(new DefaultWebChromeClient(this));
        webViewContainer.addWebView(defaultWebView, true);
        webViewContainer.setFitsSystemWindows(true);
    }

    private void parseParams() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(KET_WINDOW_HOLDER)) {
            String holderJSON = intent.getStringExtra(KET_WINDOW_HOLDER);
            if (holderJSON != null) {
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
        if(windowHolder.isAutoTitle()){
            topBarLayout.setTitle(title);
        }
    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onPageStarted() {
        errorView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onReceivedError(int errorCode, String description, String failingUrl) {
        errorView.setVisibility(View.VISIBLE);
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


}
