package com.android.library.web;

import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.qmuiteam.qmui.widget.webview.QMUIBridgeWebViewClient;
import com.qmuiteam.qmui.widget.webview.QMUIWebViewBridgeHandler;

import java.util.ArrayList;
import java.util.List;

public class DefaultWebViewClient extends QMUIBridgeWebViewClient {
    private AppCompatActivity activity;

    public DefaultWebViewClient(WebView webView){
        this(false,false,new BridgeHandler(webView));
        this.activity = (AppCompatActivity) webView.getContext();
    }

    private DefaultWebViewClient(boolean needDispatchSafeAreaInset, boolean disableVideoFullscreenBtnAlways, @NonNull QMUIWebViewBridgeHandler bridgeHandler) {
        super(needDispatchSafeAreaInset, disableVideoFullscreenBtnAlways, bridgeHandler);
    }


    private static class BridgeHandler extends QMUIWebViewBridgeHandler{
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
