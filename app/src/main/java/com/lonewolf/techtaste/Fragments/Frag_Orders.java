package com.lonewolf.techtaste.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.lonewolf.techtaste.Dialogues.Show_Me;
import com.lonewolf.techtaste.R;
import com.lonewolf.techtaste.Resources.ShortCut_To;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        try {

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
                            hashMap.put("serviceId", father.getKey().toString());


                            if(father.child("Comment_Extra").exists()){
                                hashMap.put("comment_extra", father.child("Comment_Extra").getValue().toString());
                            }else{
                                hashMap.put("comment_extra", "Empty");
                            }

                            if(father.child("Solution").exists()){
                                hashMap.put("solution", father.child("Solution").getValue().toString());
                            }else{
                                hashMap.put("solution", "Empty");
                            }

                            if(father.child("Solution_Extra").exists()){
                                hashMap.put("solution_extra", father.child("Solution_Extra").getValue().toString());
                            }else{
                                hashMap.put("solution_extra", "Empty");
                            }


                            arrayList.add(hashMap);

                        }
                    }
                    if(arrayList.size()>0 && getActivity()!=null){
                        setOrders();
                    }else{
                        if(getActivity()!=null) {
                            Toast.makeText(getActivity(), "No Records found", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                    if(getActivity()!=null) {
                        Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setOrders() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        for(int a=0; a<arrayList.size(); a++){
            final HashMap<String, String> hashMap = arrayList.get(a);

            View view = layoutInflater.inflate(R.layout.layout_show_services_list, linearLayout, false);
            TextView service = view.findViewById(R.id.txtService);
            final TextView title = view.findViewById(R.id.txtTitle);
            TextView date = view.findViewById(R.id.txtDate);
            TextView status = view.findViewById(R.id.txtStatus);

            service.setText(hashMap.get("service"));
            title.setText(hashMap.get("title"));
            date.setText(hashMap.get("date"));
            status.setText(hashMap.get("status"));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> list = new ArrayList<>();
                    list.add(0, hashMap.get("service"));
                    list.add(1, hashMap.get("title"));
                    list.add(2, hashMap.get("date"));
                    list.add(3, hashMap.get("status"));
                    list.add(4, hashMap.get("comment"));
                    list.add(5, hashMap.get("solution"));
                    list.add(6, hashMap.get("comment_extra"));
                    list.add(7, hashMap.get("solution_extra"));
                    list.add(8, hashMap.get("serviceId"));



                    Show_Me.issueDetails(getActivity(), linearLayout, list);

                }
            });

            linearLayout.addView(view);


        }
        progressBar.setVisibility(View.GONE);
    }




}
