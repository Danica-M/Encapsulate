package com.example.encapsulate;

import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encapsulate.models.TimeCapsule;



import java.util.ArrayList;
import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    Context context;
    List<TimeCapsule> timeCapsuleList;

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
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        TimeCapsule clickedCapsule = timeCapsuleList.get(position);
        holder.name.setText(timeCapsuleList.get(position).getCapsuleName());
        holder.description.setText(timeCapsuleList.get(position).getDescription());
        holder.location.setText(timeCapsuleList.get(position).getLocation());
        holder.openDate.setText(timeCapsuleList.get(position).getOpenDate());

        holder.fileSize.setText(String.valueOf(timeCapsuleList.get(position).getUploads().size()));
        if(timeCapsuleList.get(position).getClose()){
            holder.stat.setBackgroundResource(R.drawable.icon_lock);
            holder.capHolder.setBackgroundResource(R.color.purple_200);
        }else{
            holder.stat.setBackgroundResource(R.drawable.icon_unlock);
            holder.capHolder.setBackgroundResource(R.color.teal_200);
        }

        holder.capHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CapsuleDisplay.class);
                intent.putExtra("id", clickedCapsule.getCapsuleID());
                context.startActivity(intent);
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

