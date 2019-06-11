package com.app.adminpannel.Models;

public class SelectDriverModel {
    private String name;
    private String phone;

    private String vahicletype;
    private String availability;

//    String image;

    public SelectDriverModel() {

    }

    public SelectDriverModel(String name, String phone, String vahicletype, String availability) {
        this.name = name;
        this.phone = phone;
        this.vahicletype = vahicletype;
        this.availability = availability;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVahicleType() {
        return vahicletype;
    }

    public void setVahicleType(String vahicletype) {
        this.vahicletype = vahicletype;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}

