package com.example.android.tripmanager;

public class Upload {
    String tripName,description,imageUrl,time,UploadedBy;

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

    public Upload(String tripName, String description, String imageUrl, String time, String uploadedBy) {
        this.tripName = tripName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.time = time;
        UploadedBy = uploadedBy;
    }

    public Upload() {
    }
}
