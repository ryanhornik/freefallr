package com.freefaller.freefallr;


import android.content.Intent;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;

public class FreeFallrHttpClient{
    public static final String BASE_URL = "http://freefallr-dev.elasticbeanstalk.com";
    static AsyncHttpClient client = new AsyncHttpClient();
    static PersistentCookieStore cookieStore = null;

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if(cookieStore == null){
            cookieStore = new PersistentCookieStore(MainActivity.instance);
            client.setCookieStore(cookieStore);
        }
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if(cookieStore == null){
            cookieStore = new PersistentCookieStore(MainActivity.instance);
            client.setCookieStore(cookieStore);
        }
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}