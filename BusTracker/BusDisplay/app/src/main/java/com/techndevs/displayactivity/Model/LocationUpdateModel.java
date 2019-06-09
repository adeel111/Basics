package com.techndevs.displayactivity.Model;

public class LocationUpdateModel {
    String accuracy,altitude,longitude,latitiude,speed,extras;

    public LocationUpdateModel() {

    }

    public LocationUpdateModel(String accuracy, String altitude, String longitude, String latitiude, String speed, String extras) {
        this.accuracy = accuracy;
        this.altitude = altitude;
        this.longitude = longitude;
        this.latitiude = latitiude;
        this.speed = speed;
        this.extras = extras;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitiude() {
        return latitiude;
    }

    public void setLatitiude(String latitiude) {
        this.latitiude = latitiude;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }
}
