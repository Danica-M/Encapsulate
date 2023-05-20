package com.example.encapsulate.models;

public class TimeCapsule {
    String capsuleID;
    String capsuleName;
    String Description;
    String location;
    Boolean isOpen;
    String openDate;
    String pin;
    Integer fileCapacity;

    public TimeCapsule(){}
    public TimeCapsule(String capsuleID, String capsuleName, String description, String location, Boolean isOpen, String openDate, String pin) {
        this.capsuleID = capsuleID;
        this.capsuleName = capsuleName;
        Description = description;
        this.location = location;
        this.isOpen = isOpen;
        this.openDate = openDate;
        this.pin = pin;
        this.fileCapacity = 20;
    }

    public String getCapsuleID() {
        return capsuleID;
    }

    public void setCapsuleID(String capsuleID) {
        this.capsuleID = capsuleID;
    }

    public String getCapsuleName() {
        return capsuleName;
    }

    public void setCapsuleName(String capsuleName) {
        this.capsuleName = capsuleName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Integer getFileCapacity() {
        return fileCapacity;
    }

    public void setFileCapacity(Integer fileCapacity) {
        this.fileCapacity = fileCapacity;
    }
}
