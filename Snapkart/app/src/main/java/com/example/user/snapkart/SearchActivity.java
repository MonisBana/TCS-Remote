package com.example.user.snapkart;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import static com.example.user.snapkart.R.menu.main_menu;
import static com.example.user.snapkart.R.id.action_logout;
import static com.example.user.snapkart.R.id.action_mycart;
import static com.example.user.snapkart.R.id.action_mywishlist;
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
    private FirebaseAuth mAuth;

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
        mAuth = FirebaseAuth.getInstance();
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
                mDataSet.clear();
                mAdapter.refresh(mDataSet);
                String searchQuery = mSearchQuery.getText().toString();
                if(!searchQuery.equals("")) {
                    if (Category != null && Price == 0) {
                        loadFilteredData(Category, searchQuery);
                    } else if (Price != 0) {
                        loadFilteredPriceData(Category, Price, searchQuery);
                    } else {
                        loadData(searchQuery);
                    }
                    mSearchBtn.setClickable(false);
                }
                else{
                    Toast.makeText(SearchActivity.this, "Enter Search Query", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mSearchQuery.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mSearchBtn.setClickable(true);
                return false;
            }
        });
        CustomerId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("customerId","abc");
        //Toast.makeText(this, CustomerId+"", Toast.LENGTH_SHORT).show();
    }

    private void loadFilteredPriceData(String category, final int price, final String searchQuery) {
        mProductReference.child(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot fire : dataSnapshot.getChildren())
                {
                    if((fire.child("name").getValue().toString().toLowerCase().indexOf(searchQuery.toLowerCase()) != -1)&&
                            (Integer.parseInt(fire.child("price").getValue().toString()) < price )) {
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

    private void loadFilteredData(String category, final String searchQuery) {
        mProductReference.child(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot fire : dataSnapshot.getChildren())
                {

                    if(fire.child("name").getValue().toString().toLowerCase().indexOf(searchQuery.toLowerCase()) != -1) {
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

    private void loadData(final String searchQuery) {
        mProductReference.child("Mobiles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot fire : dataSnapshot.getChildren())
                {
                    if(fire.child("name").getValue().toString().toLowerCase().indexOf(searchQuery.toLowerCase()) != -1) {
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
        mProductReference.child("Electronics").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot fire : dataSnapshot.getChildren())
                {
                    if(fire.child("name").getValue().toString().toLowerCase().indexOf(searchQuery.toLowerCase()) != -1) {
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
        });mProductReference.child("Sports").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot fire : dataSnapshot.getChildren())
                {
                    if(fire.child("name").getValue().toString().toLowerCase().indexOf(searchQuery.toLowerCase()) != -1) {
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
        });mProductReference.child("Clothing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot fire : dataSnapshot.getChildren())
                {
                    if(fire.child("name").getValue().toString().toLowerCase().indexOf(searchQuery.toLowerCase()) != -1) {
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
        });mProductReference.child("Baby Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot fire : dataSnapshot.getChildren())
                {
                    if(fire.child("name").getValue().toString().toLowerCase().indexOf(searchQuery.toLowerCase()) != -1) {
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == action_mycart){
            startActivity(new Intent(SearchActivity.this,MyCart.class));
        }
        if(item.getItemId() == action_mywishlist){
            startActivity(new Intent(SearchActivity.this,MyWishlist.class));
        }
        if(item.getItemId() == action_logout)
        {
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("loggedIn",Boolean.FALSE).apply();;
            mAuth.signOut();
            Intent SignoutIntent = new Intent(SearchActivity.this,Login.class);
            startActivity(SignoutIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
