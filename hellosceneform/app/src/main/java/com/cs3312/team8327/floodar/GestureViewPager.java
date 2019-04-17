package com.cs3312.team8327.floodar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.core.view.GestureDetectorCompat;
import androidx.viewpager.widget.ViewPager;

/**
 * Gesture handling for the View Pager class used for the bottom cards in the AR View
 */
public class GestureViewPager extends ViewPager {
    private GestureDetectorCompat mGestureDetector;
    private Swipeable swipeable;
    private int initialX;
    private int initialY;
    private boolean newSwipe = true;

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

    public void setSwipeable(Swipeable s) {
        swipeable = s;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
//        Log.e("TOUCH", "onTouchEvent: " + initialY);
        int swipeThreshold = 450;
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//            Log.e("FINGER DOWN", "" + (int)motionEvent.getY());
            initialX = (int)motionEvent.getX();
            initialY = (int)motionEvent.getY();
            newSwipe = true;
        }
        if ((int)motionEvent.getY() - initialY < (-1 * swipeThreshold) && newSwipe) {
            newSwipe = false;
            Log.e("ENOUGH GESTURE", "onTouchEvent: ");
            swipeable.slideTransition("UP");
        }
        if ((int)motionEvent.getY() - initialY > swipeThreshold && initialY < 250 && newSwipe) {
            newSwipe = false;
            Log.e("ENOUGH GESTURE", "onTouchEvent: ");
            swipeable.slideTransition("DOWN");
        }
        mGestureDetector.onTouchEvent(motionEvent);
        return super.onTouchEvent(motionEvent);
    }
}
