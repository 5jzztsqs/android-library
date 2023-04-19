package com.android.library.web;

import android.content.Context;
import android.util.AttributeSet;

import com.android.library.base.BaseWebView;


public class BridgeWebView extends BaseWebView {

    public BridgeWebView(Context context) {
        this(context,null);
    }

    public BridgeWebView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BridgeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
