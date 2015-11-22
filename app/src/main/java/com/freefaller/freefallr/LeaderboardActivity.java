package com.freefaller.freefallr;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LeaderboardActivity extends AppCompatActivity {

    TableLayout leaderboard_table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        leaderboard_table = (TableLayout) findViewById(R.id.leaderboard_table);
        RequestParams params = new RequestParams();
        params.add("n", "50");
        FreeFallrHttpClient.get("/top/", params, new LeaderboardHandler());
    }

    class LeaderboardHandler extends JsonHttpResponseHandler {
        public boolean success = false;
        public String message = null;
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            try{
                for(int i = 0; i < response.length(); i++){
                    JSONObject item = response.getJSONObject(i);

                    TextView rank = new TextView(LeaderboardActivity.this);
                    rank.setText(Integer.toString(i + 1));
                    TextView username = new TextView(LeaderboardActivity.this);
                    username.setText(item.getString("username"));
                    TextView time = new TextView(LeaderboardActivity.this);
                    time.setText(Integer.toString(item.getInt("score")));

                    TableRow row = new TableRow(LeaderboardActivity.this);
                    row.addView(rank);
                    row.addView(username);
                    row.addView(time);

                    leaderboard_table.addView(row);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            success = false;
            message = responseString;
        }
    }

}
