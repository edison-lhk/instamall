package com.example.b07_project_group5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddProductActivity extends AppCompatActivity {
    double price;
    int stock;
    String userInputImage;
    String userInputName;
    String storeId;
    FirebaseDatabase db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");

        Intent intent = getIntent();
        if (intent != null) {
            this.storeId = intent.getStringExtra("storeId");
        }

        EditText editName = findViewById(R.id.editName);
        EditText editPrice = findViewById(R.id.editPrice);
        EditText editStock = findViewById(R.id.editStock);
        EditText editImage = findViewById(R.id.editImage);
        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        EditText editDescription = findViewById(R.id.editDescription);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text entered by the user
                userInputName = editName.getText().toString();
                String userInputPrice = editPrice.getText().toString();
                String userInputStock = editStock.getText().toString();
                userInputImage = editImage.getText().toString();
                String userInputDescription = editDescription.getText().toString();


                // Convert price and stock to integers
                price = Double.parseDouble(userInputPrice);
                stock = Integer.parseInt(userInputStock);

                // Write the data to the database
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("products");
                String uniqueKey = ref.push().getKey();
                Product product = new Product(userInputName, price, storeId, stock, userInputImage, userInputDescription);
                ref.orderByChild("name").equalTo(product.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot productSnapshot: snapshot.getChildren()) {
                                if (storeId.equals(productSnapshot.child("storeId").getValue().toString())) {
                                    setWarningText(getString(R.string.product_already_exists_warning));
                                    return;
                                }
                            }
                        }
                        ref.child(uniqueKey).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AddProductActivity.this, getString(R.string.add_product_successful_text), Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        });
    }

    public void setWarningText(String warning) {
        TextView warningText = (TextView) findViewById(R.id.add_warning_text);
        warningText.setText(warning);
    }

    public void onBackButtonClicked(View view) {
        finish(); // This will navigate back to the previous activity
    }
}