package com.example.b07_project_group5;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShopperOrderHistoryAdapter extends RecyclerView.Adapter<ShopperOrderHistoryAdapter.OrderHistoryHolder>  {
    private ArrayList<ShopperTransactionHistory> orders;
    private String userID;
    private FirebaseDatabase db;

    public ShopperOrderHistoryAdapter(String userID, ArrayList<ShopperTransactionHistory> orders) {
        this.userID = userID;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopper_order_card, parent, false);
        return new OrderHistoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryHolder holder, int position) {
        ShopperTransactionHistory order = orders.get(position);
        holder.orderID.setText(order.getOrderID());

        Glide.with(holder.itemView.getContext())
                .load(order.getImage())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.storeImageList);

        holder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = db.getReference();
                ref.child("transactions").orderByChild("userId").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot productSnapshot: snapshot.getChildren()) {
                                String orderID = productSnapshot.getKey();
                                Intent intent = new Intent(view.getContext(), ShopperOrderHistoryActivity.class);
                                intent.putExtra("userID", userID);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderHistoryHolder extends RecyclerView.ViewHolder {
        TextView orderID;
        Button viewBtn;

        ImageView storeImageList;

        public OrderHistoryHolder(@NonNull View itemView) {
            super(itemView);
            orderID = (TextView) itemView.findViewById(R.id.orderOrderID);
            viewBtn = (Button) itemView.findViewById(R.id.viewbtn);
            storeImageList = (ImageView) itemView.findViewById(R.id.orderStoreImageList);
        }
    }
}
