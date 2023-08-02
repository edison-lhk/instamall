package com.example.b07_project_group5;

public class StoreOrder {
    private String orderId;
    private String customer;
    private boolean status;

    public StoreOrder() {}

    public StoreOrder(String orderId, String customer, boolean status) {
        this.orderId = orderId;
        this.customer = customer;
        this.status = status;
    }

    public String getOrderId() { return this.orderId; }

    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomer() { return this.customer; }

    public void setCustomer(String customer) { this.customer = customer; }

    public boolean getStatus() { return this.status; }

    public void setStatus(boolean status) { this.status = status; }
}
