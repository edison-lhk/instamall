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

public class LoginActivity extends AppCompatActivity implements LoginView {

    private LoginPresenter presenter;
    FirebaseDatabase db;
    String accountType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        presenter = new LoginPresenter(this);
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
    @Override
    public String getStringFromResource(int resourceId) {
        return getString(resourceId);
    }

    @Override
    public void setWarningText(String warning) {
        TextView warningText = (TextView) findViewById(R.id.login_warning_text);
        warningText.setText(warning);
    }

    @Override
    public void clearInputFields() {
        EditText emailInput = findViewById(R.id.login_type_input);
        EditText passwordInput = findViewById(R.id.login_password_input);
        emailInput.setText("");
        passwordInput.setText("");
    }

    @Override
    public void showToastMessage() {
        Toast.makeText(LoginActivity.this, getString(R.string.login_successful_text), Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToStoreActivity(String userId, String accountType, String storeId) {
        Intent intent = new Intent(this, StoreActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("accountType", accountType);
        intent.putExtra("storeId", storeId);
        startActivity(intent);
    }

    public void navigateToBrowseStoreActivity(String userId) {
        Intent intent = new Intent(LoginActivity.this, BrowseStoreActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("accountType", accountType);
        startActivity(intent);
    }


    public void loginUser(View v) {
        EditText emailInput = (EditText) findViewById(R.id.login_email_input);
        String email = emailInput.getText().toString();
        EditText passwordInput = (EditText) findViewById(R.id.login_password_input);
        String password = passwordInput.getText().toString();
        AutoCompleteTextView loginTypeInput = findViewById(R.id.login_type_input);
        String accountType = loginTypeInput.getText().toString();
        presenter.loginUser(email, password, accountType);
    }

    public void navigateToRegisterActivity(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}