package com.android.library.web;

import com.android.library.base.BaseEntity;

public class WindowHolder extends BaseEntity {

    private String title;
    private String url;
    private String topBarColor;
    private String statusBarColor;
    private boolean autoTitle;
    private boolean showStatusBar;
    private boolean hideTopBar;
    private boolean hideBack;
    private boolean canGoBack = true;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isHideTopBar() {
        return hideTopBar;
    }

    public void setHideTopBar(boolean hideTopBar) {
        this.hideTopBar = hideTopBar;
    }

    public boolean isCanGoBack() {
        return canGoBack;
    }

    public void setCanGoBack(boolean canGoBack) {
        this.canGoBack = canGoBack;
    }

    public boolean isAutoTitle() {
        return autoTitle;
    }

    public void setAutoTitle(boolean autoTitle) {
        this.autoTitle = autoTitle;
    }

    public boolean isHideBack() {
        return hideBack;
    }

    public void setHideBack(boolean hideBack) {
        this.hideBack = hideBack;
    }

    public String getTopBarColor() {
        return topBarColor;
    }

    public void setTopBarColor(String topBarColor) {
        this.topBarColor = topBarColor;
    }

    public boolean isShowStatusBar() {
        return showStatusBar;
    }

    public void setShowStatusBar(boolean showStatusBar) {
        this.showStatusBar = showStatusBar;
    }

    public String getStatusBarColor() {
        return statusBarColor;
    }

    public void setStatusBarColor(String statusBarColor) {
        this.statusBarColor = statusBarColor;
    }
}
