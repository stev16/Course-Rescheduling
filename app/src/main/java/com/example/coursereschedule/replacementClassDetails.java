package com.example.coursereschedule;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.MenuItem;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursereschedule.Adapter.replacementTimeAdapter;
import com.example.coursereschedule.Model.generalClassModel;
import com.example.coursereschedule.Model.timeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.whygraphics.multilineradiogroup.MultiLineRadioGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class replacementClassDetails extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    DatePickerDialog datePickerDialog;
    int Year, Month, Day;
    Calendar calendar;

    public static String replacementTime = "00:00 AM";
    int chday = 0;

    EditText editTextSubjectCode, editTextNumber;
    TextView textViewDate, replacementSubjectName;
    Spinner spinner;
    MultiLineRadioGroup replacementRG;
    RadioButton sunday_RB, saturday_RB;
    RecyclerView replacementViewTime;
    Button checkBtn, proceedBtn, chooseDateBtn, generateSubjectNameBtn;

    DatabaseReference db;
    replacementTimeAdapter replacementTimeAdapter;
    List<timeModel> timeModelList;
    List<String> replacementVenue;

    String venueChosen;
    FirebaseAuth auth;
    String date;

    ActionBar actionBar;
    String checkDay;
    String replacementDay;
    String replacementSubjectCode;
    String lecturer;
    String takeSubjectCode;
    String replacementDate;
    String scheduledSubjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replacement_class_detail);

        // showing the back button in action bar
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Replacement Class");

        calendar = Calendar.getInstance();

        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        //choosing calendar
        chooseDateBtn = findViewById(R.id.chooseDateBtn);
        chooseDateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                calendarMethod();


            }
        });

        editTextSubjectCode = findViewById(R.id.editTextSubjectCode);
        editTextNumber = findViewById(R.id.editTextNumber);

        InputFilter[] editFilters = editTextSubjectCode.getFilters();
        InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
        newFilters[editFilters.length] = new InputFilter.AllCaps();
        editTextSubjectCode.setFilters(newFilters);


        spinner = findViewById(R.id.spinnerVenue);

        sunday_RB = findViewById(R.id.sunday_RB);
        saturday_RB = findViewById(R.id.saturday_RB);

        replacementViewTime = findViewById(R.id.replacementTimeView);
        replacementSubjectName = findViewById(R.id.replacementSubjectName);

        checkBtn = findViewById(R.id.checkBtn);
        proceedBtn = findViewById(R.id.proceedBtn);
        generateSubjectNameBtn = findViewById(R.id.generateBtn);

        db = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        replacementVenue = new ArrayList<>();


        generateSubjectNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cekSubject = editTextSubjectCode.getText().toString();
                if(TextUtils.isEmpty(cekSubject)){
                    Toast.makeText(replacementClassDetails.this, "Subject code is empty", Toast.LENGTH_SHORT).show();
                    return;

                }

                Query query = db.child("Subject");

                query.orderByChild("SubjectCode1").equalTo(cekSubject).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot replaceSubject : dataSnapshot.getChildren()) {

                                String subjectName = replaceSubject.child("SubjectName").getValue(String.class);
                                String subjectCode = replaceSubject.child("SubjectCode").getValue(String.class);

                                replacementSubjectName.setText(subjectName);
                                takeSubjectCode = subjectCode;
                            }
                                generateSubjectNameBtn.setVisibility(View.INVISIBLE);
                                replacementSubjectName.setVisibility(View.VISIBLE);

                        }
                        else{
                            query.orderByChild("SubjectCode2").equalTo(cekSubject).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        for(DataSnapshot replacing : snapshot.getChildren()) {

                                            String subjectName = replacing.child("SubjectName").getValue(String.class);
                                            String subjectCode = replacing.child("SubjectCode").getValue(String.class);

                                            replacementSubjectName.setText(subjectName);
                                            takeSubjectCode = subjectCode;
                                        }
                                        generateSubjectNameBtn.setVisibility(View.INVISIBLE);
                                        replacementSubjectName.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        Toast.makeText(replacementClassDetails.this
                                                , "Please check your subject code", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        // generate the replacement classroom
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextNumber.getText().toString().isEmpty()) {
                    Toast.makeText(replacementClassDetails.this, "Insert Number of Student Please!"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    generateClass();
                }

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                venueChosen = adapterView.getItemAtPosition(i).toString();

            }

            public void onNothingSelected(AdapterView adapterView) {
                Toast.makeText(replacementClassDetails.this, "Choose your venue", Toast.LENGTH_SHORT).show();
            }
        });

        // generate replacement time
        replacementViewTime.setLayoutManager(new GridLayoutManager(this, 3));
        timeModelList = new ArrayList<>();
        replacementTimeAdapter = new replacementTimeAdapter(replacementClassDetails.this, timeModelList);
        replacementViewTime.setAdapter(replacementTimeAdapter);


        replacementRG = findViewById(R.id.radioGroup);
        replacementRG.setOnCheckedChangeListener(new MultiLineRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ViewGroup view, RadioButton button) {
                // calling radioAction to show time
                radioAction(String.valueOf(button.getText()));
            }

        });

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chday = replacementRG.getCheckedRadioButtonId();

                if (replacementSubjectName.getText().toString().trim().length() >= 1 && spinner.getSelectedItem() != null
                        && chday >= 0 && replacementTimeAdapter.getItemCount() >= 0) {
                    runChecking();
                } else {
                    Toast.makeText(replacementClassDetails.this, "Please fill in details", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void calendarMethod(){
        datePickerDialog = DatePickerDialog.newInstance(replacementClassDetails.this, Year, Month, Day);
        datePickerDialog.setThemeDark(false);
        datePickerDialog.showYearPickerFirst(false);
        datePickerDialog.setTitle("Date Picker");

        // setting Min Date to today date
        Calendar min_date_c = Calendar.getInstance();
        datePickerDialog.setMinDate(min_date_c);

        // setting Max Date to next 2 years
        Calendar max_date_c = Calendar.getInstance();
        max_date_c.set(Calendar.YEAR, Year + 2);
        datePickerDialog.setMaxDate(max_date_c);

        //enable all SUNDAYS and SATURDAYS between Min and Max Dates
        for (Calendar loopdate = min_date_c; min_date_c.before(max_date_c); min_date_c.add(Calendar.DATE,
                1), loopdate = min_date_c) {
            int dayOfWeek = loopdate.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
                Calendar[] selectableDay = new Calendar[1];
                selectableDay[0] = loopdate;
                datePickerDialog.setSelectableDays(selectableDay);
            }
        }

        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialogInterface) {

                Toast.makeText(replacementClassDetails.this, "Datepicker Canceled", Toast.LENGTH_SHORT).show();
            }
        });

        datePickerDialog.show(getSupportFragmentManager(), "DatePickerDialog");
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onDateSet(DatePickerDialog view, int Year, int Month, int Day) {


        date = String.format("%02d",Month + 1) + "-" + String.format("%02d", Day) + "-" + Year;

        textViewDate = findViewById(R.id.textDateView);
        textViewDate.setText(date);

    }

    public void generateClass() {

        String student = editTextNumber.getText().toString();
        int finalStudent = Integer.parseInt(student);

        if (finalStudent >= 0 && finalStudent <= 50) {
            int totalStudent = finalStudent - 60;
            studentNumber(totalStudent);
        } else if (finalStudent >= 51 && finalStudent <= 100) {
            int totalStudent = finalStudent;
            studentNumber(totalStudent);
        } else if (finalStudent > 100 && finalStudent < 200) {
            int totalStudent = finalStudent;
            studentNumber(totalStudent);
        } else if (finalStudent >= 200 && finalStudent <= 300) {
            int totalStudent = finalStudent + 100;
            studentNumber(totalStudent);
        } else if (finalStudent> 300 && finalStudent <400) {
            int totalStudent = finalStudent;
            studentNumber(totalStudent);
        }

        else {
            Toast.makeText(replacementClassDetails.this, "Please considerate your input", Toast.LENGTH_SHORT).show();
        }

    }

    public void studentNumber(Integer student) {
        Query databaseReference = db.child("Classroom").orderByChild("ClassCapacity").startAt(student)
                .endAt(student + 100);

        replacementVenue.clear();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot classSnapshot : snapshot.getChildren()) {
                    String spinnerClass = classSnapshot.child("ClassName").getValue(String.class);
                    replacementVenue.add(spinnerClass);
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(replacementClassDetails.this,
                        android.R.layout.simple_spinner_item, replacementVenue);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spinner.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void radioAction(String day) {
        Query dataTime = db.child("Time").child(day);

        // clear arraylist
        timeModelList.clear();

        dataTime.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot timeSnapshot : snapshot.getChildren()) {
                    timeModel day = new timeModel();

                    day.setTimeOn(timeSnapshot.child("TimeOn").getValue().toString());
                    timeModelList.add(day);

                }
                replacementTimeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });

    }




    public void runChecking() {
        checkDay = textViewDate.getText().toString() + ", " + replacementTime;

        replacementSubjectCode = takeSubjectCode;
        scheduledSubjectName = replacementSubjectName.getText().toString();
        replacementDate = textViewDate.getText().toString();
        replacementDay = replacementRG.getCheckedRadioButtonText().toString();
        String subjectType = "Replacement";


        Query callDB = db.child("User").child(auth.getCurrentUser().getUid());
        callDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("name").getValue().toString();

                    lecturer = name;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Query checkData = db.child("GeneralClass").child(venueChosen);

        checkData.orderByKey().equalTo(checkDay).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Toast.makeText(replacementClassDetails.this, "Please Choose Different Time or Classroom!",
                            Toast.LENGTH_SHORT).show();

                } else {

                    generalClassModel generalClassModel = new generalClassModel
                            (replacementSubjectCode, scheduledSubjectName, replacementDate, replacementTime, venueChosen, subjectType, lecturer);

                    db.child("GeneralClass").child(venueChosen).child(checkDay).setValue(generalClassModel);
                    Toast.makeText(replacementClassDetails.this, "No error", Toast.LENGTH_SHORT).show();


                    Intent replacement = new Intent(replacementClassDetails.this, replacementCheck.class);
                    replacement.putExtra("repCheckCode", replacementSubjectCode);
                    replacement.putExtra("repCheckDay", replacementRG.getCheckedRadioButtonText());
                    replacement.putExtra("repCheckDate", replacementDate);
                    replacement.putExtra("repCheckTime", replacementTime);
                    replacement.putExtra("repCheckVenue", venueChosen);
                    replacement.putExtra("repCheckName", scheduledSubjectName);
                    startActivity(replacement);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
