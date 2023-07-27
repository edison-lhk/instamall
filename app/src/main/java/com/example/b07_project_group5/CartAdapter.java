package com.example.b07_project_group5;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    public ArrayList<CartProduct> shoppingCart;

    public CartAdapter() {}

    // Override onCreateViewHolder to inflate the product item layout
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_card, parent, false);
        return new CartViewHolder(itemView);
    }

    // Override onBindViewHolder to bind product data to the views
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartProduct cartProduct = shoppingCart.get(position);
//        holder.nameTextView.setText(store.getStoreName());
//        holder.priceTextView.setText("Price: $" + String.valueOf(store.getPrice()));
//        holder.stockTextView.setText("Stock: " + String.valueOf(store.getStock()));


        // Load the image using Glide with the image URL
        Glide.with(holder.itemView.getContext())
                .load(cartProduct.product.getImage())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.productImageList);

        // Set an OnClickListener to the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to navigate to the ProductPage activity
                Intent intent = new Intent(view.getContext(), ProductDetailsActivity.class);

                // Pass the product information to the ProductPage activity
                intent.putExtra("userId", intent.getStringExtra("userId"));
//                intent.putExtra("storeId", storeId);
//                intent.putExtra("productName", product.getName());
//                intent.putExtra("productPrice", product.getPrice());
//                intent.putExtra("productImage", product.getImage());
//                intent.putExtra("productDescription", product.getDescription());

                // Start the ProductPage activity with the intent
                view.getContext().startActivity(intent);
            }
        });

        holder.editProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddOrEditProductActivity.class);
//                intent.putExtra("storeId", storeId);
//                intent.putExtra("productName", product.getName());
//                intent.putExtra("productPrice", product.getPrice());
//                intent.putExtra("productStock", product.getStock());
//                intent.putExtra("productImage", product.getImage());
//                intent.putExtra("productDescription", product.getDescription());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingCart.size();
    }

    public void addProduct(Product product, int amount) {
        shoppingCart.add(new CartProduct(product, amount));
    }

    public ArrayList<CartProduct> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ArrayList<CartProduct> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        TextView stockTextView;
        ImageView productImageList;
        ImageButton editProductBtn;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
//            cartProductImage = itemView.findViewById(R.id.productImageList);
//            cartStoreNameTextView;
//            cartProductNameTextView = itemView.findViewById(R.id.productName);
//            cartProductPriceTextView = itemView.findViewById(R.id.productPrice);
//            cartProductTotalPriceTextView;
//            cartProductAmountTextView;
//            cartTotalPriceTextView;
//            editProductBtn = itemView.findViewById(R.id.editProductBtn);
        }
    }
}
