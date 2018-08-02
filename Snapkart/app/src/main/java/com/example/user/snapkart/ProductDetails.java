package com.example.user.snapkart;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class ProductDetails extends AppCompatActivity {
    private TextView mName, mDesc, mPrice,mRatingLabel, mRating;
    private DatabaseReference mProductReference,mCustomerReference;
    private String category,id;
    private ImageView mImage;
    private String Name,Desc,Category,Image,UserId,CustomerId;
    private int Price;
    private ImageButton mWishlistBtn,mReviewBtn;
    private SimpleRatingBar mRatingBar;
    private View mDivider2;
    private RecyclerView mRecyclerView;
    private ArrayList<Review> mDataSet;
    private MyReviewAdapter mAdapter;
    private EditText mReviewField;
    private Button mBuyNow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mWishlistBtn = findViewById(R.id.wishlistBtn);
        mRatingLabel = findViewById(R.id.ratingLabel);
        //setSupportActionBar(toolbar);
        /*if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }*/
        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        //category = "Electronics";
        id = intent.getStringExtra("id");
        //id = "-LIqndVHTibmVBy5HayB";
        CustomerId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("customerId","xyz");
        //CustomerId = "-LIFAuxUwWySyYFfptCl";
        mName = findViewById(R.id.productName);
        mDesc = findViewById(R.id.productDesc);
        mPrice = findViewById(R.id.productPrice);
        mImage = findViewById(R.id.productImage);
        mRatingBar = findViewById(R.id.ratingBar);
        mRating = findViewById(R.id.rating);
        mDivider2 = findViewById(R.id.divider2);
        mReviewField = findViewById(R.id.reviewField);
        mReviewBtn = findViewById(R.id.reviewBtn);
        mRecyclerView = findViewById(R.id.reviewRecyclerView);
        mBuyNow = findViewById(R.id.buyNow);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mDataSet = new ArrayList<Review>();
        mAdapter = new MyReviewAdapter(mDataSet,this, ProductDetails.this);
        mProductReference = FirebaseDatabase.getInstance().getReference().child("Products");
        mCustomerReference = FirebaseDatabase.getInstance().getReference().child("Customer");
        mRatingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float NewRating = mRatingBar.getRating();
                postRating(NewRating);
                return false;
            }
        });
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
                updateRating();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refreshRecyclerView();
        mReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCustomerReference.child(CustomerId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String Username = String.valueOf(dataSnapshot.child("name").getValue());
                        Review review = new Review(Username,mReviewField.getText().toString());
                        mProductReference.child(category).child(id).child("review").push().setValue(review);
                        mReviewField.setText("");
                        refreshRecyclerView();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        mWishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mWishlistBtn.isPressed()){
                    mWishlistBtn.setImageResource(R.drawable.heart_r32);
                }
                mCustomerReference.child(CustomerId).child("wishlist").push().setValue(id);
                mWishlistBtn.setEnabled(false);
                Toast.makeText(ProductDetails.this, "Added to Wishlist", Toast.LENGTH_SHORT).show();
        }
        });
        mBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetails.this,PaymentGateway.class);
                intent.putExtra("amt",Price);
                startActivity(intent);
            }
        });
        final FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Product product = new Product(Name,Desc,Image,id,UserId,Category,Price,1);
                mCustomerReference.child(CustomerId).child("cart").push().setValue(id);
               fab.setClickable(false);
                Toast.makeText(ProductDetails.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshRecyclerView() {
        mProductReference.child(category).child(id).child("review").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot fire : dataSnapshot.getChildren())
            {
                Review temp = fire.getValue(Review.class);
                mDataSet.add(temp);
                mDataSet.size();
            }
            mAdapter.refresh(mDataSet);
            mRecyclerView.setAdapter(mAdapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }

    private void postRating(final float newRating) {
        mProductReference.child(category).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean ratingB = dataSnapshot.hasChild("rating");
                Boolean noOfRatingB = dataSnapshot.hasChild("noOfRating");
                if(ratingB.equals(Boolean.FALSE) && noOfRatingB.equals(Boolean.FALSE)){
                    float newrating = newRating;
                    int noOfrating = 1;
                    mProductReference.child(category).child(id).child("rating").setValue(newrating);
                    mProductReference.child(category).child(id).child("noOfRating").setValue(noOfrating);
                    mRatingBar.setVisibility(View.GONE);
                    mRatingLabel.setVisibility(View.GONE);
                    mDivider2.setVisibility(View.GONE);
                    updateRating();
                }
                else if(ratingB.equals(Boolean.TRUE)){
                    long rating = (long) dataSnapshot.child("rating").getValue();
                    long noOfRating = (long) dataSnapshot.child("noOfRating").getValue();
                    long Rating = (long) (((rating*noOfRating)+ newRating) / (noOfRating+1));
                    mProductReference.child(category).child(id).child("rating").setValue(Rating);
                    mProductReference.child(category).child(id).child("noOfRating").setValue(noOfRating+1);
                    mRatingBar.setVisibility(View.GONE);
                    mRatingLabel.setVisibility(View.GONE);
                    mDivider2.setVisibility(View.GONE);
                    updateRating();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void updateRating(){
        mProductReference.child(category).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean ratingB = dataSnapshot.hasChild("rating");
                if(ratingB.equals(Boolean.TRUE)){
                    String rating = String.format(Locale.ENGLISH,"%.01f ",Float.parseFloat(String.valueOf(dataSnapshot.child("rating").getValue())));
                    mRating.setText(rating);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
