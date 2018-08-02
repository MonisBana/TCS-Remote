package com.example.user.merchantapp;

public class Review {
    private String username,review;
    public Review() {
    }

    public Review(String username, String review) {
        this.username = username;
        this.review = review;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
