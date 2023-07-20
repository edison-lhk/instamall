package com.example.b07_project_group5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;




public class StoreActivity extends AppCompatActivity {
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);


        // Initialize your product list here (e.g., fetch from a server or database)
        productList = createSampleProductList();

        RecyclerView recyclerView = findViewById(R.id.productCarousel);
        ProductAdapter adapter = new ProductAdapter(productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void Homepage(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



    // Method to create a sample list of products (for testing purposes)
    private List<Product> createSampleProductList() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Product 1", 9.99, R.drawable.product));
        products.add(new Product("Product 2", 19.99, R.drawable.product));
        // Add more products here...
        return products;
    }
}