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

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    FirebaseDatabase db;
    String accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
        accountType = "";
        String[] user_types = getResources().getStringArray(R.array.user_types);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, user_types);
        AutoCompleteTextView login_type_input = (AutoCompleteTextView) findViewById(R.id.login_type_input);
        login_type_input.setAdapter(arrayAdapter);
        login_type_input.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                accountType = parent.getItemAtPosition(position).toString().toLowerCase();
            }
        });
    }

    public void setWarningText(String warning) {
        TextView warning_text = (TextView) findViewById(R.id.login_warning_text);
        warning_text.setText(warning);
    }

    public void loginUser(View v) {
        EditText email_input = (EditText) findViewById(R.id.login_email_input);
        String email = email_input.getText().toString();
        EditText password_input = (EditText) findViewById(R.id.login_password_input);
        String password = password_input.getText().toString();
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
                    boolean user_exist = false;
                    for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                        if (accountType.equals(childSnapshot.child("accountType").getValue().toString())) {
                            if (!password.equals(childSnapshot.child("password").getValue().toString())) {
                                setWarningText(getString(R.string.login_password_incorrect_warning));
                                return;
                            }
                            user_exist = true;
                        }
                    }
                    if (!user_exist) {
                        setWarningText(getString(R.string.login_cannot_find_user_warning));
                        return;
                    }
                    email_input.setText("");
                    password_input.setText("");
                    Toast.makeText(LoginActivity.this, getString(R.string.login_successful_text), Toast.LENGTH_LONG).show();
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


    //Vincent-Store_Owner_Display Stuff
    //USE this to access activity
    public void openStorePage(View view) {
        Intent intent = new Intent(this, StoreActivity.class);

        // Pass the store information to the Store activity
        //REMEMBER TO REPLACE CONSTANTS WITH store ".get" functions
        intent.putExtra("storeName", "Vincent's Store2");
        intent.putExtra("storeOwner", "Vincent");
        intent.putExtra("storeId", 1001);
        intent.putExtra("storeLogo", "https://firebasestorage.googleapis.com/v0/b/testing-7a8a5.appspot.com/o/StoreLogo%2Fstore_logo.png?alt=media&token=2660325b-c4cd-4659-bf6b-60239d06f84b");

        intent.putExtra("accountType", "owner");
        intent.putExtra("ID", "vincent@yahoo.com");

        startActivity(intent);
    }

}