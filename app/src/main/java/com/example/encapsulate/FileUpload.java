package com.example.encapsulate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.encapsulate.models.Controller;

public class FileUpload extends AppCompatActivity {
    RecyclerView imageRecycler;
    GridAdapter gridAdapter;
    Button upload, capture, back, save;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);

        upload = findViewById(R.id.uploadButton);
        capture = findViewById(R.id.capBtn);
        back = findViewById(R.id.back);
        save = findViewById(R.id.save);
//        intent = getIntent();
//        String tid = intent.getStringExtra("id");

        imageRecycler = findViewById(R.id.imageRecycler);
        imageRecycler.setLayoutManager(new GridLayoutManager(this,2));
        gridAdapter = new GridAdapter(FileUpload.this, Controller.fileList, 0);
        imageRecycler.setAdapter(gridAdapter);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FileUpload.this, uploadImage.class);
                startActivity(intent);

            }
        });

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FileUpload.this, Capture_Image.class);
                startActivity(intent);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nIntent = new Intent(FileUpload.this, MainActivity.class);
                startActivity(nIntent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Controller controller = new Controller();
                controller.addFile(Controller.getCurrentTCID(), Controller.getFileList());
                Toast.makeText(FileUpload.this, "Time Capsule successfully created",Toast.LENGTH_SHORT).show();
                Intent nIntent = new Intent(FileUpload.this, Home.class);
                startActivity(nIntent);
                Controller.setCurrentTCID(null);
                Controller.NewList();

            }
        });

    }
}