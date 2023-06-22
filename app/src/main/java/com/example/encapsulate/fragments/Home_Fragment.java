package com.example.encapsulate.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.encapsulate.Adapter;
import com.example.encapsulate.R;
import com.example.encapsulate.models.Controller;
import com.example.encapsulate.models.TimeCapsule;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<TimeCapsule> allCapsule;
    Button lock, unlock;
    TextView wel, total;

    public Home_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Home_Fragment newInstance(String param1, String param2) {
        Home_Fragment fragment = new Home_Fragment();
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
        allCapsule = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_, container, false);
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