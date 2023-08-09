package com.example.b07_project_group5;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginModel implements LoginContract.Model {
    private FirebaseDatabase db;

    public LoginModel() {
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
    }

    @Override
    public void findUserWithEmail(String email, LoginContract.Model.findUserWithEmailCallback callback) {
        DatabaseReference ref = db.getReference();
        DatabaseReference query = ref.child("users");
        query.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    callback.onSuccess();
                    return;
                }
                callback.onFailure();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public void getUserIdByEmailAndAccountType(String email, String accountType, LoginContract.Model.getUserIdByEmailAndAccountTypeCallback callback) {
        DatabaseReference ref = db.getReference();
        DatabaseReference query = ref.child("users");
        query.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                    if (accountType.equals(userSnapshot.child("accountType").getValue().toString())) {
                        callback.onSuccess(userSnapshot.getKey());
                        return;
                    }
                }
                callback.onFailure();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public void checkUserPasswordIsCorrect(String userId, String password, LoginContract.Model.checkUserPasswordIsCorrectCallback callback) {
        DatabaseReference ref = db.getReference();
        DatabaseReference query = ref.child("users");
        query.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (password.equals(snapshot.child("password").getValue().toString())) {
                    callback.onSuccess();
                    return;
                }
                callback.onFailure();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public void getUsernameById(String userId, LoginContract.Model.getUsernameByIdCallback callback) {
        DatabaseReference ref = db.getReference();
        DatabaseReference query = ref.child("users");
        query.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onSuccess(snapshot.child("username").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public void createStoreForOwner(String userId, String username, LoginContract.Model.createStoreForOwnerCallback callback) {
        DatabaseReference ref = db.getReference();
        DatabaseReference query = ref.child("stores");
        query.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    String storeName = username.length() >= 4 ? username.substring(0, 4) + "'s Store" : username + "'s Store";
                    Store store = new Store(userId, storeName, "https://firebasestorage.googleapis.com/v0/b/testing-7a8a5.appspot.com/o/Stores%2Fstore_logo.png?alt=media&token=eb88af2b-c455-4e82-9ab8-88ec107bd2cc");
                    String uniqueKey = ref.push().getKey();
                    query.child(uniqueKey).setValue(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            callback.onSuccess(uniqueKey);
                        }
                    });
                } else {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        callback.onSuccess(childSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}