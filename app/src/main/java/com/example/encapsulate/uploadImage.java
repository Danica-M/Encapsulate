package com.example.encapsulate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.encapsulate.models.Controller;
import com.example.encapsulate.models.File;
import com.github.dhaval2404.imagepicker.ImagePicker;

public class uploadImage extends AppCompatActivity {

    Uri uri;
    Controller controller;
    ImageView imgView;
    EditText et_caption;
    Button chooseBtn, addBtn, cancelBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        imgView = findViewById(R.id.imageView_1);
        et_caption = findViewById(R.id.captionText_1);
        chooseBtn = findViewById(R.id.chooseBtn);
        addBtn = findViewById(R.id.addBtn_1);
        cancelBtn = findViewById(R.id.cancelBtn_1);
        controller = new Controller();

        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(uploadImage.this)
                        .crop()
                        .galleryOnly()
//                        .cropSquare()	    			//Crop image(Optional), Check Customization for more option
//                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(uploadImage.this, MainActivity.class);
                startActivity(intent);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uri!=null){
                    String type = getFileExtension(uri);
                    String cap = et_caption.getText().toString();
                    File newF = new File(String.valueOf(Controller.fileList.size()+1),"",cap, String.valueOf(uri), type);

                    Controller.addItem(newF);

                    Toast.makeText(uploadImage.this, "Reached",Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "array:"+ Controller.getFileList().get(0).getFileUri());

//                    Intent intent = new Intent(uploadImage.this, MainActivity.class);
//                    startActivity(intent);
                }else{
                    Toast.makeText(uploadImage.this, "No file selected",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = data.getData();
        imgView.setImageURI(uri);
        Log.d("TAG", "uri:"+ uri);



    }

    public String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}