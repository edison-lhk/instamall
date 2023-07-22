package com.example.b07_project_group5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class StoreActivity extends AppCompatActivity {
    private List<Product> productList;
    private RecyclerView recyclerView; // Declare 'recyclerView' as a class-level member

    FirebaseDatabase db;
    int store_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");


        //Start initializing store information
        Intent intent = getIntent();
        if (intent != null) {
            String storeName = intent.getStringExtra("storeName");
            String storeOwner = intent.getStringExtra("storeOwner");
            String storeLogo = intent.getStringExtra("storeLogo");
            this.store_id = intent.getIntExtra("storeId",0);

            // Now, you can use these values to populate the layout
            TextView textViewStoreName = findViewById(R.id.storeName);
            TextView textViewStoreOwner = findViewById(R.id.textView);
            ImageView imageViewStore = findViewById(R.id.storeLogo);

            textViewStoreName.setText(storeName);
            textViewStoreOwner.setText(storeOwner);
            Glide.with(this).load(storeLogo).into(imageViewStore);
        }


        // Initialize your product list here (e.g., fetch from a server or database)
        productList = new ArrayList<>();

        recyclerView = findViewById(R.id.productCarousel);
        ProductAdapter adapter = new ProductAdapter(productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        readData(productList); //data
        adapter.notifyDataSetChanged(); //update
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
                        int product_store_id = productSnapshot.child("store_id").getValue(Integer.class);

                        if( store_id == product_store_id){
                            String productName = productSnapshot.child("name").getValue(String.class);
                            double productPrice = productSnapshot.child("price").getValue(Double.class);
                            String productImage = productSnapshot.child("image").getValue(String.class);
                            int productId = productSnapshot.child("product_id").getValue(Integer.class);
                            int storeId = productSnapshot.child("store_id").getValue(Integer.class);
                            int stock = productSnapshot.child("stock").getValue(Integer.class);


                            productList.add(new Product(productName, productPrice, productId, storeId, stock, productImage)); //USE DEFAULT PRODUCT PHOTO for now
                        }

                    }

                    recyclerView.getAdapter().notifyDataSetChanged();  // Notify the adapter about the data change
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
        finish(); // This will navigate back to the previous activity
    }

}