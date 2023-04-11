package com.android.library.web;

import android.webkit.WebView;

public interface IBridge {
    void updateTitle(String title);
    void onProgress(int progress);
    void onPageStarted();
    void onPageFinished();
    void onReceivedError(int errorCode, String description, String failingUrl);
}
