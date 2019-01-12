package com.example.adeeliftikhar.admission.DataProvider;

public class TeamDataProvider {
    private int teamMateImage;
    private String teamMateName, teamMateDesignation, teamMateMessage;

    public TeamDataProvider(int teamMateImage, String teamMateName, String teamMateDesignation, String teamMateMessage) {
        this.setTeamMateImage(teamMateImage);
        this.setTeamMateName(teamMateName);
        this.setTeamMateDesignation(teamMateDesignation);
        this.setTeamMateMessage(teamMateMessage);
    }

    public int getTeamMateImage() {
        return teamMateImage;
    }

    public void setTeamMateImage(int teamMateImage) {
        this.teamMateImage = teamMateImage;
    }

    public String getTeamMateName() {
        return teamMateName;
    }

    public void setTeamMateName(String teamMateName) {
        this.teamMateName = teamMateName;
    }

    public String getTeamMateDesignation() {
        return teamMateDesignation;
    }

    public void setTeamMateDesignation(String teamMateDesignation) {
        this.teamMateDesignation = teamMateDesignation;
    }

    public String getTeamMateMessage() {
        return teamMateMessage;
    }

    public void setTeamMateMessage(String teamMateMessage) {
        this.teamMateMessage = teamMateMessage;
    }
}
