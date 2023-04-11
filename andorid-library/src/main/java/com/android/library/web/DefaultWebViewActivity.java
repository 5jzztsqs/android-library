package com.android.library.web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.library.R;
import com.android.library.base.BaseActivity;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.webview.QMUIWebViewContainer;


public class DefaultWebViewActivity extends BaseActivity implements IBridge{

    private QMUITopBarLayout topBarLayout;
    private QMUIWebViewContainer webViewContainer;
    private DefaultWebView defaultWebView;

    private static final String KET_WINDOW_HOLDER = "windowHolder";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initViews();
        parseWindowHolder();
    }

    private void initViews(){
        topBarLayout = findViewById(R.id.topBarLayout);
        webViewContainer = findViewById(R.id.webViewContainer);
        defaultWebView = new DefaultWebView(this);
        defaultWebView.setWebViewClient(new DefaultWebViewClient(defaultWebView));
        defaultWebView.setWebChromeClient(new DefaultWebChromeClient(this));
        webViewContainer.addWebView(defaultWebView,true);
    }

    private void parseWindowHolder(){
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(KET_WINDOW_HOLDER)){
            WindowHolder windowHolder = (WindowHolder) intent.getSerializableExtra(KET_WINDOW_HOLDER);
            if(windowHolder != null){

            }
        }

    }
    public static void start(Context context){
        Intent intent = new Intent(context,DefaultWebViewActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void updateTitle(String title) {

    }

    @Override
    public void onProgress(int progress) {

    }
}
