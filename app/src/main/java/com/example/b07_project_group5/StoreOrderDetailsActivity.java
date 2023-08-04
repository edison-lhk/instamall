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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class StoreOrderDetailsActivity extends AppCompatActivity {
    private String userId;
    private String accountType;
    private String storeId;
    private String orderId;
    private String customer;
    private Boolean status;
    private FirebaseDatabase db;
    private List<StoreOrderProduct> storeOrderProductList;
    private RecyclerView storeOrderProductCarousel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order_details);

        Intent intent = getIntent();
        this.userId = intent.getStringExtra("userId");
        this.accountType = intent.getStringExtra("accountType");
        this.storeId = intent.getStringExtra("storeId");
        this.orderId = intent.getStringExtra("orderId");
        this.customer = intent.getStringExtra("customer");
        this.status = intent.getBooleanExtra("status", false);
        this.db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");

        TextView orderIdTextView = findViewById(R.id.orderIdDetailsValueText);
        orderIdTextView.setText(orderId);
        TextView customerTextView = findViewById(R.id.customerDetailsValueText);
        customerTextView.setText(customer);
        TextView statusTextView = findViewById(R.id.statusValueText);
        Button statusCompletedBtn = findViewById(R.id.storeOrderCompletedBtn);
        if (status) {
            statusTextView.setText("Completed");
            statusCompletedBtn.setVisibility(View.INVISIBLE);
        } else {
            statusTextView.setText("Uncompleted");
        }

        storeOrderProductList = new ArrayList<>();
        storeOrderProductCarousel = findViewById(R.id.storeOrderProductCarousel);
        StoreOrderProductAdapter storeOrderProductAdapter = new StoreOrderProductAdapter(userId, storeOrderProductList);
        storeOrderProductCarousel.setLayoutManager(new LinearLayoutManager(this));
        storeOrderProductCarousel.setAdapter(storeOrderProductAdapter);

        BottomNavigationView ownerBottomNavigationView = findViewById(R.id.owner_nav_menu);
        ownerBottomNavigationView.setSelectedItemId(R.id.owner_nav_menu_orders);
        ownerBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.owner_nav_menu_orders) {
                    return true;
                } else if (itemId == R.id.owner_nav_menu_store) {
                    Intent intent = new Intent(StoreOrderDetailsActivity.this, StoreActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("accountType", accountType);
                    intent.putExtra("storeId", storeId);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.owner_nav_menu_logout) {
                    Toast.makeText(StoreOrderDetailsActivity.this, getString(R.string.logout_successful_text), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(StoreOrderDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    public void readData(List<StoreOrderProduct> storeOrderProductList) {
        DatabaseReference ref = db.getReference();
        DatabaseReference query = ref.child("orders");
        query.child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    storeOrderProductList.clear();
                    Order order = snapshot.getValue(Order.class);
                    for (ProductOrder productOrder : order.getProductList()) {
                        DatabaseReference query2 = ref.child("products");
                        query2.child(productOrder.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Product product = snapshot.getValue(Product.class);
                                storeOrderProductList.add(new StoreOrderProduct(productOrder, product));
                                displayTotalCost(storeOrderProductList);
                                storeOrderProductCarousel.getAdapter().notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("StoreOrderDetailsActivity", "Database read error: " + databaseError.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("StoreOrderDetailsActivity", "Database read error: " + databaseError.getMessage());
            }
        });
    }

    public void displayTotalCost(List<StoreOrderProduct> storeOrderProductList) {
        TextView totalPriceTextView = findViewById(R.id.storeOrderTotalPrice);
        double total = 0;
        for (StoreOrderProduct storeOrderProduct : storeOrderProductList) {
            total += storeOrderProduct.getProductOrder().getAmount() * storeOrderProduct.getProduct().getPrice();
        }
        totalPriceTextView.setText("Total: $ " + String.format("%.2f", total));
    }

    public void completeOrder(View v) {
        DatabaseReference ref = db.getReference();
        DatabaseReference query = ref.child("orders");
        query.child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.child("status").getRef().setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        DatabaseReference query2 = ref.child("transactions");
                        query2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                                    GenericTypeIndicator<List<String>> orderListClass = new GenericTypeIndicator<List<String>>() {};
                                    List<String> orderIdList = transactionSnapshot.child("orderList").getValue(orderListClass);
                                    if (orderIdList.contains(orderId)) {
                                        String transactionId = transactionSnapshot.getKey();
                                        List<Order> orderList = new ArrayList<>();
                                        for (String id : orderIdList) {
                                            query.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    orderList.add(snapshot.getValue(Order.class));
                                                    if (orderList.size() == orderIdList.size()) {
                                                        boolean completed = true;
                                                        for (Order order : orderList) {
                                                            if (!order.getStatus()) {
                                                                completed = false;
                                                            }
                                                        }
                                                        Toast.makeText(StoreOrderDetailsActivity.this, "Order " + orderId + " " + getString(R.string.store_order_marked_as_completed_text), Toast.LENGTH_LONG).show();
                                                        Button statusCompletedBtn = findViewById(R.id.storeOrderCompletedBtn);
                                                        statusCompletedBtn.setVisibility(View.INVISIBLE);
                                                        if (completed) {
                                                            query2.child(transactionId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    snapshot.child("status").getRef().setValue(true);
                                                                    finish();
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                    Log.e("StoreOrderDetailsActivity", "Database read error: " + databaseError.getMessage());
                                                                }
                                                            });
                                                        } else {
                                                            finish();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    Log.e("StoreOrderDetailsActivity", "Database read error: " + databaseError.getMessage());
                                                }
                                            });
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("StoreOrderDetailsActivity", "Database read error: " + databaseError.getMessage());
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("StoreOrderDetailsActivity", "Database read error: " + databaseError.getMessage());
            }
        });
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        storeOrderProductList.clear();
        readData(storeOrderProductList);
    }
}