package com.shunix.scavenger.app;

import android.util.Log;

import com.shunix.scavenger.log.Crash;

/**
 * Replacement for the default uncaught exception handler
 * Created by shunix on 15-1-31.
 */
public class ScavengerExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final static String TAG = ScavengerExceptionHandler.class.getName();
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler = null;
    private ScavengerApplication mApp = null;

    public ScavengerExceptionHandler(Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler, ScavengerApplication app) {
        mDefaultExceptionHandler = defaultUncaughtExceptionHandler;
        mApp = app;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        final Crash crash = new Crash(ex, mApp);
        mApp.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mApp.getLogger().log(crash);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        if (mDefaultExceptionHandler == null) {
            return;
        } else {
            mDefaultExceptionHandler.uncaughtException(thread, ex);
        }
    }
}
