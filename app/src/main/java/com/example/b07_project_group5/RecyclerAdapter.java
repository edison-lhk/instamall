package com.example.b07_project_group5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.b07_project_group5.order;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.OrderViewHolder> {

    private List<order> orderList;

    public RecyclerAdapter(List<order> orderList) {
        this.orderList = orderList;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView productTextView;
        public TextView amountTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            productTextView = itemView.findViewById(R.id.ordername);
            amountTextView = itemView.findViewById(R.id.orderamount);
        }
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.userentry, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        order order = orderList.get(position);
        holder.productTextView.setText(order.getProduct());
        holder.amountTextView.setText(order.getAmount());
    }

    @Override
    public int getItemCount() {

        return orderList.size();
    }
}
