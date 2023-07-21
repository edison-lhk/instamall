package com.example.b07_project_group5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    FirebaseDatabase db;
    String accountType;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
        accountType = "";
        String[] user_types = getResources().getStringArray(R.array.user_types);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, user_types);
        AutoCompleteTextView login_type_input = (AutoCompleteTextView) findViewById(R.id.register_type_input);
        login_type_input.setAdapter(arrayAdapter);
        login_type_input.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                accountType = parent.getItemAtPosition(position).toString().toLowerCase();
            }
        });
        intent = new Intent(this, LoginActivity.class);
    }

    public void setWarningText(String warning) {
        TextView warning_text = (TextView) findViewById(R.id.register_warning_text);
        warning_text.setText(warning);
    }

    public void registerUser(View v) {
        EditText username_input = (EditText) findViewById(R.id.register_username_input);
        String username = username_input.getText().toString();
        EditText email_input = (EditText) findViewById(R.id.register_email_input);
        String email = email_input.getText().toString();
        EditText password_input = (EditText) findViewById(R.id.register_password_input);
        String password = password_input.getText().toString();
        if (username.equals("") || email.equals("") || password.equals("") || accountType.equals("")) {
            setWarningText(getString(R.string.login_empty_fields_warning));
            return;
        }
        DatabaseReference ref = db.getReference();
        String uniqueKey = ref.push().getKey();
        DatabaseReference query = ref.child("users");

        query.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = new User(username, email, password, accountType);
                if (!snapshot.exists()) {
                    query.child(uniqueKey).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            username_input.setText("");
                            email_input.setText("");
                            password_input.setText("");
                        }
                    });
                } else {
                    boolean user_exist = false;
                    for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                        if (accountType.equals(childSnapshot.child("type").getValue().toString())) {
                            user_exist = true;
                        }
                    }
                    if (user_exist) {
                        setWarningText(getString(R.string.register_email_already_exists_warning));
                        return;
                    }
                    query.child(username).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            username_input.setText("");
                            email_input.setText("");
                            password_input.setText("");
                        }
                    });
                    Toast.makeText(RegisterActivity.this, getString(R.string.register_successful_text), Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void navigateToLoginActivity(View v) {
        startActivity(intent);
    }
}