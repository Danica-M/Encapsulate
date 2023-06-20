package com.example.encapsulate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import com.example.encapsulate.models.Controller;
import com.example.encapsulate.models.File;
import com.example.encapsulate.models.TimeCapsule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CapsuleDisplay extends AppCompatActivity {

    List<File> files;
    ImageButton edit, delete;
    FloatingActionButton upload, capture;
    EditText d_name, d_desc, d_loc, d_date,d_pin;
    Switch d_switch;
    RecyclerView d_recycler;

    GridAdapter gridAdapter;
    String tid;
    Intent dIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capsule_display);

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


        dIntent = getIntent();
        tid = dIntent.getStringExtra("id");

        getQuestions();



    }

    public void getQuestions(){
        DatabaseReference tcRef = Controller.getReference().child("timeCapsules");
        tcRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot capsItem: snapshot.getChildren()){
                    TimeCapsule capsule = capsItem.getValue(TimeCapsule.class);
                    if(capsule!=null && capsule.getCapsuleID().equals(tid)){
                        Log.d("TAG", "name: "+capsule.getCapsuleName() );
                        d_name.setText(capsule.getCapsuleName());
                        d_desc.setText(capsule.getDescription());
                        d_loc.setText(capsule.getLocation());
                        if(capsule.getClose()){
                            d_switch.setChecked(capsule.getClose());
                            d_date.setText(capsule.getOpenDate());
                            d_pin.setText(capsule.getPin());
                        }
                        if(capsule.getUploads().size()>0){
                            Controller.setFileList(capsule.getUploads());
                            gridAdapter = new GridAdapter(CapsuleDisplay.this, Controller.getFileList(), 1);
                            d_recycler.setAdapter(gridAdapter);
                        }
                        Log.d("TAG", "c_: "+Controller.getFileList().size());
                        Log.d("TAG", "size: "+capsule.getUploads().size() );
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}