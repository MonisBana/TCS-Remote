package com.example.user.snapkart;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
    private TextView mName, mDesc, mPrice;
    private DatabaseReference mProductReference,mCustomerReference;
    private String category,id;
    private ImageView mImage;
    private String Name,Desc,Category,Image,UserId,Id,CustomerId;
    private int Price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        category = "Mobiles";
        //id = intent.getStringExtra("id");
        id = "-LHiPlUSPTuw1Df-8DZy";
        CustomerId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("customerId","xyz");
        CustomerId = "-LIFAuxUwWySyYFfptCl";
        mName = findViewById(R.id.productName);
        mDesc = findViewById(R.id.productDesc);
        mPrice = findViewById(R.id.productPrice);
        mImage = findViewById(R.id.productImage);
        mProductReference = FirebaseDatabase.getInstance().getReference().child("Products");
        mCustomerReference = FirebaseDatabase.getInstance().getReference().child("Customer");
        mProductReference.child(category).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Name = dataSnapshot.child("name").getValue().toString();
                Desc = dataSnapshot.child("desc").getValue().toString();
                Price = Integer.parseInt(String.valueOf(dataSnapshot.child("price").getValue()));
                Category = dataSnapshot.child("category").getValue().toString();
                Image = dataSnapshot.child("image").getValue().toString();
                UserId = dataSnapshot.child("userid").getValue().toString();
                mName.setText(Name);
                mDesc.setText(Desc);
                mPrice.setText("₹"+Price);
                Picasso.with(getApplicationContext()).load(Image).into(mImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product product = new Product(Name,Desc,Image,id,UserId,Category,Price,1);
                mCustomerReference.child(CustomerId).child("cart").child(id).setValue(product);
            }
        });
    }
}
