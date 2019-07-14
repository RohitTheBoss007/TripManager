package com.example.android.tripmanager;

import java.util.List;

public class Trip {
    String tripName;
    String creator;
    String TripKey;

    public String getTripKey() {
        return TripKey;
    }

    public void setTripKey(String tripKey) {
        TripKey = tripKey;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Trip(String tripName, String creator) {
        this.tripName = tripName;
        this.creator = creator;
    }

    public Trip() {
    }
}