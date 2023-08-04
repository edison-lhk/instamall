package com.example.b07_project_group5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShopperOrderActivity extends AppCompatActivity {

    FirebaseDatabase db;

    RecyclerView finishedOrderCarousel;

    RecyclerView pendingOrderCarousel;
    String userId;

    List<ShopperOrder> finishedOrders;

    List<ShopperOrder> pendingOrders;

    ShopperOrderAdapter finishedOrderAdapter;

    ShopperOrderAdapter pendingOrderAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopper_order);
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        finishedOrders = new ArrayList<>();
        pendingOrders = new ArrayList<>();

        finishedOrderCarousel = findViewById(R.id.FinishedOrderCarousel);
        pendingOrderCarousel = findViewById(R.id.PendingOrderCarousel);

        finishedOrderAdapter = new ShopperOrderAdapter(userId, finishedOrders);
        finishedOrderCarousel.setLayoutManager(new LinearLayoutManager(this));
        finishedOrderCarousel.setAdapter(finishedOrderAdapter);

        pendingOrderAdapter = new ShopperOrderAdapter(userId, pendingOrders);
        pendingOrderCarousel.setLayoutManager(new LinearLayoutManager(this));
        pendingOrderCarousel.setAdapter(pendingOrderAdapter);

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
                    Intent intent = new Intent(ShopperOrderActivity.this, BrowseStoreActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    return true;
                }
                if (itemId == R.id.shopper_nav_menu_cart) {
                    Intent intent = new Intent(ShopperOrderActivity.this, CartActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    return true;
                }
                if (itemId == R.id.shopper_nav_menu_logout) {
                    Toast.makeText(ShopperOrderActivity.this, getString(R.string.logout_successful_text), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ShopperOrderActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });

    }
    public void readData(List<ShopperOrder> finishedOrders, List<ShopperOrder> pendingOrders) {
        finishedOrders.clear();
        pendingOrders.clear();
        DatabaseReference ref = db.getReference();
        DatabaseReference query = ref.child("transactions");

        query.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot transactionSnapshot: snapshot.getChildren()) {
                        String orderId = transactionSnapshot.getKey();
                        Boolean status = (Boolean) transactionSnapshot.child("status").getValue();
                        if(!status) {
                            pendingOrders.add(new ShopperOrder(orderId, status));
                            pendingOrderCarousel.getAdapter().notifyDataSetChanged();
                        } else {
                            finishedOrders.add(new ShopperOrder(orderId, status));
                            finishedOrderCarousel.getAdapter().notifyDataSetChanged();

                        }

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ShopperTransactionHistoryActivity", "Database read error: " + error.getMessage());
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        pendingOrders.clear();
        finishedOrders.clear();
        readData(finishedOrders, pendingOrders);

    }


}