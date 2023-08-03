package com.example.b07_project_group5;

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
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShopperTransactionHistoryAdapter extends RecyclerView.Adapter<ShopperTransactionHistoryAdapter.OrderHistoryHolder>  {
    private List<ShopperTransactionHistory> orders;
    private String userId;
    private FirebaseDatabase db;

    public ShopperTransactionHistoryAdapter(String userId, List<ShopperTransactionHistory> orders) {
        this.userId = userId;
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
                Intent intent = new Intent(view.getContext(), ShopperTransactionHistoryActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("status", order.getStatus());
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
