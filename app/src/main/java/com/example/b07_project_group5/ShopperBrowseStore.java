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


public class ShopperBrowseStore extends AppCompatActivity {
    FirebaseDatabase db;
    Button storeButton1;
    Button storeButton2;
    DatabaseReference ref;
    LinearLayout StoreLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopper_browse_store);
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
        //storeButton1 = findViewById(R.id.StoreButton1);
        //storeButton2 = findViewById(R.id.StoreButton2);
        StoreLayout = findViewById(R.id.StoreLayout);
        ref = db.getReference();
        //loadImage_Text("store2", storeButton1);
        //loadImage_Text("store2", storeButton2);
        /*ref.child("stores").child("store1").child("logo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String imageUrl = snapshot.getValue(String.class);
                Glide.with(getApplicationContext()).load(imageUrl).into(storeImage1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });*/
        ref.child("stores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StoreLayout.removeAllViews(); //Delete all buttons
                //Automatically  generate stores' buttons on Shopper_browse_store.xml
                for (DataSnapshot storeSnapshot : dataSnapshot.getChildren()) {
                    String storeName = storeSnapshot.getKey();
                    //String logoUrl = storeSnapshot.child("logo").getValue(String.class);
                    //String storeDisplayName = storeSnapshot.child("name").getValue(String.class);

                    Button storeButton = new Button(ShopperBrowseStore.this);
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
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String imageUrl = snapshot.child("logo").getValue(String.class);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ShopperBrowseStore.this, StoreActivity.class);
                        intent.putExtra("name", storeName);
                        startActivity(intent);
                    }
                });
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

