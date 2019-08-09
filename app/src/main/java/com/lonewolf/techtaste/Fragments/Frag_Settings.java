package com.lonewolf.techtaste.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lonewolf.techtaste.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Settings extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private EditText fname, lname, phone, email;
    private ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private Button update;

    public Frag_Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag__settings, container, false);
        fname = view.findViewById(R.id.edtfname);
        lname = view.findViewById(R.id.edtLastName);
        phone = view.findViewById(R.id.edtPhone);
        email = view.findViewById(R.id.edtEmail);
        update = view.findViewById(R.id.btnUpdate);


        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        getUser();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    private void getUser() {
        databaseReference.child("users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("fname", dataSnapshot.child("First_Name").getValue().toString());
                hashMap.put("lname", dataSnapshot.child("Last_Name").getValue().toString());
                hashMap.put("email", dataSnapshot.child("Email").getValue().toString());
                hashMap.put("phone", dataSnapshot.child("Phone").getValue().toString());
                hashMap.put("password", dataSnapshot.child("Password").getValue().toString());

                fname.setText(dataSnapshot.child("First_Name").getValue().toString());
                lname.setText(dataSnapshot.child("Last_Name").getValue().toString());
                email.setText(dataSnapshot.child("Email").getValue().toString());
                phone.setText(dataSnapshot.child("Phone").getValue().toString());

                arrayList.add(hashMap);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpdate(){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("First_Name", fname.getText().toString());
        hashMap.put("Last_Name", lname.getText().toString());
        hashMap.put("Email", email.getText().toString());
        hashMap.put("Phone", phone.getText().toString());
       // hashMap.put("Password", dataSnapshot.child("Password").getValue().toString());
        if(!hashMap.equals(arrayList.get(0))){
            databaseReference.child("users").child(auth.getCurrentUser().getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    updateProfile("Email");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }) ;
        }
    }

    private void updateProfile(String type){
        if(type.equals("Email")){
            auth.getCurrentUser().updateEmail(email.getText().toString());

        }
    }

}
