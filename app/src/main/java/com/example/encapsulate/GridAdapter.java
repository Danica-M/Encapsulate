package com.example.encapsulate;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encapsulate.models.Controller;
import com.example.encapsulate.models.File;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    List<File> recyclerFileList;
    Context context;
    public GridAdapter(Context context, List<File> recyclerFileList){
        this.context = context;
        this.recyclerFileList = recyclerFileList;

    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        FloatingActionButton delBtn;
        TextView caption;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            delBtn = view.findViewById(R.id.delBtn);
            caption = view.findViewById(R.id.cap);
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
        holder.caption.setText(recyclerFileList.get(position).getCaption());
        holder.image.setImageURI(Uri.parse(recyclerFileList.get(rec_position).getFileUri()));
        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                recyclerFileList.remove(rec_position);
//                Controller.removeItem(recyclerFileList.get(rec_position));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return recyclerFileList.size();
    }
}
