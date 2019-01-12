package com.example.adeeliftikhar.admission.DataProvider;

public class FacilityDataProvider {
    private int facilityImage;
    private String facilityName, facilityMessage;

    public FacilityDataProvider(int facilityImage, String facilityName, String facilityMessage) {
        this.setFacilityImage(facilityImage);
        this.setFacilityName(facilityName);
        this.setFacilityMessage(facilityMessage);
    }

    public int getFacilityImage() {
        return facilityImage;
    }

    public void setFacilityImage(int facilityImage) {
        this.facilityImage = facilityImage;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getFacilityMessage() {
        return facilityMessage;
    }

    public void setFacilityMessage(String facilityMessage) {
        this.facilityMessage = facilityMessage;
    }
}
