package com.example.b07_project_group5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    FirebaseDatabase db;
    ArrayList<CartProduct> shoppingCart;
    String userId;
    Transaction cart;
    CartTotalCostView totalCostView;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");
        }
        shoppingCart = new ArrayList<CartProduct>();
        totalCostView = new CartTotalCostView((TextView) findViewById(R.id.totalCost));
        recyclerView = findViewById(R.id.cartCarousel);
        CartProductAdapter adapter = new CartProductAdapter(userId, shoppingCart, (TextView) findViewById(R.id.totalCost), (Button) findViewById(R.id.checkoutBtn));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.getAdapter().notifyDataSetChanged();

        BottomNavigationView bottomNavigationView = findViewById(R.id.shopper_nav_menu);
        bottomNavigationView.setSelectedItemId(R.id.shopper_nav_menu_cart);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.shopper_nav_menu_cart) {
                    return true;
                } else if (itemId == R.id.shopper_nav_menu_store) {
                    Intent intent = new Intent(CartActivity.this, BrowseStoreActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.shopper_nav_menu_orders) {
                    Intent intent = new Intent(CartActivity.this, LoginActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("accountType", "shopper");
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.shopper_nav_menu_logout) {
                    Toast.makeText(CartActivity.this, getString(R.string.logout_successful_text), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CartActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    public void readData() {
        shoppingCart.clear();
        cart = new Transaction();
        getCartFromDatabase();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void checkout(View view) {
        DatabaseReference ref = db.getReference();
        ref.child("transactions").orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                    if (!Boolean.parseBoolean(transactionSnapshot.child("finalized").getValue().toString())) {
                        transactionSnapshot.getRef().child("finalized").setValue(true);
                        shoppingCart.clear();
                        ((TextView) findViewById(R.id.totalCost)).setText(getString(R.string.total_cost_placeholder_text));
                        ((Button) findViewById(R.id.checkoutBtn)).setEnabled(false);
                        recyclerView.getAdapter().notifyDataSetChanged();
                        Toast.makeText(CartActivity.this, getString(R.string.checkout_success_text), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        readData();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void getCartFromDatabase() {
        DatabaseReference ref = db.getReference();
        cart = new Transaction();
        ref.child("transactions").orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                    if (!Boolean.parseBoolean(transactionSnapshot.child("finalized").getValue().toString())) {
                        Transaction tempCart = transactionSnapshot.getValue(Transaction.class);
                        cart.setUserId(tempCart.getUserId());
                        cart.setOrderList(tempCart.getOrderList());
                        cart.setFinalized(tempCart.getFinalized());
                        cart.setStatus(tempCart.getStatus());
                        loadCart();
                        recyclerView.getAdapter().notifyDataSetChanged();
                        return;
                    }
                }
                ((Button) findViewById(R.id.checkoutBtn)).setEnabled(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadCart() {
        DatabaseReference ref = db.getReference();

        // Check if there are no orders in cart
        if (cart.getOrderList().size() == 0) {
            ((Button) findViewById(R.id.checkoutBtn)).setEnabled(false);
        } else {
            ((Button) findViewById(R.id.checkoutBtn)).setEnabled(true);
        }

        for (String orderId : cart.getOrderList()) {
            ref.child("orders").child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Order order = snapshot.getValue(Order.class);
                        loadOrder(order, orderId);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void loadOrder(Order order, String orderId) {
        DatabaseReference ref = db.getReference();
        for (ProductOrder productOrder : order.getProductList()) {
            String productId = productOrder.getProductId();
            int amount = productOrder.getAmount();

            // Check if product is already in cart
            if (checkProductInCart(productId)) {
                continue;
            }
            ref.child("products").child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Product product = snapshot.getValue(Product.class);
                        CartProduct cartProduct = new CartProduct(product, amount, orderId, productId, userId, "");
                        loadProductStoreName(cartProduct, cartProduct.getProduct().getStoreId());
                        shoppingCart.add(cartProduct);
                        totalCostView.displayTotalCost(shoppingCart);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void loadProductStoreName(CartProduct cartProduct, String storeId) {
        DatabaseReference ref = db.getReference();
        ref.child("stores").child(storeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    cartProduct.setStoreName(snapshot.child("name").getValue().toString());
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean checkProductInCart(String productId) {
        for (CartProduct cartProduct : shoppingCart) {
            if (productId.equals(cartProduct.getProductId())) {
                return true;
            }
        }
        return false;
    }
}