package com.android.library.web;

import android.graphics.Bitmap;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.qmuiteam.qmui.widget.webview.QMUIBridgeWebViewClient;
import com.qmuiteam.qmui.widget.webview.QMUIWebViewBridgeHandler;

import java.util.List;

public class BridgeWebViewClient extends QMUIBridgeWebViewClient {
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
    public void onPageStarted(WebView view, String url, @Nullable Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
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
        BridgeCmdHandler.injectJS(activity,view);
    }



    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        if (bridge != null) {
            bridge.onReceivedError(error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
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
