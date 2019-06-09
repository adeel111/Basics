package com.example.muhammadashfaq.eatit.Model;

public class Rating {
    private String userPhone;
    private String foodid;
    private String rateValue;
    private String comment;


    public Rating() {
    }

    public Rating(String userPhone, String foodid, String rateValue, String comment) {
        this.userPhone = userPhone;
        this.foodid = foodid;
        this.rateValue = rateValue;
        this.comment = comment;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
