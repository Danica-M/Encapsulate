package com.example.encapsulate.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.encapsulate.R;
import com.example.encapsulate.models.Controller;
import com.example.encapsulate.models.TimeCapsule;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<TimeCapsule> allCapsule;
    Button lock, unlock;
    TextView wel, total;

    public Home_Fragment() {
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
        allCapsule = new ArrayList<>();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        lock = view.findViewById(R.id.btn_lock);
        unlock = view.findViewById(R.id.btn_unlock);
        wel = view.findViewById(R.id.tv_wel);
        total = view.findViewById(R.id.tv_total);
        getAllCapsules();
        wel.setText("Hi, "+Controller.getCurrentUser().getFirstName());
        // Inflate the layout for this fragment
        return view;
    }
    public void getAllCapsules(){
        DatabaseReference capRef = Controller.getReference().child("timeCapsules");

        capRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int openCount = 0;
                int closedCount = 0;
                for(DataSnapshot capItems: snapshot.getChildren()){
                    TimeCapsule timeCapsule = capItems.getValue(TimeCapsule.class);
                    if(timeCapsule != null && timeCapsule.getOwner().equals(Controller.getCurrentUser().getUserID())){
                        allCapsule.add(timeCapsule);
                        if (timeCapsule.getClose()) {
                            closedCount++;
                        } else {
                            openCount++;
                        }
                    }
                }
                total.setText(String.valueOf(allCapsule.size()));
                unlock.setText(String.valueOf(openCount));
                lock.setText(String.valueOf(closedCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}