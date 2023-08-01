package com.example.b07_project_group5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private List<order> orderList;
    Context context;

    public RecyclerAdapter(List<order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.userentry, parent, false);
        return new RecyclerViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        order currentOrder = orderList.get(position);
        holder.productTextView.setText(currentOrder.getProductId());
        holder.amountTextView.setText(currentOrder.getAmount());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // You don't need to override getItemCount() when using FirebaseRecyclerAdapter.
    // The count will be automatically handled by FirebaseRecyclerAdapter.

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView productTextView;
        public TextView amountTextView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            productTextView = itemView.findViewById(R.id.ordername);
            amountTextView = itemView.findViewById(R.id.orderamount);
        }
    }
}
