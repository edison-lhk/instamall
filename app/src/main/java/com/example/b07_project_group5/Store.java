package com.example.b07_project_group5;

public class Store {
    String userId;
    String name;
    String logo;

    public Store(String userId, String name, String logo) {
        this.userId = userId;
        this.name = name;
        this.logo = logo;
    }

    public String getUserId() { return this.userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public String getLogo() { return this.logo; }

    public void setLogo(String logo) { this.logo = logo; }
}
