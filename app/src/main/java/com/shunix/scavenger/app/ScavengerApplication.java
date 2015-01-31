package com.shunix.scavenger.app;

import android.app.Application;
import android.content.pm.ApplicationInfo;

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

    private LogConfig mLogConfig = null;
    private ExecutorService mExecutor = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new ScavengerExceptionHandler(Thread.getDefaultUncaughtExceptionHandler(), this));
        mLogConfig = new LogConfig(getLoggerName());
        mExecutor = Executors.newCachedThreadPool();
    }

    /**
     * Get custom logger class name from AndroidManifest.xml
     * <meta-data android:name="logger" android:value="custom logger name" />
     *
     * @return name of custom logger
     */
    private String getLoggerName() {
        ApplicationInfo ai = getApplicationInfo();
        return ai.metaData.getString(LogConfig.META_LOGGER_NAME);
    }

    public Logger getLogger() {
        return mLogConfig.getLogger();
    }

    public void execute(Runnable task) {
        if (mExecutor != null) {
            mExecutor.execute(task);
        }
    }
}
