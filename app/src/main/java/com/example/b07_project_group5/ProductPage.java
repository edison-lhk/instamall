package com.example.b07_project_group5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        // Retrieve the data sent from the ProductAdapter using getExtra()
        Intent intent = getIntent();
        if (intent != null) {
            String productName = intent.getStringExtra("productName");
            double productPrice = intent.getDoubleExtra("productPrice", 0.0);
            int productImageResId = intent.getIntExtra("productImageResId", R.drawable.product);

            // Now, you can use these values to populate the ProductPage layout
            TextView textViewProductName = findViewById(R.id.textViewProductName);
            TextView textViewProductPrice = findViewById(R.id.textViewProductPrice);
            ImageView imageViewProduct = findViewById(R.id.imageViewProduct);

            textViewProductName.setText(productName);
            textViewProductPrice.setText(String.valueOf(productPrice));
            imageViewProduct.setImageResource(productImageResId);
        }
    }

    public void onBackButtonClicked(View view) {
        // Handle back button click event here
        finish(); // This will navigate back to the previous activity (StoreActivity)
    }
}