package com.freefaller.freefallr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UserStatsActivity extends AppCompatActivity {

    TextView username;
    TextView last_fall;
    TextView first_fall;
    TextView longest_fall;
    TextView shortest_fall;
    TextView average_fall;
    Button leaderboard_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        leaderboard_button = (Button) findViewById(R.id.leaderboard_button);
        leaderboard_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserStatsActivity.this, LeaderboardActivity.class);
                startActivity(intent);
            }
        });

        username = (TextView) findViewById(R.id.username);
        average_fall = (TextView) findViewById(R.id.average_fall);
        last_fall = (TextView) findViewById(R.id.last_fall);
        first_fall = (TextView) findViewById(R.id.first_fall);
        longest_fall = (TextView) findViewById(R.id.longest_fall);
        shortest_fall = (TextView) findViewById(R.id.shortest_fall);

        FreeFallrHttpClient.get("/stats/", new RequestParams(), new UserStatsHandler());
    }

    class UserStatsHandler extends JsonHttpResponseHandler {
        public String first_fall_str = null;
        public String last_fall_str = null;
        public String longest_fall_str = null;
        public String shortest_fall_str = null;
        public Double average_fall_time_str = null;

        public boolean success = false;
        public String message = null;
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                first_fall_str = response.getString("First_Fall");
                last_fall_str = response.getString("Last_Fall");
                longest_fall_str = response.getString("Longest_Fall");
                shortest_fall_str = response.getString("Shortest_Fall");
                average_fall_time_str = response.getDouble("Average_Fall_Time");
            } catch (JSONException e) {
                onFailure(statusCode, headers, "One or more values not found", new Throwable());
            }

            last_fall.setText(last_fall_str);
            first_fall.setText(first_fall_str);
            longest_fall.setText(longest_fall_str);
            shortest_fall.setText(shortest_fall_str);
            average_fall.setText(String.format("%fms", average_fall_time_str));
            username.setText(MainActivity.username);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            success = false;
            message = responseString;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                //Transition to settings menu
                Intent myIntent = new Intent(this, SettingsActivity.class);
                startActivity(myIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}