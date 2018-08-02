package com.example.user.merchantapp;

import java.util.ArrayList;

public class Product {
    String name,desc,image,id,userid,category;
    int price,quantity,noOfRating;
    float rating;
    ArrayList<Review> review;
    public Product() {
    }

    public Product(String name, String desc, String image, String id, String userid, String category, int price, int quantity, int noOfRating, float rating, ArrayList<Review> review) {
        this.name = name;
        this.desc = desc;
        this.image = image;
        this.id = id;
        this.userid = userid;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.noOfRating = noOfRating;
        this.rating = rating;
        this.review = review;
    }

    public int getNoOfRating() {
        return noOfRating;
    }

    public void setNoOfRating(int noOfRating) {
        this.noOfRating = noOfRating;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public ArrayList<Review> getReview() {
        return review;
    }

    public void setReview(ArrayList<Review> review) {
        this.review = review;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
