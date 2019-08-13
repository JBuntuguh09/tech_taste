package com.lonewolf.techtaste;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.lonewolf.techtaste.Dialogues.Show_Me;

public class MainActivity extends AppCompatActivity {

    private Button login;
    private TextView register;
    private EditText email, pword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.txtRegister);
        email = findViewById(R.id.edtEmail);
        pword = findViewById(R.id.edtPassword);
        progressBar = findViewById(R.id.progressBar);
        linearLayout =findViewById(R.id.linLogin);


        
        if(auth.getUid()!=null){
            Intent intent = new Intent(MainActivity.this, Dashboard.class);
            startActivity(intent);
        }

        getButtons();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void getButtons() {

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }

    private void logMeIn() {
        auth.signInWithEmailAndPassword(email.getText().toString(), pword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressBar.setVisibility(View.GONE);
                email.setEnabled(true);
                pword.setEnabled(true);
                login.setEnabled(true);
                Toast.makeText(MainActivity.this, "Successfully loggen in", Toast.LENGTH_SHORT).show();
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


}
