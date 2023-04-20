package com.android.library.web;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.android.library.R;
import com.android.library.base.BaseActivity;
import com.android.library.widget.ViewHelper;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.webview.QMUIWebViewContainer;


public class BridgeWebViewActivity extends BaseActivity implements IBridge {

    private static final String TAG = BridgeWebViewActivity.class.getSimpleName();
    private QMUITopBarLayout topBarLayout;
    private QMUIWebViewContainer webViewContainer;
    private BridgeWebView defaultWebView;
    private ProgressBar progressBar;
    private View holderView;
    private TextView errorText;
    private WindowHolder windowHolder;
    private boolean receiveError;
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
        defaultWebView = new BridgeWebView(this);
        defaultWebView.setWebViewClient(new BridgeWebViewClient(defaultWebView));
        defaultWebView.setWebChromeClient(new BridgeWebChromeClient(this));
        webViewContainer.addWebView(defaultWebView, true);
        if(windowHolder.isHideTopBar()){
            topBarLayout.setFitsSystemWindows(false);
            topBarLayout.setVisibility(View.GONE);
        }else{
            topBarLayout.setFitsSystemWindows(true);
            topBarLayout.setVisibility(View.VISIBLE);
            if(windowHolder.isCanGoBack()){
                topBarLayout.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(defaultWebView.canGoBack()){
                            defaultWebView.goBack();
                            return;
                        }
                        finish();
                    }
                });
            }
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
        holderView.setVisibility(View.GONE);
        progressBar = new ProgressBar(this);
        progressBar.setIndeterminateTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.app_primary_color)));
        FrameLayout.LayoutParams lp = ViewHelper.generateWrapContentFrameLayoutLayoutParams();
        lp.gravity = Gravity.CENTER;
        webViewContainer.addView(progressBar,lp);

        if(windowHolder.isShowStatusBar()){
            webViewContainer.setFitsSystemWindows(true);
            if(windowHolder.getStatusBarColor() != null){
                BarUtils.setStatusBarColor(this,Color.parseColor(windowHolder.getStatusBarColor()));
            }
        }
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
        receiveError = false;
        WebLog.i("onPageStarted");
        holderView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished() {
        WebLog.i("onPageFinished");
        if(!receiveError){
            defaultWebView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onReceivedError(int errorCode, String description, String failingUrl) {
            receiveError = true;
            defaultWebView.setVisibility(View.INVISIBLE);
            holderView.setVisibility(View.VISIBLE);
            errorText.setTag(failingUrl);
            errorText.setText(description);
    }


    public static void start(Context context, String windowHolderJSON) {
        Intent intent = new Intent(context, BridgeWebViewActivity.class);
        intent.putExtra(KET_WINDOW_HOLDER, windowHolderJSON);
        context.startActivity(intent);
    }

    public static void startWidthUrl(Context context, String url) {
        Intent intent = new Intent(context, BridgeWebViewActivity.class);
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
