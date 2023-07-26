package com.example.b07_project_group5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductDetailsActivity extends AppCompatActivity {
    String productId;
    String name;
    Double price;
    String image;
    String description;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
        Intent intent = getIntent();
        if (intent != null) {
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
    }

    public void onBackButtonClicked(View view) {
        // Handle back button click event here
        finish(); // This will navigate back to the previous activity (StoreActivity)
    }
}