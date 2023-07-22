package com.example.b07_project_group5;

public class Product {
    private String name;
    private double price;
    private int product_id;
    private int store_id;
    private int stock;
    private int image; // Resource ID of the product image

    public Product(String name, double price, int product_id, int store_id, int stock, int image) {
        this.name = name;
        this.price = price;
        this.product_id = product_id;
        this.store_id = store_id;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getProductID() {return product_id;}

    public void setProductID(int product_id) {this.product_id = product_id;}

    public int getStoreID() {return store_id;}

    public void setStoreID(int store_id) {this.store_id = store_id;    }

    public int getStock() {return stock;}

    public void setStock(int stock) {this.stock = stock;}
}
