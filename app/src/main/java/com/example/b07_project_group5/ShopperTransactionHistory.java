package com.example.b07_project_group5;

public class ShopperTransactionHistory {
    private String orderID;

    private boolean status;

    private String image;

    public ShopperTransactionHistory(String orderID, String image) {
        this.orderID = orderID;
        this.image = image;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ShopperTransactionHistory() {

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ShopperTransactionHistory(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}
