package com.example.encapsulate.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    String userID;
    String firstName;
    String lastName;
    String email;
    String password;
    List<TimeCapsule> timeCaps;

    public User(){}

    public User(String userID, String firstName, String lastName, String email, String password) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        timeCaps = new ArrayList<>();
    }

    public String getUserID() {
        return userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<TimeCapsule> getTimeCaps() {
        return timeCaps;
    }

    public void setTimeCaps(List<TimeCapsule> timeCaps) {
        this.timeCaps = timeCaps;
    }
}
