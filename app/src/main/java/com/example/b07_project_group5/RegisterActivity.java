package com.example.b07_project_group5;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;

public class RegisterActivity extends AppCompatActivity {
    FirebaseDatabase db;
    String username;
    String email;
    String password;
    String accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
    }

    public void warningText(String message) {
        TextView warning_msg = (TextView) findViewById(R.id.reg_warning);
        warning_msg.setText(message);
    }

    public void registerUser(View view) {
        String email = ((EditText)findViewById(R.id.regEmail_input)).getText().toString();
        String password = ((EditText)findViewById(R.id.regPass_input)).getText().toString();
        String username = ((EditText)findViewById(R.id.regUser_input)).getText().toString();
        if (email == "" || password == "" || username == "") {
            warningText("Please fill in all fields");
            return;
        }
        Users User = new Users(username, email, password, "owner");

        DatabaseReference UserRef = db.getReference("users");
        UserRef.child(username).setValue(User)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        EditText usernameEditText = findViewById(R.id.regUser_input);
                        usernameEditText.setText("");
                        EditText emailEditText = findViewById(R.id.regEmail_input);
                        emailEditText.setText("");
                        EditText passwordEditText = findViewById(R.id.regPass_input);
                        passwordEditText.setText("");
                        Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                    }
                });

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}