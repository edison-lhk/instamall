package com.example.b07_project_group5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddActivity extends AppCompatActivity {
    int productId;
    int price;
    int stock;
    String userInputImage;
    String userInputName;
    int store_id;
    FirebaseDatabase db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");

        Intent intent = getIntent();
        if (intent != null) {
            this.store_id = intent.getIntExtra("storeId", 0);
        }

        EditText editProductId = findViewById(R.id.editProductId);
        EditText editName = findViewById(R.id.editName);
        EditText editPrice = findViewById(R.id.editPrice);
        EditText editStock = findViewById(R.id.editStock);
        EditText editImage = findViewById(R.id.editImage);
        Button buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text entered by the user
                String userInputProductId = editProductId.getText().toString();
                userInputName = editName.getText().toString();
                String userInputPrice = editPrice.getText().toString();
                String userInputStock = editStock.getText().toString();
                userInputImage = editImage.getText().toString();

                // Convert price and stock to integers
                productId = Integer.parseInt(userInputProductId);
                price = Integer.parseInt(userInputPrice);
                stock = Integer.parseInt(userInputStock);

                // Write the data to the database
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("products").push();
                ref.child("name").setValue(userInputName);
                ref.child("price").setValue(price);
                ref.child("stock").setValue(stock);
                ref.child("image").setValue(userInputImage);
                ref.child("product_id").setValue(productId);
                ref.child("store_id").setValue(store_id);

                finish();
            }
        });
    }

    public void onBackButtonClicked(View view) {
        finish(); // This will navigate back to the previous activity
    }
}