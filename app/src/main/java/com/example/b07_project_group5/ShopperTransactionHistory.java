package com.example.b07_project_group5;

public class ShopperTransactionHistory {
    private String orderID;

    private boolean status;

    public ShopperTransactionHistory(String orderID) {
        this.orderID = orderID;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ShopperTransactionHistory() {

    }
    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}
