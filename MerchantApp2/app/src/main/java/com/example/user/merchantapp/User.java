package com.example.user.merchantapp;

import java.util.ArrayList;

public class User {
    private String name,mobile,email,userId;
    private ArrayList<Product> product;

    public User() {
    }

    public User(String name, String mobile, String email, String userId) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Product> getProduct() {
        return product;
    }

    public void setProduct(ArrayList<Product> product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
