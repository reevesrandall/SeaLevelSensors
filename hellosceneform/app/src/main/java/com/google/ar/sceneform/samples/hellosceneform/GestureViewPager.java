package com.google.ar.sceneform.samples.hellosceneform;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.core.view.GestureDetectorCompat;
import androidx.viewpager.widget.ViewPager;

public class GestureViewPager extends ViewPager {
    private GestureDetectorCompat mGestureDetector;

    public GestureViewPager(Context context) {
        super(context);
    }

    public GestureViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    //can write a custom method that we can call when we hit a motion event
    public void setGestureDetector(GestureDetectorCompat gestureDetector) {
        mGestureDetector = gestureDetector;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        mGestureDetector.onTouchEvent(motionEvent);
        return super.onTouchEvent(motionEvent);
    }
}
