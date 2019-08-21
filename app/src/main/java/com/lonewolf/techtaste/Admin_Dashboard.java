package com.lonewolf.techtaste;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Admin_Dashboard extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private LinearLayout linearLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__dashboard);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        linearLayout = findViewById(R.id.linOrders);
        progressBar = findViewById(R.id.progressBar);

        getOrders();
    }

    private void getOrders() {

        databaseReference.child("Request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot grand : dataSnapshot.getChildren()){
                    for(DataSnapshot parent : grand.getChildren()){
                        for(DataSnapshot father : parent.getChildren()){

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("title", father.child("Title").getValue().toString());
                            hashMap.put("comment", father.child("Comment").getValue().toString());
                            hashMap.put("service", father.child("Service").getValue().toString());
                            hashMap.put("date", father.child("Created_Date").getValue().toString());
                            hashMap.put("status", father.child("Status").getValue().toString());
                            hashMap.put("serviceId", father.getKey().toString());
                            hashMap.put("userId", grand.getKey().toString());
                            if(father.child("Username").exists()) {
                                hashMap.put("username", father.child("Username").getValue().toString());
                            }else{
                                hashMap.put("username", "");
                            }

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
                }

                if(arrayList.size()>0 ){
                    setOrders();
                }else{
                    Toast.makeText(Admin_Dashboard.this, "No Records found", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void setOrders() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        for(int a=0; a<arrayList.size(); a++){
            final HashMap<String, String> hashMap = arrayList.get(a);

            View view = layoutInflater.inflate(R.layout.layout_show_services_list, linearLayout, false);
            TextView service = view.findViewById(R.id.txtService);
            final TextView title = view.findViewById(R.id.txtTitle);
            TextView date = view.findViewById(R.id.txtDate);
            TextView status = view.findViewById(R.id.txtStatus);
            TextView username = view.findViewById(R.id.txtName);
            LinearLayout linUsername = view.findViewById(R.id.linUsername);

            service.setText(hashMap.get("service"));
            title.setText(hashMap.get("title"));
            date.setText(hashMap.get("date"));
            status.setText(hashMap.get("status"));
            username.setText(hashMap.get("username"));
            linUsername.setVisibility(View.VISIBLE);

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
                    list.add(8, hashMap.get("userId"));
                    list.add(8, hashMap.get("username"));




                    Show_Me.issueDetails(Admin_Dashboard.this, linearLayout, list);

                }
            });

            linearLayout.addView(view);


        }
        progressBar.setVisibility(View.GONE);
    }
}
