package com.lonewolf.techtaste.Fragments;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lonewolf.techtaste.R;
import com.lonewolf.techtaste.Register;
import com.lonewolf.techtaste.Resources.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Settings extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private EditText fname, lname, phone, email;
    private ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private Button update, updPass, updatePass;
    private String newPass, newEmail, newPhone;
    private LinearLayout linearLayout;
    private ProgressBar progress, progressBar, progressBarPass;
    private Settings settings;
    private String userId, mVerificationId;
    private AlertDialog.Builder alert;
    private AlertDialog dialog;
    private TextInputLayout t1,t2,t3;


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
        linearLayout = view.findViewById(R.id.linSettings);
        updPass = view.findViewById(R.id.btnChangePass);
        progressBar = view.findViewById(R.id.progressBar);



        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        getUser();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fname.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Enter first name", Toast.LENGTH_SHORT).show();
                    fname.setError("Enter first name");
                }else if(lname.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Enter last name", Toast.LENGTH_SHORT).show();
                    lname.setError("Enter last name");
                }else if(email.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Enter email address", Toast.LENGTH_SHORT).show();
                    email.setError("Enter email address");
                }else if(!email.getText().toString().contains("@")){
                    Toast.makeText(getActivity(), "Enter email in the correct format eg +233548255903", Toast.LENGTH_SHORT).show();
                    email.setError("Enter email in the correct format eg +233548255903");
                }else if(phone.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Enter a phone number", Toast.LENGTH_SHORT).show();
                }else if(!phone.getText().toString().equals(newPhone)){
                    showPhomeVerify();
                }else if(!email.getText().toString().equals(newEmail)){
                    updateProfile("Email",newEmail, dialog);
                }else{
                    setUpdate();
                }
            }
        });

        updPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMyPassword();
            }
        });
        return view;
    }

    private void getUser() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.child("users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("First_Name", dataSnapshot.child("First_Name").getValue().toString());
                hashMap.put("Last_Name", dataSnapshot.child("Last_Name").getValue().toString());
                hashMap.put("Email", dataSnapshot.child("Email").getValue().toString());
                hashMap.put("Phone", dataSnapshot.child("Phone").getValue().toString());
                hashMap.put("Password", dataSnapshot.child("Password").getValue().toString());

                fname.setText(dataSnapshot.child("First_Name").getValue().toString());
                lname.setText(dataSnapshot.child("Last_Name").getValue().toString());
                email.setText(dataSnapshot.child("Email").getValue().toString());
                phone.setText(dataSnapshot.child("Phone").getValue().toString());
                newPass = dataSnapshot.child("Password").getValue().toString();
                newEmail = dataSnapshot.child("Email").getValue().toString();
                newPhone = dataSnapshot.child("Phone").getValue().toString();

                arrayList.add(hashMap);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setUpdate(){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("First_Name", fname.getText().toString());
        hashMap.put("Last_Name", lname.getText().toString());
        hashMap.put("Email", email.getText().toString());
        hashMap.put("Phone", phone.getText().toString());
        hashMap.put("Password", newPass);
       // hashMap.put("Password", dataSnapshot.child("Password").getValue().toString());
        Log.d("hash1", hashMap.toString());
        Log.d("hash2", arrayList.get(0).toString());

        if(!hashMap.equals(arrayList.get(0))){
            databaseReference.child("users").child(auth.getCurrentUser().getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //AlertDialog dialog = null;
                    //updateProfile("Email", email.getText().toString(), dialog);
                    Toast.makeText(getActivity(), "Successfully updated", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    update.setEnabled(true);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    update.setEnabled(true);
                }
            }) ;
        }else{
            Toast.makeText(getActivity(), "No changes", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            update.setEnabled(true);
        }
    }

    private void updateProfile(String type, final String value, final AlertDialog close){
        if(type.equals("Email")){
            progressBar.setVisibility(View.VISIBLE);
            update.setEnabled(false);
            auth.signInWithEmailAndPassword(newEmail, newPass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    auth.getCurrentUser().updateEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            setUpdate();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            update.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else if(type.equals("Password")){
            progressBarPass.setVisibility(View.VISIBLE);
            updatePass.setEnabled(false);
            auth.signInWithEmailAndPassword(newEmail, newPass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    newPass = value;
                    auth.getCurrentUser().updatePassword(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("Password").setValue(newPass);
                            close.dismiss();
                            Toast.makeText(getActivity(), "Successfully Updated password", Toast.LENGTH_SHORT).show();
                            progressBarPass.setVisibility(View.GONE);
                            updatePass.setEnabled(true);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBarPass.setVisibility(View.GONE);
                            updatePass.setEnabled(true);
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBarPass.setVisibility(View.GONE);
                    updatePass.setEnabled(true);
                }
            });

        }
    }

    private void changeMyPassword(){
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.layout_password_change, linearLayout, false);

        final EditText pass1 = view.findViewById(R.id.edtOldPass);
        final EditText pass2 = view.findViewById(R.id.edtNewPass);
        final EditText pass3 = view.findViewById(R.id.edtConfirmPass);
        t1 = view.findViewById(R.id.txtInput1);
        t2 = view.findViewById(R.id.txtInput2);
        t3 = view.findViewById(R.id.txtInput3);

        progressBarPass = view.findViewById(R.id.progressBar);
        updatePass = view.findViewById(R.id.btnChange);
        final ImageView close = view.findViewById(R.id.imgCloseTab);
        alert = new AlertDialog.Builder(getActivity());
        dialog = alert.create();
        dialog.setView(view);
        dialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pass1.getText().toString().equals(newPass)){
                    Toast.makeText(getActivity(), "Current password entered is wrong", Toast.LENGTH_SHORT).show();
                    pass1.setError("Current password entered is wrong");
                }else if(pass2.getText().toString().length()<8){
                    Toast.makeText(getActivity(), "New password should be 8 characters or more", Toast.LENGTH_SHORT).show();
                    pass2.setError("New password should be 8 characters or more");
                }else if(!pass2.getText().toString().equals(pass3.getText().toString())){
                    Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                }else{

                    updateProfile("Password", pass2.getText().toString(), dialog);
                }
            }
        });

        pass1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    t1.setPasswordVisibilityToggleEnabled(true);
                }else {
                    t1.setPasswordVisibilityToggleEnabled(false);
                }
            }
        });

        pass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    t2.setPasswordVisibilityToggleEnabled(true);
                }else {
                    t2.setPasswordVisibilityToggleEnabled(false);
                }
            }
        });

        pass3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    t3.setPasswordVisibilityToggleEnabled(true);
                }else {
                    t3.setPasswordVisibilityToggleEnabled(false);
                }
            }
        });
    }


    private  void showPhomeVerify(){
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.layout_phone_auth, linearLayout, false);
        final EditText verifyEdt = view.findViewById(R.id.edtVerify);
        ImageView close = view.findViewById(R.id.imgCloseTab);
        final Button submit = view.findViewById(R.id.btnVerify);
        progress = view.findViewById(R.id.progressBar2);

        verifyEdt.setText(phone.getText().toString());
        alert = new AlertDialog.Builder(getActivity());

        dialog = alert.create();
        dialog.setView(view);
        dialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(submit.getText().toString().toLowerCase().equals("verify")){
                    if(verifyEdt.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Enter a number", Toast.LENGTH_SHORT).show();
                    }else {
                        verifyPhoneNumber(verifyEdt, submit);
                    }
                }else if(submit.getText().toString().toLowerCase().equals("submit")){
                    sendCode(verifyEdt);
                }
            }
        });
    }

    private void verifyPhoneNumber(final EditText verifyPhone, final Button send) {
        progress.setVisibility(View.VISIBLE);
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                progress.setVisibility(View.GONE);
                verifyPhone.setText("");
                verifyPhone.setHint("Enter verification code here");
                send.setText("Submit");

                Toast.makeText(getActivity(), "Successfully sent code code. Please wait a few seconds and check your phone for your verificstion code", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onVerificationFailed(FirebaseException e) {
                progress.setVisibility(View.GONE);
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                //mResendToken = token;
                progress.setVisibility(View.GONE);


                Toast.makeText(getActivity(), "Successfully sent code code. Please wait a few seconds and check your phone for your verificstion code", Toast.LENGTH_LONG).show();


                // ...
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                verifyPhone.getText().toString(),
                60,
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks




    }


    private void sendCode(final EditText verifyPhone) {
        progress.setVisibility(View.VISIBLE);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verifyPhone.getText().toString());
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {



                            Toast.makeText(getActivity(), "Successfully verified. Logging in.........", Toast.LENGTH_LONG).show();





                            // ...
                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                progress.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Not Verfied", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }


}
