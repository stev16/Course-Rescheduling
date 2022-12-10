package com.example.coursereschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursereschedule.Email.JavaMailAPI;
import com.example.coursereschedule.Model.generalClassModel;
import com.example.coursereschedule.Model.subjectModel;
import com.example.coursereschedule.Model.timeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class updateClass extends AppCompatActivity {

    TextView newSubjectName, newSubjectCode, newSubjectTime, newSubjectClass, newSubjectDay, newClassType, lecturerChecking;
    Button saveBtn;

    DatabaseReference databaseReference;
    FirebaseAuth authentication;
    String SubjectCode, ClassType, ClassTime, ClassDay, SubjectName, ClassRoom;
    String newDaytime, currentDay, currentTime, currentClass;
    String lecturer, lecturerEmail;
    String key;

    long id= 0;
    List<String> studentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_class);

        saveBtn = findViewById(R.id.UpdateandSave);

        authentication = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //data for the new subject
        Intent intent=getIntent();
        newSubjectClass= findViewById(R.id.newSubVenue);
        newSubjectCode= findViewById(R.id.newSubCode);
        newSubjectTime=findViewById(R.id.newSubTime);
        newSubjectName= findViewById(R.id.newSubjectName);
        newSubjectDay= findViewById(R.id.newSubjectDay);
        newClassType= findViewById(R.id.newClassType);

        SubjectCode = intent.getStringExtra("subjectCode");
        ClassType = intent.getStringExtra("classType");
        SubjectName = intent.getStringExtra("subjectName");
        ClassTime = intent.getStringExtra("subjectTime");
        ClassRoom = intent.getStringExtra("subjectVenue");

        currentDay = intent.getStringExtra("currentSubjectDay");
        currentTime = intent.getStringExtra("currentSubjectTime");
        currentClass = intent.getStringExtra("PrevVenue");

        newSubjectName.setText(intent.getStringExtra("subjectName"));
        newSubjectCode.setText(intent.getStringExtra("subjectCode"));
        newClassType.setText(intent.getStringExtra("classType"));
        newSubjectDay.setText(intent.getStringExtra("subjectDay"));
        newSubjectTime.setText(intent.getStringExtra("subjectTime"));
        newSubjectClass.setText(intent.getStringExtra("subjectVenue"));

        studentEmail = new ArrayList<>();
        Query studentDB = databaseReference.child("Class").child("Class Enrollment").child(SubjectCode);
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

                saveData();
            }
        });

    }

    public void saveData(){

        ClassDay = newSubjectDay.getText().toString();
        newDaytime= ClassDay +", "+ ClassTime;

        //save on timetable
        Query saveDB = databaseReference.child("Timetable").child(authentication.getCurrentUser().getUid()).child("Subjects");

        saveDB.orderByKey().equalTo(newDaytime).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    Toast.makeText(updateClass.this, "Your timetable is clashing!",
                            Toast.LENGTH_SHORT).show();

                    databaseReference.child("GeneralClass").child(ClassRoom).child(newDaytime).removeValue();

                    generalClassModel generalClassModel = new generalClassModel(SubjectCode, SubjectName, currentDay, currentTime,
                            currentClass, ClassType, lecturer);

                    databaseReference.child("GeneralClass").child(currentClass).child(currentDay+", "+currentTime).setValue(generalClassModel);

                    Query time = databaseReference.child("Time").child(ClassRoom).child(ClassDay);
                    time.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            switch(ClassTime){
                                case "08:00":
                                    key= ClassDay + "1";
                                    break;
                                case "09:30":
                                    key= ClassDay+"2";
                                    break;
                                case "11:00":
                                    key= ClassDay+"3";
                                    break;
                                case "12:30":
                                    key= ClassDay+"4";
                                    break;
                                case "14:00":
                                    key= ClassDay+"5";
                                    break;
                                case "15:30":
                                    key= ClassDay+"6";
                                    break;
                            }
                            //adding value
                            timeModel timeModel = new timeModel(ClassTime);

                            databaseReference.child("Time").child(ClassRoom).child(ClassDay).child(key).setValue(timeModel);

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                    finish();

                } else {

                    databaseReference.child("Timetable").child(authentication.getCurrentUser().getUid())
                            .child("Subjects").child(currentDay+", "+currentTime).removeValue();

                    subjectModel subjectModel = new subjectModel(SubjectCode, SubjectName,ClassTime,ClassRoom,ClassDay,ClassType);

                    databaseReference.child("Timetable").child(authentication.getCurrentUser().getUid())
                            .child("Subjects").child(newDaytime).setValue(subjectModel);
                    Toast.makeText(updateClass.this, "Your class is updated", Toast.LENGTH_SHORT).show();

                    //update the class
                    saveOnClass();

                    Intent saveUpdate = new Intent(updateClass.this, homePage.class);
                    startActivity(saveUpdate);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void saveOnClass(){

        Query updateClass= databaseReference.child("Class").child("Subject").child(SubjectCode)
                .orderByKey().equalTo(ClassType);

        updateClass.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    databaseReference.child("Class").child("Subject").child(SubjectCode).child(ClassType).removeValue();

                    generalClassModel generalClassModel =new generalClassModel
                            (SubjectCode, SubjectName, ClassDay, ClassTime, ClassRoom,ClassType,lecturer);

                    databaseReference.child("Class").child("Subject").child(SubjectCode)
                            .child(ClassType).setValue(generalClassModel);
                }

                else{
                    generalClassModel generalClassModel =new generalClassModel
                            (SubjectCode, SubjectName, ClassDay, ClassTime, ClassRoom,ClassType,lecturer);

                    databaseReference.child("Class").child("Subject").child(SubjectCode)
                            .child(ClassType).setValue(generalClassModel);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query time = databaseReference.child("Time").child(currentClass).child(currentDay);
        time.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                switch(currentTime){
                    case "08:00":
                        key= currentDay+"1";
                        break;
                    case "09:30":
                        key= currentDay+"2";
                        break;
                    case "11:00":
                        key= currentDay+"3";
                        break;
                    case "12:30":
                        key= currentDay+"4";
                        break;
                    case "14:00":
                        key= currentDay+"5";
                        break;
                    case "15:30":
                        key= currentDay+"6";
                        break;
                }
                //adding value
                timeModel timeModel = new timeModel(currentTime);

                databaseReference.child("Time").child(currentClass).child(currentDay).child(key).setValue(timeModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //send email
        sendUpdateEmail();

    }


    public void sendUpdateEmail() {
        String toMailLecturer = lecturerEmail;

        String CCEmail = String.join(",", studentEmail);
        String subject = "Successfully Reschedule "+SubjectCode+" Class";
        String message = "Good Day, "+lecturer+"!\n\n"+
                "You have successfully update your schedule, with the following details:\n\n"
                +"Subject Code: "+SubjectCode+"\n"
                +"Subject Name: "+SubjectName+ "\n"
                +"Subject Venue: "+ClassRoom+ "\n"
                +"Subject Day: "+newSubjectDay.getText().toString()+ "\n"
                +"Subject Time: "+ClassTime+ "\n"
                +"Subject Type: "+ClassType+ "\n\n"
                +"I wish you the best for your new schedule of the class!\n"+"Thank you.\n\n"+
                "Regards\n\nCourse Reschedule Team";

        JavaMailAPI javaMailAPI = new JavaMailAPI(this,toMailLecturer,CCEmail,subject,message);

        javaMailAPI.execute();
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(updateClass.this, "Please save it first!", Toast.LENGTH_SHORT).show();
    }
}