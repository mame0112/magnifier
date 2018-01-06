package com.mame.magnifying;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

import com.mame.magnifying.util.LogUtil;

/**
 * Created by kosukeEndo on 2018/01/06.
 */

public class CustomSurfaceView extends SurfaceView {

    private final static String TAG = "CustomSurfaceView";

    private CustomSurfaceViewListener mListener;

    public CustomSurfaceView(Context context) {
        super(context);

        LogUtil.d(TAG, "CustomSurfaceView constructor1");

    }

    public CustomSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.originalViewStyle);
        LogUtil.d(TAG, "CustomSurfaceView constructor2");
    }

    public CustomSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LogUtil.d(TAG, "CustomSurfaceView constructor3");

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        LogUtil.d("AAAA", "onSizeChanged Width:" + w + ",Height:" + h + " oldw: " + oldw + " oldh: " + oldh );
        mListener.onViewSizeChanged();

    }

//    @Override
//    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        LogUtil.d("AAAA", "onMeasure");
//        //TODO
//        setMeasuredDimension(300, 300);
//    }

    public void setCustomSurfaceViewListner(CustomSurfaceViewListener listener){
        mListener = listener;
    }


    public interface CustomSurfaceViewListener {
        void onViewSizeChanged();
    }

}
