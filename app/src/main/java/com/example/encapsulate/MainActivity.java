package com.example.encapsulate;



import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.IOException;



public class MainActivity extends AppCompatActivity {

    Uri uri;
    Bitmap img;
    ImageView imageView;
    private final int CAMERA_REQ_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        Button button = findViewById(R.id.uploadButton);
        Button button2 = findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePicker.with(MainActivity.this)
                        .galleryOnly()
                        .crop()	    			//Crop image(Optional), Check Customization for more option
//                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

//                Intent iCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(iCam,CAMERA_REQ_CODE);
//
////                ImagePicker.with(this)
////
//
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                activityResultLauncher.launch(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePicker.with(MainActivity.this)
                        .cameraOnly()
                        .crop()	    			//Crop image(Optional), Check Customization for more option
//                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();
        imageView.setImageURI(uri);
//        if(requestCode == RESULT_OK){


//            Intent data = data.getData();
//            if(requestCode == CAMERA_REQ_CODE){
//                Bitmap img = (Bitmap)(data.getExtras().get("data"));
//                imageView.setImageBitmap(img);
//            }
//        }

    }


}