package com.example.encapsulate.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    public static List<File> fileList = new ArrayList<>();
    private static DatabaseReference reference;
    private static SimpleDateFormat sdf;
    public static User currentUser;
    public static Integer Capacity = 5;

    public Controller() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public void NewList(){
        fileList.clear();
    }

    public static List<File> getFileList(){return fileList;}

    public static void addItem(File item){
        fileList.add(item);
    }

    // validation for firstname and lastname
    public static boolean validateString(String name) {
        Pattern pattern = Pattern.compile(".*\\d.*");
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    public User registerUser(String userID, String fName, String lName, String email, String password) {
        try {
            User user = new User(userID, fName, lName, email, password);
            reference.child("users").child(userID).setValue(user);
            return user;

        } catch (Exception ex) {
            return null;
        }
    }


}
