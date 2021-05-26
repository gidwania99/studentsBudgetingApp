package com.willteksoftwares.studentsbudgetingapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.willteksoftwares.studentsbudgetingapp.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    private TextView regPageQuestion;
    private TextInputEditText regEmail, regPassword;
    private Button regBtn;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog loader;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        regEmail = findViewById(R.id.regEmail);
        regPassword = findViewById(R.id.regPassword);
        regBtn = findViewById(R.id.regBtn);
        regPageQuestion = findViewById(R.id.regPageQuestion);
        mAuth = FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);

        regPageQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performValidations();
            }
        });
    }

    String osVersion = System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
    String apiLevel= String.valueOf(android.os.Build.VERSION.SDK_INT);
    String deviceName = android.os.Build.DEVICE;
    String model =  android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";
    String date = DateFormat.getDateInstance().format(new Date());

    private void performValidations() {
        final String email = regEmail.getText().toString();
        String password = regPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            regEmail.setError("Email is required!");
            return;
        }
        if (TextUtils.isEmpty(password)){
            regPassword.setError("Password is required!");
            return;
        }else {
            loader.setMessage("Registration in progress, please wait...");
            loader.setCanceledOnTouchOutside(false);
            loader.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("OS Version",osVersion );
                        hashMap.put("API Level", apiLevel);
                        hashMap.put("Device Name", deviceName);
                        hashMap.put("Model", model);
                        hashMap.put("logedInOn", date);
                        hashMap.put("email", mAuth.getCurrentUser().getEmail());

                        reference = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());
                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(RegistrationActivity.this, "registration success", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(RegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                    else {
                        Toast.makeText(RegistrationActivity.this, "Sign up process failed, please try again "+task.getException(), Toast.LENGTH_LONG).show();
                    }
                    loader.dismiss();

                }
            });
        }
    }
}