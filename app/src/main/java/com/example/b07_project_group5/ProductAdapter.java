package com.example.b07_project_group5;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private String storeId;
    private String accountType;
    private List<Product> productList;
    private Context context; // Add a Context variable to use Glide

    // Constructor to pass the list of products
    public ProductAdapter(List<Product> productList, String accountType, String storeId) {
        this.productList = productList;
        this.accountType = accountType;
        this.storeId = storeId;
    }


    // Override onCreateViewHolder to inflate the product item layout
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_product, parent, false);
        return new ProductViewHolder(itemView);
    }

    // Override onBindViewHolder to bind product data to the views
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText("Price: $" + String.valueOf(product.getPrice()));
        holder.stockTextView.setText("Stock: " + String.valueOf(product.getStock()));


        if (accountType.equals("shopper")) {
            holder.editProductBtn.setVisibility(View.INVISIBLE);
        }

        // Load the image using Glide with the image URL
        Glide.with(holder.itemView.getContext())
                .load(product.getImage())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.productImageList);

        // Set an OnClickListener to the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to navigate to the ProductPage activity
                Intent intent = new Intent(view.getContext(), ProductDetailsActivity.class);

                // Pass the product information to the ProductPage activity
                intent.putExtra("name", product.getName());
                intent.putExtra("price", product.getPrice());
                intent.putExtra("image", product.getImage());
                intent.putExtra("description", product.getDescription());

                // Start the ProductPage activity with the intent
                view.getContext().startActivity(intent);
            }
        });

        holder.editProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddOrEditProductActivity.class);
                intent.putExtra("storeId", storeId);
                intent.putExtra("name", product.getName());
                intent.putExtra("price", product.getPrice());
                intent.putExtra("stock", product.getStock());
                intent.putExtra("image", product.getImage());
                intent.putExtra("description", product.getDescription());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Define the ViewHolder to hold the views of the product item
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        TextView stockTextView;
        ImageView productImageList;
        ImageButton editProductBtn;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageList = itemView.findViewById(R.id.productImageList);
            nameTextView = itemView.findViewById(R.id.productName);
            priceTextView = itemView.findViewById(R.id.productPrice);
            stockTextView = itemView.findViewById(R.id.productStock);
            editProductBtn = itemView.findViewById(R.id.editProductBtn);
        }
    }
}
