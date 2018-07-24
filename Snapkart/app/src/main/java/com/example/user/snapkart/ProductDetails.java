package com.example.user.snapkart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetails extends AppCompatActivity {
    private TextView Name,Desc,Category,Price;
    private DatabaseReference mProductReference;
    private String category,id;
    private ImageView mImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        id = intent.getStringExtra("id");
        Name = findViewById(R.id.productName);
        Desc = findViewById(R.id.productDesc);
        Price = findViewById(R.id.productPrice);
        Category = findViewById(R.id.productCategory);
        mImage = findViewById(R.id.productImage);
        mProductReference = FirebaseDatabase.getInstance().getReference().child("Products");
        mProductReference.child(category).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Name.setText(dataSnapshot.child("name").getValue().toString());
                Desc.setText(dataSnapshot.child("desc").getValue().toString());
                Price.setText(dataSnapshot.child("price").getValue().toString());
                Category.setText(dataSnapshot.child("category").getValue().toString());
                String image = dataSnapshot.child("image").getValue().toString();
                Picasso.with(getApplicationContext()).load(image).into(mImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
