package com.android.library.manager;

import android.app.Activity;

import java.util.Stack;

public class ActivityManager {
    private static Stack<Activity> activityStack;
    private static ActivityManager instance;

    public static ActivityManager getAppManager() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    public void popActivity() {
        Activity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
        }
    }

    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                removeActivity(activity);
            }
        }
    }

    public void finishActivityByName(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().getSimpleName().equals(cls.getSimpleName())) {
                activity.finish();
            }
        }
    }

    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public void exitApp() {
        finishAllActivity();
    }
}
