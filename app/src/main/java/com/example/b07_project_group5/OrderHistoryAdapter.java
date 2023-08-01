package com.example.b07_project_group5;

import android.media.Image;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryHolder>  {
    private List<OrderHistory> orders;
    private String userID;
    private FirebaseDatabase db;

    public OrderHistoryAdapter(String userID, List<OrderHistory> orders) {
        this.userID = userID;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_card, parent, false);
        return new OrderHistoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryHolder holder, int position) {
        OrderHistory order = orders.get(position);
        holder.orderID.setText(order.getOrderID());

        Glide.with(holder.itemView.getContext())
                .load(order.getImage())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.storeImageList);

    }

    @Override
    public int getItemCount() {
        return 0;
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
