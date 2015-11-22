package com.freefaller.freefallr;


import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

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

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseBody) {
            Log.d("DEBUG", responseBody);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
            Log.d("DEBUG", "Failure");
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
}