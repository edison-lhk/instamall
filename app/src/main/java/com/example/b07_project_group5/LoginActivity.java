package com.example.b07_project_group5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    FirebaseDatabase db;
    String userId = "";
    String username = "";
    String email = "";
    String password = "";
    String accountType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
        String[] userTypes = getResources().getStringArray(R.array.user_types);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, userTypes);
        AutoCompleteTextView loginTypeInput = (AutoCompleteTextView) findViewById(R.id.login_type_input);
        loginTypeInput.setAdapter(arrayAdapter);
        loginTypeInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                accountType = parent.getItemAtPosition(position).toString().toLowerCase();
            }
        });
    }

    public void setWarningText(String warning) {
        TextView warningText = (TextView) findViewById(R.id.login_warning_text);
        warningText.setText(warning);
    }

    public void loginUser(View v) {
        EditText emailInput = (EditText) findViewById(R.id.login_email_input);
        email = emailInput.getText().toString();
        EditText passwordInput = (EditText) findViewById(R.id.login_password_input);
        password = passwordInput.getText().toString();
        if (email.equals("") || password.equals("") || accountType.equals("")) {
            setWarningText(getString(R.string.login_empty_fields_warning));
            return;
        }
        DatabaseReference ref = db.getReference();
        DatabaseReference query = ref.child("users");
        query.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    setWarningText(getString(R.string.login_cannot_find_user_warning));
                } else {
                    boolean userExist = false;
                    for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                        if (accountType.equals(childSnapshot.child("accountType").getValue().toString())) {
                            if (!password.equals(childSnapshot.child("password").getValue().toString())) {
                                setWarningText(getString(R.string.login_password_incorrect_warning));
                                return;
                            }
                            userExist = true;
                            userId = childSnapshot.getKey();
                            username = childSnapshot.child("username").getValue().toString();
                        }
                    }
                    if (!userExist) {
                        setWarningText(getString(R.string.login_cannot_find_user_warning));
                        return;
                    }
                    emailInput.setText("");
                    passwordInput.setText("");
                    Toast.makeText(LoginActivity.this, getString(R.string.login_successful_text), Toast.LENGTH_LONG).show();
                    setWarningText("");
                    if (accountType.equals("owner")) {
                        DatabaseReference query2 = ref.child("stores");
                        query2.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            String storeId = "";
                            String storeName = "";
                            String storeOwner = "";
                            String storeLogo = "";
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!snapshot.exists()) {
                                    Store store = new Store(userId, username + "'s Store", "https://firebasestorage.googleapis.com/v0/b/testing-7a8a5.appspot.com/o/StoreLogo%2Fstore_logo.png?alt=media&token=2660325b-c4cd-4659-bf6b-60239d06f84b");
                                    String uniqueKey = ref.push().getKey();
                                    query2.child(uniqueKey).setValue(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            storeId = uniqueKey;
                                            storeName = store.name;
                                            storeOwner = username;
                                            storeLogo = store.logo;
                                            Intent intent = new Intent(LoginActivity.this, StoreActivity.class);
                                            intent.putExtra("storeId", storeId);
                                            intent.putExtra("storeName", storeName);
                                            intent.putExtra("storeOwner", storeOwner);
                                            intent.putExtra("storeLogo", storeLogo);
                                            intent.putExtra("accountType", accountType);
                                            intent.putExtra("ID", userId);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                                        Store store = new Store(userId, childSnapshot.child("name").getValue().toString(), childSnapshot.child("logo").getValue().toString());
                                        storeId = childSnapshot.getKey();
                                        storeName = store.name;
                                        storeOwner = username;
                                        storeLogo = store.logo;
                                    }
                                    Intent intent = new Intent(LoginActivity.this, StoreActivity.class);
                                    intent.putExtra("storeId", storeId);
                                    intent.putExtra("storeName", storeName);
                                    intent.putExtra("storeOwner", storeOwner);
                                    intent.putExtra("storeLogo", storeLogo);
                                    intent.putExtra("accountType", accountType);
                                    intent.putExtra("ID", userId);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    public void navigateToRegisterActivity(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}