package com.example.b07_project_group5;

import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

public class ShopperOrderHistoryAdapter extends RecyclerView.Adapter<ShopperOrderHistoryAdapter.ShopperOrderHistoryViewHolder> {
    private String userId;
    private List<ShopperOrderHistory> shopperOrderHistoryList;
    private FirebaseDatabase db;

    public ShopperOrderHistoryAdapter(String userId, List<ShopperOrderHistory> shopperOrderHistoryList) {
        this.userId = userId;
        this.shopperOrderHistoryList = shopperOrderHistoryList;
        this.db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
    }


    @NonNull
    @Override
    public ShopperOrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopper_singleorder_view_card, parent, false);
        return new ShopperOrderHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopperOrderHistoryViewHolder holder, int position) {
        ShopperOrderHistory shopperOrderHistory = shopperOrderHistoryList.get(position);

        Double price = shopperOrderHistory.getProduct().getPrice();
        Double totalPrice = shopperOrderHistory.getProductOrder().getAmount() * price;
        int amount = shopperOrderHistory.getProductOrder().getAmount();

        String storeId = shopperOrderHistory.getProduct().getStoreId();

        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
        DatabaseReference ref = db.getReference();
        DatabaseReference query = ref.child("stores");
        query.child(storeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String storeName = (String) snapshot.child("name").getValue();
                holder.storeNameTextView.setText(storeName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ShopperOrderHistoryActivity", "Database read error: " + error.getMessage());
            }
        });
        holder.productNameTextView.setText(shopperOrderHistory.getProduct().getName());
        holder.productPriceTextView.setText("$" + String.format("%.2f", price));
        holder.amountTextView.setText(String.valueOf(amount));
        holder.orderTotalPriceTextView.setText("$" + String.format("%.2f", totalPrice));

        Glide.with(holder.itemView.getContext())
                .load(shopperOrderHistory.getProduct().getImage())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.orderProductImageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProductDetailsActivity.class);
                intent.putExtra("previousActivity", "ShopperOrderHistoryActivity");
                intent.putExtra("userId", userId);
                intent.putExtra("accountType", "shopper");
                intent.putExtra("productId", shopperOrderHistory.getProductOrder().getProductId());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopperOrderHistoryList.size();
    }

    public static class ShopperOrderHistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView orderProductImageView;
        TextView orderTotalPriceTextView;
        TextView storeNameTextView;
        TextView productPriceTextView;
        TextView amountTextView;
        TextView productNameTextView;

        public ShopperOrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            orderProductImageView = itemView.findViewById(R.id.singleorderviewImageList);
            productNameTextView = itemView.findViewById(R.id.singleorderviewProductName);
            productPriceTextView = itemView.findViewById(R.id.singleorderviewProductPriceTextView);
            amountTextView = itemView.findViewById(R.id.singleorderviewQuantityTextView);
            orderTotalPriceTextView = itemView.findViewById(R.id.singleorderviewProductTotalPrice);
            storeNameTextView = itemView.findViewById(R.id.singleorderviewStoreName);
        }
    }
}
