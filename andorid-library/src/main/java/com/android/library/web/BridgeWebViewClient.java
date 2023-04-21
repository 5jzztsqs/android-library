package com.android.library.web;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.SnackbarUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.webview.QMUIBridgeWebViewClient;
import com.qmuiteam.qmui.widget.webview.QMUIWebViewBridgeHandler;

import java.nio.file.WatchEvent;
import java.util.List;

public class BridgeWebViewClient extends QMUIBridgeWebViewClient {
    private static final String SCHEMA_HTTP = "http://";
    private static final String SCHEMA_HTTPS = "https://";
    private AppCompatActivity activity;
    private IBridge bridge;

    public BridgeWebViewClient(WebView webView) {
        this(false, false, new BridgeHandler(webView));
        this.activity = (AppCompatActivity) webView.getContext();
        if (activity instanceof IBridge) {
            this.bridge = (IBridge) activity;
        }
    }

    private BridgeWebViewClient(boolean needDispatchSafeAreaInset, boolean disableVideoFullscreenBtnAlways, @NonNull QMUIWebViewBridgeHandler bridgeHandler) {
        super(needDispatchSafeAreaInset, disableVideoFullscreenBtnAlways, bridgeHandler);
    }



    @Override
    protected boolean onShouldOverrideUrlLoading(WebView view, String url) {
        if (url.toLowerCase().startsWith(SCHEMA_HTTP) || url.toLowerCase().startsWith(SCHEMA_HTTPS)) {
            return super.onShouldOverrideUrlLoading(view, url);
        } else {
            WebLog.i("onShouldOverrideUrlLoading->"+url);
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return true;
        }
    }

    @Override
    protected boolean onShouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        if (url.toLowerCase().startsWith(SCHEMA_HTTP) || url.toLowerCase().startsWith(SCHEMA_HTTPS)) {
            return super.onShouldOverrideUrlLoading(view, url);
        } else {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return true;
        }
    }


    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public void onPageStarted(WebView view, String url, @Nullable Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        view.evaluateJavascript(BridgeData.getInstance().getVConsole(), null);
        BridgeCmdHandler.injectJS(activity, view);
        if (bridge != null) {
            bridge.onPageStarted();
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (bridge != null) {
            bridge.onPageFinished();
        }
    }


    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        int errorCode = error.getErrorCode();
        String description = error.getDescription().toString();
        String loadUrlHost = Uri.parse(view.getUrl()).getHost();
        String errorUrlHost = request.getUrl().getHost();
        String url = request.getUrl().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errorCode", errorCode);
        jsonObject.put("loadUrlHost", loadUrlHost);
        jsonObject.put("errorUrlHost", errorUrlHost);
        jsonObject.put("description", description);
        jsonObject.put("errorUrl", url);
        WebLog.json(jsonObject);
        if (bridge != null) {
            if (errorCode == ERROR_CONNECT && loadUrlHost.equals(errorUrlHost) && description.contains("ERR_CONNECTION_REFUSED")) {
                bridge.onReceivedError(error.getErrorCode(), "无法连接到该网站", request.getUrl().toString());
            }
        }
    }


    @SuppressLint("WebViewClientOnReceivedSslError")
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        //https忽略证书问题
        if (handler != null) {
            //表示等待证书响应
            handler.proceed();
        }
    }


    private static class BridgeHandler extends QMUIWebViewBridgeHandler {
        private BridgeCmdHandler bridgeCmdHandler;

        public BridgeHandler(@NonNull WebView webView) {
            super(webView);
            bridgeCmdHandler = new BridgeCmdHandler((AppCompatActivity) webView.getContext());
        }

        @Override
        protected List<String> getSupportedCmdList() {
            return bridgeCmdHandler.getBridgeCmdList();
        }

        @Override
        protected void handleMessage(String message, MessageFinishCallback callback) {
            bridgeCmdHandler.handleMessage(message, callback);
        }
    }


}
