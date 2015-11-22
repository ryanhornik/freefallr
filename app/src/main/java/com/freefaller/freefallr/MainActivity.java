package com.freefaller.freefallr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    public static MainActivity instance;
    Button login, signUp;
    EditText usernameField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);

        login = (Button) findViewById(R.id.login);
        signUp = (Button) findViewById(R.id.signup);
        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                FreeFallrHttpClient.LoginHandler.username = usernameField.getText().toString();
                params.add("username", usernameField.getText().toString());
                params.add("password", passwordField.getText().toString());
                FreeFallrHttpClient.post("/login/", params, new FreeFallrHttpClient.LoginHandler());
                if(FreeFallrHttpClient.LoginHandler.success){
                    Intent intent = new Intent(MainActivity.instance, UserStatsActivity.class);
                    MainActivity.instance.startActivity(intent);
                }else{
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Username/Password do not match")
                            .setMessage(FreeFallrHttpClient.LoginHandler.message)
                            .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
