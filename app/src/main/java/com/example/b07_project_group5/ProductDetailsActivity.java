package com.example.b07_project_group5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ProductDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Retrieve the data sent from the ProductAdapter using getExtra()
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String price = "$" + intent.getDoubleExtra("price", 0.0);
            String image = intent.getStringExtra("image");
            String description = intent.getStringExtra("description");


            // use these values to populate the ProductPage layout
            TextView textViewProductName = findViewById(R.id.textViewProductName);
            TextView textViewProductPrice = findViewById(R.id.textViewProductPrice);
            ImageView imageViewProduct = findViewById(R.id.imageViewProduct);
            TextView textViewDescription = findViewById(R.id.textViewProductDescription);

            Glide.with(this).load(image).into(imageViewProduct);

            textViewDescription.setText(description);
            textViewProductName.setText(name);
            textViewProductPrice.setText(String.valueOf(price));

        }
    }

    public void onBackButtonClicked(View view) {
        // Handle back button click event here
        finish(); // This will navigate back to the previous activity (StoreActivity)
    }
}