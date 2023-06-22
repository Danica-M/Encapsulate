package com.example.encapsulate;




import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.example.encapsulate.models.Controller;
import com.example.encapsulate.models.TimeCapsule;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.ParseException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity  implements LocationListener {

    private LocationManager fusedLocationProvider;
    TextView openDateLabel, pinLabel;
    EditText name, desc, loc, openDate, pin;
    Switch isOpen;
    Controller controller;
    TimeCapsule timeCapsule;
    Button cancel, create, locationBtn;
    double longitude;
    double latitude;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String currentTCID = Controller.getCurrentTCID();
        String owner = Controller.getCurrentUser().getUserID();
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
        locationBtn = findViewById(R.id.locationBtn);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (currentTCID != null && !currentTCID.isEmpty()) {getCurrentTC(currentTCID);}

        openDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Controller.setDate(openDate, MainActivity.this);
            }
        });
        isOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Perform actions based on the switch state change
                if (isChecked) {
                    openDate.setVisibility(View.VISIBLE);
                    openDateLabel.setVisibility(View.VISIBLE);
                    pin.setVisibility(View.VISIBLE);
                    pinLabel.setVisibility(View.VISIBLE);
                } else {
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
                                }else{
                                    Controller.deleteTimeCapsule(currentTCID, getApplicationContext());
                                    Intent intent = new Intent(MainActivity.this, Home.class);
                                    startActivity(intent);
                                    if(Controller.getFileList().size()>0){
                                        Controller.deleteStorageFiles(Controller.getFileList());
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

                if (TextUtils.isEmpty(tName) || TextUtils.isEmpty(tDesc)) {
                    Toast.makeText(MainActivity.this, "Please provide time capsule name and description.", Toast.LENGTH_SHORT).show();
                } else if (stat && (TextUtils.isEmpty(tOpenDate) || TextUtils.isEmpty(tPin))) {
                    Toast.makeText(MainActivity.this, "Please set open date and pin!", Toast.LENGTH_SHORT).show();
                } else {
                    if (currentTCID != null && !currentTCID.isEmpty()) {
                        Controller.updateTimeCapsule(currentTCID,tName, tDesc, tLoc,owner, stat, tOpenDate, tPin);
                        Intent nIntent = new Intent(MainActivity.this, FileUpload.class);
                        startActivity(nIntent);
                        Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();
                        timeCapsule = Controller.addTimeCapsule(tName, tDesc, tLoc, owner, stat, tOpenDate, tPin);
                        if (timeCapsule != null) {
                            Log.d("TAG", "reached");
                            Controller.setCurrentTCID(timeCapsule.getCapsuleID());
                            Intent nIntent = new Intent(MainActivity.this, FileUpload.class);
                            startActivity(nIntent);
                        } else {
                            Log.d("TAG", "not saved");
                        }
                    }
                }

            }
        });

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check for location permissions
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Request permissions if not granted
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                } else {
                    // Permissions are already granted, proceed with location retrieval
                    fusedLocationProviderClient.getLastLocation()
                            .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        // Location retrieved successfully
                                        onLocationChanged(location);
                                        locationFunc(location);
                                    } else {
                                        // Location is null, handle the case
                                        Toast.makeText(MainActivity.this, "Unable to retrieve location", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(MainActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Failed to retrieve location
                                    Toast.makeText(MainActivity.this, "Failed to retrieve location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }




    public void getCurrentTC(String id){
        DatabaseReference tRef = Controller.getReference().child("timeCapsules").child(id);
        tRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TimeCapsule timeCapsule = snapshot.getValue(TimeCapsule.class);
                name.setText(timeCapsule.getCapsuleName());
                desc.setText(timeCapsule.getDescription());
                loc.setText(timeCapsule.getLocation());
                if(timeCapsule.getClose()){
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


    @Override
    public void onLocationChanged(@NonNull Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        locationFunc(location);


    }
    public void locationFunc(Location location){
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addressList = null;
            addressList= geocoder.getFromLocation(latitude,longitude,1);
            String city = addressList.get(0).getLocality();
            String country = addressList.get(0).getCountryName();
            String street = addressList.get(0).getAddressLine(0);
            loc.setText(street+", "+city+","+country);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "error occurred: "+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}