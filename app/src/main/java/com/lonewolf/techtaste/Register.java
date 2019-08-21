package com.lonewolf.techtaste;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lonewolf.techtaste.Resources.Settings;
import com.lonewolf.techtaste.Resources.ShortCut_To;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {

    private EditText fname, lname, phone, email, pword, confirm, verifyEdt;
    private Button register, submit;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private ProgressBar progressBar, progress;
    private Settings settings;
    private String userId, mVerificationId;
    private AlertDialog.Builder alert;
    private AlertDialog dialog;
    private LinearLayout linearLayout;
    private String number ="";
    private TextInputLayout p1, p2;

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
        linearLayout = findViewById(R.id.linRegister);

        p1 = findViewById(R.id.textInputLayoutPass);
        p2 = findViewById(R.id.textInputLayoutConfirm);

        getButtons();




    }

    private void getButtons() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShortCut_To.hideKeyboard(Register.this);
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
                }else if(!pword.getText().toString().equals(confirm.getText().toString())){
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

                    showPhomeVerify();
                    //registerMe();
                }
                ShortCut_To.hideKeyboard(Register.this);
            }
        });

        pword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    p1.setPasswordVisibilityToggleEnabled(true);
                }else {
                    p1.setPasswordVisibilityToggleEnabled(false);
                }
            }
        });


        confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    p2.setPasswordVisibilityToggleEnabled(true);
                }else {
                    p2.setPasswordVisibilityToggleEnabled(false);
                }
            }
        });
    }

    private void registerMe() {
        auth.createUserWithEmailAndPassword(email.getText().toString(), pword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String userId = auth.getCurrentUser().getUid();
                number = phone.getText().toString();
//                UserProfileChangeRequest updateUser = new UserProfileChangeRequest.Builder()
//                        .setDisplayName(fname.getText().toString()+" "+lname.getText().toString())
//                        .build();


                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("First_Name", fname.getText().toString());
                hashMap.put("Last_Name", lname.getText().toString());
                hashMap.put("Email", email.getText().toString());
                hashMap.put("Password", pword.getText().toString());
                hashMap.put("Phone", number);
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
                        verifyEdt.setEnabled(true);
                        submit.setEnabled(true);
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
                        verifyEdt.setEnabled(true);
                        submit.setEnabled(true);
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
                verifyEdt.setEnabled(true);
                submit.setEnabled(true);
                progress.setVisibility(View.GONE);
                Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  void showPhomeVerify(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.layout_phone_auth, linearLayout, false);
        verifyEdt = view.findViewById(R.id.edtVerify);
        ImageView close = view.findViewById(R.id.imgCloseTab);
        submit = view.findViewById(R.id.btnVerify);
        progress = view.findViewById(R.id.progressBar2);

        verifyEdt.setText(phone.getText().toString());
        alert = new AlertDialog.Builder(this);

        dialog = alert.create();
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(false);

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
                ShortCut_To.hideKeyboard(Register.this);
                if(submit.getText().toString().toLowerCase().equals("verify")){
                    if(verifyEdt.getText().toString().isEmpty()) {
                        Toast.makeText(Register.this, "Enter a number", Toast.LENGTH_SHORT).show();
                    }else {
                        verifyPhoneNumber();
                    }
                }else if(submit.getText().toString().toLowerCase().equals("submit")){
                    sendCode();
                }
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                progressBar.setVisibility(View.GONE);
                fname.setEnabled(true);
                lname.setEnabled(true);
                email.setEnabled(true);
                pword.setEnabled(true);
                phone.setEnabled(true);
                confirm.setEnabled(true);
                register.setEnabled(true);

                Toast.makeText(Register.this, "Cancelled Phone Authentication", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyPhoneNumber() {
        progress.setVisibility(View.VISIBLE);
        verifyEdt.setEnabled(false);
        submit.setEnabled(false);
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                progress.setVisibility(View.GONE);
                verifyEdt.setEnabled(true);
                submit.setEnabled(true);
                verifyEdt.setText("");
                verifyEdt.setHint("Enter verification code here");
                submit.setText("Submit");

                Toast.makeText(Register.this, "Successfully sent code code. Please wait a few seconds and check your phone for your verificstion code", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onVerificationFailed(FirebaseException e) {
                progress.setVisibility(View.GONE);
                verifyEdt.setEnabled(true);
                submit.setEnabled(true);
                Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();

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


                Toast.makeText(Register.this, "Successfully sent code code. Please wait a few seconds and check your phone for your verificstion code", Toast.LENGTH_LONG).show();


                // ...
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                verifyEdt.getText().toString(),
                60,
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks




    }


    private void sendCode() {
        progress.setVisibility(View.VISIBLE);
        verifyEdt.setEnabled(false);
        submit.setEnabled(false);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verifyEdt.getText().toString());
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            //progress.setVisibility(View.GONE);


                            Toast.makeText(Register.this, "Successfully verified. Logging in.........", Toast.LENGTH_LONG).show();
                            //registerUser();
                            registerMe();



                            // ...
                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                progress.setVisibility(View.GONE);
                                Toast.makeText(Register.this, "Not Verfied", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }




}
