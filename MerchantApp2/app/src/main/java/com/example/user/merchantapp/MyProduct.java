package com.example.user.merchantapp;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyProduct extends AppCompatActivity {
    private TextView tvEmptyTextView;
    private RecyclerView mRecyclerView;
    private ArrayList<Product> mDataSet;
    private DatabaseReference mUserReference;
    private String UserId;
    private SwipeRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product);
        getSupportActionBar().setElevation(0);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDataSet = new ArrayList<Product>();
        UserId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("userId","abc");
        //Toast.makeText(this,UserId+ "", Toast.LENGTH_SHORT).show();
        mUserReference = FirebaseDatabase.getInstance().getReference().child("User");
        mUserReference.child(UserId).child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot fire : dataSnapshot.getChildren())
                {
                    Product temp= fire.getValue(Product.class);
                    mDataSet.add(temp);
                    //Toast.makeText(MyProduct.this,mDataSet.size()+ "", Toast.LENGTH_SHORT).show();
                }
                mAdapter.refresh(mDataSet);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*if(mDataSet.isEmpty()){
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyTextView.setVisibility(View.VISIBLE);
        }else{
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyTextView.setVisibility(View.GONE);
        }*/

        mAdapter = new SwipeRecyclerViewAdapter(MyProduct.this,this, mDataSet);

        (mAdapter).setMode(Attributes.Mode.Single);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("RecyclerView", "onScrollStateChanged");
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }
}
