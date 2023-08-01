package com.example.b07_project_group5;

public class order {
    private String productID;
    private String amount;

    public order(){

    }

    public order(String amount, String productId, Boolean status, String storeId) {
    }

    public String getProductID(){
        return productID;
    }
    public void setProductID(String product){
        this.productID = productID;
    }
    public String getAmount(){
        return amount;
    }
    public void setAmount(String amount){
        this.amount = amount;
    }
}
