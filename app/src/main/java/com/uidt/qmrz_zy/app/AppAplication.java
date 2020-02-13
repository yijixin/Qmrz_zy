package com.uidt.qmrz_zy.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

/**
 * @author Created by yijixin at 2017/11/16
 */
public class AppAplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static AppAplication appAplication;

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 7.0文件适配
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }

        // 程序崩溃时触发线程  以下用来捕获程序崩溃异常
        Thread.setDefaultUncaughtExceptionHandler(restartHandler);
        appAplication = this;

    }

    // 创建服务用于捕获崩溃异常
    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
        }
    };




    public static Context getAppContext() {
        return appAplication;
    }

    public static Resources getAppResources() {
        return appAplication.getResources();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.e("YJX", "onActivityCreated:");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.e("YJX", "onActivityStarted:");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.e("YJX", "onActivityResumed:");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.e("YJX", "onActivityPaused:");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.e("YJX", "onActivityStopped:");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.e("YJX", "onActivitySaveInstanceState:");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e("YJX", "onActivityDestroyed:");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

}
