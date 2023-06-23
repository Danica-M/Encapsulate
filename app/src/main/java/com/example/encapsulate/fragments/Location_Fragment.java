package com.example.encapsulate.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.encapsulate.LocationAdapter;
import com.example.encapsulate.R;
import com.example.encapsulate.models.Controller;
import com.example.encapsulate.models.TimeCapsule;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Location_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    List<String> uniqueLocations;

    private LocationAdapter locationAdapter;
    private TextView none2;
    RecyclerView locationRecycler;
    public Location_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
        uniqueLocations = new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        getLocation();
        locationRecycler = view.findViewById(R.id.locationRecycler);
        none2 = view.findViewById(R.id.none2);
        none2.setVisibility(View.GONE);
        locationRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        locationAdapter = new LocationAdapter(getContext(), uniqueLocations);
        locationRecycler.setAdapter(locationAdapter);

        return view;
    }

    public void getLocation(){
        DatabaseReference tcRef = Controller.getReference().child("timeCapsules");
        tcRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot capsItem: snapshot.getChildren()){
                    TimeCapsule capsule = capsItem.getValue(TimeCapsule.class);
                    assert capsule != null;
                    String location = capsule.getLocation();

                    if (!uniqueLocations.contains(location) && !Objects.equals(location, "")) {
                        uniqueLocations.add(location);
                    }
                    }
                if(uniqueLocations.size()==0) {
                    none2.setVisibility(View.VISIBLE);
                }
                locationAdapter.notifyDataSetChanged();
                Log.d("TAG", "loc:"+uniqueLocations);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}