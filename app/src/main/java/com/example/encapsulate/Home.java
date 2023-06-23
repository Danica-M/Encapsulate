package com.example.encapsulate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.encapsulate.fragments.CapsuleFragment;
import com.example.encapsulate.fragments.HomeFragment;
import com.example.encapsulate.fragments.LocationFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;


public class Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        fab = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, CreateDetailsPage.class);
                startActivity(intent);
            }
        });



    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.home:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, new HomeFragment())
                        .commit();
                return true;

            case R.id.capsule:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, new CapsuleFragment())
                        .commit();
                return true;

            case R.id.locations:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, new LocationFragment())
                        .commit();
                return true;
            case R.id.exit:
                logoutUser();
        }
        return false;
    }

    private void logoutUser(){
        new androidx.appcompat.app.AlertDialog.Builder(Home.this)
                .setTitle("Exit Confirmation")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent nlIntent = new Intent(Home.this, Login.class);
                        startActivity(nlIntent);
                        finishAffinity();
                        FirebaseAuth.getInstance().signOut();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
    }

}