package com.example.b07_project_group5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShopperOrderDetailsActivity extends AppCompatActivity {
    private String userId;
    private String transactionId;
    private boolean status;
    private FirebaseDatabase db;
    private List<ShopperOrderDetails> products;
    private RecyclerView ShopperOrderHistoryCarousel;
    private ShopperOrderDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopper_order_details);
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        transactionId = intent.getStringExtra("transactionId");
        status = intent.getBooleanExtra("status", false);
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");

        TextView orderIdTextView = findViewById(R.id.singlevieworderID);
        orderIdTextView.setText(transactionId);
        TextView statusTextView = findViewById(R.id.singleorderviewStatus);
        if (status) {
            statusTextView.setText("Finished");
            statusTextView.setTextColor(getColor(R.color.success_green));
        } else {
            statusTextView.setText("Pending");
            statusTextView.setTextColor(getColor(R.color.light_gray));
        }
        products = new ArrayList<>();
        ShopperOrderHistoryCarousel = findViewById(R.id.cartCarousel);
        adapter = new ShopperOrderDetailsAdapter(userId, products);
        ShopperOrderHistoryCarousel.setLayoutManager(new LinearLayoutManager(this));
        ShopperOrderHistoryCarousel.setAdapter(adapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.shopper_nav_menu);
        bottomNavigationView.setSelectedItemId(R.id.shopper_nav_menu_orders);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.shopper_nav_menu_orders) {
                    return true;
                }
                if (itemId == R.id.shopper_nav_menu_store) {
                    Intent intent = new Intent(ShopperOrderDetailsActivity.this, BrowseStoreActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    return true;
                }
                if (itemId == R.id.shopper_nav_menu_cart) {
                    Intent intent = new Intent(ShopperOrderDetailsActivity.this, CartActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    return true;
                }
                if (itemId == R.id.shopper_nav_menu_logout) {
                    Toast.makeText(ShopperOrderDetailsActivity.this, getString(R.string.logout_successful_text), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ShopperOrderDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    public void readData(List<ShopperOrderDetails> products) {
        DatabaseReference ref = db.getReference();
        DatabaseReference query = ref.child("transactions");
        query.child(transactionId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    GenericTypeIndicator<List<String>> orderListClass = new GenericTypeIndicator<List<String>>() {};
                    List<String> orderIdList = snapshot.child("orderList").getValue(orderListClass);
                    products.clear();
                    for (String ownerOrderId: orderIdList) {
                        DatabaseReference query2 = ref.child("orders");
                        query2.child(ownerOrderId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Order order = snapshot.getValue(Order.class);
                                    for (ProductOrder productOrder: order.getProductList()) {
                                        DatabaseReference query3 = ref.child("products");
                                        query3.child(productOrder.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Product product = snapshot.getValue(Product.class);
                                                products.add(new ShopperOrderDetails(product, productOrder));
                                                displayTotalCost(products);
                                                ShopperOrderHistoryCarousel.getAdapter().notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Log.e("ShopperOrderHistoryActivity", "Database read error: " + error.getMessage());
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("ShopperOrderHistoryActivity", "Database read error: " + error.getMessage());
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ShopperOrderHistoryActivity", "Database read error: " + error.getMessage());
            }
        });
    }


    public void displayTotalCost(List<ShopperOrderDetails> products) {
        TextView totalPriceTextView = findViewById(R.id.singleorderviewtotalText);
        double total = 0;
        for (ShopperOrderDetails product : products) {
            total += product.getProductOrder().getAmount() * product.getProduct().getPrice();
        }
        totalPriceTextView.setText("$ " + String.format("%.2f", total));
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        products.clear();
        readData(products);
    }




}