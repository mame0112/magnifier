package com.mame.magnifying;

import android.media.projection.MediaProjectionManager;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by kosukeEndo on 2018/01/04.
 */

public class OriginalMediaProjectionManager implements Parcelable {

    private final static String TAG = "OriginalMediaProjectionManager";

    private MediaProjectionManager mMediaProjectionManager;

    public OriginalMediaProjectionManager(MediaProjectionManager manager){
        //TODO
        mMediaProjectionManager = manager;
    }

    protected OriginalMediaProjectionManager(Parcel in) {
        mMediaProjectionManager = in.readParcelable(MediaProjectionManager.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mMediaProjectionManager);
//        dest.writeParcelable(mMediaProjectionManager, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OriginalMediaProjectionManager> CREATOR = new Creator<OriginalMediaProjectionManager>() {
        @Override
        public OriginalMediaProjectionManager createFromParcel(Parcel in) {
            return new OriginalMediaProjectionManager(in);
        }

        @Override
        public OriginalMediaProjectionManager[] newArray(int size) {
            return new OriginalMediaProjectionManager[size];
        }
    };

    public MediaProjectionManager getMediaProjectionManager(){
        return mMediaProjectionManager;
    }

}
