package com.android.library;

import android.app.Application;

import com.android.library.manager.MMKVManger;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;

public abstract class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MMKVManger.init(this);
        QMUISwipeBackActivityManager.init(this);
    }
}
