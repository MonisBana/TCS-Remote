package com.example.user.merchantapp;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private String Email,Name,Mobile;
    private DatabaseReference mUserReference;
    private Button mPostBtn,mMyProductsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Email = intent.getStringExtra("email");
        Name = intent.getStringExtra("name");
        Mobile = intent.getStringExtra("mobile");
        mPostBtn = findViewById(R.id.postBtn);
        mMyProductsBtn = findViewById(R.id.myProductsBtn);
        mUserReference = FirebaseDatabase.getInstance().getReference().child("User");
        String id = mUserReference.push().getKey();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("userId",id).apply();
        User user = new User(Name,Mobile,Email);
        mUserReference.child(id).setValue(user);
        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,PostProduct.class);
                startActivity(i);
            }
        });
        mMyProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,MyProduct.class);
                startActivity(i);
            }
        });
        /*mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                *//*if(dataSnapshot.child("email").toString().equals(Email)){
                    Toast.makeText(MainActivity.this, "Email Already signed up,try to login", Toast.LENGTH_SHORT).show();
                }
                else{
                    User user = new User(Name,Mobile,Email);
                    mUserReference.push().setValue(user);
                }*//*
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/
    }
}
