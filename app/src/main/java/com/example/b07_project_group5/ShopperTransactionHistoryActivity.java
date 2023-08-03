package com.example.b07_project_group5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

public class ShopperTransactionHistoryActivity extends AppCompatActivity {

    FirebaseDatabase db;

    RecyclerView recyclerView;

    String orderID;

    ArrayList<ShopperTransactionHistory> orders;

    ShopperOrderHistoryAdapter historyAdapter;

    String userId;

    String StoreId;

    String accountType = "shopper";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");
        }
        recyclerView = findViewById(R.id.PendingOrderCarousel);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orders = new ArrayList<>();
        historyAdapter = new ShopperOrderHistoryAdapter(userId, orders);
        recyclerView.setAdapter(historyAdapter);
        DatabaseReference ref = db.getReference();
        DatabaseReference query = ref.child("transactions");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                        ShopperTransactionHistory order = transactionSnapshot.getValue(ShopperTransactionHistory.class);
                        orders.add(order);
                    }
                    historyAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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
                    Intent intent = new Intent(ShopperTransactionHistoryActivity.this, BrowseStoreActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    return true;
                }
                if (itemId == R.id.shopper_nav_menu_cart) {
                    Intent intent = new Intent(ShopperTransactionHistoryActivity.this, CartActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    return true;
                }
                if (itemId == R.id.shopper_nav_menu_logout) {
                    Toast.makeText(ShopperTransactionHistoryActivity.this, getString(R.string.logout_successful_text), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ShopperTransactionHistoryActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });

    }


}