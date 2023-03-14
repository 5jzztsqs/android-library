package com.android.library;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.library.base.BaseActivity;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;


public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//
         RxHttp.get("http://www.baidu.com")
                       .toObservableString()
                       .observeOn(AndroidSchedulers.mainThread())
                       .subscribe(s -> {
                }, throwable  -> {
                    throwable.printStackTrace();
                    //Log.i(TAG,"ssss"+throwable.toString());
                   Toast.makeText(MainActivity.this,throwable.toString(),Toast.LENGTH_LONG).show();
                });
    }
}
