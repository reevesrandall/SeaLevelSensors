package com.cs3312.team8327.floodar;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Class to detect and handle gestures throughout the app
 */
class GestureListener extends GestureDetector.SimpleOnGestureListener {

    // Minimal x and y axis swipe distance.
    private static int MIN_SWIPE_DISTANCE_X = 100;
    private static int MIN_SWIPE_DISTANCE_Y = 100;

    // Maximal x and y axis swipe distance.
    private static int MAX_SWIPE_DISTANCE_X = 1000;
    private static int MAX_SWIPE_DISTANCE_Y = 1000;

    private static final String DEBUG_TAG = "Gestures";

    private Swipeable activity;

    private int initialX;
    private int initialY;

    public void setActivity(Swipeable activity) {
        this.activity = activity;
    }

    public Swipeable getActivity() {
        return this.activity;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Log.e(DEBUG_TAG,"onDown: " + event.toString());
        initialX = (int) event.getX();
        initialY = (int) event.getY();
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        if (event1 != null && event2 != null) {
            // Get swipe delta value in x axis.
            float deltaX = event1.getX() - event2.getX();

            // Get swipe delta value in y axis.
            float deltaY = event1.getY() - event2.getY();

            // Get absolute value.
            float deltaXAbs = Math.abs(deltaX);
            float deltaYAbs = Math.abs(deltaY);

            // Only when swipe distance between minimal and maximal distance value then we treat it as effective swipe
            if ((deltaXAbs >= MIN_SWIPE_DISTANCE_X) && (deltaXAbs <= MAX_SWIPE_DISTANCE_X)) {
                if (deltaX > 0) {
//                    Log.d(DEBUG_TAG, "swipe left ");
                } else {
//                    Log.d(DEBUG_TAG, "swipe right ");
                }
            }

            if ((deltaYAbs >= MIN_SWIPE_DISTANCE_Y) && (deltaYAbs <= MAX_SWIPE_DISTANCE_Y)) {
                if (deltaY > 0) {
//                    Log.d(DEBUG_TAG, "swipe up ");
//                    this.activity.slideTransition("UP");
                } else {
//                    Log.d(DEBUG_TAG, "swipe down ");
                    this.activity.slideTransition("DOWN");
                }
            }

        }
        return true;
    }
}
