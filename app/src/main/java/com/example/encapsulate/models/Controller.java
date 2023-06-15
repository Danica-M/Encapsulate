package com.example.encapsulate.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
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

//    public static void removeItem(File item){
//        // Find the index of the fileToRemove in the ArrayList
//        int index = fileList.indexOf(item);
//
//        // Check if the fileToRemove exists in the ArrayList
//        if (index != -1) {
//            // Remove the fileToRemove from the ArrayList
//            fileList.remove(index);
//        }
//    }

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

    public TimeCapsule addTimeCapsule(String capsuleName, String description, String location, Boolean isOpen, String openDate, String pin){
        try{
            String capsuleID = reference.push().getKey();
            TimeCapsule timeCapsule = new TimeCapsule(capsuleID, capsuleName, description, location, isOpen, openDate, pin);
            reference.child("timeCapsules").child(Objects.requireNonNull(capsuleID)).setValue(timeCapsule);
            return timeCapsule;
        }catch(Exception e){
            Log.d("TAG", "error: "+e.getMessage());
            return null;
        }
    }
    public void deleteTimeCapsule(String capsuleID, Context context) {
        DatabaseReference capsuleRef = FirebaseDatabase.getInstance().getReference("timeCapsules").child(capsuleID);
        capsuleRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Tournament deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete time capsule", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
