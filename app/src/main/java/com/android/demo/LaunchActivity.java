package com.android.demo;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.library.base.BaseActivity;
import com.android.library.manager.MMKVManger;
import com.android.library.web.BridgeWebViewActivity;
import com.android.library.web.WindowHolder;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;


public class LaunchActivity extends BaseActivity {

    private static final long MILLIS_IN_FUTURE = 2000;
    private static final long COUNT_DOWN_INTERVAL = 1000;
    private CountDownTimer countDownTimer;
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
        countDownTimer = new CountDownTimer(MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                launcherActivity(url);
            }
        };

      //  countDownTimer.start();
    }

    private void launcherActivity(String url) {
        WindowHolder windowHolder = new WindowHolder();
        windowHolder.setShowStatusBar(true);
      //  windowHolder.setStatusBarColor("#c82519");
      //  windowHolder.setAutoTitle(true);
        windowHolder.setHideTopBar(true);
        windowHolder.setUrl(url);
      //  windowHolder.setDebug(true);
        BridgeWebViewActivity.start(LaunchActivity.this,windowHolder.toJSONString());
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }
}
