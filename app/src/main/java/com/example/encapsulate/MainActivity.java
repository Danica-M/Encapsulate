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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.ParseException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity  implements LocationListener {

    private LocationManager locationManager;
    TextView openDateLabel, pinLabel;
    EditText name, desc, loc, openDate, pin;
    Switch isOpen;
    Controller controller;
    TimeCapsule timeCapsule;
    Button cancel, create, locationBtn;
    double longitude;
    double latitude;


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


        if (currentTCID != null && !currentTCID.isEmpty()) {getCurrentTC(currentTCID);}

        openDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(openDate);
            }
        });
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

                Log.d("TAG","userID:"+owner);
                Log.d("TAG","openDate:"+tOpenDate);
                Log.d("TAG","pin:"+tPin);

                if (TextUtils.isEmpty(tName) || TextUtils.isEmpty(tDesc)) {
                    Toast.makeText(MainActivity.this, "Please provide time capsule name and description.", Toast.LENGTH_SHORT).show();
                } else if (stat) {
                    if(TextUtils.isEmpty(tOpenDate) || TextUtils.isEmpty(tPin))
                        Toast.makeText(MainActivity.this, "Please set open date and pin!", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(MainActivity.this, "reached", Toast.LENGTH_SHORT).show();
                    if (currentTCID != null && !currentTCID.isEmpty()) {
                        controller.updateTimeCapsule(currentTCID,tName, tDesc, tLoc,owner, stat, tOpenDate, tPin);
                        Intent nIntent = new Intent(MainActivity.this, FileUpload.class);
                        startActivity(nIntent);
                        Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();
                        timeCapsule = controller.addTimeCapsule(tName, tDesc, tLoc, owner, stat, tOpenDate, tPin);
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
                Log.d("TAG", "location Clicked");
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                } else {
                    // Location permissions are already granted, proceed with your code

                    if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            onLocationChanged(location);

                        } else {
                            // Location is null, handle the case
                            Log.d("TAG", "2unable");
                            Toast.makeText(MainActivity.this, "Unable to retrieve location", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Network provider is not enabled, handle the case
                        Log.d("TAG", "1unable");
                        Toast.makeText(MainActivity.this, "Network provider is not enabled", Toast.LENGTH_SHORT).show();
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

    public void setDate(EditText editText) {
        // Get the current date to set it as the minimum date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new date picker dialog and set the minimum date
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this,
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
            Geocoder geocoder = new Geocoder(this);
            List<Address> addressList = null;
            addressList= geocoder.getFromLocation(latitude,longitude,1);
            String city = addressList.get(0).getLocality();
            String country = addressList.get(0).getCountryName();
            loc.setText(city+","+country);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "error occurred: "+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}