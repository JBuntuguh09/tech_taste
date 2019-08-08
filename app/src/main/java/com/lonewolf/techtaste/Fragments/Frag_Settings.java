package com.lonewolf.techtaste.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lonewolf.techtaste.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Settings extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private EditText fname, lname, phone, email;

    public Frag_Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag__settings, container, false);



        return view;
    }

}
