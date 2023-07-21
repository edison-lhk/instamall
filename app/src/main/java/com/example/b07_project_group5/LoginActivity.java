package com.example.b07_project_group5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
        type = "";
        String[] user_types = getResources().getStringArray(R.array.user_types);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, user_types);
        AutoCompleteTextView login_type_input = (AutoCompleteTextView) findViewById(R.id.login_type_input);
        login_type_input.setAdapter(arrayAdapter);
        login_type_input.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type = parent.getItemAtPosition(position).toString().toLowerCase();
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
        if (email.equals("") || password.equals("") || type.equals("")) {
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
                        if (type.equals(childSnapshot.child("type").getValue().toString())) {
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