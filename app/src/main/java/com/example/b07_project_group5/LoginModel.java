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
                    callback.isUserExists(true);
                    return;
                }
                callback.isUserExists(false);
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
                        callback.returnUserId(userSnapshot.getKey());
                        return;
                    }
                }
                callback.returnUserId(null);
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
                    callback.isPasswordCorrect(true);
                    return;
                }
                callback.isPasswordCorrect(false);
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
                callback.returnUsername(snapshot.child("username").getValue().toString());
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
                    Store store = new Store(userId, username.substring(0, 4) + "'s Store", "https://firebasestorage.googleapis.com/v0/b/testing-7a8a5.appspot.com/o/StoreLogo%2Fstore_logo.png?alt=media&token=2660325b-c4cd-4659-bf6b-60239d06f84b");
                    String uniqueKey = ref.push().getKey();
                    query.child(uniqueKey).setValue(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            callback.returnStoreId(uniqueKey);
                        }
                    });
                } else {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        callback.returnStoreId(childSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}

