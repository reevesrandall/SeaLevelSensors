package com.cs3312.team8327.floodar.Util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cs3312.team8327.floodar.AsyncListener;
import com.cs3312.team8327.floodar.Model.Location;
import com.cs3312.team8327.floodar.Model.StormList;

import org.json.JSONObject;

/**
 * Helper class for performing http requests to the AirTable database
 */
public class HttpRequest {

    /**
     * Method for requesting list of storms from the AirTable Database
     * @param context the context the request is being called from - the activity usually
     * @param url url to send the request to
     * @param callback function to handle the JSON data received from the request
     */
    public static void sendStormRequest(Context context, String url, AsyncListener callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        StormList.getInstance().addStorms(response, callback);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        StormList.getInstance().stormsRequestError(error, callback);
                    }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Method for requesting list of storms from the AirTable Database
     * @param context the context the request is being called from - the activity usually
     * @param url url to send the request to
     * @param callback function to handle the JSON data received from the request
     */
    public static void sendBoxRequest(Context context, String url, AsyncListener callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Location.updateLocation(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                handle error on the request of bounding boxes
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
