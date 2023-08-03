package com.example.b07_project_group5;

public class ShopperOrderHistory {
    private Product product;
    private int amount;
    private String orderId;
    private String productId;
    private String userId;
    private String storeName;

    public ShopperOrderHistory(Product product, int amount, String orderId, String productId, String userId, String storeName) {
        this.product = product;
        this.amount = amount;
        this.orderId = orderId;
        this.productId = productId;
        this.userId = userId;
        this.storeName = storeName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
