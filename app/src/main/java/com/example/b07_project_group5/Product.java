package com.example.b07_project_group5;

public class Product {
    private String name;
    private double price;
    private int product_id;
    private String store_id;
    private int stock;
    private String image; // Resource ID of the product image

    public Product(String name, double price, int product_id, String store_id, int stock, String image) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getProductID() {return product_id;}

    public void setProductID(int product_id) {this.product_id = product_id;}

    public String getStoreID() {return store_id;}

    public void setStoreID(String store_id) {this.store_id = store_id;    }

    public int getStock() {return stock;}

    public void setStock(int stock) {this.stock = stock;}
}
