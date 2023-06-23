package com.example.encapsulate.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.encapsulate.Adapter;
import com.example.encapsulate.R;
import com.example.encapsulate.models.Controller;
import com.example.encapsulate.models.TimeCapsule;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Capsule_Fragment extends Fragment {
    Controller controller;
    private Adapter adapter;
    private TextView none;

    ArrayList<TimeCapsule> filteredList;
    SearchView search;
    RecyclerView capsuleRecycler;
    private List<TimeCapsule> timeCapsList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public Capsule_Fragment() {
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
        controller = new Controller();
        timeCapsList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getUserCapsules();
        View view = inflater.inflate(R.layout.fragment_capsule, container, false);
        none = view.findViewById(R.id.none);
        none.setVisibility(View.GONE);
        capsuleRecycler = view.findViewById(R.id.capsuleRecycler);
        capsuleRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        search = view.findViewById(R.id.search);
        adapter = new Adapter(getContext(), timeCapsList);
        capsuleRecycler.setAdapter(adapter);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                filterList(s);
                return true;
            }
        });
        return view;


    }

    public void getUserCapsules(){
        DatabaseReference capRef = Controller.getReference().child("timeCapsules");
        capRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot capItems: snapshot.getChildren()){
                    TimeCapsule timeCapsule = capItems.getValue(TimeCapsule.class);
                    if(timeCapsule != null && timeCapsule.getOwner().equals(Controller.getCurrentUser().getUserID())){
                        timeCapsList.add(timeCapsule);
                    }
                }
                if(timeCapsList.size()==0) {
                    none.setText("You don't have any time capsule.");
                    none.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void filterList(String text){
        filteredList = new ArrayList<>();
        for(TimeCapsule item:timeCapsList){
            if(item.getCapsuleName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        adapter.setFilteredList(filteredList);
        if(filteredList.isEmpty()){
            Toast.makeText(getContext(), "No Time Capsule found with this name", Toast.LENGTH_SHORT).show();
        }

    }
}