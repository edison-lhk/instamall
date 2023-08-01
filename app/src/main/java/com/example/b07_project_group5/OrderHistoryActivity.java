package com.example.b07_project_group5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

public class OrderHistoryActivity extends AppCompatActivity {

    FirebaseDatabase db;

    RecyclerView recylerView;

    String orderID;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);
    }
}