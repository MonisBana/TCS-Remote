package com.example.user.merchantapp;

import android.content.Intent;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    private int SPLASH_DISPLAY_LENGTH = 4000;
    private Boolean isLoggedin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                isLoggedin = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("loggedIn",Boolean.FALSE);
                if(isLoggedin.equals(Boolean.TRUE)){
                    Intent mainIntent = new Intent(Splash.this,MainActivity.class);
                    Splash.this.startActivity(mainIntent);
                }
                else{
                Intent mainIntent = new Intent(Splash.this,Login.class);
                    Splash.this.startActivity(mainIntent);}
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
