package com.mame.magnifying.service;

import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.mame.magnifying.OriginalMediaProjectionManager;
import com.mame.magnifying.OriginalMediaProjectionManager2;
import com.mame.magnifying.OriginalMediaProjectionManager3;
import com.mame.magnifying.R;
import com.mame.magnifying.util.LogUtil;

import jp.co.recruit_lifestyle.android.floatingview.FloatingViewListener;
import jp.co.recruit_lifestyle.android.floatingview.FloatingViewManager;

/**
 * Created by kosukeEndo on 2018/01/03.
 */

public class ChatHeadService  extends Service implements FloatingViewListener {

    /**
     * デバッグログ用のタグ
     */
    private static final String TAG = "ChatHeadService";

    /**
     * 通知ID
     */
    private static final int NOTIFICATION_ID = 9083150;

    /**
     * FloatingViewManager
     */
    private FloatingViewManager mFloatingViewManager;

    private static final int REQUEST_MEDIA_PROJECTION = 1;

    private int mScreenDensity;

    private int mResultCode;
    private Intent mResultData;

    private Surface mSurface;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaProjectionManager mMediaProjectionManager;
    private SurfaceView mSurfaceView;

    @Override
    public void onCreate(){
        super.onCreate();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        LogUtil.d(TAG, "onStartCommand");

//        OriginalMediaProjectionManager2 original = (OriginalMediaProjectionManager2)extras.getSerializable("media_projection");
//
//        if(original == null){
//            LogUtil.d(TAG, "OriginalMediaProjectionManager is null");
//        } else {
//            LogUtil.d(TAG, "OriginalMediaProjectionManager is not null");
//        }
//
//        mMediaProjection = original.getMediaProjectionManager();
//
//        if(mMediaProjection == null){
//            LogUtil.d(TAG, "mMediaProjection is null");
//        } else {
//            LogUtil.d(TAG, "mMediaProjection is not null");
//        }

        mMediaProjection = OriginalMediaProjectionManager3.getInstance().getMediaProjection();

        final DisplayMetrics metrics = new DisplayMetrics();
        final WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;

        // 既にManagerが存在していたら何もしない
        if (mFloatingViewManager != null) {
            LogUtil.d(TAG, "mFloatingViewManager is not null");
            return START_STICKY;
        }
//        mMediaProjectionManager = (MediaProjectionManager)
//                activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);

//        final LayoutInflater inflater = LayoutInflater.from(this);
//        final ImageView iconView = (ImageView) inflater.inflate(R.layout.widget_chathead, null, false);
//        iconView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, getString(R.string.chathead_click_message));
//            }
//        });

//        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        mMediaProjectionManager = (MediaProjectionManager)
//                activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);

//        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        mSurfaceView = new SurfaceView(this);
//        mSurfaceView.setMinimumWidth(100);
//        mSurfaceView.setMinimumHeight(100);
//        mSurfaceView = (SurfaceView) inflater.inflate(R.layout.widget_chathead, null, false);

//        final LayoutInflater inflater = LayoutInflater.from(this);
        final LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.widget_chathead, null);
//        final LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSurfaceView = (SurfaceView) view.findViewById(R.id.surface);
//        mSurfaceView = (SurfaceView) inflater.inflate(R.layout.widget_chathead, null, false);
        mSurface = mSurfaceView.getHolder().getSurface();

        mFloatingViewManager = new FloatingViewManager(this, this);
        mFloatingViewManager.setFixedTrashIconImage(R.drawable.ic_trash_fixed);
        mFloatingViewManager.setActionTrashIconImage(R.drawable.ic_trash_action);
        final FloatingViewManager.Options options = new FloatingViewManager.Options();
        options.overMargin = (int) (16 * metrics.density);
        options.floatingViewHeight = 10;
        options.floatingViewWidth = 10;
//        mFloatingViewManager.addViewToWindow(mSurfaceView, options);
        mFloatingViewManager.addViewToWindow(mSurfaceView, options);
//        mFloatingViewManager.addViewToWindow(iconView, options);

        setUpVirtualDisplay();

        // 常駐起動
        startForeground(NOTIFICATION_ID, createNotification(this));

        startScreenCapture();

        return START_REDELIVER_INTENT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        destroy();
        super.onDestroy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d(TAG, "onBind");
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFinishFloatingView() {
        stopSelf();
        Log.d(TAG, getString(R.string.finish_deleted));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTouchFinished(boolean isFinishing, int x, int y) {
        if (isFinishing) {
            Log.d(TAG, getString(R.string.deleted_soon));
        } else {
            Log.d(TAG, getString(R.string.touch_finished_position, x, y));
        }
    }

    /**
     * Viewを破棄します。
     */
    private void destroy() {
        if (mFloatingViewManager != null) {
            mFloatingViewManager.removeAllViewToWindow();
            mFloatingViewManager = null;
        }
    }

    /**
     * 通知を表示します。
     * クリック時のアクションはありません。
     */
    private static Notification createNotification(Context context) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.default_floatingview_channel_id));
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(context.getString(R.string.chathead_content_title));
        builder.setContentText(context.getString(R.string.content_text));
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_MIN);
        builder.setCategory(NotificationCompat.CATEGORY_SERVICE);

        return builder.build();
    }

    private void startScreenCapture() {
        LogUtil.d(TAG, "startScreenCapture");
//        Activity activity = getActivity();
        if (mSurface == null) {
            LogUtil.d(TAG, "mSurface is null");
            return;
        }
        if (mMediaProjection != null) {
            LogUtil.d(TAG, "mMediaProjection is no null");
            setUpVirtualDisplay();
        } else if (mResultCode != 0 && mResultData != null) {
            LogUtil.d(TAG, "mResultCode is not 0 and mResultData is not null. Something wrong");
//            setUpMediaProjection();
            setUpVirtualDisplay();
        } else {
            Log.i(TAG, "Something wrong....");
            // This initiates a prompt dialog for the user to confirm screen projection.
//            startActivityForResult(
//                    mMediaProjectionManager.createScreenCaptureIntent(),
//                    REQUEST_MEDIA_PROJECTION);
        }
    }

    private void setUpMediaProjectiaaon() {

    }

    private void setUpVirtualDisplay() {
        Log.i(TAG, "Setting up a VirtualDisplay: " +
                mSurfaceView.getWidth() + "x" + mSurfaceView.getHeight() +
                " (" + mScreenDensity + ")");
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenCapture",
                mSurfaceView.getWidth(), mSurfaceView.getHeight(), mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mSurface, null, null);
//        mButtonToggle.setText(R.string.stop);
    }

    private void stopScreenCapture() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        mVirtualDisplay = null;
//        mButtonToggle.setText(R.string.start);
    }
}
