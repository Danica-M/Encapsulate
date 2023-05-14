package com.example.encapsulate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
                        .cropSquare()	    			//Crop image(Optional), Check Customization for more option
//                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
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
                    Controller.addItem(String.valueOf(uri));
                    controller.addCaptionItem(et_caption.getText().toString());
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

}