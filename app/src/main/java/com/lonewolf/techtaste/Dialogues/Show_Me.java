package com.lonewolf.techtaste.Dialogues;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lonewolf.techtaste.R;
import com.lonewolf.techtaste.Resources.ShortCut_To;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Show_Me {

    private static AlertDialog.Builder alert;
    private static AlertDialog dialog;
    private static FirebaseAuth auth;
    private static DatabaseReference databaseReference;
    public static void issueDetails(final Activity activity, final LinearLayout linearLayout, final List<String> list){
        alert = new AlertDialog.Builder(activity);
        dialog = alert.create();
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.layout_view_details, linearLayout, false);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
       // LinearLayout linear = view.findViewById(R.id.linImages);
        ImageView close = view.findViewById(R.id.imgCloseTab);
        ImageView edit = view.findViewById(R.id.imgEdit);
        //TextView tTitle = view.findViewById(R.id.txtTitle);
        final TextView tTitle = view.findViewById(R.id.txtTitle);
        final TextView tIssue = view.findViewById(R.id.txtIssue);
        TextView tStatus = view.findViewById(R.id.txtStatus);
        TextView tSolution = view.findViewById(R.id.txtSolution);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar);
        final Button update = view.findViewById(R.id.btnUpdate);
        final TextInputLayout titleEdt = view.findViewById(R.id.txtInputTitle);
        final TextInputLayout issueEdt = view.findViewById(R.id.txtInputIssue);
        final EditText titletxt = view.findViewById(R.id.edtTitle);
        final EditText issuetxt = view.findViewById(R.id.edtIssue);
        final TextView viewPics = view.findViewById(R.id.txtViewPics);
        final TextView viewSolutionPics = view.findViewById(R.id.txtViewSolutionPics);

//        final ImageView img1 = view.findViewById(R.id.img1);
//        final ImageView img2 = view.findViewById(R.id.img2);
//        final ImageView img3 = view.findViewById(R.id.img3);
//        final ImageView img4 = view.findViewById(R.id.img4);

//        img1.setVisibility(View.GONE);
//        img2.setVisibility(View.GONE);
//        img3.setVisibility(View.GONE);
//        img4.setVisibility(View.GONE);

        String vService = list.get(0);
        String vTitle = list.get(1);
        String vDate = list.get(2);
        String vStatus = list.get(3);
        String vIssue = list.get(4);
        String vSolution = list.get(5);
        String vIssue_Extra = list.get(6);
        String vSolution_Extra = list.get(7);

        tTitle.setText(vTitle);
        tIssue.setText(vIssue);
        titletxt.setText(vTitle);
        issuetxt.setText(vIssue);
        tStatus.setText(vStatus);
        if(vSolution.toLowerCase().equals("empty")){
            tSolution.setText("No Answers yet....");
        }else{
            tSolution.setText(vSolution);
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tTitle.setVisibility(View.GONE);
                tIssue.setVisibility(View.GONE);
                titleEdt.setVisibility(View.VISIBLE);
                issueEdt.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                titleEdt.setEnabled(false);
                issueEdt.setEnabled(false);
                update.setEnabled(false);
//                Object oTit = titletxt.getText().toString();
//                Object oComm = titletxt.getText().toString();
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("Title", titletxt.getText().toString());
                hashMap.put("Comment", issuetxt.getText().toString());

                databaseReference.child("Request").child(auth.getCurrentUser().getUid()).child(list.get(0)).child(list.get(8)).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        tTitle.setVisibility(View.VISIBLE);
                        tIssue.setVisibility(View.VISIBLE);
                        titleEdt.setVisibility(View.GONE);
                        issueEdt.setVisibility(View.GONE);
                        update.setVisibility(View.GONE);
                        titleEdt.setEnabled(true);
                        issueEdt.setEnabled(true);
                        update.setEnabled(true);
                        tTitle.setText(titletxt.getText().toString());
                        tIssue.setText(issuetxt.getText().toString());
                        Toast.makeText(activity, "Successfully Updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        databaseReference.child("Pictures").child(auth.getCurrentUser().getUid()).child(list.get(8)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("My_Images").exists()) {
                    final String[] pics = dataSnapshot.child("My_Images").getValue().toString().split(",");



                    final List list1 = Arrays.asList(pics);
                    int num = pics.length;
                    if(pics.length>0){
                        String view_Pic = "Tap to view "+num+" attached pictures";
                       viewPics.setText(view_Pic);
                       viewPics.setVisibility(View.VISIBLE);
                    }else{
                        viewPics.setVisibility(View.GONE);
                    }


                    viewPics.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pics(activity, linearLayout, list1);
                        }
                    });


                }
                if(dataSnapshot.child("My_Solution_Images").exists()) {

                    final String[] pics2 = dataSnapshot.child("My_Solution_Images").getValue().toString().split(",");


                    final List list1 = Arrays.asList(pics2);
                    int num = pics2.length;

                    if(pics2.length>0){
                        String view_Pic = "Tap to view "+pics2.length+" attached pictures";
                        viewSolutionPics.setText(view_Pic);
                        viewSolutionPics.setVisibility(View.VISIBLE);

                    }else{
                        viewSolutionPics.setVisibility(View.GONE);
                    }


                    viewSolutionPics.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pics(activity, linearLayout, list1);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(activity, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        dialog.setView(view);
        dialog.show();

    }

    public static void pics(Activity activity, LinearLayout linearLayout, final List<String> listLinks) {
         alert = new AlertDialog.Builder(activity);
         dialog = alert.create();
        LayoutInflater inflater = LayoutInflater.from(activity);
        final View view = inflater.inflate(R.layout.layout_full_images, linearLayout, false);

        final int[] num = {0};

        ImageView imgClose = view.findViewById(R.id.txtCloseTab);
        //TextView tTitle = view.findViewById(R.id.txtTitle);
        final ImageView image = view.findViewById(R.id.imgPic);
        final ImageView forward = view.findViewById(R.id.imgForward);
        final ImageView back = view.findViewById(R.id.imgBack);

        //tTitle.setText(title);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final List<Integer> lastList = new ArrayList<>();
      //  for (int a = 0; a < listLinks.size(); a++) {
//            final List<Uri> listCheck = new ArrayList<>();
        Bitmap bitmap = ShortCut_To.decodeBase64(listLinks.get(num[0]));
        image.setImageBitmap(bitmap);
//
//            ImageView remove = views.findViewById(R.id.imgClose);

          forward.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  num[0] = num[0] +1;
                  Bitmap bitmap = ShortCut_To.decodeBase64(listLinks.get(num[0]));
                  image.setImageBitmap(bitmap);


                  if(num[0]==listLinks.size()-1){
                      forward.setVisibility(View.GONE);
                      back.setVisibility(View.VISIBLE);
                  }else {
                      forward.setVisibility(View.VISIBLE);
                      back.setVisibility(View.VISIBLE);
                  }
              }
          });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num[0] = num[0] -1;
                Bitmap bitmap = ShortCut_To.decodeBase64(listLinks.get(num[0]));
                image.setImageBitmap(bitmap);

                if(num[0]==0){
                    back.setVisibility(View.GONE);
                    forward.setVisibility(View.VISIBLE);
                }else {
                    back.setVisibility(View.VISIBLE);
                    forward.setVisibility(View.VISIBLE);

                }
            }
        });


        dialog.setView(view);
        dialog.show();

    }

    public static void help(Activity activity, LinearLayout linearLayout){
        alert = new AlertDialog.Builder(activity);
         dialog = alert.create();
        LayoutInflater inflater = LayoutInflater.from(activity);
        final View view = inflater.inflate(R.layout.layout_help, linearLayout, false);

        ImageView imgClose = view.findViewById(R.id.imgCloseTab);


        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.setView(view);
        dialog.show();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
    }

}
