package com.example.user.merchantapp;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import static com.example.user.merchantapp.R.menu.main_menu;
import static com.example.user.merchantapp.R.id.action_logout;

public class MainActivity extends AppCompatActivity {
    private String Email,Name,Mobile;
    private DatabaseReference mUserReference;
    private CardView mPostBtn,mMyProductsBtn;
    private String UserId;
    private FirebaseAuth mAuth;

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
        mAuth = FirebaseAuth.getInstance();
        UserId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("userId","abc");
        mUserReference = FirebaseDatabase.getInstance().getReference().child("User");
        Boolean login = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("login",Boolean.TRUE);
        //Toast.makeText(this, UserId+"", Toast.LENGTH_SHORT).show();
        if(login.equals(Boolean.FALSE)){
            String id = mUserReference.push().getKey();
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("userId",id).apply();
            User user = new User(Name,Mobile,Email);
            mUserReference.child(id).setValue(user);
            //Toast.makeText(this,"Login", Toast.LENGTH_SHORT).show();
        }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() == action_logout)
        {
            mAuth.signOut();
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("loggedIn",Boolean.FALSE).apply();;
            Intent SignoutIntent = new Intent(MainActivity.this,Login.class);
            startActivity(SignoutIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}
