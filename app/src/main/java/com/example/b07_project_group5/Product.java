package com.example.b07_project_group5;

public class Product {
    private String name;
    private double price;
    private int image; // Resource ID of the product image

    public Product(String name, double price, int image) {
        this.name = name;
        this.price = price;
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
}
