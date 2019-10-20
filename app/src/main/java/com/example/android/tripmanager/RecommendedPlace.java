package com.example.android.tripmanager;

public class RecommendedPlace {
    String tripName,category,description,dov,addedBy;

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDov() {
        return dov;
    }

    public void setDov(String dov) {
        this.dov = dov;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public RecommendedPlace(String tripName, String category, String description, String dov, String addedBy) {
        this.tripName = tripName;
        this.category = category;
        this.description = description;
        this.dov = dov;
        this.addedBy = addedBy;
    }

    public RecommendedPlace() {
    }
}
