package com.example.user.snapkart;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class MyReviewAdapter extends RecyclerView.Adapter<MyReviewAdapter.MyViewHolder>{
    private ArrayList<Review> reviewSet;
    private Activity activity;
    private Context mContext;
    private DatabaseReference mCustomerReference;
    private String CustomerId;
    public MyReviewAdapter(ArrayList<Review> reviewSet, Activity activity, Context mContext) {
        this.reviewSet = reviewSet;
        this.activity = activity;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyReviewAdapter.MyViewHolder viewHolder, int position) {
        final Review item = reviewSet.get(position);
        viewHolder.Username.setText(item.getUsername());
        viewHolder.Review.setText(item.getReview());
    }

    @Override
    public int getItemCount() {
        return reviewSet.size();
    }

    public void refresh(ArrayList<Review> mDataSet) {
        this.reviewSet = mDataSet;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView Username;
        public TextView Review;
        public MyViewHolder(View itemView) {
            super(itemView);
            Username = itemView.findViewById(R.id.username);
            Review = itemView.findViewById(R.id.review);
        }
    }
}
