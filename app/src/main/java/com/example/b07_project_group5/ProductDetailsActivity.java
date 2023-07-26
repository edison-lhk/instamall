package com.example.b07_project_group5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;

public class ProductDetailsActivity extends AppCompatActivity {
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");

        // Retrieve the data sent from the ProductAdapter using getExtra()
        Intent intent = getIntent();
        if (intent != null) {
            String productName = intent.getStringExtra("productName");
            String productPrice = "$" + intent.getDoubleExtra("productPrice", 0.0);
            String productImage = intent.getStringExtra("productImage");

            // use these values to populate the ProductPage layout
            TextView textViewProductName = findViewById(R.id.textViewProductName);
            TextView textViewProductPrice = findViewById(R.id.textViewProductPrice);
            ImageView imageViewProduct = findViewById(R.id.imageViewProduct);

            Glide.with(this).load(productImage).into(imageViewProduct);

            textViewProductName.setText(productName);
            textViewProductPrice.setText(String.valueOf(productPrice));
        }
    }

    public void onBackButtonClicked(View view) {
        // Handle back button click event here
        finish(); // This will navigate back to the previous activity (StoreActivity)
    }

    public void addToCart(View view) {
        DatabaseReference ref;
//        Intent intent = getIntent();

        ref = db.getReference();

        // Replace these test data with ids from the intent
        String productId = "-Na2nCskPGnO60TtN7jx";
        String storeId = "-Na0hkzpABU-yW9U0HBa";
        String userId = "-N_yltGRIhkRij7M1qcA";

        DatabaseReference transactionQuery = ref.child("transactions");
        DatabaseReference orderQuery = ref.child("orders");

        // Make a query for all transactions made by user (including their shopping cart, if it exists)
        transactionQuery.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String orderId;
                String transactionId;
                Order order;
                Transaction cart;
                // Check if no transactions for user exist
                if (!snapshot.exists()) {
                    order = new Order(storeId);
                    order.addProduct(productId, 1);
                    orderId = orderQuery.push().getKey();
                    orderQuery.child(orderId).setValue(order);
                    cart = new Transaction(userId);
                    cart.addOrder(orderId);
                    transactionId = transactionQuery.push().getKey();
                    transactionQuery.child(transactionId).setValue(cart);
                    return;
                }

                // Get user's shopping cart
                DataSnapshot transactionSnapshot = getShoppingCart(snapshot);
                // Check if user's has an available shopping cart
                if (transactionSnapshot != null) {
                    addAmountToOrder(view, transactionSnapshot, orderQuery);
                } else {
                    order = new Order(storeId);
                    cart = new Transaction(userId);

                    // Replace amount by the amount specified by user in some input field on the page
                    order.addProduct(productId, 1);

                    orderId = orderQuery.push().getKey();
                    orderQuery.child(orderId).setValue(order);

                    cart.addOrder(orderId);

                    transactionId = transactionQuery.push().getKey();
                    transactionQuery.child(transactionId).setValue(cart);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addAmountToOrder(View view, DataSnapshot transactionSnapshot, DatabaseReference orderQuery) {
//        Intent intent = getIntent();
        // Replace these test data with ids from the intent
        String productId = "-Na2nCskPGnO60TtN7jx";
        String storeId = "-Na0hkzpABU-yW9U0HBa";

        // Add a temporary order with the storeId (will be removed if another order with same
        // storeId exists in cart)
        Order tempOrder = new Order(storeId);
        tempOrder.addProduct(productId, 1);
        String tempOrderId = orderQuery.push().getKey();
        orderQuery.child(tempOrderId).setValue(tempOrder);
        String transactionId = transactionSnapshot.getKey().toString();
        Transaction cart = transactionSnapshot.getValue(Transaction.class);
        cart.addOrder(tempOrderId);
        transactionSnapshot.getRef().setValue(cart);

        Order order = new Order(storeId);

        // Go through every orderId in orderList of the cart
        for (DataSnapshot transOrderSnapshot : transactionSnapshot.child("orderList").getChildren()) {
            String orderId = transOrderSnapshot.getValue().toString();
            Intent intent = getIntent();

            orderQuery.child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Check if the product exists within the order
                    if (snapshot.exists()) {
                        Order order = snapshot.getValue(Order.class);
                        // Check if storeId of the order is equal to storeId of the product
                        if (!storeId.equals(order.getStoreId())) {
                            return;
                        }
                        if (!tempOrderId.equals(orderId)) {
                            orderQuery.child(tempOrderId).removeValue();
                            cart.removeOrder(tempOrderId);
                            transactionSnapshot.getRef().setValue(cart);
                        }
                        ProductOrder product = order.getProduct(productId);
                        // Change the 1s for user input for product amount in next sprint
                        if (product != null) {
                            product.setAmount(product.getAmount() + 1);
                        } else {
                            order.addProduct(productId, 1);
                        }
                        snapshot.getRef().setValue(order);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private DataSnapshot getShoppingCart(DataSnapshot snapshot) {
        // Return the snapshot of the user's shopping cart if it exists, otherwise return null.
        for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
            if (!Boolean.parseBoolean(transactionSnapshot.child("finalized").getValue().toString())) {
                return transactionSnapshot;
            }
        }
        return null;
    }
}