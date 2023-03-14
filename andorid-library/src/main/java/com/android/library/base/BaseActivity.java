package com.android.library.base;

import android.os.Bundle;

import com.android.library.manager.AppManager;
import com.qmuiteam.qmui.arch.QMUIFragmentActivity;

public class BaseActivity extends QMUIFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }
}
