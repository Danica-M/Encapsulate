package com.example.encapsulate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.encapsulate.models.Controller;
import com.example.encapsulate.models.TimeCapsule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class Capsule_Display extends AppCompatActivity {
    Controller controller;

    FloatingActionButton upload, capture,edit, delete, close;
    EditText d_name, d_desc, d_loc, d_date,d_pin;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch d_switch;
    RecyclerView d_recycler;
    TextView size2;
    GridAdapter gridAdapter;
    String tid, stat;
    Intent dIntent;
    TimeCapsule capsule;
    String tName, tDesc, tLoc, tOpenDate, tPin;
    boolean stat2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capsule_display);
        controller = new Controller();
        close = findViewById(R.id.d_close);
        edit = findViewById(R.id.d_edit);
        delete = findViewById(R.id.d_delete);
        upload = findViewById(R.id.d_upload);
        capture = findViewById(R.id.d_capture);
        d_name = findViewById(R.id.d_name);
        d_desc = findViewById(R.id.d_desc);
        d_loc = findViewById(R.id.d_loc);
        d_date = findViewById(R.id.d_date);
        d_pin = findViewById(R.id.d_pin);
        d_switch = findViewById(R.id.d_switch);
        d_recycler = findViewById(R.id.d_recycler);
        d_recycler.setLayoutManager(new GridLayoutManager(this,2));

        size2 = findViewById(R.id.currentSize2);
        getQuestions();

        dIntent = getIntent();
        tid = dIntent.getStringExtra("id");
        stat = dIntent.getStringExtra("stat");
        if(stat!=null){
            d_name.setEnabled(true);
            d_desc.setEnabled(true);
            d_loc.setEnabled(true);
            d_date.setEnabled(true);
            d_pin.setEnabled(true);
            d_switch.setEnabled(true);
            edit.setImageResource(R.drawable.icon_save);
            upload.setVisibility(View.VISIBLE);
            capture.setVisibility(View.VISIBLE);
            d_recycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
            gridAdapter = new GridAdapter(Capsule_Display.this, Controller.getFileList(), 0);
            d_recycler.setAdapter(gridAdapter);

        }





        d_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Controller.setDate(d_date, Capsule_Display.this);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!d_name.isEnabled()){
                    d_name.setEnabled(true);
                    d_desc.setEnabled(true);
                    d_loc.setEnabled(true);
                    d_date.setEnabled(true);
                    d_pin.setEnabled(true);
                    d_switch.setEnabled(true);
                    edit.setImageResource(R.drawable.icon_save);
                    upload.setVisibility(View.VISIBLE);
                    capture.setVisibility(View.VISIBLE);
                    gridAdapter = new GridAdapter(Capsule_Display.this, Controller.getFileList(), 0);
                    d_recycler.setAdapter(gridAdapter);

                }else{
                    stat2 = d_switch.isChecked();
                    tName = d_name.getText().toString().toUpperCase();
                    tDesc = d_desc.getText().toString();
                    tLoc = d_loc.getText().toString();
                    tOpenDate = d_date.getText().toString();
                    tPin = d_pin.getText().toString();


                    if (TextUtils.isEmpty(tName) || TextUtils.isEmpty(tDesc)) {
                        Toast.makeText(Capsule_Display.this, "Please provide time capsule name and description.", Toast.LENGTH_SHORT).show();
                    } else if (stat2 && (TextUtils.isEmpty(tOpenDate) || TextUtils.isEmpty(tPin))) {
                        Toast.makeText(Capsule_Display.this, "Please set open date and pin!", Toast.LENGTH_SHORT).show();
                    } else {
                        Controller.updateTimeCapsule(Controller.getCurrentTCID(), tName, tDesc, tLoc, Controller.getCurrentUser().getUserID(), stat2, tOpenDate, tPin);
                        Controller.addFile(Controller.getCurrentTCID(), Controller.getFileList());
                        Toast.makeText(Capsule_Display.this, "Time Capsule successfully updated", Toast.LENGTH_SHORT).show();
                        Intent nIntent = new Intent(Capsule_Display.this, Home.class);
                        startActivity(nIntent);
                    }
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new androidx.appcompat.app.AlertDialog.Builder(Capsule_Display.this)
                        .setTitle("Time Capsule Deletion Confirmation")
                        .setMessage("Are you sure you want to delete this time capsule?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(Controller.getCurrentTCID()==null){
                                    Intent intent = new Intent(Capsule_Display.this, Home.class);
                                    startActivity(intent);
                                }else{
                                    Controller.deleteTimeCapsule(Controller.getCurrentTCID(), getApplicationContext());
                                    Intent intent = new Intent(Capsule_Display.this, Home.class);
                                    startActivity(intent);
                                    Controller.setCurrentTCID(null);
                                    if(Controller.getFileList().size()>0){
                                        Controller.deleteStorageFiles(Controller.getFileList());
                                    }
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        }).show();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Controller.fileList.size()==20){
                    Toast.makeText(Capsule_Display.this, "You have reach the maximum number of files.", Toast.LENGTH_SHORT).show();
                }else{
                    stat2 = d_switch.isChecked();
                    tName = d_name.getText().toString();
                    tDesc = d_desc.getText().toString();
                    tLoc = d_loc.getText().toString();
                    tOpenDate = d_date.getText().toString();
                    tPin = d_pin.getText().toString();
                    saveDetails();

                    Intent intent = new Intent(Capsule_Display.this, uploadImage.class);
                    intent.putExtra("type", "edit");
                    intent.putExtra("id", Controller.getCurrentTCID());
                    startActivity(intent);
                }
            }
        });

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Controller.fileList.size()==20){
                    Toast.makeText(Capsule_Display.this, "You have reach the maximum number of files.", Toast.LENGTH_SHORT).show();
                }else{
                    stat2 = d_switch.isChecked();
                    tName = d_name.getText().toString().toUpperCase();
                    tDesc = d_desc.getText().toString();
                    tLoc = d_loc.getText().toString();
                    tOpenDate = d_date.getText().toString();
                    tPin = d_pin.getText().toString();
                    saveDetails();

                    Intent intent = new Intent(Capsule_Display.this, Capture_Image.class);
                    intent.putExtra("type", "edit");
                    intent.putExtra("id", Controller.getCurrentTCID());
                    startActivity(intent);
                }

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Capsule_Display.this, Home.class);
                startActivity(intent);
                Controller.setCurrentTCID(null);


            }
        });
    }


    public void getQuestions(){
        DatabaseReference tcRef = Controller.getReference().child("timeCapsules");
        tcRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot capsItem: snapshot.getChildren()){
                    capsule = capsItem.getValue(TimeCapsule.class);
                    if(capsule!=null && capsule.getCapsuleID().equals(tid)){
                        Controller.setCurrentTCID(capsule.getCapsuleID());
                        d_name.setText(capsule.getCapsuleName());
                        d_desc.setText(capsule.getDescription());
                        d_loc.setText(capsule.getLocation());
                        d_switch.setChecked(capsule.getClose());
                        if(stat==null && capsule.getUploads().size()>0){
                            Controller.setFileList(capsule.getUploads());
                            gridAdapter = new GridAdapter(Capsule_Display.this, Controller.getFileList(), 1);
                            d_recycler.setAdapter(gridAdapter);
                        }
                        if(capsule.getClose()){
                            d_date.setText(capsule.getOpenDate());
                            d_pin.setText(capsule.getPin());
                        }
                    }
                }
                size2.setText(String.valueOf(Controller.getFileList().size()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void saveDetails(){
        if (TextUtils.isEmpty(tName) || TextUtils.isEmpty(tDesc)) {
            Toast.makeText(Capsule_Display.this, "Please provide time capsule name and description.", Toast.LENGTH_SHORT).show();
        } else if (stat2) {
            if(TextUtils.isEmpty(tOpenDate) || TextUtils.isEmpty(tPin))
                Toast.makeText(Capsule_Display.this, "Please set open date and pin!", Toast.LENGTH_SHORT).show();
        } else {
            Controller.updateTimeCapsule(Controller.getCurrentTCID(), tName, tDesc, tLoc, Controller.getCurrentUser().getUserID(), stat2, tOpenDate, tPin);
        }
    }


}