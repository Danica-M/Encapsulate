package com.example.encapsulate;




import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Controller controller;
    List<Uri> imageList = new ArrayList<>();;
    Uri uri;
    Bitmap img;
    ImageView imageView;
    RecyclerView imageRecycler;
    private final int CAMERA_REQ_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageRecycler = findViewById(R.id.imageRecycler);
        imageRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        StaggeredAdapter staggeredAdapter = new StaggeredAdapter(MainActivity.this, Controller.uriList, Controller.captionList );
        imageRecycler.setAdapter(staggeredAdapter);
        Button button = findViewById(R.id.uploadButton);
        Button button2 = findViewById(R.id.button2);
        controller = new Controller();
        Toast.makeText(this, "image size: "+controller.getUriList(), Toast.LENGTH_SHORT).show();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, uploadImage.class);
                startActivity(intent);


            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "image size: "+controller.getUriList(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "image size: "+controller.getUriList());
//                ImagePicker.with(MainActivity.this)
//                        .cameraOnly()
//                        .crop()	    			//Crop image(Optional), Check Customization for more option
////                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
////                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
//                        .start();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();
        imageView.setImageURI(uri);
        imageList.add(uri);
        Log.d("TAG", "listSize: "+imageList.size());

    }

    public String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


}