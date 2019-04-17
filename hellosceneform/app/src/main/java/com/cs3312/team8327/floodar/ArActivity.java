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
package com.cs3312.team8327.floodar;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cs3312.team8327.R;
import com.cs3312.team8327.floodar.Model.Location;
import com.cs3312.team8327.floodar.Model.StormList;
import com.cs3312.team8327.floodar.Util.HeightFormatter;
import com.cs3312.team8327.floodar.Util.HttpRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.TransformableNode;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;

import java.util.List;

/**
 * This is the activity for showing the AR water level fragment on the user's camera
 * as well as a list of past storms for further viewing
 */
public class ArActivity extends AppCompatActivity implements Swipeable, AsyncListener, PermissionsListener {
    private static final String TAG = ArActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private WaterLevelARFragment arFragment;
    private ModelRenderable andyRenderable;

    private SeekBar heightPicker;
    private TextView heightLabel;
    private float planeHeight = 1.0f;

    private static final float METER_LIMIT = 3.0f;

//    private GestureViewPager pager;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private PagerContainer pagerContainer;
    private GestureDetectorCompat gestureDetector;

    // locations permissions
    private PermissionsManager permissionsManager;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        enableLocationComponent();

        // perform http request to get storms
        HttpRequest.sendStormRequest(this, getString(R.string.airtable_key), this);
        HttpRequest.sendBoxRequest(this, getString(R.string.airtable_key_coordinates), this);

        setContentView(R.layout.activity_ar);

        arFragment = (WaterLevelARFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
    }

    /**
     * Handles the population of the bottom drawer of cards through the http request
     * Used as a callback in the add storms method
     */
    public void onEventCompleted() {
        pagerContainer = findViewById(R.id.pager_container);
        pager = pagerContainer.getViewPager();
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        // configure the page settings
        pager.setOffscreenPageLimit(pagerAdapter.getCount());
        pager.setPageMargin(20);
        pager.setClipChildren(false);
        pager.setAdapter(pagerAdapter);

        pager.setCurrentItem(StormList.getInstance().getLength() - 1);

        FloatingActionButton mapButton;
        FloatingActionButton helpButton;

        mapButton = findViewById(R.id.map_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(i);
            }
        });

        helpButton = findViewById(R.id.help_button);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(i);
            }
        });
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
     * Allows the water level for the AR planes to be changed based on the current storm being viewed
     * in the bottom cards
     * @param waterLevel the height to set the AR water level to
     */
    public void changeWaterLevel(float waterLevel) {
        Log.d("AR ACTIVITY", "changing storm level");
        planeHeight = waterLevel;
        arFragment.setWaterHeight(planeHeight);
    }

    /**
     * Allows for the seek bar to be initialized from the page fragments since it only appears on one
     * @param view the seek bar that we initialize. Retrieved from the fragment class
     * @param bottomSheetFragment
     */
    public void initSeekBar(SeekBar view, boolean disabled, int category, BottomSheetFragment bottomSheetFragment) {
        heightPicker = view;

        heightPicker.setProgress(category);
        if (disabled) {
//            heightPicker.setEnabled(false);
        }
        heightPicker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                planeHeight = progress / (float) seekBar.getMax() * METER_LIMIT;
                arFragment.setWaterHeight(planeHeight);
                updateHeightLabel();
                if (!disabled) {
                    Log.e("PROGRESS", "" + progress);
                    bottomSheetFragment.updateDescOnSeekBarChange(progress + 1);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        heightLabel = new TextView(this);
        updateHeightLabel();
        // arFragment.setWaterLabel(heightLabel);

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


    private void enableLocationComponent() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
                @Override
                public void onSuccess(android.location.Location location) {
                    if (location != null) {
                        Location.getInstance().setLatLon(location.getLatitude(), location.getLongitude(), getApplicationContext());
                        Location.getInstance().setElevation(location.getAltitude());
                    } else {
                        Location.getInstance().setLatLon(32.080926, -81.091216, getApplicationContext());
                        Location.getInstance().setElevation(5);
                    }
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Location.getInstance().setLatLon(32.080926, -81.091216, getApplicationContext());
                    Location.getInstance().setElevation(5);
                }
            }).addOnCanceledListener(this, new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    Location.getInstance().setLatLon(32.080926, -81.091216, getApplicationContext());
                    Location.getInstance().setElevation(5);
                }
            });
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
                @Override
                public void onSuccess(android.location.Location location) {
                    if (location != null) {
                        Location.getInstance().setLatLon(location.getLatitude(), location.getLongitude(), getApplicationContext());
                        Location.getInstance().setElevation(location.getAltitude());
                    } else {
                        Location.getInstance().setLatLon(32.080926, -81.091216, getApplicationContext());
                        Location.getInstance().setElevation(5);
                    }
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Location.getInstance().setLatLon(32.080926, -81.091216, getApplicationContext());
                    Location.getInstance().setElevation(5);
                }
            }).addOnCanceledListener(this, new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    Location.getInstance().setLatLon(32.080926, -81.091216, getApplicationContext());
                    Location.getInstance().setElevation(5);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            // permissions denied, setting default location to be in savannah
            Location.getInstance().setLatLon(32.080926, -81.091216, getApplicationContext());
            Location.getInstance().setElevation(5);
            finish();
        }
    }
}
