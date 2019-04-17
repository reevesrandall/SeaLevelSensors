package com.cs3312.team8327.floodar.Model;

import android.content.Context;
import android.util.Log;

import com.cs3312.team8327.floodar.Util.BoundingBoxUtil;
import com.cs3312.team8327.floodar.Util.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Location {
    private double lat;
    private double lon;
    private double elevation;
    private double category1;
    private double category2;
    private double category3;
    private double category4;
    private double category5;

    private static Location instance = null;

    private Location() {}

    /**
     * Getter for the sole instance of the location class -> follows singleton design pattern
     * @return persistent Location object
     */
    public static Location getInstance() {
        if (instance == null) {
            instance = new Location();
        }
        return instance;
    }

    public static void updateLocation(JSONObject array) {
        try {
            JSONArray jsonBoxes = array.getJSONArray("records");
            JSONObject bBox = BoundingBoxUtil.findBoundingBox(jsonBoxes);
            if (bBox != null) {
                getInstance().setCategories(bBox);
            } else {
                // revert to the elevation provided by location data
                double cat1Surge = 7.5;
                double cat2Surge = 14;
                double cat3Surge = 20;
                double cat4Surge = 24.5;
                double cat5Surge = 28.75;
                // check to make sure we don't end up with negative category heights
                getInstance().category1 = getInstance().elevation < cat1Surge ? cat1Surge - getInstance().elevation : 0;
                getInstance().category2 = getInstance().elevation < cat2Surge ? cat2Surge - getInstance().elevation : 0;
                getInstance().category3 = getInstance().elevation < cat3Surge ? cat3Surge - getInstance().elevation : 0;
                getInstance().category4 = getInstance().elevation < cat4Surge ? cat4Surge - getInstance().elevation : 0;
                getInstance().category5 = getInstance().elevation < cat5Surge ? cat5Surge - getInstance().elevation : 0;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setCategories(JSONObject box) {
        try {
            category1 = box.getDouble("category1");
            category2 = box.getDouble("category2");
            category3 = box.getDouble("category3");
            category4 = box.getDouble("category4");
            category5 = box.getDouble("category5");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setLatLon(double lat, double lon, Context context) {
        Log.e("LOCATION", "" +  lat + " long " + lon);
        this.lat = lat;
        this.lon = lon;
        HttpRequest.sendBoxRequest(context, "https://api.airtable.com/v0/appsTMdPpOmcl5xKO/Coordinates?api_key=key6J4mOwouRSs457", null);
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public double getCategory1() {
        return category1;
    }

    public void setCategory1(double category1) {
        this.category1 = category1;
    }

    public double getCategory2() {
        return category2;
    }

    public void setCategory2(double category2) {
        this.category2 = category2;
    }

    public double getCategory3() {
        return category3;
    }

    public void setCategory3(double category3) {
        this.category3 = category3;
    }

    public double getCategory4() {
        return category4;
    }

    public void setCategory4(double category4) {
        this.category4 = category4;
    }

    public double getCategory5() {
        return category5;
    }

    public void setCategory5(double category5) {
        this.category5 = category5;
    }
}
