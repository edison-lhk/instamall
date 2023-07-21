package com.example.b07_project_group5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    FirebaseDatabase db;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
        type = "";
        String[] user_types = getResources().getStringArray(R.array.user_types);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, user_types);
        AutoCompleteTextView login_type_input = (AutoCompleteTextView) findViewById(R.id.register_type_input);
        login_type_input.setAdapter(arrayAdapter);
        login_type_input.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type = parent.getItemAtPosition(position).toString().toLowerCase();
            }
        });
    }

    public void setWarningText(String warning) {
        TextView warning_text = (TextView) findViewById(R.id.register_warning_text);
        warning_text.setText(warning);
    }

    public void registerUser(View v) {

    }

    public void navigateToLoginActivity(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}