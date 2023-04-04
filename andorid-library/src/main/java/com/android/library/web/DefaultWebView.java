package com.android.library.web;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.android.library.base.BaseWebView;

public class DefaultWebView extends BaseWebView {
    private AppCompatActivity activity;

    public DefaultWebView(Context context) {
        this(context,null);
    }

    public DefaultWebView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DefaultWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.activity = (AppCompatActivity) context;
    }


    @Override
    public WebViewClient createWebViewClient() {
        return new WebViewClient();
    }

    @Override
    public WebChromeClient createWebChromeClient() {
        return new DefaultWebChromeClient(activity);
    }
}
