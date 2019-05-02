package com.example.adeeliftikhar.ambulancetracker.Models;

import android.widget.TextView;

public class HelperHistoryModel {
    private String name;
    private String number;
    private String date_time;
    private String ambulance;
    private String current_user_id;
    private String imageURI;

    public HelperHistoryModel() {

    }

    public HelperHistoryModel(String name, String number, String date_time, String ambulance, String current_user_id, String imageURI) {
        this.name = name;
        this.number = number;
        this.date_time = date_time;
        this.ambulance = ambulance;
        this.current_user_id = current_user_id;
        this.imageURI = imageURI;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getAmbulance() {
        return ambulance;
    }

    public void setAmbulance(String ambulance) {
        this.ambulance = ambulance;
    }

    public String getCurrent_user_id() {
        return current_user_id;
    }

    public void setCurrent_user_id(String current_user_id) {
        this.current_user_id = current_user_id;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }
}
