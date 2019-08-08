package com.lonewolf.techtaste.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lonewolf.techtaste.R;
import com.lonewolf.techtaste.Resources.ShortCut_To;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Orders extends Fragment {

    private LinearLayout linearLayout;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();


    public Frag_Orders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag__orders, container, false);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        linearLayout = view.findViewById(R.id.linOrders);
        progressBar = view.findViewById(R.id.progressBar);

        getOrders();
        return view;
    }

    private void getOrders() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.child("Request").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                linearLayout.removeAllViews();
                for(DataSnapshot grand : dataSnapshot.getChildren()){
                    for(DataSnapshot father : grand.getChildren()){

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("title", father.child("Title").getValue().toString());
                        hashMap.put("comment", father.child("Comment").getValue().toString());
                        hashMap.put("service", father.child("Service").getValue().toString());
                        hashMap.put("date", father.child("Created_Date").getValue().toString());
                        hashMap.put("status", father.child("Status").getValue().toString());


                      arrayList.add(hashMap);

                    }
                }
                if(arrayList.size()>0){
                    setOrders();
                }else{
                    Toast.makeText(getActivity(), "No Records found", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setOrders() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        for(int a=0; a<arrayList.size(); a++){
            HashMap<String, String> hashMap = arrayList.get(a);

            View view = layoutInflater.inflate(R.layout.layout_show_services_list, linearLayout, false);
            TextView service = view.findViewById(R.id.txtService);
            TextView title = view.findViewById(R.id.txtTitle);
            TextView date = view.findViewById(R.id.txtDate);
            TextView status = view.findViewById(R.id.txtStatus);

            service.setText(hashMap.get("service"));
            title.setText(hashMap.get("title"));
            date.setText(hashMap.get("date"));
            status.setText(hashMap.get("status"));

            linearLayout.addView(view);


        }
        progressBar.setVisibility(View.GONE);
    }




}
