
/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.ar.sceneform.samples.hellosceneform;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.ar.sceneform.samples.hellosceneform.Model.StormList;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

/** A fragment that displays the main BottomSheet demo for the Catalog app. */
public class BottomSheetFragment extends Fragment {
    private int stormIndex;

    @Override
    public View onCreateView(
            LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View view = layoutInflater.inflate(getContent(), viewGroup, false /* attachToRoot */);

        TextView stormName = view.findViewById(R.id.bottom_storm_name);
        stormName.setText(StormList.getStorm(stormIndex).getName());

        if (stormIndex == 0) {
            // set up the slider
            ((HelloSceneformActivity) getActivity()).initSeekBar(view.findViewById(R.id.heightPicker));
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
