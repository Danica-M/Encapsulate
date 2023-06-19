package com.example.encapsulate;





import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.example.encapsulate.models.Controller;
import com.example.encapsulate.models.File;
import com.example.encapsulate.models.TimeCapsule;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    TextView openDateLabel, pinLabel;
    EditText name, desc, loc, openDate, pin;
    Switch isOpen;
    Controller controller;
    TimeCapsule timeCapsule;
    Button cancel, create;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String currentTCID = Controller.getCurrentTCID();
        controller = new Controller();
        openDateLabel = findViewById(R.id.textView5);
        pinLabel = findViewById(R.id.textView6);
        name = findViewById(R.id.name);
        desc = findViewById(R.id.description);
        loc = findViewById(R.id.location);
        isOpen = findViewById(R.id.isOpenSwitch);
        openDate = findViewById(R.id.openDate);
        pin = findViewById(R.id.pin);
        cancel = findViewById(R.id.cancelBtn);
        create = findViewById(R.id.createBtn);

        if (currentTCID != null && !currentTCID.isEmpty()) {getCurrentTC(currentTCID);}
        isOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Perform actions based on the switch state change
                if (isChecked) {
                    Toast.makeText(MainActivity.this, "ON", Toast.LENGTH_SHORT).show();
                    openDate.setVisibility(View.VISIBLE);
                    openDateLabel.setVisibility(View.VISIBLE);
                    pin.setVisibility(View.VISIBLE);
                    pinLabel.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(MainActivity.this, "OFF", Toast.LENGTH_SHORT).show();
                    openDate.setVisibility(View.INVISIBLE);
                    openDateLabel.setVisibility(View.INVISIBLE);
                    pin.setVisibility(View.INVISIBLE);
                    pinLabel.setVisibility(View.INVISIBLE);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)
                        .setTitle("Create Time Capsule Cancellation")
                        .setMessage("Are you sure you want to cancel creating this time capsule?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(currentTCID==null){
                                    Intent intent = new Intent(MainActivity.this, Home.class);
                                    startActivity(intent);
                                    Controller.setCurrentTCID(null);
                                }else{
                                    controller.deleteTimeCapsule(currentTCID, getApplicationContext());
                                    Intent intent = new Intent(MainActivity.this, Home.class);
                                    startActivity(intent);
                                    if(Controller.getFileList().size()>0){
                                        controller.deleteStorageFiles(Controller.getFileList());
                                    }
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tName, tDesc, tLoc, tOpenDate, tPin;
                boolean stat = isOpen.isChecked();
                tName = name.getText().toString();
                tDesc = desc.getText().toString();
                tLoc = loc.getText().toString();
                tOpenDate = openDate.getText().toString();
                tPin = pin.getText().toString();

                if (currentTCID != null && !currentTCID.isEmpty()) {
                    controller.updateTimeCapsule(currentTCID,tName, tDesc, tLoc, stat, tOpenDate, tPin);
                    Intent nIntent = new Intent(MainActivity.this, FileUpload.class);
                    startActivity(nIntent);

                } else {

                    Log.d("TAG", "name: " + tName);

                    if (TextUtils.isEmpty(tName) || TextUtils.isEmpty(tDesc)) {
                        Toast.makeText(MainActivity.this, "Please provide time capsule name and description.", Toast.LENGTH_SHORT).show();
                    } else if (stat) {
                        if (TextUtils.isEmpty(tOpenDate) || TextUtils.isEmpty(tPin)) {
                            Toast.makeText(MainActivity.this, "Please set open date and pin!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            timeCapsule = controller.addTimeCapsule(tName, tDesc, tLoc, stat, tOpenDate, tPin);
                            if (timeCapsule != null) {
                                Controller.setCurrentTCID(timeCapsule.getCapsuleID());
                                Intent nIntent = new Intent(MainActivity.this, FileUpload.class);
                                startActivity(nIntent);
                            }

                        } catch (Exception ex) {
                            Toast.makeText(MainActivity.this, "Error Occurred: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        uri = data.getData();
//        imageView.setImageURI(uri);
//        imageList.add(uri);
//
//        if(Controller.getFileList().size()== Controller.Capacity){
//            upload.setEnabled(false);
//            capture.setEnabled(false);
//        }
//
//    }

    public void getCurrentTC(String id){
        DatabaseReference tRef = Controller.getReference().child("timeCapsules").child(id);
        tRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TimeCapsule timeCapsule = snapshot.getValue(TimeCapsule.class);
                name.setText(timeCapsule.getCapsuleName());
                desc.setText(timeCapsule.getDescription());
                loc.setText(timeCapsule.getLocation());
                if(timeCapsule.getOpen()){
                    isOpen.setChecked(true);
                    openDate.setText(timeCapsule.getOpenDate());
                    pin.setText(timeCapsule.getPin());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }






}