package com.freefaller.freefallr;

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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button login, signUp;
    EditText usernameField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (Button) findViewById(R.id.login);
        signUp = (Button) findViewById(R.id.login);
        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View v){
               HttpClient httpClient = new DefaultHttpClient();
               HttpPost httpPost = new HttpPost("http://10.0.2.2");

               List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
               nameValuePair.add(new BasicNameValuePair("username", usernameField.toString()));
               nameValuePair.add(new BasicNameValuePair("password", passwordField.toString()));

               try{
                   httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
               } catch (UnsupportedEncodingException e) {
                   e.printStackTrace();
               }

               try{
                   HttpResponse response = httpClient.execute(httpPost);
                   Log.d("Http Post response: ", response.toString());
               } catch (ClientProtocolException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
        });
    }

}
