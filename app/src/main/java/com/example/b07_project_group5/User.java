package com.example.b07_project_group5;

public class User {
    String username;
    String email;
    String password;
    String accountType;

    public User() {
    }

    public User(String username, String email, String password, String accountType) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
