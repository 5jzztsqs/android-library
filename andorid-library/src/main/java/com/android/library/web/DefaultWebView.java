package com.android.library.web;

import android.content.Context;
import android.util.AttributeSet;

import com.android.library.base.BaseWebView;


public class DefaultWebView extends BaseWebView {

    public DefaultWebView(Context context) {
        this(context,null);
    }

    public DefaultWebView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DefaultWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
