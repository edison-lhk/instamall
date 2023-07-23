package com.example.b07_project_group5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    String storeId = "";
    String accountType="";
    String ID = "";

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


        //Start initializing store information
        Intent intent = getIntent();
        if (intent != null) {
            String storeName = intent.getStringExtra("storeName");
            String storeOwner = intent.getStringExtra("storeOwner");
            String storeLogo = intent.getStringExtra("storeLogo");
            storeId = intent.getStringExtra("storeId");
            accountType = intent.getStringExtra("accountType");
            ID = intent.getStringExtra("ID");

            // Now, you can use these values to populate the layout
            TextView textViewStoreName = findViewById(R.id.storeName);
            TextView textViewStoreOwner = findViewById(R.id.textViewOwner);
            ImageView imageViewStore = findViewById(R.id.storeLogo);

            textViewStoreName.setText(storeName);
            textViewStoreOwner.setText(storeOwner);
            Glide.with(this).load(storeLogo).into(imageViewStore);
        }

        readData(productList); //data
        adapter.notifyDataSetChanged(); //update
    }


    public void readData(List<Product> productList) {
        DatabaseReference ref = db.getReference().child("products");

        //ALL THIS JUST TO MAKE CONDITIONAL BUTTON FOR OWNER
        Button conditionalButton = findViewById(R.id.conditionalButton);
        if (accountType == "owner") {
            conditionalButton.setVisibility(View.VISIBLE);
        } else {
            conditionalButton.setVisibility(View.GONE);
        };

        ref.orderByChild("storeId").equalTo(storeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    productList.clear(); // Clear the products list to avoid duplicate entries
                    // Loop through the data snapshot and add products to the list
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        String productName = productSnapshot.child("name").getValue().toString();
                        double productPrice = Double.parseDouble(productSnapshot.child("price").getValue().toString());
                        String productImage = productSnapshot.child("image").getValue().toString();
                        String productId = productSnapshot.child("productId").getValue().toString();
                        String storeId = productSnapshot.child("storeId").getValue().toString();
                        int stock = Integer.parseInt(productSnapshot.child("stock").getValue().toString());
                        productList.add(new Product(productName, productPrice, productId, storeId, stock, productImage)); //USE DEFAULT PRODUCT PHOTO for now
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

    private static final int ADD_PRODUCT_REQUEST_CODE = 1;

    public void onADD(View view){
        Intent intent = new Intent(this, AddActivity.class);

        // Pass the store information to the
        intent.putExtra("storeId", storeId);
        intent.putExtra("userID", ID);

        startActivity(intent);
    }

    //to refresh upon entry
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the product list whenever the activity resumes
        productList.clear(); // Clear the list
        readData(productList); // Fetch the data again from the database
        recyclerView.getAdapter().notifyDataSetChanged(); // Notify the adapter about the data change
    }

}