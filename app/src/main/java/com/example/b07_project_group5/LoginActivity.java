package com.example.b07_project_group5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginUser(View v) {
        String email = ((EditText) findViewById(R.id.login_email_input)).getText().toString();
        String password = ((EditText) findViewById(R.id.login_password_input)).getText().toString();


    }
}