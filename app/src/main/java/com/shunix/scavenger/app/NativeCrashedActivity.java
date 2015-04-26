package com.shunix.scavenger.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shunix.scavenger.R;
import com.shunix.scavenger.log.Crash;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by shunix on 15-4-26.
 */
public class NativeCrashedActivity extends Activity {

    private static final String TAG = NativeCrashedActivity.class.getName();
    private ProgressBar mProgressBar;
    private Button mButton;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.native_crashed_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mButton = (Button) findViewById(R.id.button);
        mTextView = (TextView) findViewById(R.id.textView);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonitorLogcatTask task = new MonitorLogcatTask();
                task.execute();
            }
        });
    }

    class MonitorLogcatTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            finish();
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mTextView.setText("Gathering stack trace");
            mButton.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Process process = Runtime.getRuntime().exec(new String[]{"logcat", "-d", "-v", "threadtime"});
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()), 8096);
                String line;
                StringBuilder log = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    log.append(line);
                    log.append("\n");
                }
                final ScavengerApplication mApp = (ScavengerApplication) getApplication();
                mApp.getLogger().log(new Crash(new Throwable(log.toString()), mApp));
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            return null;
        }
    }
}
