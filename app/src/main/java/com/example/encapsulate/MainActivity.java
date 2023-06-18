package com.example.encapsulate;





import androidx.appcompat.app.AppCompatActivity;

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
import com.example.encapsulate.models.TimeCapsule;



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
//                controller.NewList();
                if(timeCapsule==null){
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    startActivity(intent);
                }else{
                    controller.deleteTimeCapsule(timeCapsule.getCapsuleID(), getApplicationContext());
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    startActivity(intent);
                }

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
                Log.d("TAG", "name: "+tName);

                if(TextUtils.isEmpty(tName) || TextUtils.isEmpty(tDesc)){
                    Toast.makeText(MainActivity.this, "Please provide time capsule name and description.", Toast.LENGTH_SHORT).show();
                }else if(stat) {
                    if (TextUtils.isEmpty(tOpenDate) || TextUtils.isEmpty(tPin)) {
                        Toast.makeText(MainActivity.this, "Please set open date and pin!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                        try{
                            timeCapsule = controller.addTimeCapsule(tName, tDesc, tLoc, stat, tOpenDate, tPin);
                            if(timeCapsule != null){
                                Intent nIntent = new Intent(MainActivity.this, FileUpload.class);
                                nIntent.putExtra("id", timeCapsule.getCapsuleID());
                                startActivity(nIntent);




                            }

                        }catch (Exception ex){
                            Toast.makeText(MainActivity.this, "Error Occurred: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
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






}