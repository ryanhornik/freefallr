package com.freefaller.freefallr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    Button login, signUp;
    EditText usernameField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (Button) findViewById(R.id.login);
        signUp = (Button) findViewById(R.id.signup);
        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View v){
               Log.d("Debug", "This is working");
               try {
                   URL url = new URL("http://freefallr-dev.elasticbeanstalk.com/login");
                   HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                   conn.setRequestMethod("POST");
                   conn.setReadTimeout(10000);
                   conn.setConnectTimeout(15000);
                   conn.setDoInput(true);
                   conn.setDoOutput(true);

                   Uri.Builder builder = new Uri.Builder()
                           .appendQueryParameter("username", usernameField.getText().toString())
                           .appendQueryParameter("password", passwordField.getText().toString());
                   String query = builder.build().getEncodedQuery();

                   OutputStream os = conn.getOutputStream();
                   BufferedWriter writer = new BufferedWriter(
                           new OutputStreamWriter(os, "UTF-8"));
                   writer.write(query);
                   writer.flush();
                   writer.close();
                   os.close();

                   conn.connect();
                   Log.d("Debug", conn.getResponseMessage());
               } catch (MalformedURLException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }

           }
        });

        signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
        }
}
