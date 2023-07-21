package com.example.b07_project_group5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//database stuff
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class StoreActivity extends AppCompatActivity {
    private List<Product> productList;
    private RecyclerView recyclerView; // Declare 'recyclerView' as a class-level member


    FirebaseDatabase db;
    Boolean isFound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");

        // Initialize your product list here (e.g., fetch from a server or database)
        productList = new ArrayList<>();

        recyclerView = findViewById(R.id.productCarousel);
        ProductAdapter adapter = new ProductAdapter(productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        //System.out.println("TEST");

        readData(productList); //data
        adapter.notifyDataSetChanged(); //update

    }

    public void Homepage(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void readData(List<Product> productList) {
        DatabaseReference ref = db.getReference().child("products");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    productList.clear(); // Clear the products list to avoid duplicate entries

                    // Loop through the data snapshot and add products to the list
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        String productName = productSnapshot.child("name").getValue(String.class);
                        double productPrice = productSnapshot.child("price").getValue(Double.class);
                        //int productImageResId = R.drawable.product; // You may get image URL from Firebase as well.

                        productList.add(new Product(productName, productPrice, R.drawable.product));
                    }

                    // Notify the adapter about the data change
                    recyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    Log.e("StoreActivity", "Database read error: No data found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the onCancelled event if needed
                Log.e("StoreActivity", "Database read error: " + databaseError.getMessage());
            }
        });
    }

    public void onBackButtonClicked(View view) {
        // Handle back button click event here
        finish(); // This will navigate back to the previous activity (StoreActivity)
    }

/*
    // Method to create a sample list of products (for testing purposes)
    private List<Product> createSampleProductList() {
        List<Product> products = new ArrayList<>();

        products.add(new Product("Product 1", 9.99, R.drawable.product));
        products.add(new Product("Product 2", 19.99, R.drawable.product));

        // Add more products here...
        return products;
    }

*/


}