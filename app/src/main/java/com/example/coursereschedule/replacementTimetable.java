package com.example.coursereschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.coursereschedule.Adapter.replacementAdapter;
import com.example.coursereschedule.Model.replacementModel;
import com.example.coursereschedule.Model.subjectModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class replacementTimetable extends AppCompatActivity {


    RecyclerView replacementView;
    List<replacementModel> replacementModelList;
    replacementAdapter replacementAdapter;
    FirebaseAuth auth;
    ActionBar actionBar;
    Button addReplacement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replacement_timetable);


        actionBar = getSupportActionBar();
        actionBar.setTitle("Replacement Class");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //adding class button
        addReplacement = findViewById(R.id.addReplacementClass);

        addReplacement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(replacementTimetable.this, replacementClassDetails.class);
                startActivity(intent);
            }
        });

        //timetable shown
        replacementView= findViewById(R.id.replacement_view);
        replacementView.setLayoutManager(new LinearLayoutManager(this));
        replacementModelList= new ArrayList<>();
        replacementAdapter = new replacementAdapter(this, replacementModelList);
        replacementView.setAdapter(replacementAdapter);

        auth = FirebaseAuth.getInstance();

        Query db= FirebaseDatabase.getInstance().getReference().child("Timetable").child(auth.getCurrentUser().getUid())
                .child("Replacement");

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot subjectSnapshot: snapshot.getChildren()){
                    replacementModel subject= new replacementModel();

                    subject.setSubjectCode(subjectSnapshot.child("subjectCode").getValue().toString());
                    subject.setSubjectName(subjectSnapshot.child("subjectName").getValue().toString());
                    subject.setSubjectTime(subjectSnapshot.child("subjectTime").getValue().toString());
                    subject.setSubjectClass(subjectSnapshot.child("subjectClass").getValue().toString());
                    subject.setSubjectDate(subjectSnapshot.child("subjectDate").getValue().toString());
                    subject.setSubjectDay(subjectSnapshot.child("subjectDay").getValue().toString());
                    replacementModelList.add(subject);

                }
                replacementAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(replacementTimetable.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                Intent intent =new Intent(replacementTimetable.this, homePage.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}