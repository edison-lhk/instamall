package com.example.b07_project_group5;

public class order {
    private String productId;
    private String amount;

    public order(String amount, String productId){
        this.amount = amount;
        this.productId = productId;
    }


    public String getProductId(){
        return productId;
    }
    public String getAmount(){
        return amount;
    }

}
