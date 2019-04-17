package com.cs3312.team8327.floodar;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.cs3312.team8327.R;
import com.cs3312.team8327.floodar.Model.StormList;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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

        stormLevel = StormList.getInstance().getStorm(stormIndex).getLevel();

        // Set up BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_content);
        View bottomSheetInternal = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        if (bottomSheetInternal != null) {
            BottomSheetBehavior.from(bottomSheetInternal).setPeekHeight(400);
//            TextView dialogText = bottomSheetInternal.findViewById(R.id.bottomsheet_state);
            BottomSheetBehavior.from(bottomSheetInternal)
                    .setBottomSheetCallback(createBottomSheetCallback());
//            TextView bottomSheetText = view.findViewById(R.id.cat_persistent_bottomsheet_state);
            View bottomSheetPersistent = view.findViewById(R.id.bottom_drawer);
            BottomSheetBehavior.from(bottomSheetPersistent)
                    .setBottomSheetCallback(createBottomSheetCallback());
        }

        if (getActivity() != null) {
            if (stormLevel > 0) {
                Log.e("SLIDER", "" + stormIndex);
                ((ArActivity) getActivity()).initSeekBar(view.findViewById(R.id.heightPicker), true, StormList.getInstance().getStorm(stormIndex).getCategory() - 1);
//                ((ArActivity) getActivity()).initSeekBar(bottomSheetDialog.getCurrentFocus().findViewById(R.id.heightPicker), true, StormList.getInstance().getStorm(stormIndex).getCategory());
            } else {
//                ((ArActivity) getActivity()).initSeekBar(bottomSheetDialog.getCurrentFocus().findViewById(R.id.heightPicker), false, StormList.getInstance().getStorm(stormIndex).getCategory());
                ((ArActivity) getActivity()).initSeekBar(view.findViewById(R.id.heightPicker), false, StormList.getInstance().getStorm(stormIndex).getCategory());
            }
        }

        populateStorm(stormIndex, view);
//        if (stormIndex == 0) {
//            // set up the slider
//            ((ArActivity) getActivity()).initSeekBar(view.findViewById(R.id.heightPicker));
//        }

        // set the storm level to be that of the storm we are viewing
        Log.d("BOTTOM SHEET", "changing storm level");
        ((ArActivity) getActivity()).changeWaterLevel(stormLevel);

        return view;
    }

    @LayoutRes
    private int getContent() {
        return R.layout.bottom_sheet_with_slider;
    }

    public void setStormIndex(int index) {
        this.stormIndex = index;
    }

    /**
     * Programmatically set storm information in the activity since we don't know which storm
     * will be viewed
     * @param stormIndex the index of the storm being viewed in the stormlist, used to look up
     *                   storm information
     */
    private void populateStorm(int stormIndex, View view) {
        // TODO: Make a notched slider that is set to the category
        TextView stormName = view.findViewById(R.id.bottom_storm_name);
        TextView stormSubtext = view.findViewById(R.id.storm_subtext);
        TextView stormDescription = view.findViewById(R.id.storm_description);
        TextView stormLink = view.findViewById(R.id.storm_link);
        stormName.setText(StormList.getInstance().getStorm(stormIndex).getName());
        stormSubtext.setText(StormList.getInstance().getStorm(stormIndex).getSubtext());
        stormDescription.setText(StormList.getInstance().getStorm(stormIndex).getDescription());
//        stormDescription.setMovementMethod(new ScrollingMovementMethod());

        // assumes that the Custom flood level will be the last element of the list (far right)
        // also assumes that there wont be a link for this. Get rid of the if statement if there is
        if (stormIndex != StormList.getInstance().getLength() - 1) {
            Log.e("BOTTOM FRAGMENT", "" + StormList.getInstance().getStorm(stormIndex).getLink());
            stormLink.setText(Html.fromHtml("<a href=\"" + StormList.getInstance().getStorm(stormIndex).getLink() + "\">Read more</a>"));
//            stormLink.setMovementMethod(LinkMovementMethod.getInstance());
//            Linkify.addLinks(stormLink, Linkify.WEB_URLS);
//            stormLink.setLinksClickable(true);
        }
        // TODO: Make the sliders disabled for historical storms
        // TODO: Make descriptions change for new locations => on resume probably
        // TODO: Make descriptions change on the change of the slider for the custom storm view
    }

    private BottomSheetBehavior.BottomSheetCallback createBottomSheetCallback() {
        // Set up BottomSheetCallback
        BottomSheetBehavior.BottomSheetCallback bottomSheetCallback =
                new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {

                        switch (newState) {
                            case BottomSheetBehavior.STATE_DRAGGING:
//                                text.setText(R.string.cat_bottomsheet_state_dragging);
                                break;
                            case BottomSheetBehavior.STATE_EXPANDED:
//                                text.setText(R.string.cat_bottomsheet_state_expanded);
                                break;
                            case BottomSheetBehavior.STATE_COLLAPSED:
//                                text.setText(R.string.cat_bottomsheet_state_collapsed);
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
                };
        return bottomSheetCallback;
    }
}
