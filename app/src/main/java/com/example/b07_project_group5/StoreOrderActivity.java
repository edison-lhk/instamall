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

public class StoreOrderActivity extends AppCompatActivity {
    private String userId;
    private String accountType;
    private String storeId;
    private FirebaseDatabase db;
    private List<StoreOrder> uncompletedOrderList;
    private List<StoreOrder> completedOrderList;
    private RecyclerView uncompletedOrderCarousel;
    private RecyclerView completedOrderCarousel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order);

        Intent intent = getIntent();
        this.userId = intent.getStringExtra("userId");
        this.accountType = intent.getStringExtra("accountType");
        this.storeId = intent.getStringExtra("storeId");

        this.db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");

        uncompletedOrderList = new ArrayList<>();
        completedOrderList = new ArrayList<>();
        uncompletedOrderCarousel = findViewById(R.id.uncompletedOrderCarousel);
        completedOrderCarousel = findViewById(R.id.completedOrderCarousel);

        StoreOrderAdapter uncompletedOrderAdapter = new StoreOrderAdapter(userId, accountType, storeId, uncompletedOrderList, completedOrderList, completedOrderCarousel);
        uncompletedOrderCarousel.setLayoutManager(new LinearLayoutManager(this));
        uncompletedOrderCarousel.setAdapter(uncompletedOrderAdapter);

        StoreOrderAdapter completedOrderAdapter = new StoreOrderAdapter(userId, accountType, storeId, completedOrderList, completedOrderList, completedOrderCarousel);
        completedOrderCarousel.setLayoutManager(new LinearLayoutManager(this));
        completedOrderCarousel.setAdapter(completedOrderAdapter);

        BottomNavigationView ownerBottomNavigationView = findViewById(R.id.owner_nav_menu);
        ownerBottomNavigationView.setSelectedItemId(R.id.owner_nav_menu_orders);
        ownerBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.owner_nav_menu_orders) {
                    return true;
                } else if (itemId == R.id.owner_nav_menu_store) {
                    Intent intent = new Intent(StoreOrderActivity.this, StoreActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("accountType", accountType);
                    intent.putExtra("storeId", storeId);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.owner_nav_menu_logout) {
                    Toast.makeText(StoreOrderActivity.this, getString(R.string.logout_successful_text), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(StoreOrderActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    public void readData(List<StoreOrder> uncompletedOrderList, List<StoreOrder> completedOrderList) {
        DatabaseReference ref = db.getReference();
        DatabaseReference query = ref.child("orders");
        uncompletedOrderList.clear();
        completedOrderList.clear();
        query.orderByChild("storeId").equalTo(storeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                        String orderId = orderSnapshot.getKey();
                        Boolean status = (Boolean) orderSnapshot.child("status").getValue();

                        DatabaseReference query2 = ref.child("transactions");
                        query2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                                        Transaction transaction = transactionSnapshot.getValue(Transaction.class);
                                        for (String id : transaction.getOrderList()) {
                                            if (id.equals(orderId)) {
                                                String userId = transaction.getUserId();
                                                DatabaseReference query3 = ref.child("users");
                                                query3.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        String customer = snapshot.child("username").getValue().toString();
                                                        if (!status) {
                                                            uncompletedOrderList.add(new StoreOrder(orderId, customer, status));
                                                            uncompletedOrderCarousel.getAdapter().notifyDataSetChanged();
                                                        } else {
                                                            completedOrderList.add(new StoreOrder(orderId, customer, status));
                                                            completedOrderCarousel.getAdapter().notifyDataSetChanged();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        Log.e("StoreOrderActivity", "Database read error: " + databaseError.getMessage());
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("StoreOrderActivity", "Database read error: " + databaseError.getMessage());
                            }
                        });
                    }
                } else {
                    Log.e("StoreOrderActivity", "Database read error: No data found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the onCancelled event if needed
                Log.e("StoreOrderActivity", "Database read error: " + databaseError.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        uncompletedOrderList.clear();
        completedOrderList.clear();
        readData(uncompletedOrderList, completedOrderList);
    }
}