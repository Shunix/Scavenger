package com.shunix.scavenger.log;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * This class represent a crash
 * Created by shunix on 15-2-2.
 */
public class Crash {
    protected Throwable mException;
    protected Context mContext;
    private StringBuilder mBuilder;

    public Crash(Throwable throwable, Context context) {
        this.mException = throwable;
        this.mContext = context;
    }

    @Override
    public String toString() {
        if (mBuilder != null) {
            return mBuilder.toString();
        }
        mBuilder = new StringBuilder();
        mBuilder.append(getPackageInfo());
        mBuilder.append(getDeviceInfo());
        if (mException != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                mException.printStackTrace(new PrintStream(baos));
                mBuilder.append(new String(baos.toByteArray()));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return mBuilder.toString();
    }

    protected String getPackageInfo() {
        String packageInfoString = "";
        if (mContext == null) {
            return "";
        }
        PackageManager packageManager = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder
                    .append("Package Name: ")
                    .append(packageInfo.packageName)
                    .append(" Package Version Name: ")
                    .append(packageInfo.versionName)
                    .append(" Package Version Code: ")
                    .append(packageInfo.versionCode)
                    .append("\n");
            packageInfoString = stringBuilder.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfoString;
    }

    protected String getDeviceInfo() {
        if (mContext == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("Device Model: ")
                .append(Build.MODEL)
                .append(" Android Version: ")
                .append(Build.VERSION.RELEASE)
                .append("\n");
        return stringBuilder.toString();
    }
}
