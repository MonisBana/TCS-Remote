package com.example.user.snapkart;

import java.util.ArrayList;

public class Product {
    String name,desc,image,id,userid,category;
    long price,quantity,noOfRating;
    long rating;
    ArrayList<Review> review;
    public Product() {
    }
    public Product(String name, String desc, String image, String id, String userid, String category, long price, long quantity, long noOfRating, long rating) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getNoOfRating() {
        return noOfRating;
    }

    public void setNoOfRating(long noOfRating) {
        this.noOfRating = noOfRating;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public ArrayList<Review> getReview() {
        return review;
    }

    public void setReview(ArrayList<Review> review) {
        this.review = review;
    }
}
