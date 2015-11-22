package com.freefaller.freefallr;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class FallListenerService extends Service implements SensorEventListener {
    static final int X = 0;
    static final int Y = 1;
    static final int Z = 2;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float NOISE = 3.0f;
    MediaPlayer mp = new MediaPlayer();

    int updates = 0;

    public FallListenerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d("DEBUG", "Started service");

        mp = MediaPlayer.create(this, R.raw.femalescream);
        mp.setLooping(true);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);

        return START_NOT_STICKY;
    }

    private void submit(long duration){
        Log.d("DEBUG", "Fell "+duration+"ms!");
        RequestParams params = new RequestParams();
        params.add("time", Long.toString(duration));
        FreeFallrHttpClient.post("/submit/", params, new SubmissionHandler());
    }

    boolean falling = false;
    long start_time;

    @Override
    public void onSensorChanged(SensorEvent event) {
        long time = System.currentTimeMillis();
        boolean currently_falling = Math.abs(event.values[X]) < NOISE &&
                                    Math.abs(event.values[Y]) < NOISE &&
                                    Math.abs(event.values[Z]) < NOISE;
        if(currently_falling && !falling){
            falling = true;
            start_time = time;
            mp.start();
        }
        if(!currently_falling && falling){
            mp.stop();
            try {
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            long elapsed = time - start_time;
            falling = false;
            submit(elapsed);
            try {
                mSensorManager.unregisterListener(this);
                Thread.sleep(2000);
                mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            mSensorManager.unregisterListener(this);
            Thread.sleep(30);
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    class SubmissionHandler extends TextHttpResponseHandler {
        public boolean success = false;
        public String message = null;


        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            success = false;
            message = responseString;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            success = true;
            message = responseString;
        }
    }

}
