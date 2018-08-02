package com.example.user.snapkart;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MyCart extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<Product>  mDataSet = new ArrayList<Product>();
    private DatabaseReference mProductReference,mCustomerReference;
    private String CustomerId;
    private MyCartAdapter mAdapter;
    private FloatingActionButton mWishlistFab;
    private Button mBuynow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        getSupportActionBar().setElevation(0);
        CustomerId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("customerId","xyz");
        //CustomerId = "-LIFAuxUwWySyYFfptCl";
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mWishlistFab = findViewById(R.id.wishlist);
        mBuynow = findViewById(R.id.buynow);
        mWishlistFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyCart.this,MyWishlist.class));
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter = new MyCartAdapter(mDataSet,this, MyCart.this);
        mProductReference = FirebaseDatabase.getInstance().getReference().child("Products");
        mCustomerReference = FirebaseDatabase.getInstance().getReference().child("Customer");
        mCustomerReference.child(CustomerId).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                        final String key= (String) childSnapshot.getValue();
                    //Toast.makeText(MyCart.this, key+"", Toast.LENGTH_SHORT).show();
                    mProductReference.child("Mobiles").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot fire : dataSnapshot.getChildren())
                            {
                                if(fire.child("id").getValue().equals(key)) {
                                    String Name = fire.child("name").getValue().toString();
                                    String Desc = fire.child("desc").getValue().toString();
                                    int Price = Integer.parseInt(String.valueOf(fire.child("price").getValue()));
                                    int Quantity = Integer.parseInt(String.valueOf(fire.child("quantity").getValue()));
                                    String Category = fire.child("category").getValue().toString();
                                    String Image = fire.child("image").getValue().toString();
                                    String UserId = fire.child("userid").getValue().toString();
                                    long rating = (long) fire.child("rating").getValue();
                                    long noOfRating = (long) fire.child("noOfRating").getValue();
                                    String id = fire.child("id").getValue().toString();
                                    Product product =
                                            new Product(Name,Desc,Image,
                                                    id,UserId,Category,Price,
                                                    Quantity,noOfRating,
                                                    rating);
                                    mDataSet.add(product);
                                }

                            }
                            //Toast.makeText(MyCart.this,mDataSet.size()+ "", Toast.LENGTH_SHORT).show();
                            mAdapter.refresh(mDataSet);
                            mRecyclerView.setAdapter(mAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mProductReference.child("Electronics").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot fire : dataSnapshot.getChildren())
                            {
                                if(fire.child("id").getValue().equals(key)) {
                                    String Name = fire.child("name").getValue().toString();
                                    String Desc = fire.child("desc").getValue().toString();
                                    int Price = Integer.parseInt(String.valueOf(fire.child("price").getValue()));
                                    int Quantity = Integer.parseInt(String.valueOf(fire.child("quantity").getValue()));
                                    String Category = fire.child("category").getValue().toString();
                                    String Image = fire.child("image").getValue().toString();
                                    String UserId = fire.child("userid").getValue().toString();
                                    long rating = (long) fire.child("rating").getValue();
                                    long noOfRating = (long) fire.child("noOfRating").getValue();
                                    String id = fire.child("id").getValue().toString();
                                    Product product =
                                            new Product(Name,Desc,Image,
                                                    id,UserId,Category,Price,
                                                   Quantity,noOfRating,
                                                    rating);
                                    mDataSet.add(product);
                                }
                                //Toast.makeText(MyProduct.this,mDataSet.size()+ "", Toast.LENGTH_SHORT).show();
                            }
                            mAdapter.refresh(mDataSet);
                            mRecyclerView.setAdapter(mAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mProductReference.child("Sports").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot fire : dataSnapshot.getChildren())
                            {
                                if(fire.child("id").getValue().equals(key)) {
                                    String Name = fire.child("name").getValue().toString();
                                    String Desc = fire.child("desc").getValue().toString();
                                    int Price = Integer.parseInt(String.valueOf(fire.child("price").getValue()));
                                    int Quantity = Integer.parseInt(String.valueOf(fire.child("quantity").getValue()));
                                    String Category = fire.child("category").getValue().toString();
                                    String Image = fire.child("image").getValue().toString();
                                    String UserId = fire.child("userid").getValue().toString();
                                    long rating = (long) fire.child("rating").getValue();
                                    long noOfRating = (long) fire.child("noOfRating").getValue();
                                    String id = fire.child("id").getValue().toString();
                                    Product product =
                                            new Product(Name,Desc,Image,
                                                    id,UserId,Category,Price,
                                                    Quantity,noOfRating,
                                                    rating);
                                    mDataSet.add(product);
                                }
                                //Toast.makeText(MyProduct.this,mDataSet.size()+ "", Toast.LENGTH_SHORT).show();
                            }
                            mAdapter.refresh(mDataSet);
                            mRecyclerView.setAdapter(mAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mProductReference.child("Clothing").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot fire : dataSnapshot.getChildren())
                            {
                                if(fire.child("id").getValue().equals(key)) {
                                    String Name = fire.child("name").getValue().toString();
                                    String Desc = fire.child("desc").getValue().toString();
                                    int Price = Integer.parseInt(String.valueOf(fire.child("price").getValue()));
                                    int Quantity = Integer.parseInt(String.valueOf(fire.child("quantity").getValue()));
                                    String Category = fire.child("category").getValue().toString();
                                    String Image = fire.child("image").getValue().toString();
                                    String UserId = fire.child("userid").getValue().toString();
                                    long rating = (long) fire.child("rating").getValue();
                                    long noOfRating = (long) fire.child("noOfRating").getValue();
                                    String id = fire.child("id").getValue().toString();
                                    Product product =
                                            new Product(Name,Desc,Image,
                                                    id,UserId,Category,Price,
                                                    Quantity,noOfRating,
                                                    rating);
                                    mDataSet.add(product);
                                }
                                //Toast.makeText(MyProduct.this,mDataSet.size()+ "", Toast.LENGTH_SHORT).show();
                            }
                            mAdapter.refresh(mDataSet);
                            mRecyclerView.setAdapter(mAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mProductReference.child("Baby Products").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot fire : dataSnapshot.getChildren())
                            {
                                if(fire.child("id").getValue().equals(key)) {
                                    String Name = fire.child("name").getValue().toString();
                                    String Desc = fire.child("desc").getValue().toString();
                                    int Price = Integer.parseInt(String.valueOf(fire.child("price").getValue()));
                                    int Quantity = Integer.parseInt(String.valueOf(fire.child("quantity").getValue()));
                                    String Category = fire.child("category").getValue().toString();
                                    String Image = fire.child("image").getValue().toString();
                                    String UserId = fire.child("userid").getValue().toString();
                                    long rating = (long) fire.child("rating").getValue();
                                    long noOfRating = (long) fire.child("noOfRating").getValue();
                                    String id = fire.child("id").getValue().toString();
                                    Product product =
                                            new Product(Name,Desc,Image,
                                                    id,UserId,Category,Price,
                                                    Quantity,noOfRating,
                                                    rating);
                                    mDataSet.add(product);
                                }
                                //Toast.makeText(MyProduct.this,mDataSet.size()+ "", Toast.LENGTH_SHORT).show();
                            }
                            mAdapter.refresh(mDataSet);
                            mRecyclerView.setAdapter(mAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mBuynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalamt = 0;
                for(int i = 0; i < mDataSet.size(); i++){
                    totalamt += mDataSet.get(i).getPrice();
                }
                Intent i = new Intent(MyCart.this,PaymentGateway.class);
                i.putExtra("amt",totalamt);
                startActivity(i);
            }
        });
    }
}
