package com.example.encapsulate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Capture_Image extends AppCompatActivity {
    Uri uri;
    Controller controller;
    ImageView imgView;
    EditText et_caption;
    Button captureBtn, addBtn, cancelBtn;
    ProgressDialog progressDialog;
    Intent intent2;
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
        intent2 = getIntent();
        String status = intent2.getStringExtra("type");
        String cid = intent2.getStringExtra("id");

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
                if (uri != null) {

                    String type = getFileExtension(uri);
                    String cap = et_caption.getText().toString();

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    Date currentDate = new Date();
                    String fileName = formatter.format(currentDate);

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);
                    storageReference.putFile(uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri downloadUri) {
                                            String downloadUrl = downloadUri.toString();
                                            // Use the download URL as needed
                                            Log.d("TAG", "dURL from cap: " + downloadUrl);
                                            File newF = new File(downloadUrl, cap, type);
                                            Controller.addItem(newF);

                                            Toast.makeText(Capture_Image.this, "Image added successfully", Toast.LENGTH_SHORT).show();
                                            Intent fIntent;
                                            if(status==null){
                                                fIntent = new Intent(Capture_Image.this, FileUpload.class);
                                            }else{
                                                fIntent = new Intent(Capture_Image.this, CapsuleDisplay.class);
                                                fIntent.putExtra("id", cid);
                                                fIntent.putExtra("stat", "yes");
                                            }
                                            startActivity(fIntent);
                                            finish();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Capture_Image.this, "unsuccessfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(Capture_Image.this, "No file selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            imgView.setImageURI(uri);
        }
    }
    public String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

}