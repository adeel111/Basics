package com.example.muhammadashfaq.eatit.Model;

public class AddResturantModel {

    String name,longitude,latitude,status;


    public AddResturantModel() {

    }

    public AddResturantModel(String name, String longitude, String latitude, String status) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
