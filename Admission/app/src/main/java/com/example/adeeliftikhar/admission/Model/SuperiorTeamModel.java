package com.example.adeeliftikhar.admission.Model;

public class SuperiorTeamModel {
    String name;
    String designation;
    String message;
    String image;

    public SuperiorTeamModel() {

    }

    public SuperiorTeamModel(String name, String designation, String message, String image) {
        this.name = name;
        this.designation = designation;
        this.message = message;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
