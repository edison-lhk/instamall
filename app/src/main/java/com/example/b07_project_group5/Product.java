package com.example.b07_project_group5;

public class Product {
    private String name;
    private double price;
    private String productId;
    private String storeId;
    private int stock;
    private String image; // Resource ID of the product image

    public Product(String name, double price, String productId, String storeId, int stock, String image) {
        this.name = name;
        this.price = price;
        this.productId = productId;
        this.storeId = storeId;
        this.stock = stock;
        this.image = image;
    }

    // Getters and setters for the product attributes
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProductID() {return productId;}

    public void setProductID(String productId) { this.productId = productId; }

    public String getStoreID() {return storeId;}

    public void setStoreID(String storeId) { this.storeId = storeId; }

    public int getStock() { return stock; }

    public void setStock(int stock) {this.stock = stock;}
}
