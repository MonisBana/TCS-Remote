package com.example.user.merchantapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SwipeRecyclerViewAdapter extends RecyclerSwipeAdapter<SwipeRecyclerViewAdapter.SimpleViewHolder> {

    private Context mContext;
    private ArrayList<Product> productList;
    private Activity activity;
    private DatabaseReference mProductReference,mUserReference;

    public SwipeRecyclerViewAdapter(Activity a, Context context, ArrayList<Product> objects) {
        this.mContext = context;
        this.productList = objects;
        this.activity = a;
    }


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_layout, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final Product item = productList.get(position);
        //Toast.makeText(mContext,item.getName()+ "", Toast.LENGTH_SHORT).show();
        viewHolder.Name.setText(item.getName());
        viewHolder.Price.setText( "₹"+item.getPrice());
        viewHolder.Quantity.setText(Integer.toString(item.getQuantity()));
        Picasso.with(mContext).load(item.getImage()).into(viewHolder.Image);
        viewHolder.Category.setText(item.getCategory());

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //dari kiri
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));

        //dari kanan
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wraper));



        viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });


        viewHolder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(mContext,EditProduct.class);
                i.putExtra("productId",item.getId());
                i.putExtra("category",item.getCategory());
                i.putExtra("name",item.getName());
                i.putExtra("desc",item.getDesc());
                i.putExtra("image",item.getImage());
                i.putExtra("quantity",item.getQuantity());
                i.putExtra("price",item.getPrice());
                //Toast.makeText(mContext,item.getPrice()+ "", Toast.LENGTH_SHORT).show();
                activity.startActivity(i);
            }
        });

        viewHolder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductReference = FirebaseDatabase.getInstance().getReference().child("Products");
                mUserReference = FirebaseDatabase.getInstance().getReference().child("User");
                mUserReference.child(item.getUserid()).child("Products").child(item.getId()).setValue(null);
                mProductReference.child(item.getCategory()).child(item.getId()).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                        productList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, productList.size());
                        mItemManger.closeAllItems();
                        //Toast.makeText(v.getContext(), "Deleted " + viewHolder.Name.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        mItemManger.bindView(viewHolder.itemView, position);
    }
    public void refresh(ArrayList<Product> productlist){
        this.productList= productlist;
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder{
        public SwipeLayout swipeLayout;
        public TextView Name;
        public TextView Price;
        public TextView Quantity;
        public TextView Category;
        public ImageButton Edit;
        public ImageButton Delete;
        public ImageView Image;
        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = itemView.findViewById(R.id.swipe);
            Name = itemView.findViewById(R.id.productName);
            Price = itemView.findViewById(R.id.productPrice);
            Quantity = itemView.findViewById(R.id.productQuantity);
            Image = itemView.findViewById(R.id.productImage);
            Delete = itemView.findViewById(R.id.Delete);
            Edit = itemView.findViewById(R.id.btnLocation);
            Category = itemView.findViewById(R.id.productCategory);
        }
    }
}
