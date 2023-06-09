package com.android.library;

import android.app.Application;

import com.android.library.manager.MMKVManger;
import com.android.library.web.BridgeData;
import com.blankj.utilcode.util.Utils;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;

public abstract class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BridgeData.init();
        Utils.init(this);
        MMKVManger.init(this);
        QMUISwipeBackActivityManager.init(this);
    }
}
