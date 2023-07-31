package com.example.b07_project_group5;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.CartViewHolder> {
    private String userId;
    private String accountType;
    private ArrayList<CartProduct> shoppingCart;
    private TextView totalCostTextView;
    private Button checkoutBtn;
    private FirebaseDatabase db;
    private CartTotalCostView totalCostView;

    public CartProductAdapter(String userId, ArrayList<CartProduct> shoppingCart, TextView totalCostTextView, Button checkoutBtn) {
        this.userId = userId;
        this.accountType = "shopper";
        this.shoppingCart = shoppingCart;
        this.totalCostTextView = totalCostTextView;
        this.checkoutBtn = checkoutBtn;
        db = FirebaseDatabase.getInstance("https://testing-7a8a5-default-rtdb.firebaseio.com/");
        totalCostView = new CartTotalCostView();
    }

    // Override onCreateViewHolder to inflate the product item layout
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_product_card, parent, false);
        return new CartViewHolder(itemView);
    }

    // Override onBindViewHolder to bind product data to the views
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartProduct cartProduct = shoppingCart.get(position);
        Double price = cartProduct.getProduct().getPrice();
        int amount = cartProduct.getAmount();
        Double totalPrice = price * cartProduct.getAmount();

        holder.productNameTextView.setText(cartProduct.getProduct().getName());
        holder.productPriceTextView.setText("$" + String.format("%.2f", price));
        holder.amountTextView.setText(String.valueOf(amount));
        holder.productTotalPriceTextView.setText("$" + String.format("%.2f", totalPrice));
        holder.storeNameTextView.setText(cartProduct.getStoreName());

        // Load the image using Glide with the image URL
        Glide.with(holder.itemView.getContext())
                .load(cartProduct.getProduct().getImage())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.productImageList);

        // Navigate to product details page when user clicks on cart products
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProductDetailsActivity.class);
                intent.putExtra("previousActivity", "CartActivity");
                intent.putExtra("userId", userId);
                intent.putExtra("accountType", accountType);
                intent.putExtra("productId", cartProduct.getProductId());
                v.getContext().startActivity(intent);
            }
        });

        // Set an OnClickListener to the item view
        holder.removeProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = db.getReference();
                shoppingCart.remove(cartProduct);
                totalCostView.displayTotalCost(shoppingCart);

                ref.child("orders").child(cartProduct.getOrderId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Order order = snapshot.getValue(Order.class);
                        for (ProductOrder product : order.getProductList()) {
                            if (cartProduct.getProductId().equals(product.getProductId())) {
                                order.getProductList().remove(product);
                                snapshot.getRef().setValue(order);
                                notifyDataSetChanged();
                                break;
                            }
                        }
                        if (order.getProductList().size() > 0) {
                            snapshot.getRef().setValue(order);
                            notifyDataSetChanged();
                            Toast.makeText(view.getContext(), cartProduct.getAmount() + " " + cartProduct.getProduct().getName() + " " + view.getContext().getString(R.string.remove_cart_product_successful_text), Toast.LENGTH_LONG).show();
                        } else {
                            snapshot.getRef().removeValue();
                            removeOrderFromCart(cartProduct.getOrderId(), cartProduct.getUserId());
                        }
                        if (shoppingCart.size() == 0) {
                            removeTransaction(userId);
                            notifyDataSetChanged();
                            totalCostTextView.setText(view.getContext().getResources().getString(R.string.total_cost_placeholder_text));
                            checkoutBtn.setEnabled(false);
                            Toast.makeText(view.getContext(), view.getContext().getString(R.string.clear_cart_successful_text), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingCart.size();
    }

    public void removeOrderFromCart(String orderId, String userId) {
        DatabaseReference ref = db.getReference();
        ref.child("transactions").orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                    if (!Boolean.parseBoolean(transactionSnapshot.child("finalized").getValue().toString())) {
                        Transaction cart = transactionSnapshot.getValue(Transaction.class);
                        cart.getOrderList().remove(orderId);
                        transactionSnapshot.getRef().setValue(cart);
                        notifyDataSetChanged();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void removeTransaction(String userId) {
        DatabaseReference ref = db.getReference();
        ref.child("transactions").orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                    if (!Boolean.parseBoolean(transactionSnapshot.child("finalized").getValue().toString())) {
                        transactionSnapshot.getRef().removeValue();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView;
        TextView productPriceTextView;
        TextView amountTextView;
        TextView productTotalPriceTextView;
        TextView storeNameTextView;
        ImageView productImageList;
        ImageButton removeProductBtn;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageList = (ImageView) itemView.findViewById(R.id.orderStoreImageList);
            productNameTextView = (TextView) itemView.findViewById(R.id.cartProductName);
            productPriceTextView = (TextView) itemView.findViewById(R.id.orderOrderID);
            amountTextView = (TextView) itemView.findViewById(R.id.cartProductAmount);
            productTotalPriceTextView = (TextView) itemView.findViewById(R.id.cartProductTotalPrice);
            storeNameTextView = (TextView) itemView.findViewById(R.id.orderStoreName);
            removeProductBtn = (ImageButton) itemView.findViewById(R.id.removeProductBtn);
        }
    }
}
