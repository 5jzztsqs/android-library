package com.android.library.web;

import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.alibaba.fastjson.JSON;
import com.android.library.glide.GlideEngine;
import com.blankj.utilcode.util.UriUtils;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;

import java.io.File;
import java.util.ArrayList;

public class DefaultWebChromeClient extends WebChromeClient {
    private AppCompatActivity activity;
    private IBridge bridge;

    public DefaultWebChromeClient(AppCompatActivity activity) {
        this.activity = activity;
        if (activity instanceof IBridge) {
            this.bridge = (IBridge) activity;
        }
    }


    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (bridge != null) {
            bridge.onProgress(newProgress);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if (bridge != null) {
            bridge.updateTitle(title);
        }
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        super.onShowCustomView(view, callback);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        return super.onJsBeforeUnload(view, url, message, result);
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt();
    }

    @Override
    public void onPermissionRequest(PermissionRequest request) {
        super.onPermissionRequest(request);
    }

    @Override
    public void onPermissionRequestCanceled(PermissionRequest request) {
        super.onPermissionRequestCanceled(request);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return super.onConsoleMessage(consoleMessage);
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        WebLog.i("onShowFileChooser:" + JSON.toJSONString(fileChooserParams));
        PictureSelector.create(activity)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        if (result == null || result.size() == 0) {
                            filePathCallback.onReceiveValue(null);
                        } else {
                            Uri uri[] = new Uri[result.size()];
                            for (int i = 0; i < result.size(); i++) {
                                String path = result.get(i).getRealPath();
                                WebLog.i(path);
                                File file = new File(path);
                                WebLog.i("provider:"+activity.getPackageName() + ".FileProvider");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    uri[i] = FileProvider.getUriForFile(activity, activity.getPackageName() + ".FileProvider", file);
                                } else {
                                    uri[i] = Uri.fromFile(file);
                                }
                            }
                            filePathCallback.onReceiveValue(uri);
                        }
                    }

                    @Override
                    public void onCancel() {
                        filePathCallback.onReceiveValue(null);
                    }
                });

        return true;
    }
}
