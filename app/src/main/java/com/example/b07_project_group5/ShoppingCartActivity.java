package com.example.b07_project_group5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShoppingCartActivity extends AppCompatActivity {
    FirebaseDatabase db;
    ArrayList<CartProduct> shoppingCart;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
        shoppingCart = new ArrayList<CartProduct>();
        readData(shoppingCart);
    }

    public void readData(ArrayList<CartProduct> shoppingCart) {

    }
}