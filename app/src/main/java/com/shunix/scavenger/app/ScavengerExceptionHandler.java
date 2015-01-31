package com.shunix.scavenger.app;

/**
 * Replacement for the default uncaught exception handler
 * Created by shunix on 15-1-31.
 */
public class ScavengerExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler = null;

    public ScavengerExceptionHandler(Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler) {
        mDefaultExceptionHandler = defaultUncaughtExceptionHandler;
    }
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (mDefaultExceptionHandler == null) {
            return;
        } else {
            mDefaultExceptionHandler.uncaughtException(thread, ex);
        }
    }
}
