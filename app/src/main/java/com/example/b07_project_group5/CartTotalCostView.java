package com.example.b07_project_group5;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CartTotalCostView {
    private static TextView view;

    public CartTotalCostView(TextView view) {
        this.view = view;
    }

    public CartTotalCostView() {}

    public void displayTotalCost(ArrayList<CartProduct> shoppingCart) {
        double total = 0;
        for (CartProduct product : shoppingCart) {
            total += product.getAmount() * product.getProduct().getPrice();
        }
        view.setText("Total: $" + String.format("%.2f", total));
    }
}
