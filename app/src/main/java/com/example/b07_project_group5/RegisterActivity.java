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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    FirebaseDatabase db;
    String username = "";
    String email = "";
    String password = "";
    String accountType = "";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
        String[] userTypes = getResources().getStringArray(R.array.user_types);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, userTypes);
        AutoCompleteTextView loginTypeInput = (AutoCompleteTextView) findViewById(R.id.register_type_input);
        loginTypeInput.setAdapter(arrayAdapter);
        loginTypeInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                accountType = parent.getItemAtPosition(position).toString().toLowerCase();
            }
        });
        intent = new Intent(this, LoginActivity.class);
    }

    public void setWarningText(String warning) {
        TextView warningText = (TextView) findViewById(R.id.register_warning_text);
        warningText.setText(warning);
    }

    public void registerUser(View v) {
        EditText usernameInput = (EditText) findViewById(R.id.register_username_input);
        username = usernameInput.getText().toString();
        EditText emailInput = (EditText) findViewById(R.id.register_email_input);
        email = emailInput.getText().toString();
        EditText passwordInput = (EditText) findViewById(R.id.register_password_input);
        password = passwordInput.getText().toString();
        if (username.equals("") || email.equals("") || password.equals("") || accountType.equals("")) {
            setWarningText(getString(R.string.login_empty_fields_warning));
            return;
        }
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            setWarningText(getString(R.string.register_email_invalid_warning));
            return;
        }
        DatabaseReference ref = db.getReference();
        DatabaseReference query = ref.child("users");
        String uniqueKey = ref.push().getKey();

        query.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = new User(username, email, password, accountType);
                if (!snapshot.exists()) {
                    query.child(uniqueKey).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            usernameInput.setText("");
                            emailInput.setText("");
                            passwordInput.setText("");
                        }
                    });
                } else {
                    boolean userExist = false;
                    for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                        if (accountType.equals(childSnapshot.child("accountType").getValue().toString())) {
                            userExist = true;
                        }
                    }
                    if (userExist) {
                        setWarningText(getString(R.string.register_email_already_exists_warning));
                        return;
                    }
                    query.child(uniqueKey).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            usernameInput.setText("");
                            emailInput.setText("");
                            passwordInput.setText("");
                        }
                    });
                }
                Toast.makeText(RegisterActivity.this, getString(R.string.register_successful_text), Toast.LENGTH_LONG).show();
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    public void navigateToLoginActivity(View v) {
        startActivity(intent);
    }
}