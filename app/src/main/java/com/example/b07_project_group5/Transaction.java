package com.example.b07_project_group5;

import java.util.ArrayList;

public class Transaction {
    private String userId;
    private ArrayList<String> orderList;
    private boolean finalized;
    private boolean status;

    public Transaction(String userId) {
        this.userId = userId;
        orderList = new ArrayList<String>();
        finalized = false;
        status = false;
    }

    public Transaction() {}

    public void initOrderList() {
        orderList = new ArrayList<String>();
    }

    public void addOrder(String orderId) {
        orderList.add(orderId);
    }

    public void removeOrder(String orderId) {
        orderList.remove(orderId);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<String> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<String> orderList) {
        this.orderList = orderList;
    }

    public boolean getFinalized() {
        return finalized;
    }

    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
