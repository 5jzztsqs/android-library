package com.android.library.web;

import com.blankj.utilcode.util.LogUtils;

public class WebLog {
    private static final String TAG = WebLog.class.getSimpleName();


    public static void i(Object content){
        LogUtils.iTag(TAG,content);
    }

    public static void json(final Object content) {
        LogUtils.json(content);
    }
}
