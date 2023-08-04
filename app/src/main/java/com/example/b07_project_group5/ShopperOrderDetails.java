package com.example.b07_project_group5;

public class ShopperOrderDetails {
    private Product product;
    private ProductOrder productOrder;
    private String orderId;

    private String storeName;

    private boolean status;


    public ShopperOrderDetails(Product product, ProductOrder productOrder) {
        this.product = product;
        this.productOrder = productOrder;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductOrder getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(ProductOrder productOrder) {
        this.productOrder = productOrder;
    }
}
