package com.example.user.snapkart;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Splash extends AppCompatActivity {
    private int SPLASH_DISPLAY_LENGTH = 4000;
    private Boolean isLoggedin =  Boolean.FALSE;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            }
        }
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                isLoggedin = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("loggedIn",Boolean.FALSE);
                //Toast.makeText(Splash.this, isLoggedin+"", Toast.LENGTH_SHORT).show();
                if(isLoggedin.equals(Boolean.TRUE)){
                        Intent mainIntent = new Intent(Splash.this, HomeActivity.class);
                        Splash.this.startActivity(mainIntent);
                        Splash.this.finish();

                }
                else{
                    Intent mainIntent = new Intent(Splash.this,Login.class);
                    Splash.this.startActivity(mainIntent);}
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
                                             @NonNull int[] grantResults){

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }
    }
}
