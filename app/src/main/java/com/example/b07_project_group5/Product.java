package com.example.b07_project_group5;

public class Product {
    private String storeId;
    private String name;
    private double price;
    private int stock;
    private String image; // Resource ID of the product image
    private String description;

    public Product() {
    }

    public Product(String storeId, String name, double price, int stock, String image, String description) {
        this.storeId = storeId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.image = image;
        this.description = description;
    }

    public String getStoreId() {return storeId;}

    public void setStoreId(String storeId) { this.storeId = storeId; }

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

    public int getStock() { return stock; }

    public void setStock(int stock) {this.stock = stock;}

    public void setDescription(String description){this.description = description;}

    public String getDescription(){return description;};
}
