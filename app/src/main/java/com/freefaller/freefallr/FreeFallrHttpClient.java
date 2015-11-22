package com.freefaller.freefallr;


import android.content.Intent;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

class FreeFallrHttpClient{
    public static final String BASE_URL = "http://freefallr-dev.elasticbeanstalk.com";
    static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    static class LoginHandler extends TextHttpResponseHandler {
        public static String username = null;
        public static boolean success = false;
        public static String message = null;

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseBody) {
            success = true;
            message = responseBody;
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error){
            username = null;
            success = false;
            message = responseBody;
        }
    }

    static class RegisterHandler extends TextHttpResponseHandler {

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Log.d("DEBUG", "Failure");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseBody) {
            Log.d("DEBUG", responseBody);
        }
    }

    static class UserStatsHandler extends JsonHttpResponseHandler {
        public static String first_fall = null;
        public static String last_fall = null;
        public static String longest_fall = null;
        public static String shortest_fall = null;
        public static Double average_fall_time = null;
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                first_fall = response.getString("First_Fall");
                last_fall = response.getString("Last_Fall");
                longest_fall = response.getString("Longest_Fall");
                shortest_fall = response.getString("Shortest_Fall");
                average_fall_time = response.getDouble("Average_Fall_Time");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}