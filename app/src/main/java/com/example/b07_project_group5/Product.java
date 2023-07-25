package com.example.b07_project_group5;

public class Product {
    private String name;
    private double price;
    private String storeId;
    private int stock;
    private String image; // Resource ID of the product image
    private String description;

    public Product() {
    }

    public Product(String name, double price, String storeId, int stock, String image, String description) {
        this.name = name;
        this.price = price;
        this.storeId = storeId;
        this.stock = stock;
        this.image = image;
        this.description = description;
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

    public String getStoreId() {return storeId;}

    public void setStoreId(String storeId) { this.storeId = storeId; }

    public int getStock() { return stock; }

    public void setStock(int stock) {this.stock = stock;}

    public void setDescription(String description){this.description = description;}

    public String getDescription(){return description;};
}
