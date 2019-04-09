package com.cs3312.team8327.floodar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs3312.team8327.R;
import com.cs3312.team8327.floodar.Model.StormList;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * A fragment that displays the main BottomSheet cards for the floodAR app.
 */
public class BottomSheetFragment extends Fragment {
    private int stormIndex;
    private float stormLevel;

    @Override
    public View onCreateView(
            LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View view = layoutInflater.inflate(getContent(), viewGroup, false /* attachToRoot */);

        TextView stormName = view.findViewById(R.id.bottom_storm_name);
        stormName.setText(StormList.getInstance().getStorm(stormIndex).getName());

        stormLevel = StormList.getInstance().getStorm(stormIndex).getLevel();

        if (stormIndex == 0) {
            // set up the slider
            ((ArActivity) getActivity()).initSeekBar(view.findViewById(R.id.heightPicker));
        }

        if (stormLevel > 0) {
            // set the storm level to be that of the storm we are viewing
            Log.d("BOTTOM SHEET", "changing storm level");
            ((ArActivity) getActivity()).changeWaterLevel(stormLevel);
        }

        return view;
    }

    @LayoutRes
    protected int getContent() {
        if (stormIndex == 0) {
            return R.layout.bottom_sheet_with_slider;
        } else {
            return R.layout.bottomsheet_fragment;
        }
    }

    public void setStormIndex(int index) {
        this.stormIndex = index;
    }
}
