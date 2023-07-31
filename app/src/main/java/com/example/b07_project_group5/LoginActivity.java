package com.example.b07_project_group5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        presenter = new LoginPresenter(this, new LoginModel());
        displayAccountTypeDropdown();
    }

    @Override
    public String getStringFromResource(int resourceId) {
        return getString(resourceId);
    }

    @Override
    public void displayAccountTypeDropdown() {
        String[] userTypes = getResources().getStringArray(R.array.user_types);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, userTypes);
        AutoCompleteTextView loginTypeInput = findViewById(R.id.login_type_input);
        loginTypeInput.setAdapter(arrayAdapter);
    }

    @Override
    public String getEmailInput() {
        EditText emailInput = findViewById(R.id.login_email_input);
        return emailInput.getText().toString();
    }

    @Override
    public String getPasswordInput() {
        EditText passwordInput = findViewById(R.id.login_password_input);
        return passwordInput.getText().toString();
    }

    @Override
    public String getAccountTypeInput() {
        AutoCompleteTextView loginTypeInput = findViewById(R.id.login_type_input);
        return loginTypeInput.getText().toString();
    }

    @Override
    public void setWarningText(String warning) {
        TextView warningText = findViewById(R.id.login_warning_text);
        warningText.setText(warning);
    }

    @Override
    public void clearInputFields() {
        EditText emailInput = findViewById(R.id.login_email_input);
        EditText passwordInput = findViewById(R.id.login_password_input);
        EditText accountTypeInput = findViewById(R.id.login_type_input);
        emailInput.setText("");
        passwordInput.setText("");
        accountTypeInput.setText("");
    }

    @Override
    public void showToastMessage() {
        Toast.makeText(LoginActivity.this, getStringFromResource(R.string.login_successful_text), Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToStoreActivity(String userId, String storeId) {
        Intent intent = new Intent(this, StoreActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("accountType", "owner");
        intent.putExtra("storeId", storeId);
        startActivity(intent);
    }

    @Override
    public void navigateToBrowseStoreActivity(String userId) {
        Intent intent = new Intent(LoginActivity.this, BrowseStoreActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("accountType", "shopper");
        startActivity(intent);
    }

    @Override
    public void navigateToRegisterActivity(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void loginUser(View v) {
        presenter.loginUser(getEmailInput(), getPasswordInput(), getAccountTypeInput());
    }
}