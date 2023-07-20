package com.example.b07_project_group5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
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
        if (email.equals("") || password.equals("")) {
            setWarningText(getString(R.string.login_empty_fields_warning));
            return;
        }
        DatabaseReference ref = db.getReference();
        DatabaseReference query = ref.child("users").child(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    setWarningText(getString(R.string.login_cannot_find_user_warning));
                } else {
                    if (!password.equals(snapshot.child("password").getValue().toString())) {
                        setWarningText(getString(R.string.login_password_incorrect_warning));
                        return;
                    }
                    Toast.makeText(LoginActivity.this, getString(R.string.login_successful_text), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void navigateToSignUpScreen(View v) {

    }
}