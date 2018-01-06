package com.mame.magnifying;

import android.media.projection.MediaProjection;

/**
 * Created by kosukeEndo on 2018/01/05.
 */

public class OriginalMediaProjectionManager3 {

    private static OriginalMediaProjectionManager3 sManager = new OriginalMediaProjectionManager3();

    private static MediaProjection mMediaProjection;

    /**
     * Singletone
     */
    private OriginalMediaProjectionManager3(){

    }

    public static OriginalMediaProjectionManager3 getInstance(){
        return sManager;
    }

    public void setMediaProjection(MediaProjection mediaProjection){
        mMediaProjection = mediaProjection;
    }

    public MediaProjection getMediaProjection(){
        return mMediaProjection;
    }

}
