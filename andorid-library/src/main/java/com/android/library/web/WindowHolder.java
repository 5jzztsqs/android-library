package com.android.library.web;

import com.android.library.base.BaseEntity;

public class WindowHolder extends BaseEntity {

    private String title;
    private boolean autoTitle;
    private boolean hideTopBar;
    private boolean hideBack;
    private boolean canGoBack = true;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
