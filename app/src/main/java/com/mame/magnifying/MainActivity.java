package com.mame.magnifying;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ViewAnimator;

import com.mame.magnifying.logger.Log;
import com.mame.magnifying.logger.LogFragment;
import com.mame.magnifying.logger.LogWrapper;
import com.mame.magnifying.logger.MessageOnlyLogFilter;

public class MainActivity extends SampleActivityBase {
    public static final String TAG = "MainActivity";

    // Whether the Log Fragment is currently shown
    private boolean mLogShown;

    @RequiresApi(api = 26)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            ScreenCaptureFragment fragment = new ScreenCaptureFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }

        // create default notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String channelId = getString(R.string.default_floatingview_channel_id);
            final String channelName = getString(R.string.default_floatingview_channel_name);
            final NotificationChannel defaultChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_MIN);
            final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(defaultChannel);
            }
        }

        if (savedInstanceState == null) {
            android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.container, FloatingViewControlFragment.newInstance());
            ft.commit();
        }

        startService(new Intent(getApplicationContext(), ChatHeadService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        MenuItem logToggle = menu.findItem(R.id.menu_toggle_log);
//        logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);
//        logToggle.setTitle(mLogShown ? R.string.sample_hide_log : R.string.sample_show_log);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_toggle_log:
                mLogShown = !mLogShown;
//                ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);
//                if (mLogShown) {
//                    output.setDisplayedChild(1);
//                } else {
//                    output.setDisplayedChild(0);
//                }
                supportInvalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Create a chain of targets that will receive log data */
    @Override
    public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
//        LogFragment logFragment = (LogFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.log_fragment);
//        msgFilter.setNext(logFragment.getLogView());

        Log.i(TAG, "Ready");
    }
}
