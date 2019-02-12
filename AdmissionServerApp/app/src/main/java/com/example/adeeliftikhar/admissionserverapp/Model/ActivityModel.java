package com.example.adeeliftikhar.admissionserverapp.Model;

//    Model is used to set and get data from Firebase...
public class ActivityModel {
//    These  variables names are the key at which value will store in Firebase...
    String name;
    String description;
    String image;

//    Empty Constructor is necessary to write...
    public ActivityModel() {

    }

    public ActivityModel(String name, String description, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
