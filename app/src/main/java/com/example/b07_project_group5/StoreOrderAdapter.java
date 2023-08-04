package com.example.b07_project_group5;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StoreOrderAdapter extends RecyclerView.Adapter<StoreOrderAdapter.StoreOrderViewHolder> {
    private String userId;
    private String accountType;
    private String storeId;
    private List<StoreOrder> storeOrderList;
    private List<StoreOrder> completedOrderList;
    private RecyclerView completedOrderCarousel;
    private FirebaseDatabase db;

    public StoreOrderAdapter(String userId, String accountType, String storeId, List<StoreOrder> storeOrderList, List<StoreOrder> completedOrderList, RecyclerView completedOrderCarousel) {
        this.userId = userId;
        this.accountType = accountType;
        this.storeId = storeId;
        this.storeOrderList = storeOrderList;
        this.completedOrderList = completedOrderList;
        this.completedOrderCarousel = completedOrderCarousel;
        this.db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
    }

    @NonNull
    @Override
    public StoreOrderAdapter.StoreOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_order_card, parent, false);
        return new StoreOrderAdapter.StoreOrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreOrderAdapter.StoreOrderViewHolder holder, int position) {
        StoreOrder storeOrder = storeOrderList.get(position);
        holder.orderIdTextView.setText(storeOrder.getOrderId());
        holder.customerTextView.setText(storeOrder.getCustomer());
        if (storeOrder.getStatus()) { holder.orderStatusComplete.setVisibility(View.VISIBLE); }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), StoreOrderDetailsActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("accountType", accountType);
                intent.putExtra("storeId", storeId);
                intent.putExtra("orderId", storeOrder.getOrderId());
                intent.putExtra("customer", storeOrder.getCustomer());
                intent.putExtra("status", storeOrder.getStatus());
                v.getContext().startActivity(intent);
            }
        });

        holder.orderStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!storeOrder.getStatus()) {
                    DatabaseReference ref = db.getReference();
                    DatabaseReference query = ref.child("orders");
                    query.child(storeOrder.getOrderId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.child("status").getRef().setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    DatabaseReference query2 = ref.child("transactions");
                                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                                                GenericTypeIndicator<List<String>> orderListClass = new GenericTypeIndicator<List<String>>() {};
                                                List<String> orderIdList = transactionSnapshot.child("orderList").getValue(orderListClass);
                                                if (orderIdList.contains(storeOrder.getOrderId())) {
                                                    String transactionId = transactionSnapshot.getKey();
                                                    List<Order> orderList = new ArrayList<>();
                                                    for (String id : orderIdList) {
                                                        query.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                orderList.add(snapshot.getValue(Order.class));
                                                                if (orderList.size() == orderIdList.size()) {
                                                                    boolean completed = true;
                                                                    for (Order order : orderList) {
                                                                        if (!order.getStatus()) {
                                                                            completed = false;
                                                                        }
                                                                    }
                                                                    Toast.makeText(v.getContext(), "Order " + storeOrder.getOrderId() + " " + v.getContext().getString(R.string.store_order_marked_as_completed_text), Toast.LENGTH_LONG).show();
                                                                    storeOrderList.remove(storeOrder);
                                                                    storeOrder.setStatus(true);
                                                                    completedOrderList.add(storeOrder);
                                                                    notifyDataSetChanged();
                                                                    completedOrderCarousel.getAdapter().notifyDataSetChanged();
                                                                    if (completed) {
                                                                        query2.child(transactionId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                snapshot.child("status").getRef().setValue(true);
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                                                                        });
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {}
                                                        });
                                                    }
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
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() { return storeOrderList.size(); }

    public static class StoreOrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView;
        TextView customerTextView;
        ImageButton orderStatusBtn;
        ImageButton orderStatusComplete;

        public StoreOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderIdValueText);
            customerTextView = itemView.findViewById(R.id.customerValueText);
            orderStatusBtn = itemView.findViewById(R.id.orderStatusBtn);
            orderStatusComplete = itemView.findViewById(R.id.orderStatusComplete);
        }
    }
}
