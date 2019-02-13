package com.example.adeeliftikhar.ticktalk.Model;

import android.content.Context;

public class FriendsDataModel {
    String date;
    public FriendsDataModel(){

    }
    public FriendsDataModel(String date){
        this.date = date;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
