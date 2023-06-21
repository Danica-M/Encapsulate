package com.example.encapsulate;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encapsulate.models.TimeCapsule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    Context context;
    List<String> locationList;
    List<Address> addressList;


    public LocationAdapter(Context context, List<String> locationList){
        this.context = context;
        this.locationList = locationList;
        addressList = new ArrayList<>();
    }


    @NonNull
    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.location_item, parent, false);
        getLongLat();

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.loc.setText(locationList.get(position).toString());
        holder.direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double latitude = addressList.get(position).getLatitude();
                double longitude = addressList.get(position).getLongitude();

                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
        });




    }



    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public void getLongLat(){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        for (String addressString : locationList) {
            try {
                List<Address> addresses = geocoder.getFromLocationName(addressString, 1);
                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    addressList.add(address);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle exception
            }
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView loc;
        FloatingActionButton direct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            loc = itemView.findViewById(R.id.loc_name);
            direct = itemView.findViewById(R.id.location_fab);


        }
    }
}
