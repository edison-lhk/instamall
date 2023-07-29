package com.example.b07_project_group5;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDataManager {

    // Define the reference to the "orders" node in Firebase Realtime Database
    private DatabaseReference ordersRef;

    public FirebaseDataManager() {
        // Initialize Firebase and get a reference to the "orders" node
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ordersRef = database.getReference("orders");
    }

    // Method to fetch the data from Firebase
    public void fetchOrdersData(final FirebaseDataCallback<List<order>> callback) {
        // Add a ValueEventListener to get the data
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<order> ordersList = new ArrayList<>();

                // Loop through each child node under "orders"
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    // Get the "productList" node
                    DataSnapshot productListSnapshot = orderSnapshot.child("productList");

                    // Loop through each child node under "productList"
                    for (DataSnapshot productSnapshot : productListSnapshot.getChildren()) {
                        // Get the "amount" and "productId" values
                        String amount = productSnapshot.child("amount").getValue(String.class);
                        String productId = productSnapshot.child("productId").getValue(String.class);

                        // Create an order object with the fetched data
                        order order = new order();
                        order.setAmount(amount);
                        order.setProduct(productId);

                        // Add the order to the list
                        ordersList.add(order);
                    }
                }

                // Invoke the callback with the list of orders
                callback.onDataReceived(ordersList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur while retrieving data
                callback.onCancelled(databaseError.toException());
            }
        });
    }

    // Callback interface to handle the asynchronous data retrieval
    public interface FirebaseDataCallback<T> {
        void onDataReceived(T data);
        void onCancelled(Exception exception);
    }
}
