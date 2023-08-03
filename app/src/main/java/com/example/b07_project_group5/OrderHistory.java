package com.example.b07_project_group5;

public class OrderHistory {
    private String orderID;

    private String image;

    public OrderHistory(String orderID, String image) {
        this.orderID = orderID;
        this.image = image;
    }

    public OrderHistory() {

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public OrderHistory(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}
