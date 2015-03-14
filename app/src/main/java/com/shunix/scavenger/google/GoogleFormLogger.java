package com.shunix.scavenger.google;

import android.util.Log;

import com.shunix.scavenger.log.Crash;
import com.shunix.scavenger.log.Logger;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * Created by shunix on 15-3-14.
 */
public class GoogleFormLogger implements Logger {

    private static final String TAG = GoogleFormLogger.class.getName();
    private static final String REPORT_URL = "https://docs.google.com/forms/d/1qkMlHYwk7JRkTpgMFsyXkURBL2MroSaw_2Cdd5otybI/formResponse";
    private OkHttpClient mClient;
    public GoogleFormLogger() {
        mClient = new OkHttpClient();
    }
    @Override
    public void log(Crash crash) {
        RequestBody body = new FormEncodingBuilder().add("entry.580675442", crash.toString()).build();
        Request request = new Request.Builder().url(REPORT_URL).post(body).build();
        try {
            Response response = mClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.i(TAG, "Crash has been successfully reported");
            } else {
                Log.w(TAG, "Unexpected error " + response);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
