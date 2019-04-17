package com.cs3312.team8327.floodar.Util;

import android.util.Log;

import com.cs3312.team8327.floodar.Model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BoundingBoxUtil {

    /**
     * finds the bounding box for the users location
     * @param jsonBoxes list of bounding boxes from AirTable API with storm categories and preprocessed flood levels
     */
    public static JSONObject findBoundingBox(JSONArray jsonBoxes) {
        Location loc = Location.getInstance();

        double currLat = loc.getLat();
        double currLon = loc.getLon();
//        Log.e("BOunding Box", "lat" + currLat);
//        Log.e("BOunding Box", "long " + currLon);
//        Log.e("JSON BOUNDING", "" + jsonBoxes);
        for (int i = 0; i < jsonBoxes.length(); i++) {
            try {
                JSONObject box = jsonBoxes.getJSONObject(i).getJSONObject("fields");
//                Log.e("JSON BOUNDING", "" + box);
//                Log.e("JSON BOUNDING UPPER LEFT LAT", "" + (currLat < box.getDouble("ullat")));
//                Log.e("JSON BOUNDING UPPER RIGHT LAT", "" + (currLat < box.getDouble("urlat")));
//                Log.e("JSON BOUNDING LOWER RIGHT LAT", "" + (currLat > box.getDouble("lrlat")));
//                Log.e("JSON BOUNDING LOWER LEFT LAT", "" + (currLat > box.getDouble("lllat")));
//                Log.e("JSON BOUNDING UPPER LEFT LONG", "" + (currLon > box.getDouble("ullong")));
//                Log.e("JSON BOUNDING UPPER RIGHT LONG", "" + (currLon < box.getDouble("urlong")));
//                Log.e("JSON BOUNDING LOWER RIGHT LONG", "" + (currLon < box.getDouble("lrlong")));
//                Log.e("JSON BOUNDING LOWER LEFT LONG", "" + (currLon > box.getDouble("lllong")));
                //
                // assumes that this is being used in the US
                // latitude check
                if (currLat < box.getDouble("ullat") && currLat < box.getDouble("urlat") && currLat > box.getDouble("lrlat") && currLat > box.getDouble("lllat")) {
                    // longitude check
                    if (currLon > box.getDouble("ullong") && currLon < box.getDouble("urlong") && currLon < box.getDouble("lrlong") && currLon > box.getDouble("lllong")) {
                        // we found the bounding box
                        Log.e("BOUNDING BOX", "We found a box");
                        return box;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}