package com.example.b07_project_group5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductDetailsActivity extends AppCompatActivity {
    String previousActivity;
    String userId;
    String accountType;
    String productId;
    String name;
    Double price;
    String image;
    String description;
    String storeId;
    int stock;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
        Intent intent = getIntent();
        if (intent != null) {
            previousActivity = intent.getStringExtra("previousActivity");
            userId = intent.getStringExtra("userId");
            accountType = intent.getStringExtra("accountType");
            productId = intent.getStringExtra("productId");
            DatabaseReference ref = db.getReference();
            ref.child("products").child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        name = snapshot.child("name").getValue().toString();
                        price = Double.parseDouble(snapshot.child("price").getValue().toString());
                        image = snapshot.child("image").getValue().toString();
                        description = snapshot.child("description").getValue().toString();
                        storeId = snapshot.child("storeId").getValue().toString();
                        stock = Integer.parseInt(snapshot.child("stock").getValue().toString());
                        TextView textViewProductName = findViewById(R.id.textViewProductName);
                        TextView textViewProductPrice = findViewById(R.id.textViewProductPrice);
                        ImageView imageViewProduct = findViewById(R.id.imageViewProduct);
                        TextView textViewDescription = findViewById(R.id.textViewProductDescription);
                        TextView textViewProductStock = findViewById(R.id.productStockRemaining);
                        Glide.with(ProductDetailsActivity.this).load(image).into(imageViewProduct);
                        textViewDescription.setText(description);
                        textViewProductName.setText(name);
                        textViewProductPrice.setText("$" + String.format("%.2f", price));
                        textViewProductStock.setText("Stock: " + String.valueOf(stock));
                        if (stock == 0) {
                            disableAmountInput();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }

        if (accountType.equals("owner")) {
            Button addToCartBtn = findViewById(R.id.addToCartBtn);
            addToCartBtn.setVisibility(View.INVISIBLE);
            ImageButton subOneProductBtn = findViewById(R.id.subOneProductBtn);
            subOneProductBtn.setVisibility(View.INVISIBLE);
            ImageButton plusOneProductBtn = findViewById(R.id.plusOneProductBtn);
            plusOneProductBtn.setVisibility(View.INVISIBLE);
            TextInputLayout productAmountInputContainer = findViewById(R.id.productAmountInputContainer);
            productAmountInputContainer.setVisibility(View.INVISIBLE);
            TextView productStockRemaining = findViewById(R.id.productStockRemaining);
            productStockRemaining.setVisibility(View.INVISIBLE);
            ImageView imageViewProduct = findViewById(R.id.imageViewProduct);
            float scale = this.getResources().getDisplayMetrics().density;
            int pixels = (int) (374 * scale + 0.5f);
            ViewGroup.LayoutParams layoutParams = imageViewProduct.getLayoutParams();
            layoutParams.height = pixels;
            imageViewProduct.setLayoutParams(layoutParams);
        }

        BottomNavigationView ownerBottomNavigationView = findViewById(R.id.owner_nav_menu);
        if (!accountType.equals("owner")) { ownerBottomNavigationView.setVisibility(View.INVISIBLE); }
        if (previousActivity.equals("StoreActivity")) {
            ownerBottomNavigationView.setSelectedItemId(R.id.owner_nav_menu_store);
        } else if (previousActivity.equals("StoreOrderDetailsActivity")) {
            ownerBottomNavigationView.setSelectedItemId(R.id.owner_nav_menu_orders);
        }
        ownerBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.owner_nav_menu_store) {
                    return true;
                } else if (itemId == R.id.owner_nav_menu_orders) {
                    Intent intent = new Intent(ProductDetailsActivity.this, CartActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.owner_nav_menu_logout) {
                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.logout_successful_text), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ProductDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });

        BottomNavigationView shopperBottomNavigationView = findViewById(R.id.shopper_nav_menu);
        if (!accountType.equals("shopper")) { shopperBottomNavigationView.setVisibility(View.INVISIBLE); }
        if (previousActivity.equals("StoreActivity")) {
            shopperBottomNavigationView.setSelectedItemId(R.id.shopper_nav_menu_store);
        } else if (previousActivity.equals("CartActivity")) {
            shopperBottomNavigationView.setSelectedItemId(R.id.shopper_nav_menu_cart);
        }
        shopperBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.shopper_nav_menu_store) {
                    return true;
                } else if (itemId == R.id.shopper_nav_menu_cart) {
                    Intent intent = new Intent(ProductDetailsActivity.this, CartActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.shopper_nav_menu_orders) {
                    Intent intent = new Intent(ProductDetailsActivity.this, ShopperTransactionHistoryActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("accountType", accountType);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.shopper_nav_menu_logout) {
                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.logout_successful_text), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ProductDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    public void onBackButtonClicked(View view) {
        // Handle back button click event here
        finish(); // This will navigate back to the previous activity (StoreActivity)
    }

    public void addToCart(View view) {
        DatabaseReference ref;

        ref = db.getReference();

        DatabaseReference transactionQuery = ref.child("transactions");
        DatabaseReference orderQuery = ref.child("orders");

        EditText amountInputField = findViewById(R.id.productAmountInput);
        amountInputField.clearFocus();
        int amountInput;

        // Check if the input is empty
        if (amountInputField.getText().toString() == "") {
            Toast.makeText(ProductDetailsActivity.this, getString(R.string.product_amount_empty_warning), Toast.LENGTH_SHORT).show();
            return;
        }
        if (amountInputField.getText().toString().length() > 1 && amountInputField.getText().toString().charAt(0) == '0') {
            Toast.makeText(ProductDetailsActivity.this, getString(R.string.product_amount_invalid_warning), Toast.LENGTH_LONG).show();
            amountInputField.setText("");
            return;
        }
        // Fail-safe in case user types some non-integer input
        try {
            amountInput = Integer.parseInt(amountInputField.getText().toString());
            amountInputField.setText("");
            if (amountInput > stock) {
                Toast.makeText(ProductDetailsActivity.this, getString(R.string.product_amount_overload_warning), Toast.LENGTH_SHORT).show();
                return;
            }
            if (amountInput <= 0) {
                Toast.makeText(ProductDetailsActivity.this, getString(R.string.product_amount_underload_warning), Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Toast.makeText(ProductDetailsActivity.this, getString(R.string.product_amount_invalid_warning), Toast.LENGTH_SHORT).show();
            amountInputField.setText("");
            return;
        }

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
                    order.addProduct(productId, amountInput);
                    orderId = orderQuery.push().getKey();
                    orderQuery.child(orderId).setValue(order);
                    cart = new Transaction(userId);
                    cart.addOrder(orderId);
                    transactionId = transactionQuery.push().getKey();
                    transactionQuery.child(transactionId).setValue(cart);
                    displaySuccessMsg();
                    return;
                }

                // Get user's shopping cart
                DataSnapshot transactionSnapshot = getShoppingCart(snapshot);

                // Check if user has an available shopping cart
                if (transactionSnapshot != null) {
                    addAmountToOrder(view, transactionSnapshot, orderQuery, amountInput);
                } else {
                    order = new Order(storeId);
                    cart = new Transaction(userId);
                    order.addProduct(productId, amountInput);

                    orderId = orderQuery.push().getKey();
                    orderQuery.child(orderId).setValue(order);

                    cart.addOrder(orderId);

                    transactionId = transactionQuery.push().getKey();
                    transactionQuery.child(transactionId).setValue(cart);
                }
                reduceStock(amountInput);
                displaySuccessMsg();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void addAmountToOrder(View view, DataSnapshot transactionSnapshot, DatabaseReference orderQuery, int amountInput) {

        // Add a temporary order with the storeId (will be removed if another order with same
        // storeId exists in cart)
        Order tempOrder = new Order(storeId);
        tempOrder.addProduct(productId, amountInput);
        String tempOrderId = orderQuery.push().getKey();
        orderQuery.child(tempOrderId).setValue(tempOrder);
        Transaction cart = transactionSnapshot.getValue(Transaction.class);
        if (cart.getOrderList() == null) {
            cart.initOrderList();
        }
        cart.addOrder(tempOrderId);

        transactionSnapshot.getRef().setValue(cart);

        Order order = new Order(storeId);

        // Go through every orderId in orderList of the cart
        for (DataSnapshot transOrderSnapshot : transactionSnapshot.child("orderList").getChildren()) {
            String orderId = transOrderSnapshot.getValue().toString();

            orderQuery.child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Check if the product exists within the order
                    if (snapshot.exists()) {
                        Order order = snapshot.getValue(Order.class);
                        // Check if storeId of the order is not equal to storeId of the product
                        if (!storeId.equals(order.getStoreId())) {
                            return;
                        }
                        // Check if storeId of the order is not equal to tempOrderId
                        if (!tempOrderId.equals(orderId)) {
                            orderQuery.child(tempOrderId).removeValue();
                            cart.removeOrder(tempOrderId);
                            transactionSnapshot.getRef().setValue(cart);
                        }
                        ProductOrder product = order.getProduct(productId);
                        if (product != null) {
                            product.setAmount(product.getAmount() + amountInput);
                        } else {
                            order.addProduct(productId, amountInput);
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

    public DataSnapshot getShoppingCart(DataSnapshot snapshot) {
        for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
            if (!Boolean.parseBoolean(transactionSnapshot.child("finalized").getValue().toString())) {
                return transactionSnapshot;
            }
        }
        return null;
    }

    public void displaySuccessMsg() {
        Toast.makeText(ProductDetailsActivity.this, name + " " + getString(R.string.add_order_to_cart_success), Toast.LENGTH_SHORT).show();
    }

    public void subOneToAmount(View view) {
        EditText amountInputField = findViewById(R.id.productAmountInput);
        amountInputField.clearFocus();
        if (amountInputField.getText().toString() == "") {
            amountInputField.setText("1");
        }
        try {
            int amountInput = Integer.parseInt(amountInputField.getText().toString());
            amountInput = Math.min(amountInput, stock + 1);
            amountInput = Math.max(amountInput - 1, 1);
            amountInputField.setText(String.valueOf(amountInput));
        } catch (Exception e) {
            amountInputField.setText("1");
        }
    }

    public void addOneToAmount(View view) {
        EditText amountInputField = findViewById(R.id.productAmountInput);
        amountInputField.clearFocus();
        if (amountInputField.getText().toString() == "") {
            amountInputField.setText("1");
        }
        try {
            int amountInput = Integer.parseInt(amountInputField.getText().toString());
            amountInput = Math.max(amountInput, 0);
            amountInput = Math.min(amountInput + 1, stock);
            amountInputField.setText(String.valueOf(amountInput));
        } catch (Exception e) {
            amountInputField.setText("1");
        }
    }

    private void reduceStock(int amount) {
        DatabaseReference ref = db.getReference();
        stock -= amount;
        ref.child("products").child(productId).child("stock").setValue(stock);
        TextView textViewProductStock = findViewById(R.id.productStockRemaining);
        textViewProductStock.setText("Stock: " + String.valueOf(stock));
        if (stock == 0) {
            disableAmountInput();
        }
    }

    private void disableAmountInput() {
        EditText editTextAmount = findViewById(R.id.productAmountInput);
        ImageButton imageButtonSub = findViewById(R.id.subOneProductBtn);
        ImageButton imageButtonPlus = findViewById(R.id.plusOneProductBtn);
        Button addToCartBtn = findViewById(R.id.addToCartBtn);
        editTextAmount.setEnabled(false);
        imageButtonSub.setEnabled(false);
        imageButtonPlus.setEnabled(false);
        addToCartBtn.setEnabled(false);
    }
}