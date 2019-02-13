package com.example.adeeliftikhar.ticktalk.Model;

import android.media.Image;

public class MessagesDataModel {
    private String message, name;
    private String image;
    private String from;
    private String to;
    private long timestamp;

    public MessagesDataModel() {
//        No Argument Constructor...
    }

    public MessagesDataModel(String message, String name, String image, long timestamp, String from, String to) {
        this.message = message;
        this.image = image;
        this.name = name;
        this.from = from;
        this.to = to;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String type) {
        this.name = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
