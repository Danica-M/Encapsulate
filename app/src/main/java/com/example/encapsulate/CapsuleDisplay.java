package com.example.encapsulate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CapsuleDisplay extends AppCompatActivity {

    ImageButton edit, delete;
    FloatingActionButton upload, capture;
    EditText d_name, d_desc, d_loc, d_date,d_pin;
    Switch d_switch;
    RecyclerView d_recycler;

    GridAdapter gridAdapter;
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

        dIntent = getIntent();
        String tid = dIntent.getStringExtra("id");



    }


}