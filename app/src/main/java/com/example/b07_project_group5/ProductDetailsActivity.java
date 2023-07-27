package com.example.b07_project_group5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductDetailsActivity extends AppCompatActivity {
    String previousActivity;
    String userId;
    String accountType;
    String productId;
    String name;
    Double price;
    String image;
    String description;
    String storeId;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
        Intent intent = getIntent();
        if (intent != null) {
            previousActivity = intent.getStringExtra("previousActivity");
            userId = intent.getStringExtra("userId");
            accountType = intent.getStringExtra("accountType");
            productId = intent.getStringExtra("productId");
            DatabaseReference ref = db.getReference();
            ref.child("products").child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        name = snapshot.child("name").getValue().toString();
                        price = Double.parseDouble(snapshot.child("price").getValue().toString());
                        image = snapshot.child("image").getValue().toString();
                        description = snapshot.child("description").getValue().toString();
                        storeId = snapshot.child("storeId").getValue().toString();
                        TextView textViewProductName = findViewById(R.id.textViewProductName);
                        TextView textViewProductPrice = findViewById(R.id.textViewProductPrice);
                        ImageView imageViewProduct = findViewById(R.id.imageViewProduct);
                        TextView textViewDescription = findViewById(R.id.textViewProductDescription);
                        Glide.with(ProductDetailsActivity.this).load(image).into(imageViewProduct);
                        textViewDescription.setText(description);
                        textViewProductName.setText(name);
                        textViewProductPrice.setText(String.valueOf(price));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
        BottomNavigationView ownerBottomNavigationView = findViewById(R.id.owner_nav_menu);
        if (!accountType.equals("owner")) { ownerBottomNavigationView.setVisibility(View.INVISIBLE); }
        if (previousActivity.equals("StoreActivity")) {
            ownerBottomNavigationView.setSelectedItemId(R.id.owner_nav_menu_store);
        } else {
            ownerBottomNavigationView.setSelectedItemId(R.id.owner_nav_menu_store);
        }
        ownerBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.owner_nav_menu_store) {
                    return true;
                } else if (itemId == R.id.owner_nav_menu_orders) {
                    Intent intent = new Intent(ProductDetailsActivity.this, LoginActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("accountType", accountType);
                    intent.putExtra("storeId", storeId);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.owner_nav_menu_logout) {
                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.logout_successful_text), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ProductDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });

        BottomNavigationView shopperBottomNavigationView = findViewById(R.id.shopper_nav_menu);
        if (!accountType.equals("shopper")) { shopperBottomNavigationView.setVisibility(View.INVISIBLE); }
        if (previousActivity.equals("StoreActivity")) {
            shopperBottomNavigationView.setSelectedItemId(R.id.owner_nav_menu_store);
        } else {
            shopperBottomNavigationView.setSelectedItemId(R.id.owner_nav_menu_store);
        }
        shopperBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.shopper_nav_menu_store) {
                    return true;
                } else if (itemId == R.id.shopper_nav_menu_cart) {
                    Intent intent = new Intent(ProductDetailsActivity.this, LoginActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("accountType", accountType);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.shopper_nav_menu_orders) {
                    Intent intent = new Intent(ProductDetailsActivity.this, LoginActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("accountType", accountType);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.shopper_nav_menu_logout) {
                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.logout_successful_text), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ProductDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    public void onBackButtonClicked(View view) {
        // Handle back button click event here
        finish(); // This will navigate back to the previous activity (StoreActivity)
    }
}