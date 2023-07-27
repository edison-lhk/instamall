package com.example.b07_project_group5;

import android.provider.ContactsContract;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginModel {
    private FirebaseDatabase db;

    public LoginModel() {
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
    }

    public void loginUser(String email, String password, String accountType, LoginCallback callback) {
        DatabaseReference ref = db.getReference();
        DatabaseReference query = ref.child("users");
        query.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    callback.onLoginFailure("User not found");
                } else {
                    boolean userExist = false;
                    for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                        if (accountType.equals(childSnapshot.child("accountType").getValue().toString())) {
                            if (!password.equals(childSnapshot.child("password").getValue().toString())) {
                                callback.onLoginFailure("Incorrect Password");
                                return;
                            }
                            userExist = true;
                            String userId = childSnapshot.getKey();
                            String username = childSnapshot.child("username").getValue().toString();
                            final String[] storeId = {""};
                            if (accountType.equals("owner")) {
                                DatabaseReference query2 = ref.child("stores");
                                query2.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (!snapshot.exists()) {
                                            Store store = new Store(userId, username.substring(0, 4) + "'s Store", "https://firebasestorage.googleapis.com/v0/b/testing-7a8a5.appspot.com/o/StoreLogo%2Fstore_logo.png?alt=media&token=2660325b-c4cd-4659-bf6b-60239d06f84b");
                                            String uniqueKey = ref.push().getKey();
                                            query2.child(uniqueKey).setValue(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    storeId[0] = uniqueKey;
                                                    callback.onLoginSuccess(userId, accountType, storeId[0]);
                                                }
                                            });
                                        } else {
                                            for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                                                Store store = new Store(userId, childSnapshot.child("name").getValue().toString(), childSnapshot.child("logo").getValue().toString());
                                                storeId[0] = childSnapshot.getKey();
                                            }
                                            callback.onLoginSuccess(userId, accountType, storeId[0]);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        callback.onLoginFailure("Database error");
                                    }
                                });
                            } else {
                                callback.onLoginSuccess(userId, accountType, null);

                            }
                        }
                    }
                    if (!userExist) {
                        callback.onLoginFailure("Account type is invalid");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onLoginFailure("Database error");
            }
        });
    }

    public interface LoginCallback {
        void onLoginSuccess(String userId, String accountType, String storeId);
        void onLoginFailure(String error);
    }

}

