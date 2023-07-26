package com.example.b07_project_group5;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.runtime.snapshots.Snapshot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ShopperBrowseStoreActivity extends AppCompatActivity {
    FirebaseDatabase db;
    Button storeButton1;
    Button storeButton2;
    DatabaseReference ref;
    LinearLayout StoreLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopper_browse_store);
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
        StoreLayout = findViewById(R.id.StoreLayout);
        ref = db.getReference();

        ref.child("stores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StoreLayout.removeAllViews(); //Delete all buttons
                //Automatically  generate stores' buttons on Shopper_browse_store.xml
                for (DataSnapshot storeSnapshot : dataSnapshot.getChildren()) {
                    String storeName = storeSnapshot.getKey();
                    Button storeButton = new Button(ShopperBrowseStoreActivity.this);
                    loadImage_Text(storeName, storeButton);
                    StoreLayout.addView(storeButton);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private void loadImage_Text(String storeName, Button button){
        ref.child("stores").child(storeName).addValueEventListener(new ValueEventListener() {
            String storeId = "";
            String storeName = "";
            String storeOwner = "";
            String storeLogo = "";
            String userId = "";
            String accountType = "";
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Intent intent = getIntent(); //get intent from Login Shopper part
                userId = snapshot.child("userId").getValue(String.class);
                accountType = intent.getStringExtra("accountType");
                storeName = snapshot.child("name").getValue(String.class);
                storeLogo = snapshot.child("logo").getValue(String.class);
                storeId = snapshot.getKey();
                //get StoreOwnerName by userid
                ref.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot childsnapshot) {
                        storeOwner = childsnapshot.child("username").getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w(TAG, "Failed to find username by userId", error.toException());
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ShopperBrowseStoreActivity.this, ShopperStoreActivity.class);
                        intent.putExtra("storeId", storeId);
                        intent.putExtra("storeName", storeName);
                        intent.putExtra("storeOwner", storeOwner);
                        intent.putExtra("storeLogo", storeLogo);
                        intent.putExtra("accountType", accountType);
                        intent.putExtra("ID", userId);
                        startActivity(intent);
                    }
                });
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(storeLogo)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Drawable logo = new BitmapDrawable(getResources(), resource);
                                button.setCompoundDrawablesWithIntrinsicBounds(logo, null, null, null);
                                button.setText(storeName);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());

            }
        });

    }
}

