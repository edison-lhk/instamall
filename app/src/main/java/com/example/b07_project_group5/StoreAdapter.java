package com.example.b07_project_group5;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {
    private String userId;
    private String accountType;
    private List<Store> storeList;
    private FirebaseDatabase db;

    public StoreAdapter(String userId, String accountType, List<Store> storeList) {
        this.userId = userId;
        this.accountType = accountType;
        this.storeList = storeList;
        this.db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
    }

    @NonNull
    @Override
    public StoreAdapter.StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_card, parent, false);
        return new StoreAdapter.StoreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreAdapter.StoreViewHolder holder, int position) {
        Store store = storeList.get(position);
        holder.storeNameTextView.setText(store.name);
        Glide.with(holder.itemView.getContext())
                .load(store.logo)
                .into(holder.storeLogoImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = db.getReference();
                ref.child("stores").orderByChild("userId").equalTo(store.userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot storeSnapshot: snapshot.getChildren()) {
                                String storeId = storeSnapshot.getKey();
                                Intent intent = new Intent(v.getContext(), StoreActivity.class);
                                intent.putExtra("previousActivity", "BrowseStoreActivity");
                                intent.putExtra("userId", userId);
                                intent.putExtra("accountType", accountType);
                                intent.putExtra("storeId", storeId);
                                v.getContext().startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        });
    }

    @Override
    public int getItemCount() { return storeList.size(); }

    public static class StoreViewHolder extends RecyclerView.ViewHolder {
        ImageView storeLogoImageView;
        TextView storeNameTextView;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            storeLogoImageView = itemView.findViewById(R.id.browseStoreLogo);
            storeNameTextView = itemView.findViewById(R.id.browseStoreName);
        }
    }
}
