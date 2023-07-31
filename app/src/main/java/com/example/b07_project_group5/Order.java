package com.example.b07_project_group5;

import java.util.ArrayList;

public class Order {
    private String storeId;
    private ArrayList<ProductOrder> productList;
    private boolean status;

    public Order(String storeId) {
        this.storeId = storeId;
        productList = new ArrayList<ProductOrder>();
    }

    public Order() {}

    public void addProduct(String productId, int amount) {
        ProductOrder product = new ProductOrder(productId, amount);
        productList.add(product);
    }

    public ProductOrder getProduct(String productId) {
        for (ProductOrder product : productList) {
            if (productId.equals(product.getProductId())) {
                return product;
            }
        }
        return null;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public ArrayList<ProductOrder> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ProductOrder> productList) {
        this.productList = productList;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus() {
        this.status = status;
    }
}
