package com.android.demo;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.library.base.BaseActivity;
import com.android.library.manager.MMKVManger;
import com.android.library.web.BridgeWebViewActivity;
import com.android.library.web.WindowHolder;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;


public class LaunchActivity extends BaseActivity  implements EasyPermissions.PermissionCallbacks{

    private static final String TAG = LaunchActivity.class.getSimpleName();
    private static final String PERMISSIONS[] = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final long MILLIS_IN_FUTURE = 2000;
    private static final long COUNT_DOWN_INTERVAL = 1000;
    //   private String url = "http://192.168.1.81:8080/#/";
//    private String url = "https://nutui.jd.com/demo.html";
    private String url = "https://m.baidu.com";

    //   private String url = "https://www.toutiao.com/?source=m_redirect";
//    private String url = "https://m.jd.com/";
    //  private String url = "https://quark.sm.cn/";
//    private String url = "https://www.zhihu.com/?utm_id=0";
//   private String url = "http://test.imydao.cn:15888/standardapp/#/pages/login/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        PermissionUtils.permission(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        EditText editText = findViewById(R.id.editText);
        Button button = findViewById(R.id.launcher);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = editText.getText().toString().trim();
                if (StringUtils.isTrimEmpty(url)) {
                    ToastUtils.showLong(editText.getHint().toString());
                } else {
                    MMKVManger.putString("url",url);
                    launcherActivity(url);
                }
            }
        });
        String lastUrl = MMKVManger.getString("url",null);
        editText.setText(lastUrl);

        if(!EasyPermissions.hasPermissions(this,PERMISSIONS)){
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this , 100, PERMISSIONS)
                            .build());
        }

    }


    private void launcherActivity(String url) {
//        WindowHolder windowHolder = new WindowHolder();
//        windowHolder.setShowStatusBar(true);
//      //  windowHolder.setStatusBarColor("#c82519");
//      //  windowHolder.setAutoTitle(true);
//        windowHolder.setHideTopBar(true);
//       // windowHolder.setUrl("file:android_asset/test.html");
//        windowHolder.setUrl(url);
//      //  windowHolder.setDebug(true);
//        BridgeWebViewActivity.start(LaunchActivity.this,windowHolder.toJSONString());
//        finish();

        RecordParam recordParam = new RecordParam();
        recordParam.setDuration(120);
        VideoCaptureActivity.startVideoCapture(this, JSON.toJSONString(recordParam),100);
        //MainActivity.startVideoCapture(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            JSONObject jsonObject = VideoCaptureActivity.obtain(data);
            Log.e(TAG, jsonObject.toJSONString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
