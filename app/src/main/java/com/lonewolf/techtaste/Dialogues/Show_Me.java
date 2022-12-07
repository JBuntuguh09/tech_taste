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
import com.lonewolf.techtaste.Resources.Settings;
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
    private static Settings settings;
    private static int checkState = 0;
    private static String newComment;


    public static void issueDetails(final Activity activity, final LinearLayout linearLayout, final List<String> list){
        alert = new AlertDialog.Builder(activity);
        dialog = alert.create();
        LayoutInflater inflater = LayoutInflater.from(activity);
        final View view = inflater.inflate(R.layout.layout_view_details, linearLayout, false);

        settings = new Settings(activity);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
       // LinearLayout linear = view.findViewById(R.id.linImages);
        ImageView close = view.findViewById(R.id.imgCloseTab);
        final ImageView edit = view.findViewById(R.id.imgEdit);
        //TextView tTitle = view.findViewById(R.id.txtTitle);
        final TextView tTitle = view.findViewById(R.id.txtTitle);
        final TextView tIssue = view.findViewById(R.id.txtIssue);
        TextView tStatus = view.findViewById(R.id.txtStatus);
        final TextView tSolution = view.findViewById(R.id.txtSolution);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar);
        final Button update = view.findViewById(R.id.btnUpdate);
        final TextInputLayout titleEdt = view.findViewById(R.id.txtInputTitle);
        final TextInputLayout issueEdt = view.findViewById(R.id.txtInputIssue);
        final EditText titletxt = view.findViewById(R.id.edtTitle);
        final EditText issuetxt = view.findViewById(R.id.edtIssue);
        final TextView viewPics = view.findViewById(R.id.txtViewPics);
        final TextView viewSolutionPics = view.findViewById(R.id.txtViewSolutionPics);
        final Button addComment = view.findViewById(R.id.btnAddComment);
        final Button cancelComment = view.findViewById(R.id.btnCancelComment);
        final TextInputLayout txtInputComment = view.findViewById(R.id.txtInputAddComment);
        final EditText edtComment = view.findViewById(R.id.edtAddComment);
        Button closeIssue = view.findViewById(R.id.btnResolved);

        final TextInputLayout textInputLayout = view.findViewById(R.id.edtSolutionLabel);
        final EditText sol = view.findViewById(R.id.edtSolution);
        final Button solutionSubmit = view.findViewById(R.id.btnSubmit);


        checkState=0;

        String vService = list.get(0);
        String vTitle = list.get(1);
        String vDate = list.get(2);
        String vStatus = list.get(3);
        final String vIssue = list.get(4);
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
                addComment.setVisibility(View.GONE);

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(titletxt.getText().toString().isEmpty()){
                    Toast.makeText(activity, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                    titletxt.setError("Title cannot be empty");
                }else if(issuetxt.getText().toString().isEmpty()){
                    Toast.makeText(activity, "Issue cannot be empty", Toast.LENGTH_SHORT).show();
                    issuetxt.setError("Issue cannot be empty");
                }else {
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
                            addComment.setVisibility(View.VISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                            addComment.setVisibility(View.VISIBLE);
                        }
                    });
                }
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


        if(settings.getFeaturetype().equals("Admin Issue Details")){
            textInputLayout.setVisibility(View.VISIBLE);
            solutionSubmit.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
            addComment.setVisibility(View.GONE);

            solutionSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(sol.getText().toString().isEmpty()){
                        Toast.makeText(activity, "Enter a solution", Toast.LENGTH_SHORT).show();
                        sol.setError("Enter a solution");
                    }else {
                        answerQ( textInputLayout, solutionSubmit, sol, progressBar, list, activity, tSolution);
                    }

                }
            });
        }else if(settings.getFeaturetype().equals("User Issue Details")){
            edit.setVisibility(View.VISIBLE);
            addComment.setVisibility(View.VISIBLE);
            addComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkState==0){

                        txtInputComment.setVisibility(View.VISIBLE);
                        cancelComment.setVisibility(View.VISIBLE);
                        checkState=1;
                    }else {

                        if(edtComment.getText().toString().isEmpty()) {
                            Toast.makeText(activity, "Enter a comment", Toast.LENGTH_SHORT).show();
                            edtComment.setError("Enter a comment");
                        }else {
                            newComment=issuetxt.getText().toString();
                            addMyComment(addComment, cancelComment, edtComment, txtInputComment, issuetxt, tIssue, progressBar, list, activity, edit);
//                            txtInputComment.setVisibility(View.GONE);
//                            cancelComment.setVisibility(View.GONE);
                            checkState=0;
                        }


                    }
                }
            });

            cancelComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkState=0;
                    txtInputComment.setVisibility(View.GONE);
                    cancelComment.setVisibility(View.INVISIBLE);
                }
            });

            closeIssue.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder alerts = new AlertDialog.Builder(activity);
                    alerts.setTitle("Close Issue");
                    alerts.setMessage("This marks your issue as resolved and archives it. Are you sure you want to do this?");


                    alerts.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogs, int which) {
                            progressBar.setVisibility(View.VISIBLE);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("Service", list.get(0));
                            hashMap.put("Title", list.get(1));
                            hashMap.put("Comment", list.get(4));
                            hashMap.put("Created_Date", list.get(2));
                            hashMap.put("Updated_Date", ShortCut_To.getCurrentDatewithTime());
                            hashMap.put("UserId", list.get(9));
                            hashMap.put("Username", settings.getFullName());
                            hashMap.put("Phone_Number", settings.getPhoneNum());
                            hashMap.put("Email", settings.getEmailAddress());
                            hashMap.put("Status", "Resolved");
                            hashMap.put("Solution", list.get(5));


                            databaseReference.child("Closed_Request").child(list.get(9)).child(list.get(0)).child(list.get(8)).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    databaseReference.child("Request").child(list.get(9)).child(list.get(0)).child(list.get(8)).removeValue();
                                    Toast.makeText(activity, "Successfully Closed", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    dialogs.dismiss();
                                    dialog.dismiss();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            //databaseReference.
                        }
                    });

                    alerts.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogs, int which) {
                            dialogs.dismiss();

                        }
                    });


                    alerts.create();
                    alerts.show();
                    return false;
                }


            });

        }

        dialog.setView(view);
        dialog.show();

    }

    private static void addMyComment(final Button addComment, final Button cancelComment, final EditText edtComment, final TextInputLayout txtInputComment, TextView currentIssue, final TextView tIssue, final ProgressBar progressBar, List<String> list, final Activity activity, final ImageView edit) {
        progressBar.setVisibility(View.VISIBLE);
        edtComment.setEnabled(false);
        cancelComment.setEnabled(false);
        addComment.setEnabled(false);

         final String newComm = newComment+"\n\nA.I.\n"+edtComment.getText().toString();
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("Comment", newComm);




        databaseReference.child("Request").child(auth.getCurrentUser().getUid()).child(list.get(0)).child(list.get(8)).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(activity, "Successfully added comment", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                edtComment.setEnabled(true);
                cancelComment.setEnabled(true);
                addComment.setEnabled(true);

                txtInputComment.setVisibility(View.GONE);
                cancelComment.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
                tIssue.setText(newComm);
                edtComment.setText("");
                newComment = newComm;
                dialog.dismiss();





            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                edtComment.setEnabled(true);
                cancelComment.setEnabled(true);
                addComment.setEnabled(true);
                edit.setVisibility(View.VISIBLE);
            }
        });

    }

    private static void answerQ(final TextInputLayout textInputLayout, final Button solutionSubmit, final EditText sol, final ProgressBar progressBar, List<String> list, final Activity activity, final TextView tSolution) {
        progressBar.setVisibility(View.VISIBLE);
        solutionSubmit.setEnabled(false);
        sol.setEnabled(false);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("Solution", sol.getText().toString());
        hashMap.put("Status", "Active");


        databaseReference.child("Request").child(list.get(9)).child(list.get(0)).child(list.get(8)).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(activity, "Succssfully Answered", Toast.LENGTH_SHORT).show();
                textInputLayout.setVisibility(View.GONE);
                solutionSubmit.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                tSolution.setText(sol.getText().toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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


    }

}
