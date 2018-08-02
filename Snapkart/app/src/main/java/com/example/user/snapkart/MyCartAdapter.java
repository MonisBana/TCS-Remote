package com.example.user.snapkart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyCartAdapter  extends RecyclerView.Adapter<MyCartAdapter.MyViewHolder>{
    private ArrayList<Product> productSet;
    private Activity activity;
    private Context mContext;
    private DatabaseReference mCustomerReference;
    private String CustomerId;
    public MyCartAdapter(ArrayList<Product> productSet, Activity activity, Context mContext) {
        this.productSet = productSet;
        this.activity = activity;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyCartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_row, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartAdapter.MyViewHolder viewHolder, int position) {
        final Product item = productSet.get(position);
        viewHolder.Name.setText(item.getName());
        viewHolder.Price.setText( "₹"+item.getPrice());
        Picasso.with(mContext).load(item.getImage()).into(viewHolder.Image);
        viewHolder.Rating.setText(item.getRating()+"");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext,ProductDetails.class);
                i.putExtra("id",item.getId());
                i.putExtra("category",item.getCategory());
                activity.startActivity(i);
            }
        });
        viewHolder.AddToWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Clicked", Toast.LENGTH_SHORT).show();
                CustomerId = PreferenceManager.getDefaultSharedPreferences(mContext).getString("customerId","xyz");
                //CustomerId = "-LIFAuxUwWySyYFfptCl";
                mCustomerReference = FirebaseDatabase.getInstance().getReference().child("Customer");
                mCustomerReference.child(CustomerId).child("wishlist").push().setValue(item.getId());
                Toast.makeText(activity, "Added to Wishlist", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerId = PreferenceManager.getDefaultSharedPreferences(mContext).getString("customerId","xyz");
                //CustomerId = "-LIFAuxUwWySyYFfptCl";
                mCustomerReference = FirebaseDatabase.getInstance().getReference().child("Customer");
                mCustomerReference.child(CustomerId).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                            //Toast.makeText(activity,childSnapshot.getKey()+ "", Toast.LENGTH_SHORT).show();
                            if(childSnapshot.getValue().equals(item.getId())) {
                                final String key = (String) childSnapshot.getKey();
                                //Toast.makeText(activity, key+"", Toast.LENGTH_SHORT).show();
                                mCustomerReference.child(CustomerId).child("cart").child(key).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return productSet.size();
    }

    public void refresh(ArrayList<Product> mDataSet) {
        this.productSet = mDataSet;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView Name;
        public TextView Price;
        public ImageView Image;
        public TextView Rating;
        public TextView AddToWishlist,Remove;
        public MyViewHolder(View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.productName);
            Price = itemView.findViewById(R.id.productPrice);
            Image = itemView.findViewById(R.id.productImage);
            Rating = itemView.findViewById(R.id.rating);
            AddToWishlist = itemView.findViewById(R.id.addToWishlist);
            Remove = itemView.findViewById(R.id.remove);
        }
    }
}
