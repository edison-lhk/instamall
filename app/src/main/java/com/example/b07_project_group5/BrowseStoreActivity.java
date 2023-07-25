package com.example.b07_project_group5;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class BrowseStoreActivity extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference ref;
    LinearLayout StoreLayout;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_store);
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        StoreLayout = findViewById(R.id.StoreLayout);
        ref = db.getReference();
        ref.child("stores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StoreLayout.removeAllViews(); //Delete all buttons
                //Automatically  generate stores' buttons on Shopper_browse_store.xml
                for (DataSnapshot storeSnapshot : dataSnapshot.getChildren()) {
                    String storeId = storeSnapshot.getKey();
                    Button storeButton = new Button(BrowseStoreActivity.this);
                    loadImage_Text(storeId, storeButton);
                    StoreLayout.addView(storeButton);
                    storeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(BrowseStoreActivity.this, StoreActivity.class);
                            intent.putExtra("userId", userId);
                            intent.putExtra("accountType", "shopper");
                            intent.putExtra("storeId", storeId);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private void loadImage_Text(String storeId, Button button){
        ref.child("stores").child(storeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String imageUrl = snapshot.child("logo").getValue(String.class);
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(imageUrl)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Drawable logo = new BitmapDrawable(getResources(), resource);
                                button.setCompoundDrawablesWithIntrinsicBounds(logo, null, null, null);
                                button.setText(name);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {}
                        });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}

