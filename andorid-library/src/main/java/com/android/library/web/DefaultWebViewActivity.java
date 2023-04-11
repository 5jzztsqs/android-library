package com.android.library.web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.android.library.R;
import com.android.library.base.BaseActivity;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.webview.QMUIWebViewContainer;


public class DefaultWebViewActivity extends BaseActivity implements IBridge{

    private QMUITopBarLayout topBarLayout;
    private QMUIWebViewContainer webViewContainer;
    private DefaultWebView defaultWebView;
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

    private void initViews(){
        topBarLayout = findViewById(R.id.topBarLayout);
        webViewContainer = findViewById(R.id.webViewContainer);
        defaultWebView = new DefaultWebView(this);
        defaultWebView.setWebViewClient(new DefaultWebViewClient(defaultWebView));
        defaultWebView.setWebChromeClient(new DefaultWebChromeClient(this));
        webViewContainer.addWebView(defaultWebView,true);
    }

    private void parseParams(){
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(KET_WINDOW_HOLDER)){
            String holderJSON = intent.getStringExtra(KET_WINDOW_HOLDER);
            if(holderJSON != null){
                windowHolder = JSON.parseObject(holderJSON, WindowHolder.class);
            }
        }

    }

    private void loadWeb(){
        if(windowHolder != null){
            if(!windowHolder.isAutoTitle()){
                windowHolder.setTitle(windowHolder.getTitle());
            }
            defaultWebView.loadUrl(windowHolder.getUrl());
        }
    }
    public static void start(Context context,String windowHolderJSON){
        Intent intent = new Intent(context,DefaultWebViewActivity.class);
        intent.putExtra(KET_WINDOW_HOLDER,windowHolderJSON);
        context.startActivity(intent);
    }

    @Override
    public void updateTitle(String title) {

    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onPageStarted() {

    }

    @Override
    public void onPageFinished() {

    }

    @Override
    public void onReceivedError(int errorCode, String description, String failingUrl) {

    }

}
