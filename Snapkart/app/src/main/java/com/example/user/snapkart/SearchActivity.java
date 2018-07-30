package com.example.user.snapkart;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Product> mDataSet;
    private CustomAdapter mAdapter;
    private ImageButton mSearchBtn,mFilterBtn;
    private EditText mSearchQuery;
    private String Category,Result;
    private int Price;
    private DatabaseReference mProductReference;
    private TextView mPriceLabel,mCategoryLabel;
    private String CustomerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setElevation(0);
        Intent intent = getIntent();
        Category = intent.getStringExtra("category");
        Result = intent.getStringExtra("result");
        Price = intent.getIntExtra("price",0);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mFilterBtn = findViewById(R.id.filterBtn);
        mSearchBtn = findViewById(R.id.searchBtn);
        mSearchQuery = findViewById(R.id.searchQuery);
        mPriceLabel = findViewById(R.id.priceLabel);
        mCategoryLabel = findViewById(R.id.categoryLabel);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProductReference = FirebaseDatabase.getInstance().getReference().child("Products");
        mDataSet = new ArrayList<Product>();
        mAdapter = new CustomAdapter(mDataSet,this, SearchActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        if(Result != null){
        mSearchQuery.setText(Result);
        }
        mFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this,FilterActivity.class));
            }
        });
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.setVisibility(View.VISIBLE);
                String searchQuery = mSearchQuery.getText().toString();
                if(Category!= null && Price == 0) {
                    loadFilteredData(Category,searchQuery);
                }
                else if(Price != 0){
                    loadFilteredPriceData(Category,Price,searchQuery);
                }
                else{
                    loadData(searchQuery);
                }

            }
        });
        CustomerId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("customerId","abc");
        Toast.makeText(this, CustomerId+"", Toast.LENGTH_SHORT).show();
    }

    private void loadFilteredPriceData(String category, final int price, final String searchQuery) {
        mProductReference.child(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot fire : dataSnapshot.getChildren())
                {
                    Product temp= fire.getValue(Product.class);
                    if((temp.getName().toLowerCase().indexOf(searchQuery.toLowerCase()) != -1)&&
                            (temp.getPrice() < price )) {
                        Toast.makeText(SearchActivity.this, "Found", Toast.LENGTH_SHORT).show();
                        mDataSet.add(temp);
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

    private void loadFilteredData(String category, final String searchQuery) {
        mProductReference.child(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot fire : dataSnapshot.getChildren())
                {
                    Product temp= fire.getValue(Product.class);
                    if(temp.getName().toLowerCase().indexOf(searchQuery.toLowerCase()) != -1) {
                        Toast.makeText(SearchActivity.this, "Found", Toast.LENGTH_SHORT).show();
                        mDataSet.add(temp);
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

    private void loadData(final String searchQuery) {
        mProductReference.child("Mobiles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot fire : dataSnapshot.getChildren())
                {
                    Product temp= fire.getValue(Product.class);
                    if(temp.getName().toLowerCase().indexOf(searchQuery.toLowerCase()) != -1) {
                        Toast.makeText(SearchActivity.this, "Found", Toast.LENGTH_SHORT).show();
                        mDataSet.add(temp);
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
        mProductReference.child("Electronics").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot fire : dataSnapshot.getChildren())
                {
                    Product temp= fire.getValue(Product.class);
                    if(temp.getName().toLowerCase().indexOf(searchQuery.toLowerCase()) != -1) {
                        mDataSet.add(temp);
                    }
                    //Toast.makeText(MyProduct.this,mDataSet.size()+ "", Toast.LENGTH_SHORT).show();
                }
                mAdapter.refresh(mDataSet);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });mProductReference.child("Sports").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot fire : dataSnapshot.getChildren())
                {
                    Product temp= fire.getValue(Product.class);
                    if(temp.getName().toLowerCase().indexOf(searchQuery.toLowerCase()) != -1) {
                        mDataSet.add(temp);
                    }
                    //Toast.makeText(MyProduct.this,mDataSet.size()+ "", Toast.LENGTH_SHORT).show();
                }
                mAdapter.refresh(mDataSet);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });mProductReference.child("Clothing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot fire : dataSnapshot.getChildren())
                {
                    Product temp= fire.getValue(Product.class);
                    if(temp.getName().toLowerCase().indexOf(searchQuery.toLowerCase()) != -1) {
                        mDataSet.add(temp);
                    }
                    //Toast.makeText(MyProduct.this,mDataSet.size()+ "", Toast.LENGTH_SHORT).show();
                }
                mAdapter.refresh(mDataSet);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });mProductReference.child("Baby Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot fire : dataSnapshot.getChildren())
                {
                    Product temp= fire.getValue(Product.class);
                    if(temp.getName().toLowerCase().indexOf(searchQuery.toLowerCase()) != -1) {
                        mDataSet.add(temp);
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
