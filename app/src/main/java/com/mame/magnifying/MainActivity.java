package com.mame.magnifying;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.mame.magnifying.logger.Log;
import com.mame.magnifying.logger.LogFragment;
import com.mame.magnifying.logger.LogWrapper;
import com.mame.magnifying.logger.MessageOnlyLogFilter;
import com.mame.magnifying.util.LogUtil;

import java.io.Serializable;

public class MainActivity extends SampleActivityBase {
    public static final String TAG = "MainActivity";

    // Whether the Log Fragment is currently shown
    private boolean mLogShown;

    private static final String STATE_RESULT_CODE = "result_code";
    private static final String STATE_RESULT_DATA = "result_data";

    private static final int REQUEST_MEDIA_PROJECTION = 1;

    private int mResultCode;
    private Intent mResultData;

    private MediaProjectionManager mMediaProjectionManager;

    private MediaProjection mMediaProjection;

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

        if (savedInstanceState != null) {
            mResultCode = savedInstanceState.getInt(STATE_RESULT_CODE);
            mResultData = savedInstanceState.getParcelable(STATE_RESULT_DATA);
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

        mMediaProjectionManager = (MediaProjectionManager)
                getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(),
                    REQUEST_MEDIA_PROJECTION);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mResultData != null) {
            outState.putInt(STATE_RESULT_CODE, mResultCode);
            outState.putParcelable(STATE_RESULT_DATA, mResultData);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.d(TAG, "onActivityResult: " + requestCode);
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            LogUtil.d(TAG, "A");
            if (resultCode != RESULT_OK) {
                Log.i(TAG, "User cancelled");
                Toast.makeText(this, R.string.user_cancelled, Toast.LENGTH_SHORT).show();
                return;
            }
            Log.i(TAG, "Starting screen capture");
            mResultCode = resultCode;
            mResultData = data;
            mMediaProjection = mMediaProjectionManager.getMediaProjection(mResultCode, mResultData);
            // Start Chathead service
//            Intent intent = new Intent(getApplicationContext(), ChatHeadService.class);
//            OriginalMediaProjectionManager originalProjectionManager = new OriginalMediaProjectionManager(mMediaProjectionManager);
//            intent.putExtra("projection_manager", originalProjectionManager);
//            startService(intent);

            LogUtil.d(TAG, "startService");

//            Intent intent = new Intent(this, ChatHeadService.class);
//            OriginalMediaProjectionManager2 originalProjectionManager = new OriginalMediaProjectionManager2(mMediaProjectionManager);
//            intent.putExtra("projection_manager", originalProjectionManager);
//            startService(intent);


//            Intent intent2 = new Intent(getApplicationContext(), com.mame.magnifying.service.ChatHeadService.class);
//            OriginalMediaProjectionManager2 originalProjectionManager = new OriginalMediaProjectionManager2(mMediaProjection);
//            intent2.putExtra("media_projection", originalProjectionManager);
//            ContextCompat.startForegroundService(getApplicationContext(), intent2);

            OriginalMediaProjectionManager3.getInstance().setMediaProjection(mMediaProjection);

            Intent intent2 = new Intent(getApplicationContext(), com.mame.magnifying.service.ChatHeadService.class);
            ContextCompat.startForegroundService(getApplicationContext(), intent2);

            finish();

        }
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
