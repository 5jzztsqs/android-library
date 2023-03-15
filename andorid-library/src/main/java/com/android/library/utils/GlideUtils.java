package com.android.library.utils;

import android.widget.ImageView;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.request.RequestListener;

import me.bzcoder.easyglide.EasyGlide;
import me.bzcoder.easyglide.progress.OnProgressListener;

public class GlideUtils {

    public static void loadImage(String url, ImageView imageView) {
        EasyGlide.loadImage(Utils.getApp(), url, imageView);
    }

    public static void loadImage(String url, ImageView imageView, OnProgressListener onProgressListener) {
        EasyGlide.loadImage(Utils.getApp(), url, imageView, onProgressListener);
    }

    public static void loadImage(String url, ImageView imageView, RequestListener requestListener) {
        EasyGlide.loadImage(Utils.getApp(), url, imageView, requestListener);
    }

    public static void loadBlurImage(String url, ImageView imageView) {
        EasyGlide.loadBlurImage(Utils.getApp(), url, imageView);
    }

    public static void loadGrayImage(String url, ImageView imageView) {
        EasyGlide.loadGrayImage(Utils.getApp(), url, imageView);
    }

    public static void loadCircleImage(String url, ImageView imageView) {
        EasyGlide.loadCircleImage(Utils.getApp(), url, imageView);
    }

}
