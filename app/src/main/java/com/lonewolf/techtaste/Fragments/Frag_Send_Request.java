package com.lonewolf.techtaste.Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Send_Request extends AppCompatDialogFragment {

    private Spinner spinService;
    private EditText title, comment;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private Button submit, addPics;
    private Settings settings;
    private TextInputLayout textInputLayout;
    private ProgressBar progressBar;
    private TextView welcom;

    private List<String> listLinks= new ArrayList<>();
    private List<String> listLinks2= new ArrayList<>();


    private Uri fileUri;
    private List<Uri> listUri = new ArrayList<Uri>();
    private List<Uri> listUri2 = new ArrayList<Uri>();
    private TextView firstPicsText;
    private int layout , storageId;
    private LinearLayout linearLayout;
    private String myUrlList="", myUrlList2="";

    public Frag_Send_Request() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag__send__request, container, false);
        spinService = view.findViewById(R.id.spinServices);
        title = view.findViewById(R.id.edtTitle);
        comment = view.findViewById(R.id.edtComment);
        submit = view.findViewById(R.id.btnSubmit);
        textInputLayout = view.findViewById(R.id.txtInputComment);
        progressBar = view.findViewById(R.id.progressBar);
        addPics = view.findViewById(R.id.btnAddPic);
        firstPicsText = view.findViewById(R.id.txtPicsLabel);
        welcom = view.findViewById(R.id.txtWelcomeMessage);

        settings = new Settings(getContext());

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getButtons(view);
        getUser();

        return view;
    }

    private void getButtons(final View view) {
        List<String> servuceList = new ArrayList<>();
        for(int a=0; a< ShortCut_To.getServices.length; a++){
            servuceList.add(a, ShortCut_To.getServices[a]);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.layout_spinner_list, servuceList);
        arrayAdapter.setDropDownViewResource(R.layout.layout_dropdown);
        spinService.setAdapter(arrayAdapter);

        addPics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listLinks.size()<3) {
                    uploadButtons();
                }else{
                    Toast.makeText(getActivity(), "You cannot upload more than 3 pictures", Toast.LENGTH_SHORT).show();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinService.getSelectedItemId()==0){
                    Toast.makeText(getActivity(), getActivity().getString(R.string.selectService), Toast.LENGTH_SHORT).show();

                }else if(title.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), getActivity().getString(R.string.enter_title), Toast.LENGTH_SHORT).show();
                    title.setError(getActivity().getString(R.string.enter_title));
                }else if(comment.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), getActivity().getString(R.string.describe), Toast.LENGTH_SHORT).show();
                    comment.setError(getActivity().getString(R.string.describe));
                }else{
                    hideKeyboardFrom(getActivity(), view);
                    progressBar.setVisibility(View.VISIBLE);
                    spinService.setEnabled(false);
                    title.setEnabled(false);
                    comment.setEnabled(false);
                    sendRequest();
                }

            }
        });

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                int num = 50-s.length();
                textInputLayout.setHint("Title - "+num+" characters remaining");
            }
        });

        firstPicsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listUri.size()>0) {
                    showPics();
                }
            }
        });


    }

    private void sendRequest() {
        databaseReference.child("Identifiers").child("Request_Identifier").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final int currId = Integer.parseInt(dataSnapshot.getValue().toString())+1;

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Service", spinService.getSelectedItem().toString());
                hashMap.put("Title", title.getText().toString());
                hashMap.put("Comment", comment.getText().toString());
                hashMap.put("Created_Date", ShortCut_To.getCurrentDateFormat2());
                hashMap.put("Updated_Date", ShortCut_To.getCurrentDateFormat2());
                hashMap.put("UserId", auth.getCurrentUser().getUid());
                hashMap.put("Username", settings.getFullName());
                hashMap.put("Phone_Number", settings.getPhoneNum());
                hashMap.put("Email", settings.getEmailAddress());
                hashMap.put("Status", "Pending");



                databaseReference.child("Request").child(auth.getCurrentUser().getUid()).child(spinService.getSelectedItem().toString()).child(String.valueOf(currId)).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        databaseReference.child("Identifiers").child("Request_Identifier").setValue(currId);
                        if(listLinks.size()>0){
                            HashMap<String, String> hash =new HashMap<>();

                            hash.put("My_Images", listLinks.toString());
                            hash.put("Last_Updated", ShortCut_To.getCurrentDateFormat2());
                            databaseReference.child("Pictures").child(auth.getCurrentUser().getUid()).child(String.valueOf(currId)).setValue(hash);
                        }
                        Toast.makeText(getActivity(), "Successfully Submitted", Toast.LENGTH_SHORT).show();

                        progressBar.setVisibility(View.GONE);
                        spinService.setEnabled(true);
                        title.setEnabled(true);
                        comment.setEnabled(true);
                        spinService.setSelection(0);
                        title.setText("");
                        comment.setText("");
                        firstPicsText.setText("");
                        listLinks.clear();
                        listUri.clear();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        spinService.setEnabled(true);
                        title.setEnabled(true);
                        comment.setEnabled(true);
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                spinService.setEnabled(true);
                title.setEnabled(true);
                comment.setEnabled(true);
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPics(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = alert.create();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.layout_show_pictures_list, linearLayout, false);

        final LinearLayout linear = view.findViewById(R.id.linImages);
        ImageView imgClose = view.findViewById(R.id.txtCloseTab);
        //TextView tTitle = view.findViewById(R.id.txtTitle);

        //tTitle.setText(title);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        for(int a= 0; a<listUri.size(); a++) {
            final List<Uri> listCheck = new ArrayList<>();
            final View views = inflater.inflate(R.layout.layout_show_images, linear, false);
            ImageView image = views.findViewById(R.id.imgPic);
            ImageView remove = views.findViewById(R.id.imgClose);


            Picasso.with(getActivity()).load(listUri.get(a)).into(image);
            final int finalA = a;

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    try {
                        listUri.remove(finalA);
                        listLinks.remove(finalA);
                    }catch (Exception e){
                        listUri.remove(finalA-1);
                        listLinks.remove(finalA-1);
                    }

                    firstPicsText.setText("You have selected "+(listUri.size())+" pictures. \nTap here to view");
                    linear.removeView(views);
//

                }
            });




            linear.addView(views);

        }


        dialog.setView(view);
        dialog.show();
    }

    private void getUser(){
        databaseReference.child("users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                settings.setPhoneNum(dataSnapshot.child("Phone").getValue().toString());
                settings.setFullName(dataSnapshot.child("First_Name").getValue().toString()+" "+dataSnapshot.child("Last_Name").getValue().toString());
                settings.setEmailAddress(dataSnapshot.child("Email").getValue().toString());

                String newWelcome = "Welcome "+settings.getFullName();
                welcom.setText(newWelcome);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uploadButtons(){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            selectFiles();

        }else if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);

        }
    }

    private void selectFiles() {


        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                //.setAspectRatio(1,1)
                .setMinCropWindowSize(500,500)
                .start(getContext(), this);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            selectFiles();
        }else {
            Toast.makeText(getActivity(), "Please allow permissions to access this", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                fileUri = result.getUri();





                File actualImageFile = new File(fileUri.getPath());
                Bitmap compressedImageBitmap = new Compressor(getActivity())
//                        .setMaxWidth(500)
//                        .setMaxHeight(500)
                        //.setQuality(100)
                        .compressToBitmap(actualImageFile);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] thumb_byte = baos.toByteArray();
                String links = Base64.encodeToString(thumb_byte, Base64.DEFAULT);



                    listUri.add(fileUri);
                    listLinks.add(links);
                    firstPicsText.setText("You have selected "+(listUri.size())+" pictures. \nTap here to view");


            }
        }
    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
