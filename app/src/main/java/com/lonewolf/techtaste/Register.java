package com.lonewolf.techtaste;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lonewolf.techtaste.Resources.Settings;
import com.lonewolf.techtaste.Resources.ShortCut_To;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private EditText fname, lname, phone, email, pword, confirm;
    private Button register;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        settings = new Settings(this);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        fname = findViewById(R.id.edtfname);
        lname = findViewById(R.id.edtlname);
        phone = findViewById(R.id.edtphone);
        email = findViewById(R.id.edtEmail);
        pword = findViewById(R.id.edtPassword);
        confirm = findViewById(R.id.edtConfirm);
        register = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);



        getButtons();


    }

    private void getButtons() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fname.getText().toString().isEmpty()){
                    Toast.makeText(Register.this, "Enter your First Name", Toast.LENGTH_SHORT).show();
                    fname.setError("Enter your First Name");
                }else if(lname.getText().toString().isEmpty()){
                    Toast.makeText(Register.this, "Enter your Last Name", Toast.LENGTH_SHORT).show();
                    lname.setError("Enter your Last Name");
                }else if(phone.getText().toString().isEmpty()){
                    Toast.makeText(Register.this, "Enter your phone number(+233111111111)", Toast.LENGTH_SHORT).show();
                    phone.setError("Enter your phone number(+233111111111)");
                }else if(email.getText().toString().isEmpty()){
                    Toast.makeText(Register.this, "Enter Email Address", Toast.LENGTH_SHORT).show();
                    email.setError("Enter Email Address");
                }else if(!email.getText().toString().contains("@")){
                    Toast.makeText(Register.this, "Entera valid Email Address", Toast.LENGTH_SHORT).show();
                    email.setError("Enter a valid Email Address");
                }else if(pword.getText().toString().isEmpty()){
                    Toast.makeText(Register.this, "Enter your password", Toast.LENGTH_SHORT).show();
                    pword.setError("Enter your password");
                }else if(pword.getText().toString().length()<8){
                    Toast.makeText(Register.this, "Password should be 8 characters or more", Toast.LENGTH_SHORT).show();
                    pword.setError("Password should be 8 characters or more");
                }else if(pword.getText().toString().equals(confirm.getText().toString())){
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    confirm.setError("Passwords do not match");
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    fname.setEnabled(false);
                    lname.setEnabled(false);
                    email.setEnabled(false);
                    pword.setEnabled(false);
                    phone.setEnabled(false);
                    confirm.setEnabled(false);
                    register.setEnabled(false);

                    registerMe();
                }
                ShortCut_To.hideKeyboard(Register.this);
            }
        });
    }

    private void registerMe() {
        auth.createUserWithEmailAndPassword(email.getText().toString(), pword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String userId = auth.getCurrentUser().getUid();

//                UserProfileChangeRequest updateUser = new UserProfileChangeRequest.Builder()
//                        .setDisplayName(fname.getText().toString()+" "+lname.getText().toString())
//                        .build();


                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("First_Name", fname.getText().toString());
                hashMap.put("Last_Name", lname.getText().toString());
                hashMap.put("Email", email.getText().toString());
                hashMap.put("Password", pword.getText().toString());
                hashMap.put("Phone", phone.getText().toString());
                hashMap.put("Created_Date", ShortCut_To.getCurrentDateFormat2());


                databaseReference.child("users").child(userId).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        fname.setEnabled(true);
                        lname.setEnabled(true);
                        email.setEnabled(true);
                        pword.setEnabled(true);
                        phone.setEnabled(true);
                        confirm.setEnabled(true);
                        register.setEnabled(true);
                        settings.setPhoneNum(phone.getText().toString());
                        Toast.makeText(Register.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this, Dashboard.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        fname.setEnabled(true);
                        lname.setEnabled(true);
                        email.setEnabled(true);
                        pword.setEnabled(true);
                        phone.setEnabled(true);
                        confirm.setEnabled(true);
                        register.setEnabled(true);
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                fname.setEnabled(true);
                lname.setEnabled(true);
                email.setEnabled(true);
                pword.setEnabled(true);
                phone.setEnabled(true);
                confirm.setEnabled(true);
                register.setEnabled(true);
                Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
