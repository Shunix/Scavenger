package com.shunix.scavenger.app;

import android.app.Application;

/**
 * Use this class to replace the default application in manifest file.
 * Created by shunix on 15-1-31.
 */
@SuppressWarnings("unused")
public class ScavengerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new ScavengerExceptionHandler(Thread.getDefaultUncaughtExceptionHandler()));
    }
}
