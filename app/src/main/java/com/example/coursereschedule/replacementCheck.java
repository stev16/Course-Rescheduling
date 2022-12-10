package com.example.coursereschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursereschedule.Email.JavaMailAPI;
import com.example.coursereschedule.Model.replacementModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class replacementCheck extends AppCompatActivity {

    TextView repCheckVenue, repCheckCode, repCheckTime, repCheckDay, repCheckName, repCheckDate;
    Button saveBtn;


    DatabaseReference databaseReference;
    FirebaseAuth authentication;
    String subjectCode, subjectDate, subjectTime, subjectDay, subjectName, subjectVenue;

    ActionBar actionBar;

    String checkDay;
    String lecturer, lecturerEmail;

    List<String> studentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replacement_check);


        actionBar = getSupportActionBar();
        actionBar.setTitle("Replacement Check");

        repCheckVenue = findViewById(R.id.repCheckVenue);
        repCheckCode = findViewById(R.id.repCheckCode);
        repCheckTime = findViewById(R.id.repCheckTime);
        repCheckDay = findViewById(R.id.repCheckDay);
        repCheckDate = findViewById(R.id.repCheckDate);
        repCheckName = findViewById(R.id.repCheckName);

        saveBtn = findViewById(R.id.saveRepClass);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        authentication = FirebaseAuth.getInstance();


        Intent intent=getIntent();
        repCheckName.setText(intent.getStringExtra("repCheckName"));
        repCheckCode.setText(intent.getStringExtra("repCheckCode"));
        repCheckDate.setText(intent.getStringExtra("repCheckDate"));
        repCheckDay.setText(intent.getStringExtra("repCheckDay"));
        repCheckTime.setText(intent.getStringExtra("repCheckTime"));
        repCheckVenue.setText(intent.getStringExtra("repCheckVenue"));


        subjectName = intent.getStringExtra("repCheckName");
        subjectCode = intent.getStringExtra("repCheckCode");
        subjectDate =intent.getStringExtra("repCheckDate");
        subjectDay = intent.getStringExtra("repCheckDay");
        subjectTime=intent.getStringExtra("repCheckTime");
        subjectVenue=intent.getStringExtra("repCheckVenue");


        studentEmail = new ArrayList<>();

        Query studentDB = databaseReference.child("Class").child("Class Enrollment").child(subjectCode);
        studentDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot takeSnap:snapshot.getChildren()){
                    for(int i=0; i<= takeSnap.getChildrenCount()-1;i++) {

                        String getIt = takeSnap.child(String.valueOf(i)).getValue(String.class)+"@ucsiuniversity.edu.my";

                        studentEmail.add(getIt+", ");

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //taking lecturer name and email
        Query callDB = databaseReference.child("User").child(authentication.getCurrentUser().getUid());
        callDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("name").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();

                    lecturerEmail = email;
                    lecturer = name;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkingOwnSchedule();
            }
        });


    }

    public void checkingOwnSchedule() {
        checkDay = subjectDate + ", " + subjectTime;



        Query checkOwnSchedule = databaseReference.child("Timetable").child(authentication.getCurrentUser().getUid())
                .child("Replacement");

        checkOwnSchedule.orderByKey().equalTo(checkDay).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Toast.makeText(replacementCheck.this, "Please Choose Different Time or Classroom!",
                            Toast.LENGTH_SHORT).show();

                    databaseReference.child("GeneralClass").child(subjectVenue).child(checkDay).removeValue();

                    finish();
                } else {

                    replacementModel replacementModel = new replacementModel
                            (subjectCode, subjectName, subjectTime, subjectVenue, subjectDate, subjectDay);

                    databaseReference.child("Timetable").child(authentication.getCurrentUser().getUid())
                            .child("Replacement")
                            .child(checkDay).setValue(replacementModel);

                    sendEmail();

                    Intent saveUpdate = new Intent(replacementCheck.this, homePage.class);
                    startActivity(saveUpdate);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onBackPressed(){
        Toast.makeText(replacementCheck.this, "Please save it first!", Toast.LENGTH_SHORT).show();
    }

    public void sendEmail() {
        String toMailLecturer = lecturerEmail;

        String CCEmail = String.join(", ", studentEmail);

        String subject = "Replacement "+subjectCode+" Class On "+subjectDate ;

        String message = "Good Day, "+lecturer+"!\n\n"+
                "Please take a note that you have replacement class on "+subjectDate+" with the following details:\n\n"
                +"Subject Code: "+subjectCode+"\n"
                +"Subject Name: "+subjectName+"\n"
                +"Subject Venue: "+subjectVenue+"\n"
                +"Subject Date: "+subjectDate+"\n"
                +"Subject Day: "+subjectDay+"\n"
                +"Subject Time: "+subjectTime+"\n\n"
                +"I wish you the best for your replacement schedule of the class!\n"+"Thank you." +
                "\n\nRegards,\n\nUCSI" +
                "\nCourse Reschedule Team";

        JavaMailAPI javaMailAPI = new JavaMailAPI(this,toMailLecturer, CCEmail, subject, message);

        javaMailAPI.execute();
    }
}