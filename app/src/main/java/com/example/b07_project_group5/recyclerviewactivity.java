package com.example.b07_project_group5;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class recyclerviewactivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase;
    private ArrayList<order> orderList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ownerorder);

        // Create a instance of the database and get
        // its reference
        mbase = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerview);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));
        orderList = new ArrayList<>();

        adapter = new RecyclerAdapter(orderList, this);
        recyclerView.setAdapter(adapter);
        // Initialize the adapter with the empty ArrayList
        mbase.child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    String orderKey = orderSnapshot.getKey();

                    // Get the "productList" node for each order
                    DataSnapshot productListSnapshot = orderSnapshot.child("productList");

                    for (DataSnapshot productSnapshot : productListSnapshot.getChildren()) {
                        // Get the "amount" and "productId" for each product
                        Long amount = productSnapshot.child("amount").getValue(Long.class);
                        String productId = productSnapshot.child("productId").getValue(String.class);

                        String amountString = String.valueOf(amount);

                        order order = new order(amountString, productId);
                        orderList.add(order);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }}
