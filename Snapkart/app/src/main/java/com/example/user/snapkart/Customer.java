package com.example.user.snapkart;

import java.util.ArrayList;

public class Customer {
    private String name,mobile,email,customerid,address;
    private ArrayList<Product> cart,wishlist;

    public Customer() {
    }

    public Customer(String name, String mobile, String email, String customerid, String address) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.customerid = customerid;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public ArrayList<Product> getCart() {
        return cart;
    }

    public void setCart(ArrayList<Product> cart) {
        this.cart = cart;
    }

    public ArrayList<Product> getWishlist() {
        return wishlist;
    }

    public void setWishlist(ArrayList<Product> wishlist) {
        this.wishlist = wishlist;
    }
}
