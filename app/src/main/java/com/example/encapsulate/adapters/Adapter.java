package com.example.encapsulate.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import android.content.Intent;
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

import com.example.encapsulate.CapsuleDisplay;
import com.example.encapsulate.R;
import com.example.encapsulate.models.TimeCapsule;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    Context context;
    List<TimeCapsule> timeCapsuleList;
    AlertDialog dialog;

    public Adapter(Context context, List<TimeCapsule> timeCapsuleList){
        this.context = context;
        this.timeCapsuleList = timeCapsuleList;
    }


    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.capsule_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (timeCapsuleList == null || timeCapsuleList.isEmpty()) {
            return; // Skip binding if the list is empty or null
        }
        holder.name.setText(timeCapsuleList.get(position).getCapsuleName());
        holder.description.setText(timeCapsuleList.get(position).getDescription());
        if(timeCapsuleList.get(position).getLocation().equals("")){
            holder.location.setVisibility(View.GONE);
        }else{
            holder.location.setText(timeCapsuleList.get(position).getLocation());
        }

        if(Objects.equals(timeCapsuleList.get(position).getOpenDate(), "")){
            holder.openDate.setVisibility(View.GONE);
        }else{
            holder.openDate.setText(timeCapsuleList.get(position).getOpenDate());
        }
        holder.fileSize.setText(String.valueOf(timeCapsuleList.get(position).getUploads().size()));
        if(timeCapsuleList.get(position).getClose()){
            holder.stat.setBackgroundResource(R.drawable.icon_lock);
            holder.capHolder.setBackgroundResource(R.drawable.gradient_background_2);
        }else{
            holder.stat.setBackgroundResource(R.drawable.icon_unlock);
            holder.capHolder.setBackgroundResource(R.drawable.gradient_background);
        }


        holder.capHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(timeCapsuleList.get(position).getClose()){

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Pin Request");

                    // Inflate the custom layout for the dialog
                    View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null);

                    // Find the input field in the dialog layout
                    EditText editTextInput = dialogView.findViewById(R.id.ed_pin);
                    TextView errorMessage = dialogView.findViewById(R.id.tv_error);
                    Button cancel = dialogView.findViewById(R.id.dl_cancel);
                    Button submit = dialogView.findViewById(R.id.dl_submit);
                    errorMessage.setVisibility(View.INVISIBLE);
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String userInput = editTextInput.getText().toString();
                            Log.d("TAG", "ui: "+userInput);
                            Log.d("TAG", "pin: "+timeCapsuleList.get(position).getPin());
                            if(userInput.equals(timeCapsuleList.get(position).getPin())){

                                Intent intent = new Intent(view.getContext(), CapsuleDisplay.class);
                                intent.putExtra("id", timeCapsuleList.get(position).getCapsuleID());
                                context.startActivity(intent);
                                dialog.dismiss();
                            }else{
                                editTextInput.setText("");
                                errorMessage.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    builder.setView(dialogView);
                    dialog = builder.create();
                    // Show the dialog
                    if (!((Activity) context).isFinishing()) {
                        dialog.show();
                    }

                }else {
                    Intent intent = new Intent(view.getContext(), CapsuleDisplay.class);
                    intent.putExtra("id", timeCapsuleList.get(position).getCapsuleID());
                    context.startActivity(intent);
                }
            }
        });
    }

    public void setFilteredList(ArrayList<TimeCapsule> filteredList){
        this.timeCapsuleList = filteredList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return timeCapsuleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, description, location, openDate, fileSize;
        ImageView stat;
        ConstraintLayout capHolder;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            description = itemView.findViewById(R.id.tv_desc);
            location = itemView.findViewById(R.id.tv_loc);
            openDate = itemView.findViewById(R.id.tv_date);
            stat = itemView.findViewById(R.id.stat_btn);
            capHolder  = itemView.findViewById(R.id.capHolder);
            fileSize = itemView.findViewById(R.id.files_num);


        }
    }
}

