package com.cs3312.team8327.floodar;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.cs3312.team8327.R;
import com.cs3312.team8327.floodar.Model.StormList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.viewpager.widget.ViewPager;

/**
 * Activity for showing further details on a storm, launched when a user swipes up on a card in the
 * AR activity
 */
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

        // set up gesture detection for swiping down
        GestureListener gestureListener = new GestureListener();
        gestureListener.setActivity(this);
        mDetector = new GestureDetectorCompat(this, gestureListener);

        // allow the page to register gestures
        View layoutView = findViewById(R.id.bottom_drawer);
        layoutView.setOnTouchListener(touchListener);

        PagerContainer pagerContainer;
//        GestureViewPager pager;
        ViewPager pager;

        pagerContainer = findViewById(R.id.pager_container);
        pager = pagerContainer.getViewPager();
//        pager.setSwipeable(this);
//        pager.setGestureDetector(mDetector);
    }

    /**
     * Called when the user performs a swipe gesture
     * @param direction The direction of the swipe gesture. Will trigger a transition if the direction
     *                  is down
     */
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

    /**
     * Programmatically set storm information in the activity since we don't know which storm
     * will be viewed
     * @param stormIndex the index of the storm being viewed in the stormlist, used to look up
     *                   storm information
     */
    private void populateStorm(int stormIndex) {
        // TODO: Make a notched slider that is set to the category
        TextView stormName = findViewById(R.id.storm_name);
        TextView stormDescription = findViewById(R.id.storm_description);
        stormName.setText(StormList.getInstance().getStorm(stormIndex).getName());
        stormDescription.setText(StormList.getInstance().getStorm(stormIndex).getDescription());
        stormDescription.setMovementMethod(new ScrollingMovementMethod());
    }
}
