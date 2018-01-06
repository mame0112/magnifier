package com.mame.magnifying;

import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Parcel;
import android.os.Parcelable;

import com.mame.magnifying.util.LogUtil;

import java.io.Serializable;

/**
 * Created by kosukeEndo on 2018/01/04.
 */

public class OriginalMediaProjectionManager2 implements Serializable {

    private final static String TAG = "OriginalMediaProjectionManager";

    public transient MediaProjection mMediaProjection;

    public OriginalMediaProjectionManager2(MediaProjection projection){
        LogUtil.d(TAG, "OriginalMediaProjectionManager2");

        if(projection == null){
            LogUtil.d(TAG, "MediaProjection is null");
        } else {
            LogUtil.d(TAG, "MediaProjection is not null");
        }

        mMediaProjection = projection;
    }

    public MediaProjection getMediaProjectionManager(){
        return mMediaProjection;
    }

}
