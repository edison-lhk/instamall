package com.example.b07_project_group5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* This is how you pass an image (successful)
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/testing-7a8a5.appspot.com/o/ProductImages%2Fapple.png?alt=media&token=37f44daa-c2ee-450c-a35b-88fa802ab730").into(imageView);
        */
    }


    public void openStorePage(View view) {
        Intent intent = new Intent(this, StoreActivity.class);

        // Pass the store information to the Store activity
        //REMEMBER TO REPLACE CONSTANTS WITH store ".get" functions
        intent.putExtra("storeName", "Vincent's Shop");
        intent.putExtra("storeOwner", "Vincent");
        intent.putExtra("storeId", 1001);
        intent.putExtra("storeLogo", "https://firebasestorage.googleapis.com/v0/b/testing-7a8a5.appspot.com/o/StoreLogo%2Fstore_logo.png?alt=media&token=2660325b-c4cd-4659-bf6b-60239d06f84b");


        startActivity(intent);
    }

}