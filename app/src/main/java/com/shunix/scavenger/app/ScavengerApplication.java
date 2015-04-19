package com.shunix.scavenger.app;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.shunix.scavenger.log.LogConfig;
import com.shunix.scavenger.log.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Use this class to replace the default application in manifest file.
 * Created by shunix on 15-1-31.
 */
@SuppressWarnings("unused")
public class ScavengerApplication extends Application {

    private static final String TAG = ScavengerApplication.class.getName();
    private LogConfig mLogConfig = null;
    private ExecutorService mExecutor = null;
    private native void saveApplicationNative();
    static {
        System.loadLibrary("scavenger");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new ScavengerExceptionHandler(Thread.getDefaultUncaughtExceptionHandler(), this));
        mLogConfig = new LogConfig(getLoggerName());
        mExecutor = Executors.newCachedThreadPool();
//        saveApplicationNative();
    }

    /**
     * Get custom logger class name from AndroidManifest.xml
     * <meta-data android:name="logger" android:value="custom logger name" />
     *
     * @return name of custom logger
     */
    private String getLoggerName() {
        String name = null;
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            name = ai.metaData.getString(LogConfig.META_LOGGER_NAME);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Cannot get meta data");
        }
        return name;
    }

    public Logger getLogger() {
        return mLogConfig.getLogger();
    }

    public void execute(Runnable task) {
        if (mExecutor != null) {
            mExecutor.execute(task);
        }
    }

    public void handleNativeCrash() {
        // TODO handle native crash
    }
}
