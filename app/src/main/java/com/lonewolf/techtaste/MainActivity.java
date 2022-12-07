package com.lonewolf.techtaste;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.lonewolf.techtaste.Dialogues.Show_Me;
import com.lonewolf.techtaste.Resources.Settings;

public class MainActivity extends AppCompatActivity {

    private Button login;
    private TextView register, forget;
    private EditText email, pword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private TextInputLayout u1,p1;
    private Settings settings;
    private AlertDialog.Builder alert;
    private AlertDialog dialog;
    private CheckBox loggedIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = new Settings(this);
        auth = FirebaseAuth.getInstance();
        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.txtRegister);
        email = findViewById(R.id.edtEmail);
        pword = findViewById(R.id.edtPassword);
        progressBar = findViewById(R.id.progressBar);
        linearLayout =findViewById(R.id.linLogin);
        p1 = findViewById(R.id.txtInputPassword);
        forget = findViewById(R.id.txtForget);
        loggedIn  = findViewById(R.id.checkboxLoggedIn);

        if(settings.getKeeploggedin()){

            if(auth.getUid()!=null){
                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                startActivity(intent);
            }
        }

        


        getButtons();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void getButtons() {

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setUserrole("");
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setUserrole("");
                if(email.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, (R.string.enter_email), Toast.LENGTH_SHORT).show();
                }else if(pword.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, (R.string.enter_pword), Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    email.setEnabled(false);
                    pword.setEnabled(false);
                    login.setEnabled(false);

                    logMeIn();
                }
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
                }else{
                    p1.setPasswordVisibilityToggleEnabled(false);
                }
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToEmail();
            }
        });
    }

    private void logMeIn() {
        auth.signInWithEmailAndPassword(email.getText().toString(), pword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressBar.setVisibility(View.GONE);
                email.setEnabled(true);
                pword.setEnabled(true);
                login.setEnabled(true);
                if(loggedIn.isChecked()){
                    settings.setKeeploggedin(true);

                }else{
                    settings.setKeeploggedin(false);

                }
                Toast.makeText(MainActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                email.setEnabled(true);
                pword.setEnabled(true);
                login.setEnabled(true);
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sendToEmail() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.layout_forgot_password, linearLayout, false);

        final TextView message = view.findViewById(R.id.txtMessage);
        final EditText emailAddress = view.findViewById(R.id.edtEmail);
        final Button sendMail = view.findViewById(R.id.btnSendMail);
        final TextInputLayout txtInput = view.findViewById(R.id.txtInputForgotPass);
        final ProgressBar progressBarE = view.findViewById(R.id.progressSend);
        ImageView imgClose = view.findViewById(R.id.imgCloseTab);
        alert = new AlertDialog.Builder(this);
        dialog = alert.create();
        dialog.setView(view);
        dialog.show();

        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailAddress.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter an email", Toast.LENGTH_SHORT).show();
                    emailAddress.setError("Enter an email");
                }else if(!emailAddress.getText().toString().contains("@")){
                    Toast.makeText(MainActivity.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                    emailAddress.setError("Enter a valid email");
                }else{
                    progressBarE.setVisibility(View.VISIBLE);
                    emailAddress.setEnabled(false);
                    sendMail.setEnabled(false);
                    auth.sendPasswordResetEmail(emailAddress.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            message.setText("Verification sent. Check your email for password reset link");
                            progressBarE.setVisibility(View.GONE);
                            txtInput.setVisibility(View.GONE);
                            message.setVisibility(View.VISIBLE);
                            Toast.makeText(MainActivity.this, "Successfully sent", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBarE.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            emailAddress.setEnabled(true);
                            sendMail.setEnabled(true);
                        }
                    });
                }
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


}
