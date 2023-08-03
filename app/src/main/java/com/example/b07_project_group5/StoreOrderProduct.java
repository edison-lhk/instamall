package com.example.b07_project_group5;

public class StoreOrderProduct {
    private ProductOrder productOrder;
    private Product product;

    public StoreOrderProduct() {}

    public StoreOrderProduct(ProductOrder productOrder, Product product) {
        this.productOrder = productOrder;
        this.product = product;
    }

    public ProductOrder getProductOrder() { return this.productOrder; }

    public void setProductOrder(ProductOrder productOrder) { this.productOrder = productOrder; }

    public Product getProduct() { return this.product; }

    public void setProduct(Product product) { this.product = product; }
}
