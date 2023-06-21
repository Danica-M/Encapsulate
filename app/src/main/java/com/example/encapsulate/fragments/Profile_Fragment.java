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

import com.example.encapsulate.Adapter;
import com.example.encapsulate.CapsuleDisplay;
import com.example.encapsulate.GridAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<String> uniqueLocations;

    private LocationAdapter locationAdapter;
    private TextView none2;
    RecyclerView locationRecycler;
    public Profile_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile_Fragment newInstance(String param1, String param2) {
        Profile_Fragment fragment = new Profile_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        uniqueLocations = new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_, container, false);
        getLocation();
        locationRecycler = view.findViewById(R.id.locationRecycler);
        none2 = view.findViewById(R.id.none2);
        none2.setVisibility(View.GONE);
        locationRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    public void getLocation(){
        DatabaseReference tcRef = Controller.getReference().child("timeCapsules");
        tcRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot capsItem: snapshot.getChildren()){
                    TimeCapsule capsule = capsItem.getValue(TimeCapsule.class);
                    String location = capsule.getLocation();

                    if (!uniqueLocations.contains(location) && !Objects.equals(location, "")) {
                        uniqueLocations.add(location);
                    }
                    }

                if(uniqueLocations.size()==0) {
                    none2.setText("You don't have any time capsule.");
                    none2.setVisibility(View.VISIBLE);
                }

                locationAdapter = new LocationAdapter(getContext(), uniqueLocations);
                locationRecycler.setAdapter(locationAdapter);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}