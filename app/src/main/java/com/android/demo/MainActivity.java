package com.android.demo;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.library.base.BaseActivity;
import com.android.library.web.DefaultWebViewActivity;


public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Button button = findViewById(R.id.startWindow);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaultWebViewActivity.startWidthUrl(MainActivity.this,"https://cn.bing.com/");
            }
        });
    }
}
