package com.example.encapsulate;




import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.encapsulate.models.Controller;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Controller controller;
    List<Uri> imageList = new ArrayList<>();;
    Uri uri;
    Bitmap img;
    ImageView imageView;
    RecyclerView imageRecycler;
    Button upload, capture, cancel, create;
    GridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageRecycler = findViewById(R.id.imageRecycler);
        imageRecycler.setLayoutManager(new GridLayoutManager(this,2));
        gridAdapter = new GridAdapter(MainActivity.this, Controller.fileList);
        imageRecycler.setAdapter(gridAdapter);
        upload = findViewById(R.id.uploadButton);
        capture = findViewById(R.id.capBtn);
        cancel = findViewById(R.id.cancelBtn);
        create = findViewById(R.id.createBtn);
        controller = new Controller();
        Toast.makeText(this, "image size: "+controller.getFileList().size(), Toast.LENGTH_SHORT).show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.NewList();
                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "create clicked", Toast.LENGTH_SHORT).show();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, uploadImage.class);
                startActivity(intent);
            }
        });

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Capture_Image.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();
        imageView.setImageURI(uri);
        imageList.add(uri);

        if(Controller.getFileList().size()== Controller.Capacity){
            upload.setEnabled(false);
            capture.setEnabled(false);
        }

    }






}