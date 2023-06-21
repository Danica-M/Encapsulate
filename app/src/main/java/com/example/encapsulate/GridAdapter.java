package com.example.encapsulate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encapsulate.models.Controller;
import com.example.encapsulate.models.File;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    List<File> recyclerFileList;
    int stat;
    Context context;
    AlertDialog dialog1;
    public GridAdapter(Context context, List<File> recyclerFileList, int stat){
        this.context = context;
        this.recyclerFileList = recyclerFileList;
        this.stat = stat;

    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        FloatingActionButton delBtn;
        TextView caption;
        ConstraintLayout fileHolder;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            delBtn = view.findViewById(R.id.delBtn);
            caption = view.findViewById(R.id.cap);
            fileHolder = view.findViewById(R.id.layoutHolder);


        }
    }

    @NonNull
    @Override
    public GridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.file_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridAdapter.ViewHolder holder, int position) {
        int rec_position = position;
        File file = recyclerFileList.get(position);
        holder.caption.setText(recyclerFileList.get(position).getCaption());
        Picasso.get()
                .load(file.getFileUrl())
//                .placeholder(R.drawable.placeholder_image) // Optional: Display a placeholder image while loading
//                .error(R.drawable.error_image) // Optional: Display an error image if loading fails
                .into(holder.image);


        holder.fileHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // Inflate the custom layout for the dialog
                View dialogView = LayoutInflater.from(context).inflate(R.layout.image_dialog_layout, null);
                // Find the input field in the dialog layout

                Log.d("TAG", "url:"+file.getFileUrl());
//                TextView captionM = dialogView.findViewById(R.id.il_caption);
                FloatingActionButton cancel = dialogView.findViewById(R.id.exit_fab);
                ImageView image = dialogView.findViewById(R.id.il_image);
//                captionM.setText(file.getCaption());
                Picasso.get()
                        .load(file.getFileUrl())
                        .into(image);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });

                builder.setView(dialogView);
                dialog1 = builder.create();
                // Show the dialog
                if (!((Activity) context).isFinishing()) {
                    dialog1.show();
                }
            }
        });

        if(stat == 0){

            holder.delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File file1 = recyclerFileList.get(rec_position);
                    StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(file1.getFileUrl());
                    // Delete the file
                    storageRef.delete();
                    recyclerFileList.remove(rec_position);
//                Controller.removeItem(recyclerFileList.get(rec_position));
                    notifyDataSetChanged();
                }
            });

        }else{holder.delBtn.setVisibility(View.INVISIBLE);}

    }

    @Override
    public int getItemCount() {
        return recyclerFileList.size();
    }
}
