package com.shunix.scavenger.log;

import android.text.TextUtils;
import android.util.Log;

/**
 * Config class for logger
 * Created by shunix on 15-1-31.
 */
public class LogConfig {

    public final static String META_LOGGER_NAME = "logger";

    private final static String TAG = LogConfig.class.getName();
    private Logger mLogger;

    public LogConfig(String loggerClassName) {
        try {
            if (!TextUtils.isEmpty(loggerClassName)) {
                mLogger = (Logger) Class.forName(loggerClassName).newInstance();
            } else {
                Log.e(TAG, "Logger class not found");
            }
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Logger class not found");
        } catch (ClassCastException e) {
            Log.e(TAG, "Error casting logger class");
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Illegal access exception instantiating logger class");
        } catch (InstantiationException e) {
            Log.e(TAG, "Error instantiating logger class");
        }
    }

    public Logger getLogger() {
        return mLogger;
    }
}
