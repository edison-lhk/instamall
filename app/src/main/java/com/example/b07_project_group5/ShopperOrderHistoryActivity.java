package com.example.b07_project_group5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShopperOrderHistoryActivity extends AppCompatActivity {
    private String userId;
    private String storeId;
    private String orderId;
    private boolean status;
    private FirebaseDatabase db;
    private List<ShopperOrderHistory> products;
    private RecyclerView ShopperOrderHistoryCarousel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_order_history);
        Intent intent =getIntent();
        this.userId = intent.getStringExtra("userId");
        this.status = intent.getBooleanExtra("status", false);
        this.orderId = intent.getStringExtra("storeId");
        this.db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");

        TextView orderIdTextView = findViewById(R.id.singlevieworderID);
        orderIdTextView.setText(orderId);
        TextView statusTextView = findViewById(R.id.singleorderviewStatus);
        if (status) {
            statusTextView.setText("Finished");
        } else {
            statusTextView.setText("Pending");
        }
        products = new ArrayList<>();
        ShopperOrderHistoryCarousel = findViewById(R.id.orderviewCarousel);



    }
}