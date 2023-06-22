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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Capsule_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Capsule_Fragment extends Fragment {
    Controller controller;
    private Adapter adapter;
    private TextView none;

    SearchView search;
    RecyclerView capsuleRecycler;
    private List<TimeCapsule> timeCapsList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Capsule_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Capsule_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Capsule_Fragment newInstance(String param1, String param2) {
        Capsule_Fragment fragment = new Capsule_Fragment();
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
        controller = new Controller();
        timeCapsList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getUserCapsules();
        View view = inflater.inflate(R.layout.fragment_capsule_, container, false);
        none = view.findViewById(R.id.none);
        none.setVisibility(View.GONE);
        capsuleRecycler = view.findViewById(R.id.capsuleRecycler);
        capsuleRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        search = view.findViewById(R.id.search);

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

    public void filterList(String text){
        ArrayList<TimeCapsule> filteredList = new ArrayList<>();
        for(TimeCapsule item:timeCapsList){
            if(item.getCapsuleName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty()){
            adapter.setFilteredList(filteredList);
            Toast.makeText(getContext(), "No Time Capsule found with this name", Toast.LENGTH_SHORT).show();
        }else{
            adapter.setFilteredList(filteredList);
        }
    }

    public void getUserCapsules(){
        DatabaseReference capRef = Controller.getReference().child("timeCapsules");
        Query query = capRef.orderByChild("close");
        capRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                adapter = new Adapter(getContext(), timeCapsList);
                capsuleRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}