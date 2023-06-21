package com.example.encapsulate.models;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.encapsulate.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    public static List<File> fileList = new ArrayList<>();
    private static DatabaseReference reference;
    private static SimpleDateFormat sdf;

    private static FirebaseStorage firebaseStorage;
    public static User currentUser;
    public static String currentTCID;

    public Controller() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    public static SimpleDateFormat getSdf() {
        return sdf;
    }

    public static DatabaseReference getReference() {
        return reference;
    }

    public static String getCurrentTCID() {
        return currentTCID;
    }

    public static void setCurrentTCID(String currentTCID) {
        Controller.currentTCID = currentTCID;
    }

    public static FirebaseStorage getFirebaseStorage(){return firebaseStorage;}
    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void NewList(){
        fileList.clear();
    }

    public static void setFileList(List<File> fileList) {
        Controller.fileList = fileList;
    }

    public static List<File> getFileList(){return fileList;}

    public static void addItem(File item){fileList.add(item);}


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

    public static TimeCapsule addTimeCapsule(String capsuleName, String description, String location, String owner, Boolean isClose, String openDate, String pin){
        try{
            String capsuleID = reference.push().getKey();
            TimeCapsule timeCapsule = new TimeCapsule(capsuleID, capsuleName, description, location,owner, isClose, openDate, pin);
            reference.child("timeCapsules").child(Objects.requireNonNull(capsuleID)).setValue(timeCapsule);
            return timeCapsule;
        }catch(Exception e){
            Log.d("TAG", "error: "+e.getMessage());
            return null;
        }
    }
    public static void deleteTimeCapsule(String capsuleID, Context context) {
        DatabaseReference capsuleRef = FirebaseDatabase.getInstance().getReference("timeCapsules").child(capsuleID);
        capsuleRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Time Capsule deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete time capsule", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static void updateTimeCapsule(String capsuleID, String capsuleName, String description, String location,String owner, Boolean isClose, String openDate, String pin) {
        DatabaseReference capsuleRef = reference.child("timeCapsules").child(capsuleID);
        capsuleRef.child("capsuleName").setValue(capsuleName);
        capsuleRef.child("description").setValue(description);
        capsuleRef.child("location").setValue(location);
        capsuleRef.child("owner").setValue(owner);
        capsuleRef.child("close").setValue(isClose);
        capsuleRef.child("openDate").setValue(openDate);
        capsuleRef.child("pin").setValue(pin);

    }


    public static void addFile(String timeCapsuleID, List<File> fileList){
        try{

            DatabaseReference tRef = reference.child("timeCapsules").child(timeCapsuleID);
            tRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    TimeCapsule timeCapsule = snapshot.getValue(TimeCapsule.class);
                    if(timeCapsule!=null){
                        tRef.child("uploads").setValue(fileList);
                    }
                    Controller.setCurrentTCID(null);
                    Controller.NewList();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void deleteStorageFiles(List<File> fileList){
        for (File fileObject : fileList) {
            String fileUrl = fileObject.getFileUrl();
            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(fileUrl);
            // Delete the file
            storageRef.delete();
        }
    }

    public static void setDate(EditText editText, Context context) {
        // Get the current date to set it as the minimum date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new date picker dialog and set the minimum date
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year, month, day);
                        // Update the edit text with the selected date
                        String selectedDate = Controller.getSdf().format(calendar.getTimeInMillis());
                        editText.setText(selectedDate);
                    }
                },
                year, month, day);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        // Show the date picker dialog
        datePickerDialog.show();
    }


}
