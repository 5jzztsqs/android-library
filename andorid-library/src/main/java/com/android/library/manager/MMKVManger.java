package com.android.library.manager;

import android.app.Application;

import com.tencent.mmkv.MMKV;

public class MMKVManger {

    public static void init(Application application) {
        MMKV.initialize(application);
    }

    private static MMKV mmkv() {
        return MMKV.defaultMMKV();
    }


    public static String getString(String key, String defValue) {
        return mmkv().getString(key, defValue);
    }

    public static void putString(String key, String value) {
        mmkv().putString(key, value);
    }

    public static int getInt(String key, int defValue) {
        return mmkv().getInt(key, defValue);
    }

    public static void putInt(String key, int value) {
        mmkv().putInt(key, value);
    }

    public static long getLong(String key, long defValue) {
        return mmkv().getLong(key, defValue);
    }

    public static void putLong(String key, long value) {
        mmkv().putLong(key, value);
    }

    public static float getFloat(String key, float defValue) {
        return mmkv().getFloat(key, defValue);
    }

    public static void putFloat(String key, float value) {
        mmkv().putFloat(key, value);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return mmkv().getBoolean(key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        mmkv().putBoolean(key, value);
    }

    public static void putBytes(String key, byte[] bytes) {
        mmkv().putBytes(key, bytes);
    }

    public static byte[] getBytes(String key, byte[] defValue) {
        return mmkv().getBytes(key, defValue);
    }


}
