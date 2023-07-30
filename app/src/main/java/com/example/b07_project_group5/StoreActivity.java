package com.example.b07_project_group5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class StoreActivity extends AppCompatActivity {
    String previousActivity = "";
    String userId = "";
    String accountType="";
    String storeId = "";
    Store store;
    FirebaseDatabase db;
    private List<Product> productList;
    private RecyclerView recyclerView; // Declare 'recyclerView' as a class-level member

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");

        //Start initializing store information
        Intent intent = getIntent();
        if (intent != null) {
            previousActivity = intent.getStringExtra("previousActivity");
            userId = intent.getStringExtra("userId");
            accountType = intent.getStringExtra("accountType");
            storeId = intent.getStringExtra("storeId");
            DatabaseReference ref = db.getReference();
            DatabaseReference query = ref.child("stores").child(storeId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        store = snapshot.getValue(Store.class);
                        TextView textViewStoreName = findViewById(R.id.storeName);
                        ImageView imageViewStore = findViewById(R.id.storeLogo);
                        textViewStoreName.setText(store.name);
                        Glide.with(StoreActivity.this).load(store.logo).into(imageViewStore);
                        ref.child("users").child(snapshot.child("userId").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String storeOwner = snapshot.child("username").getValue().toString();
                                    TextView textViewStoreOwner = findViewById(R.id.storeOwner);
                                    textViewStoreOwner.setText(storeOwner);
                                }
                            }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {}
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }

        // Initialize your product list here (e.g., fetch from a server or database)
        productList = new ArrayList<>();

        recyclerView = findViewById(R.id.productCarousel);
        ProductAdapter adapter = new ProductAdapter(userId, accountType, storeId, productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        readData(productList, ""); //data
        adapter.notifyDataSetChanged(); //update

        Button backBtn = findViewById(R.id.storeBackBtn);
        Button addProductBtn = findViewById(R.id.addProductBtn);
        ImageButton editStoreBtn = findViewById(R.id.editStoreBtn);
        RecyclerView productCarousel = findViewById(R.id.productCarousel);
        if (accountType.equals("owner")) {
            backBtn.setVisibility(View.INVISIBLE);
            addProductBtn.setVisibility(View.VISIBLE);
            editStoreBtn.setVisibility(View.VISIBLE);
        } else {
            backBtn.setVisibility(View.VISIBLE);
            addProductBtn.setVisibility(View.INVISIBLE);
            editStoreBtn.setVisibility(View.INVISIBLE);
            ConstraintSet set = new ConstraintSet();
            ConstraintLayout layout = findViewById(R.id.store_page_container);
            set.clone(layout);
            set.clear(R.id.productCarousel, ConstraintSet.TOP);
            set.connect(R.id.productCarousel, ConstraintSet.TOP, R.id.searchBar, ConstraintSet.BOTTOM, 40);
            set.applyTo(layout);
            float scale = this.getResources().getDisplayMetrics().density;
            int pixels = (int) (450 * scale + 0.5f);
            ViewGroup.LayoutParams layoutParams = productCarousel.getLayoutParams();
            layoutParams.height = pixels;
            productCarousel.setLayoutParams(layoutParams);
        };

        BottomNavigationView ownerBottomNavigationView = findViewById(R.id.owner_nav_menu);
        if (!accountType.equals("owner")) { ownerBottomNavigationView.setVisibility(View.INVISIBLE); }
        ownerBottomNavigationView.setSelectedItemId(R.id.owner_nav_menu_store);
        ownerBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.owner_nav_menu_store) {
                    return true;
                } else if (itemId == R.id.owner_nav_menu_orders) {
                    Intent intent = new Intent(StoreActivity.this, LoginActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("accountType", accountType);
                    intent.putExtra("storeId", storeId);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.owner_nav_menu_logout) {
                    Toast.makeText(StoreActivity.this, getString(R.string.logout_successful_text), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(StoreActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });

        BottomNavigationView shopperBottomNavigationView = findViewById(R.id.shopper_nav_menu);
        if (!accountType.equals("shopper")) { shopperBottomNavigationView.setVisibility(View.INVISIBLE); }
        if (previousActivity == null) {
            shopperBottomNavigationView.setSelectedItemId(R.id.shopper_nav_menu_store);
        } else if (previousActivity.equals("BrowseStoreActivity")) {
            shopperBottomNavigationView.setSelectedItemId(R.id.shopper_nav_menu_store);
        }
        shopperBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.shopper_nav_menu_store) {
                    return true;
                } else if (itemId == R.id.shopper_nav_menu_cart) {
                    Intent intent = new Intent(StoreActivity.this, CartActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.shopper_nav_menu_orders) {
                    Intent intent = new Intent(StoreActivity.this, LoginActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("accountType", accountType);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.shopper_nav_menu_logout) {
                    Toast.makeText(StoreActivity.this, getString(R.string.logout_successful_text), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(StoreActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }


    public void readData(List<Product> productList, String searchInput) {
        DatabaseReference ref = db.getReference().child("products");
        ref.orderByChild("storeId").equalTo(storeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    productList.clear(); // Clear the products list to avoid duplicate entries
                    // Loop through the data snapshot and add products to the list
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        if (productSnapshot.child("name").getValue().toString().toLowerCase().contains(searchInput.toLowerCase())) {
                            String storeId = productSnapshot.child("storeId").getValue().toString();
                            String name = productSnapshot.child("name").getValue().toString();
                            double price = Double.parseDouble(productSnapshot.child("price").getValue().toString());
                            int stock = Integer.parseInt(productSnapshot.child("stock").getValue().toString());
                            String image = productSnapshot.child("image").getValue().toString();
                            String description = productSnapshot.child("description").getValue().toString();
                            productList.add(new Product(storeId, name, price, stock, image, description)); //USE DEFAULT PRODUCT PHOTO for now
                        }
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();  // Notify the adapter about the data change
                } else {
                    Log.e("StoreActivity", "Database read error: No data found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the onCancelled event if needed
                Log.e("StoreActivity", "Database read error: " + databaseError.getMessage());
            }
        });
    }

    public void onBackButtonClicked(View view) {
        finish(); // This will navigate back to the previous activity
    }

    public void onAdd(View view){
        Intent intent = new Intent(this, AddOrEditProductActivity.class);
        intent.putExtra("previousActivity", "StoreActivity");
        intent.putExtra("userId", userId);
        intent.putExtra("accountType", accountType);
        intent.putExtra("storeId", storeId);
        startActivity(intent);
    }

    //to refresh upon entry
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the product list whenever the activity resumes
        productList.clear(); // Clear the list
        readData(productList, ""); // Fetch the data again from the database
        recyclerView.getAdapter().notifyDataSetChanged(); // Notify the adapter about the data change
    }

    public void setWarningText(String warning) {
        TextView warningText = (TextView) findViewById(R.id.store_warning_text);
        warningText.setText(warning);
    }

    // Toggle textview to edittext when user clicks on edit button
    public void toggleStoreInput(View v) {
        TextView storeName = (TextView) findViewById(R.id.storeName);
        EditText storeNameInput = (EditText) findViewById(R.id.storeNameInput);
        storeNameInput.setVisibility(View.VISIBLE);
        storeNameInput.setText(storeName.getText().toString());
        storeName.setVisibility(View.INVISIBLE);
        ImageButton saveStoreBtn = (ImageButton) findViewById(R.id.saveStoreBtn);
        saveStoreBtn.setVisibility(View.VISIBLE);
        v.setVisibility(View.INVISIBLE);
    }

    public void editStoreInfo(View v) {
        EditText storeNameInput = (EditText) findViewById(R.id.storeNameInput);
        String storeName = storeNameInput.getText().toString();
        if (storeName.equals("")) {
            setWarningText(getString(R.string.store_name_empty_warning));
            return;
        }
        DatabaseReference ref = db.getReference();
        DatabaseReference query = ref.child("stores");
        query.child(storeId).child("name").setValue(storeName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ImageButton editStoreBtn = (ImageButton) findViewById(R.id.editStoreBtn);
                editStoreBtn.setVisibility(View.VISIBLE);
                v.setVisibility(View.INVISIBLE);
                storeNameInput.setVisibility(View.INVISIBLE);
                TextView storeNameView = (TextView) findViewById(R.id.storeName);
                storeNameView.setVisibility(View.VISIBLE);
                storeNameView.setText(storeName);
                setWarningText("");
                Toast.makeText(StoreActivity.this, getString(R.string.change_store_info_successful_text), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void searchProduct(View v) {
        EditText searchInput = (EditText) findViewById(R.id.searchBar);
        readData(productList, searchInput.getText().toString());
        recyclerView.getAdapter().notifyDataSetChanged();
        searchInput.clearFocus();
    }
}