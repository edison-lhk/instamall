package com.example.b07_project_group5;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class StoreOrderProductAdapter extends RecyclerView.Adapter<StoreOrderProductAdapter.StoreOrderProductViewHolder> {
    private String userId;
    private String accountType;
    private List<StoreOrderProduct> storeOrderProductList;
    private FirebaseDatabase db;

    public StoreOrderProductAdapter(String userId, List<StoreOrderProduct> storeOrderProductList) {
        this.userId = userId;
        this.accountType = "owner";
        this.storeOrderProductList = storeOrderProductList;
        this.db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
    }

    @NonNull
    @Override
    public StoreOrderProductAdapter.StoreOrderProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_order_product_card, parent, false);
        return new StoreOrderProductAdapter.StoreOrderProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreOrderProductAdapter.StoreOrderProductViewHolder holder, int position) {
        StoreOrderProduct storeOrderProduct = storeOrderProductList.get(position);
        holder.storeOrderProductNameTextView.setText(storeOrderProduct.getProduct().getName());
        holder.storeOrderProductTotalPriceTextView.setText("$ " + String.format("%.2f", storeOrderProduct.getProductOrder().getAmount() * storeOrderProduct.getProduct().getPrice()));
        holder.storeOrderProductAmountTextView.setText(String.valueOf(storeOrderProduct.getProductOrder().getAmount()));
        holder.storeOrderProductPriceTextView.setText("$ " + String.valueOf(storeOrderProduct.getProduct().getPrice()));
        Glide.with(holder.itemView.getContext())
                .load(storeOrderProduct.getProduct().getImage())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.storeOrderProductImageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProductDetailsActivity.class);
                intent.putExtra("previousActivity", "StoreOrderDetailsActivity");
                intent.putExtra("userId", userId);
                intent.putExtra("accountType", accountType);
                intent.putExtra("productId", storeOrderProduct.getProductOrder().getProductId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return storeOrderProductList.size(); }

    public static class StoreOrderProductViewHolder extends RecyclerView.ViewHolder {
        ImageView storeOrderProductImageView;
        TextView storeOrderProductNameTextView;
        TextView storeOrderProductTotalPriceTextView;
        TextView storeOrderProductAmountTextView;
        TextView storeOrderProductPriceTextView;

        public StoreOrderProductViewHolder(@NonNull View itemView) {
            super(itemView);
            storeOrderProductImageView = itemView.findViewById(R.id.storeOrderProductImage);
            storeOrderProductNameTextView = itemView.findViewById(R.id.storeOrderProductName);
            storeOrderProductTotalPriceTextView = itemView.findViewById(R.id.storeOrderProductTotalPrice);
            storeOrderProductAmountTextView = itemView.findViewById(R.id.storeOrderProductAmount);
            storeOrderProductPriceTextView = itemView.findViewById(R.id.storeOrderProductPrice);
        }
    }
}
