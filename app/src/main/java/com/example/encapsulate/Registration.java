package com.example.encapsulate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.encapsulate.models.Controller;
import com.example.encapsulate.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.Objects;

public class Registration extends AppCompatActivity {

    EditText edNameF, edNameL, edEmail, edPass, edPass2;
    Button register;
    TextView login;
    FirebaseAuth mAuth;
    Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        edNameF = findViewById(R.id.fName);
        edNameL = findViewById(R.id.lName);
        edEmail = findViewById(R.id.email);
        edPass = findViewById(R.id.password);
        edPass2 = findViewById(R.id.password);
        register = findViewById(R.id.registerButton);
        login = findViewById(R.id.loginText);
        mAuth = FirebaseAuth.getInstance();
        controller = new Controller();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fName, lName, email, pass, pass2;
                fName = edNameF.getText().toString().toUpperCase().trim();
                lName = edNameL.getText().toString().toUpperCase().trim();
                email = edEmail.getText().toString();
                pass = edPass.getText().toString();
                pass2 = edPass2.getText().toString();

                if (TextUtils.isEmpty(fName) || TextUtils.isEmpty(lName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                    Toast.makeText(Registration.this, "Registration form incomplete", Toast.LENGTH_SHORT).show();
                } else if (Controller.validateString(fName)) {
                    Toast.makeText(Registration.this, "Invalid character in firstname field", Toast.LENGTH_SHORT).show();
                } else if (Controller.validateString(lName)) {
                    Toast.makeText(Registration.this, "Invalid character in lastname field", Toast.LENGTH_SHORT).show();
                } else if (!pass.equals(pass2)) {
                    Toast.makeText(Registration.this, "Password does not match", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                /*
                                  user object userid is the same as the authentication userid
                                  if user is successfully added using the registerUser method in controller
                                  user will be redirected to the login page
                                */
                                String userid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                                User newUser = controller.registerUser(userid, fName, lName, email, pass);
                                if (newUser != null) {
                                    Toast.makeText(Registration.this, "Registration Successful.", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                    finishAffinity();
                                    Intent nlIntent = new Intent(Registration.this, Login.class);
                                    startActivity(nlIntent);
                                } else {
                                    Toast.makeText(Registration.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                                    mAuth.getCurrentUser().delete();
                                }
                            } else {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    // Display Toast message if email is already registered
                                    Toast.makeText(getApplicationContext(), "Email already registered", Toast.LENGTH_SHORT).show();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Registration.this, "Invalid Email or Password.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
            }
        });
    }
}