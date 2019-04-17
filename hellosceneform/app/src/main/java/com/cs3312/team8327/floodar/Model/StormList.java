package com.cs3312.team8327.floodar.Model;

import android.util.Log;

import com.android.volley.VolleyError;
import com.cs3312.team8327.floodar.AsyncListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class for persisting a common list of storms across the app
 */
public class StormList {
    private static List<Storm> list;
    private boolean hasBeenRequested = false;

    private static StormList instance = null;

    private StormList() {
        list = new ArrayList<>();
    }

    /**
     * Getter for the sole instance of the storm list class -> follows singleton design pattern
     * @return persistent stormlist object
     */
    public static StormList getInstance() {
        if (instance == null) {
            instance = new StormList();
        }
        return instance;
    }

    public Storm getStorm(int index) {
        return list.get(index);
    }

    private void addStorm(Storm storm) {
        list.add(storm);
    }

    public int getLength() {
        Log.e("LENGTH", list.size() + "");
        return list.size();
    }

    public boolean hasBeenRequested() {
        return hasBeenRequested;
    }

    /**
     * Method to add an adjustable water level independent of the storms in the database
     */
    private void addAdjustableWaterLevel() {
        list.add(new Storm("Flood risk", 0.0f, "You are currently 8 feet above sea level \n \nA Category 2 storm could bring storm surge of 11-17 feet above normal tide levels \n \nDuring a Category 1 storm, you could see flooding of 4 feet at your location", 0, "Adjust the slider to visualize flood risk at your location"));
    }

    /**
     * Method called upon the successful completion of an http request to the storms database
     * @param array A JSONObject containing an array of storms from the database
     * @param callback callback function provided by the calling activity to pass the data back when the asynchronous operation is complete
     */
    public void addStorms(JSONObject array, AsyncListener callback) {
        // add some storms from HTTP request
        try {
            JSONArray jsonStorms = array.getJSONArray("records");
            Log.e("PARSE STORMS", jsonStorms.toString());
            for (int i = 0; i < jsonStorms.length(); i++) {
                JSONObject o = jsonStorms.getJSONObject(i);
                JSONObject storm = o.getJSONObject("fields");
                // if new fields are added to the AirTable Database the storm should have them added here
                // might be better to use a factory design pattern here
                Storm s = new Storm(storm.getString("name"), ((float) storm.getDouble("category")) * 0.1f, storm.getString("description"), (int) storm.getDouble("category"), storm.getString("subtext"));
                s.setLink(storm.getString("wikipedia"));
                addStorm(s);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addAdjustableWaterLevel();
        callback.onEventCompleted();
    }

    /**
     * Method called in the event of an error in the http request to retrieve list of storms
     * In the event of a http request failure, we fallback to a hardcoded list of storms
     * @param error The error that occurred during the http request
     * @param callback A callback function passed by the calling class to handle the asynchronous nature of the request
     */
    public void stormsRequestError(VolleyError error, AsyncListener callback) {
        // handle the error by manually adding storms
        list.add(new Storm("Hurricane Matthew", 0.76f, "Hurricane Matthew was the first Category 5 Atlantic hurricane since Felix in 2007, and also caused catastrophic damage and a humanitarian crisis in Haiti, as well as widespread devastation in the southeastern United States. The deadliest Atlantic hurricane since Hurricane Stan in 2005, Matthew was the thirteenth named storm, fifth hurricane and second major hurricane of the 2016 Atlantic hurricane season. It caused extensive damage to landmasses in the Greater Antilles, and severe damage in several islands of the Bahamas who were still recovering from Joaquin which had pounded such areas nearly a year earlier. At one point, the hurricane even threatened to be the first storm of Category 3 or higher intensity to strike the United States since Wilma in 2005, but Matthew stayed just offshore paralleling the Floridan coastline.", 5, "September 2016 - October 2016"));
        list.add(new Storm("Hurricane Irma", 1.52f, "Hurricane Irma was an extremely powerful and catastrophic Cape Verde hurricane, the strongest observed in the Atlantic in terms of maximum sustained winds since Wilma, and the strongest storm on record to exist in the open Atlantic region. Irma was the first Category 5 hurricane to strike the Leeward Islands on record, followed by Maria two weeks later, and is the second-costliest Caribbean hurricane on record, after Maria. The ninth named storm, fourth hurricane, second major hurricane, and first Category 5 hurricane of the 2017 Atlantic hurricane season, Irma caused widespread and catastrophic damage throughout its long lifetime, particularly in the northeastern Caribbean and the Florida Keys. It was also the most intense hurricane to strike the continental United States since Katrina in 2005, the first major hurricane to make landfall in Florida since Wilma in the same year, and the first Category 4 hurricane to strike the state since Charley in 2004. The word Irmageddon was coined soon after the hurricane to describe the damage caused by the hurricane.", 3, "September 2017"));
        list.add(new Storm("Hurricane Michael", 1.83f, "Hurricane Michael was the third-most intense Atlantic hurricane to make landfall in the United States in terms of pressure, behind the 1935 Labor Day hurricane and Hurricane Camille of 1969, as well as the strongest storm in terms of maximum sustained wind speed to strike the contiguous United States since Andrew in 1992. In addition, it was the strongest storm on record in the Florida Panhandle, and was the fourth-strongest landfalling hurricane in the contiguous United States, in terms of wind speed.", 4, "October 2018"));
        addAdjustableWaterLevel();
        callback.onEventCompleted();
    }
}
