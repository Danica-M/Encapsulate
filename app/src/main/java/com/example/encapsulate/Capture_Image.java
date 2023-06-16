package com.example.encapsulate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.encapsulate.models.Controller;
import com.example.encapsulate.models.File;
import com.github.dhaval2404.imagepicker.ImagePicker;

public class Capture_Image extends AppCompatActivity {
    Uri uri;
    Controller controller;
    ImageView imgView;
    EditText et_caption;
    Button captureBtn, addBtn, cancelBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_image);
        imgView = findViewById(R.id.imageView_2);
        et_caption = findViewById(R.id.captionText_2);
        captureBtn = findViewById(R.id.capturebtn);
        addBtn = findViewById(R.id.addBtn_2);
        cancelBtn = findViewById(R.id.cancelBtn_2);
        controller = new Controller();

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(Capture_Image.this)
                        .cameraOnly()
//                        .cropSquare()
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Capture_Image.this, MainActivity.class);
                startActivity(intent);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uri!=null){
                    String type = getFileExtension(uri);
                    String cap = et_caption.getText().toString();
                    File newF = new File("",cap, type);

                    Controller.addItem(newF);
                    Intent intent = new Intent(Capture_Image.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(Capture_Image.this, "No file selected",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = data.getData();
        imgView.setImageURI(uri);
    }
    public String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

}