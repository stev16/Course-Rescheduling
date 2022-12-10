package com.example.coursereschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class homePage extends AppCompatActivity {

    ActionBar actionBar;
    public static String userEmail="";
    TextView userName, userID;
    CardView timetable, extraClass, addReplacementClass, logOut;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        actionBar = getSupportActionBar();
        actionBar.setTitle("Home Page");

        extraClass = findViewById(R.id.goExtraClassBtn);
        timetable = findViewById(R.id.goTimetableBtn);
        addReplacementClass = findViewById(R.id.addClassHomeBtn);
        logOut = findViewById(R.id.logOutHomeBtn);

        userID= findViewById(R.id.userIDHomePage);
        userName= findViewById(R.id.userNameHomePage);

        //using database to take user profile
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        //checking user

        Query callDB = databaseReference.child("User").child(firebaseAuth.getCurrentUser().getUid());
        callDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("name").getValue().toString();
                    String user = snapshot.child("userID").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();

                    userName.setText(name);
                    userID.setText(user);
                    userEmail= email;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(homePage.this, "check your call", Toast.LENGTH_SHORT).show();
            }
        });

        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent timetable =new Intent(homePage.this, timetableNavigation.class);
                startActivity(timetable);
            }
        });

        extraClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goingExtraClass =new Intent(homePage.this, replacementTimetable.class);
                startActivity(goingExtraClass);
            }
        });

        addReplacementClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addingReplacementClass = new Intent(new Intent(homePage.this, replacementClassDetails.class));
                startActivity(addingReplacementClass);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent loggingOut= new Intent(homePage.this, loginPage.class);
                loggingOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                loggingOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loggingOut);
            }
        });
    }
}