package com.example.android.tripmanager;

public class Expense {
    String tripName,description,cost,imageUrl,time,UploadedBy;

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUploadedBy() {
        return UploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        UploadedBy = uploadedBy;
    }

    public Expense(String tripName, String description, String cost, String imageUrl, String time, String uploadedBy) {
        this.tripName = tripName;
        this.description = description;
        this.cost = cost;
        this.imageUrl = imageUrl;
        this.time = time;
        UploadedBy = uploadedBy;
    }

    public Expense() {
    }
}
