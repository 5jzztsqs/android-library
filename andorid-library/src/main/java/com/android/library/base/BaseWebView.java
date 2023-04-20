package com.android.library.base;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;


import com.qmuiteam.qmui.widget.webview.QMUIWebView;

public abstract class BaseWebView extends QMUIWebView {

    public BaseWebView(Context context) {
        this(context,null);
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSettings();
    }

    private void initSettings(){
        WebSettings settings = getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setUseWideViewPort(true);
        settings.setAppCacheEnabled(true);
       // settings.setLoadWithOverviewMode(true);
        settings.setAllowFileAccess(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setTextZoom(100);
        settings.setDomStorageEnabled(true);
    }





}
