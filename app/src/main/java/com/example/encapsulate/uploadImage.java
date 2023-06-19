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

public class uploadImage extends AppCompatActivity {

    Controller controller;
    Intent intent2;
    Uri uri;
    ImageView imgView;
    EditText et_caption;
    Button chooseBtn, addBtn, cancelBtn;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        controller = new Controller();
        imgView = findViewById(R.id.imageView_1);
        et_caption = findViewById(R.id.captionText_1);
        chooseBtn = findViewById(R.id.chooseBtn);
        addBtn = findViewById(R.id.addBtn_1);
        cancelBtn = findViewById(R.id.cancelBtn_1);

//        intent2 = getIntent();
        String cip = Controller.getCurrentTCID();


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
                onBackPressed();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri != null) {
//                    progressDialog = new ProgressDialog(getApplicationContext());
//                    progressDialog.setTitle("Loading");
//                    progressDialog.show();

                    String type = getFileExtension(uri);
                    String cap = et_caption.getText().toString();

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    Date currentDate = new Date();
                    String fileName = formatter.format(currentDate);

                    Controller.countplus();
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
                                            Log.d("TAG", "dURL: " + downloadUrl);
                                            File newF = new File(downloadUrl, cap, type);
                                            Controller.addItem(newF);
//                                            controller.addFile(cip, newF);
                                            Toast.makeText(uploadImage.this, "Image added successfully", Toast.LENGTH_SHORT).show();

//                                            if (progressDialog.isShowing()){progressDialog.dismiss();}
                                            Intent fIntent = new Intent(uploadImage.this, FileUpload.class);
                                            startActivity(fIntent);
                                            finish();
                                        }
                                        });
//                                    Log.d("TAG", "dURL: "+taskSnapshot.getTask().getResult().getStorage().getDownloadUrl().toString());
////                                    File newF = new File(taskSnapshot.getDownloadUrl,cap, type);
//                                    Toast.makeText(uploadImage.this, "Image added successfully", Toast.LENGTH_SHORT).show();
////                                    if (progressDialog.isShowing())
////                                        progressDialog.dismiss();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(uploadImage.this, "unsuccessfully", Toast.LENGTH_SHORT).show();
//                                    if (progressDialog.isShowing())
//                                        progressDialog.dismiss();
                                }
                            });


//                    String type = getFileExtension(uri);
//                    String cap = et_caption.getText().toString();
//                    File newF = new File(String.valueOf(Controller.fileList.size()+1),"",cap, uri.toString(), type);
//
//                    Log.d("TAG", "uris:" + uri.toString());
//                    Controller.addItem(newF);
//
//                    Toast.makeText(uploadImage.this, "image size: "+Controller.getFileList().size(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(uploadImage.this, "Image added successfully", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(uploadImage.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();

                } else {
                    Toast.makeText(uploadImage.this, "No file selected", Toast.LENGTH_SHORT).show();
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
            imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Log.d("TAG", "uri:" + uri);
        }
    }

    public String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}