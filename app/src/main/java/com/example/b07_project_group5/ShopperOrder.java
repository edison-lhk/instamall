package com.example.b07_project_group5;

public class ShopperOrder {
    private String transactionId;

    private boolean status;

    public ShopperOrder() {}

    public ShopperOrder(String transactionID, boolean status) {
        this.transactionId = transactionID;
        this.status = status;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
