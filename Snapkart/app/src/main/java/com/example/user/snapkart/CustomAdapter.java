package com.example.user.snapkart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private ArrayList<Product> productSet;
    private Activity activity;
    private Context mContext;

    public CustomAdapter(ArrayList<Product> productSet, Activity activity, Context mContext) {
        this.productSet = productSet;
        this.activity = activity;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_layout, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder viewHolder, int position) {
        final Product item = productSet.get(position);
        viewHolder.Name.setText(item.getName());
        viewHolder.Price.setText( "₹"+item.getPrice());
        Picasso.with(mContext).load(item.getImage()).into(viewHolder.Image);
        viewHolder.Category.setText(item.getCategory());
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
        public TextView Category;
        public ImageView Image;
        public TextView Rating;
        public MyViewHolder(View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.productName);
            Price = itemView.findViewById(R.id.productPrice);
            Image = itemView.findViewById(R.id.productImage);
            Rating = itemView.findViewById(R.id.rating);
            Category = itemView.findViewById(R.id.productCategory);
        }
    }
}
