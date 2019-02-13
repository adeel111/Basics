package com.example.aydil.nestedrecyclerview.dataproviders;

public class HorizontalRVDataProviders {
    private int imageViewCountry;
    private String textViewCountryName,textViewNext;

    public HorizontalRVDataProviders(int imageViewCountry, String textViewCountryName, String textViewNext) {

        this.setImageViewCountry(imageViewCountry);
        this.setTextViewCountry(textViewCountryName);
        this.setTextViewNext(textViewNext);
    }

    public void setImageViewCountry(int imageViewCountry) {
        this.imageViewCountry = imageViewCountry;
    }

    public int getImageViewCountry() {
        return imageViewCountry;
    }

    public void setTextViewCountry(String textViewCountryName) {
        this.textViewCountryName = textViewCountryName;
    }

    public String getTextViewCountry() {
        return textViewCountryName;
    }

    public void setTextViewNext(String textViewNext) {
        this.textViewNext = textViewNext;
    }

    public String getTextViewNext() {
        return textViewNext;
    }
}
