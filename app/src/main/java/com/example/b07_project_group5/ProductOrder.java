package com.example.b07_project_group5;

public class ProductOrder {
    private String productId;
    private int amount;

    public ProductOrder(String productId, int amount) {
        this.productId = productId;
        this.amount = amount;
    }

    public ProductOrder() {}

    public String getProductId() {
        return productId;
    }

    public int getAmount() {
        return amount;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
