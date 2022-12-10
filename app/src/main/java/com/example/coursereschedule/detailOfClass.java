package com.example.coursereschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursereschedule.Adapter.timeAdapter;
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
import com.whygraphics.multilineradiogroup.MultiLineRadioGroup;

import java.util.ArrayList;
import java.util.List;

public class detailOfClass extends AppCompatActivity {

    TextView subjectName, subjectCode, subjectTime, subjectClass, classType, subjectDay;
    Button updateBtn, backBtn, okBtn, okok;

    List<String> classRoom;
    Spinner classSpinner;
    EditText numberOfStudent;

    public static String newTime="00:00 AM";

    String student = "0";
    int chday = 0;
    subjectModel subjectModel= null;

    Intent newSchedule;
    RadioButton monday, tuesday, wednesday, thursday, friday;

    MultiLineRadioGroup multiLineRadioGroup;
    List<timeModel> timeModelList;
    timeAdapter timeAdapter;
    RecyclerView timeRecycler;
    DatabaseReference db;
    String venueSelected;
    FirebaseAuth auth;

    String checkDay;
    String checkSubjectName;
    String checkSubjectDay;
    String checkSubjectCode;
    String subjectType;
    String userID;

    String thisSubjectVenueNow;
    String thisSubjectDayTime;

    ActionBar actionBar;

    AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_class);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Detail Class");

        auth = FirebaseAuth.getInstance();

        subjectName= findViewById(R.id.newSubjectName);
        subjectCode= findViewById(R.id.newSubCode);
        subjectTime= findViewById(R.id.newSubTime);
        subjectClass= findViewById(R.id.newSubVenue);
        classType =findViewById(R.id.detailClassType);
        subjectDay =findViewById(R.id.newSubDay);
        backBtn = findViewById(R.id.backBtn);
        updateBtn= findViewById(R.id.updateBtn);

        numberOfStudent = findViewById(R.id.noOfStudentEditText);
        classSpinner =findViewById(R.id.classSpinner);
        okBtn= findViewById(R.id.okBtn);

        db =FirebaseDatabase.getInstance().getReference();


        //taking object from prev page
        final Object object= getIntent().getSerializableExtra("update");
        if (object instanceof subjectModel){
            subjectModel=(subjectModel) object;
        }

        if (subjectModel!=null){
            subjectCode.setText(subjectModel.getSubjectCode());
            subjectName.setText(subjectModel.getSubjectName());
            subjectClass.setText(subjectModel.getSubjectClass());
            subjectTime.setText(subjectModel.getSubjectTime());
            classType.setText(subjectModel.getSubjectType());
            subjectDay.setText(subjectModel.getSubjectDay());
        }

        //close detail class
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        classRoom = new ArrayList<>();

        //generate the class
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberOfStudent.getText().toString().isEmpty()) {
                    Toast.makeText(detailOfClass.this, "Insert Number of Student Please!", Toast.LENGTH_SHORT).show();
                }
                else{
                    okButton();
                }

            }
        });

        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                venueSelected = adapterView.getItemAtPosition(i).toString();
                multiLineRadioGroup.clearCheck();

                multiLineRadioGroup.setVisibility(View.VISIBLE);
                timeRecycler.setVisibility(View.INVISIBLE);

            }

            public void onNothingSelected(AdapterView adapterView) {
                Toast.makeText(detailOfClass.this,"you need to choose", Toast.LENGTH_SHORT).show();
            }
        });

        //radio button for day pick
        monday = findViewById(R.id.saturday_RB);
        tuesday = findViewById(R.id.sunday_RB);
        wednesday = findViewById(R.id.wednesday_RB);
        thursday = findViewById(R.id.thursday_RB);
        friday = findViewById(R.id.friday_RB);

        //preparation to call the time database
        timeRecycler=findViewById(R.id.timeView);
        timeRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        timeModelList= new ArrayList<>();
        timeAdapter = new timeAdapter(detailOfClass.this, timeModelList);
        timeRecycler.setAdapter(timeAdapter);

        multiLineRadioGroup = findViewById(R.id.RGroup);
        multiLineRadioGroup.setOnCheckedChangeListener(new MultiLineRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ViewGroup view, RadioButton button) {
                //calling radioAction to show time
                timeModelList.clear();
                radioAction(String.valueOf(button.getText()));

                timeRecycler.setVisibility(View.VISIBLE);
            }

        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chday = multiLineRadioGroup.getCheckedRadioButtonId();

                if(classSpinner.getSelectedItem()!= null&& chday >= 0 && timeAdapter.getItemCount()>=0){
                    runChecking();
                }
                else {

                    Toast.makeText(detailOfClass.this, "Please fill in details", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void studentNumber(Integer student){
        Query databaseReference = db.child("Classroom").orderByChild("ClassCapacity").startAt(student).endAt(student+100);

        classRoom.clear();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot classSnapshot: snapshot.getChildren()){
                    String spinnerClass= classSnapshot.child("ClassName").getValue(String.class);
                    classRoom.add(spinnerClass);
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(detailOfClass.this, android.R.layout.simple_spinner_item, classRoom);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                classSpinner.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void okButton(){
        student = numberOfStudent.getText().toString();
        int finalStudent=Integer.parseInt(student);

        if (finalStudent>=0 && finalStudent<=50){
            int totalStudent = finalStudent-60;
            studentNumber(totalStudent);
        }
        else if (finalStudent>=51 && finalStudent<=100){
            int totalStudent = finalStudent;
            studentNumber(totalStudent);
        }
        else if(finalStudent>100 && finalStudent <200){
            int totalStudent = finalStudent;
            studentNumber(totalStudent);
        }
        else if (finalStudent>=200 && finalStudent<=300){
            int totalStudent = finalStudent+100;
            studentNumber(totalStudent);
        }
        else if(finalStudent>300 && finalStudent <400){
            int totalStudent = finalStudent;
            studentNumber(totalStudent);
        }

        else{
            Toast.makeText(detailOfClass.this, "Please considerate your input", Toast.LENGTH_SHORT).show();
        }

        updateBtn.setVisibility(View.VISIBLE);
    }

    public void radioAction(String day){
        Query dataTime = db.child("Time").child(venueSelected).child(day);

        //clear arraylist
        timeModelList.clear();

        dataTime.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot timeSnapshot: snapshot.getChildren()){
                        timeModel day= new timeModel();

                        day.setTimeOn(timeSnapshot.child("timeOn").getValue().toString());
                        timeModelList.add(day);

                    }
                    timeAdapter.notifyDataSetChanged();
                }
                else {
                    alertSetUp();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void runChecking(){
        checkDay = multiLineRadioGroup.getCheckedRadioButtonText()+", " +newTime;
        thisSubjectDayTime = subjectDay.getText().toString()+", "+subjectTime.getText().toString();
        thisSubjectVenueNow= subjectClass.getText().toString();

        checkSubjectCode= subjectCode.getText().toString();
        checkSubjectName= subjectName.getText().toString();
        userID= auth.getCurrentUser().getUid();
        checkSubjectDay = multiLineRadioGroup.getCheckedRadioButtonText().toString();
        subjectType = classType.getText().toString();

        Query checkData = db.child("GeneralClass").child(venueSelected);

        checkData.orderByKey().equalTo(checkDay).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    Toast.makeText(detailOfClass.this, "Please Choose Different Time or Classroom!", Toast.LENGTH_SHORT).show();
                }
                else{
                    db.child("GeneralClass").child(thisSubjectVenueNow).child(thisSubjectDayTime).removeValue();

                    //save into database
                    generalClassModel generalClassModel = new generalClassModel(checkSubjectCode, checkSubjectName, checkSubjectDay,
                            newTime, venueSelected, subjectType, userID);

                    db.child("GeneralClass").child(venueSelected).child(checkDay).setValue(generalClassModel);

                    updateTimeDatabase();
                    //intent to new updateclass
                    newSchedule = new Intent(detailOfClass.this, updateClass.class);
                    newSchedule.putExtra("subjectName", subjectModel.getSubjectName());
                    newSchedule.putExtra("subjectCode", subjectModel.getSubjectCode());
                    newSchedule.putExtra("classType", subjectModel.getSubjectType());
                    newSchedule.putExtra("subjectDay", multiLineRadioGroup.getCheckedRadioButtonText());
                    newSchedule.putExtra("subjectTime", newTime);
                    newSchedule.putExtra("subjectVenue", venueSelected);
                    newSchedule.putExtra("currentSubjectDay", subjectModel.getSubjectDay());
                    newSchedule.putExtra("currentSubjectTime", subjectModel.getSubjectTime());
                    newSchedule.putExtra("PrevVenue", subjectModel.getSubjectClass());
                    startActivity(newSchedule);

                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateTimeDatabase(){
        Query time = db.child("Time").child(venueSelected).child(checkSubjectDay);
        time.orderByChild("timeOn").equalTo(newTime).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot updatingTime: snapshot.getChildren()) {
                    updatingTime.getRef().removeValue();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void alertSetUp(){
        timeModelList.clear();
        //alert dialog builder
        alertBuilder = new AlertDialog.Builder(this);

        final View alertView = getLayoutInflater().inflate(R.layout.view_alert, null);
        alertBuilder.setView(alertView);

        AlertDialog dialog = alertBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.text_bg);
        dialog.show();

        okok = alertView.findViewById(R.id.okok);

        okok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}