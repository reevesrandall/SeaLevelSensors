package com.google.ar.sceneform.samples.hellosceneform;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = 4;

    public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    @NonNull
    public Fragment getItem(int position) {
        BottomSheetFragment fragment = new BottomSheetFragment();
        fragment.setStormIndex(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
