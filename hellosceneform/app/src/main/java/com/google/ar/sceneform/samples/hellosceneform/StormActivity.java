package com.google.ar.sceneform.samples.hellosceneform;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.ar.sceneform.samples.hellosceneform.Model.StormList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

public class StormActivity extends AppCompatActivity implements Swipeable {
    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storm);

        // get the storm from the intent
        Bundle extras = getIntent().getExtras();
        int stormIndex;
        if (extras != null) {
            stormIndex = extras.getInt("CURRENT_ITEM");
        } else {
            stormIndex = -1;
        }

        populateStorm(stormIndex);

        // allow the page to register gestures
        View layoutView = findViewById(R.id.bottom_drawer);
        layoutView.setOnTouchListener(touchListener);

        // set up gesture detection for swiping down
        GestureListener gestureListener = new GestureListener();
        gestureListener.setActivity(this);
        mDetector = new GestureDetectorCompat(this, gestureListener);
    }

    public void slideTransition(String direction) {
        if (direction.equals(getResources().getString(R.string.DOWN))) {
            supportFinishAfterTransition();
        }
    }

    // when we get a click on our view we give it to the gesture detector
    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.performClick();
            return mDetector.onTouchEvent(event);
        }
    };

    private void populateStorm(int stormIndex) {
        TextView stormName = findViewById(R.id.storm_name);
        stormName.setText(StormList.getStorm(stormIndex).getName());
    }
}
