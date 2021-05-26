package com.willteksoftwares.studentsbudgetingapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.willteksoftwares.studentsbudgetingapp.R;

public class AccountActivity extends AppCompatActivity {

    private Toolbar settingsToolbar;
    private TextView loggedInOn,deviceName, deviceModel, osVersion,apiLevel, userEmail;
    private Button logoutBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private String onlineUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_account);

        settingsToolbar = findViewById(R.id.my_Feed_Toolbar);
        setSupportActionBar(settingsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Account");

        loggedInOn = findViewById(R.id.loggedInOn);
        deviceName  =findViewById(R.id.deviceName);
        logoutBtn = findViewById(R.id.logoutBtn);
        deviceModel = findViewById(R.id.deviceModel);
        osVersion = findViewById(R.id.osVersion);
        apiLevel = findViewById(R.id.apiLevel);
        userEmail  = findViewById(R.id.userEmail);

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(onlineUserId);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    String osversion = snapshot.child("OS Version").getValue(String.class);
                    String apilevel = snapshot.child("API Level").getValue(String.class);
                    String devicename = snapshot.child("Device Name").getValue(String.class);
                    String model = snapshot.child("Model").getValue(String.class);
                    String loggedIndate = snapshot.child("logedInOn").getValue(String.class);
                    String email =  snapshot.child("email").getValue(String.class);

                    userEmail.setText("Email: "+email);
                    osVersion.setText("OS Version: "+osversion);
                    apiLevel.setText("API Level: "+apilevel);
                    deviceName.setText("Device Name: "+devicename);
                    deviceModel.setText("Device Model: "+model);
                    loggedInOn.setText("Logged in on: "+loggedIndate);

                }else {
                    Toast.makeText(AccountActivity.this, "Details Unavailable", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(AccountActivity.this)
                        .setTitle("Students Budgeting App")
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                                startActivity( intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}