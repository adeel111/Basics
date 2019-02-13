package com.example.aydil.nestedrecyclerview.dataproviders;


public class VerticalRVDataProviders {
    private int imgView;
    private String boldHeading, text2, text3, likes, likesAmount, disLikes, disLikesAmount, neutral, neutralAmount;

    public VerticalRVDataProviders(int imageView, String boldHeading, String text2, String text3, String likes,
                                   String likesAmount, String disLikes, String disLikesAmount, String neutral,
                                   String neutralAmount) {
        this.setImgView(imageView);
        this.setBoldHeading(boldHeading);
        this.setText2(text2);
        this.setText3(text3);
        this.setLikes(likes);
        this.setLikesAmount(likesAmount);
        this.setDisLikes(disLikes);
        this.setDisLikesAmount(disLikesAmount);
        this.setNeutral(neutral);
        this.setNeutralAmount(neutralAmount);
    }

    public int getImgView() {
        return imgView;
    }

    public void setImgView(int imgView) {
        this.imgView = imgView;
    }

    public String getBoldHeading() {
        return boldHeading;
    }

    public void setBoldHeading(String boldHeading) {
        this.boldHeading = boldHeading;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getLikesAmount() {
        return likesAmount;
    }

    public void setLikesAmount(String likesAmount) {
        this.likesAmount = likesAmount;
    }

    public String getDisLikes() {
        return disLikes;
    }

    public void setDisLikes(String disLikes) {
        this.disLikes = disLikes;
    }

    public String getDisLikesAmount() {
        return disLikesAmount;
    }

    public void setDisLikesAmount(String disLikesAmount) {
        this.disLikesAmount = disLikesAmount;
    }

    public String getNeutral() {
        return neutral;
    }

    public void setNeutral(String neutral) {
        this.neutral = neutral;
    }

    public String getNeutralAmount() {
        return neutralAmount;
    }

    public void setNeutralAmount(String neutralAmount) {
        this.neutralAmount = neutralAmount;
    }
}
