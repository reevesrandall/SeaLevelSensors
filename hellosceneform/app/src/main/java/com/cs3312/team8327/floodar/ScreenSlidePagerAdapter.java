package com.cs3312.team8327.floodar;

import com.cs3312.team8327.floodar.Model.StormList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Adapter for determining the number and position of each of the storms in the bottom drawer
 * of storm cards
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = StormList.getInstance().getLength();

    public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    /**
     * For each item in the bottom drawer we create a new bottom sheet and assign it a position
     * @param position the position of the storm in the list -> corresponds to the storm's index
     *                 in the StormList class
     * @return
     */
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
