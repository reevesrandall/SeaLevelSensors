/*
 * Copyright 2018 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.ar.sceneform.samples.hellosceneform;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.viewpager.widget.PagerAdapter;

import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.HashMap;
import java.util.Map;

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class HelloSceneformActivity extends AppCompatActivity implements Swipeable {
    private static final String TAG = HelloSceneformActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private WaterLevelARFragment arFragment;
    private ModelRenderable andyRenderable;

    private SeekBar heightPicker;
    private TextView heightLabel;
    private float planeHeight = 1.0f;

    private static final float METER_LIMIT = 3.0f;

    private GestureViewPager pager;
    private PagerAdapter pagerAdapter;
    private PagerContainer pagerContainer;
    private GestureDetectorCompat gestureDetector;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_ux);

        // set up gesture detection and the pages for horizontal storm scrolling
        GestureListener gestureListener = new GestureListener();
        gestureListener.setActivity(this);
        gestureDetector = new GestureDetectorCompat(this, gestureListener);

        pagerContainer = findViewById(R.id.pager_container);
        pager = pagerContainer.getViewPager();
        pager.setGestureDetector(gestureDetector);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        // configure the page settings
        pager.setOffscreenPageLimit(pagerAdapter.getCount());
        pager.setPageMargin(20);
        pager.setClipChildren(false);
        pager.setAdapter(pagerAdapter);
    }

    /**
     * Provides an implementation for receiving a fling gesture
     * @param direction the direction for the fling gesture
     */
    public void slideTransition(String direction) {
        if (direction.equals(getResources().getString(R.string.UP))) {
            Intent intent = new Intent(this, StormActivity.class);
            intent.putExtra("CURRENT_ITEM", pager.getCurrentItem());
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    findViewById(R.id.bottom_storm_name), getResources().getString(R.string.slide_transition_name));
            startActivity(intent, options.toBundle());
        }
    }

    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     *
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     *
     * <p>Finishes the activity if Sceneform can not run
     */
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

    /**
     * Allows for the seek bar to be initialized from the page fragments since it only appears on one
     * @param view the seek bar that we initialize. Retrieved from the fragment class
     */
    public void initSeekBar(SeekBar view) {
        arFragment = (WaterLevelARFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        heightPicker = view;
        heightPicker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                planeHeight = progress / (float) seekBar.getMax() * METER_LIMIT;
                arFragment.setWaterHeight(planeHeight);
                updateHeightLabel();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        heightLabel = findViewById(R.id.heightLabel);
        updateHeightLabel();
        arFragment.setWaterLabel(heightLabel);

        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        ModelRenderable.builder()
                .setSource(this, R.raw.andy)
                .build()
                .thenAccept(renderable -> andyRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (andyRenderable == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(andyRenderable);
                    andy.select();
                });
    }

    private void updateHeightLabel() {
        heightLabel.setText(HeightFormatter.stringForHeight(planeHeight));
    }
}
