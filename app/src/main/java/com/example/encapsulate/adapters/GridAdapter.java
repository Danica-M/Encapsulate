package com.example.encapsulate.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encapsulate.R;
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
    public static class ViewHolder extends RecyclerView.ViewHolder {
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
    public void onBindViewHolder(@NonNull GridAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        File file = recyclerFileList.get(position);
        holder.caption.setText(recyclerFileList.get(position).getCaption());
        Picasso.get()
                .load(file.getFileUrl())
                .into(holder.image);


        holder.fileHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // Inflate the custom layout for the dialog
                View dialogView = LayoutInflater.from(context).inflate(R.layout.image_dialog_layout, null);
                // Find the input field in the dialog layout

                FloatingActionButton cancel = dialogView.findViewById(R.id.exit_fab);
                ImageView image = dialogView.findViewById(R.id.il_image);
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
                    File file1 = recyclerFileList.get(position);
                    StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(file1.getFileUrl());
                    // Delete the file
                    storageRef.delete();
                    recyclerFileList.remove(position);
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
