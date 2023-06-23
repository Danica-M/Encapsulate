package com.example.encapsulate;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.encapsulate.adapters.GridAdapter;
import com.example.encapsulate.models.Controller;


public class FileUpload extends AppCompatActivity {
    RecyclerView imageRecycler;
    GridAdapter gridAdapter;
    Button upload, capture, back, save;
    TextView size;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);
        upload = findViewById(R.id.uploadButton);
        capture = findViewById(R.id.capBtn);
        back = findViewById(R.id.back);
        save = findViewById(R.id.save);
        size = findViewById(R.id.currentSize);
        imageRecycler = findViewById(R.id.imageRecycler);
        imageRecycler.setLayoutManager(new GridLayoutManager(this,2));
        gridAdapter = new GridAdapter(FileUpload.this, Controller.fileList, 0);
        imageRecycler.setAdapter(gridAdapter);
        size.setText(String.valueOf(Controller.getFileList().size()));

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Controller.fileList.size()==20){
                    Toast.makeText(FileUpload.this, "You have reach the maximum number of files.", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(FileUpload.this, UploadImage.class);
                    startActivity(intent);
                }


            }
        });

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Controller.fileList.size()==20){
                    Toast.makeText(FileUpload.this, "You have reach the maximum number of files.", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(FileUpload.this, CaptureImage.class);
                    startActivity(intent);
                }


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nIntent = new Intent(FileUpload.this, CreateDetailsPage.class);
                startActivity(nIntent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Controller.addFile(Controller.getCurrentTCID(), Controller.getFileList());
                Toast.makeText(FileUpload.this, "Time Capsule successfully created",Toast.LENGTH_SHORT).show();
                Intent nIntent = new Intent(FileUpload.this, Home.class);
                startActivity(nIntent);
            }
        });

    }
}