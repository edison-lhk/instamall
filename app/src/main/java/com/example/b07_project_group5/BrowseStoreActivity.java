package com.example.b07_project_group5;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class BrowseStoreActivity extends AppCompatActivity {
    String userId;
    String accountType = "shopper";
    FirebaseDatabase db;
    List<Store> storeList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_store);
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        storeList = new ArrayList<>();
        recyclerView = findViewById(R.id.storeCarousel);
        StoreAdapter adapter = new StoreAdapter(userId, accountType, storeList);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        readData(storeList);
        adapter.notifyDataSetChanged();
        BottomNavigationView bottomNavigationView = findViewById(R.id.shopper_nav_menu);
        bottomNavigationView.setSelectedItemId(R.id.shopper_nav_menu_store);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.shopper_nav_menu_store) {
                    return true;
                } else if (itemId == R.id.shopper_nav_menu_cart) {
                    Intent intent = new Intent(BrowseStoreActivity.this, LoginActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("accountType", accountType);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.shopper_nav_menu_orders) {
                    Intent intent = new Intent(BrowseStoreActivity.this, LoginActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("accountType", accountType);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    public void readData(List<Store> storeList) {
        DatabaseReference ref = db.getReference();
        ref.child("stores").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    storeList.clear();
                    for (DataSnapshot storeSnapshot: snapshot.getChildren()) {
                        String ownerId = storeSnapshot.child("userId").getValue().toString();
                        String name = storeSnapshot.child("name").getValue().toString();
                        String logo = storeSnapshot.child("logo").getValue().toString();
                        storeList.add(new Store(ownerId, name, logo));
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("BrowseStoreActivity", "Database read error: No data found");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        storeList.clear();
        readData(storeList);
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}

