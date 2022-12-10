package com.example.coursereschedule.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coursereschedule.Adapter.subjectAdapter;
import com.example.coursereschedule.Model.subjectModel;
import com.example.coursereschedule.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class mondayFragment extends Fragment {

    private RecyclerView subjectViewMonday;
    List<subjectModel> subjectModelList;
    subjectAdapter subjectAdapter;
    FirebaseAuth auth;

    public mondayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View monday = inflater.inflate(R.layout.fragment_monday, container, false);

        // Inflate the layout for this fragment

        subjectViewMonday=monday.findViewById(R.id.subjectViewMonday);
        subjectViewMonday.setLayoutManager(new LinearLayoutManager(monday.getContext()));
        subjectModelList= new ArrayList<>();
        subjectAdapter = new subjectAdapter(monday.getContext(), subjectModelList);
        subjectViewMonday.setAdapter(subjectAdapter);

        auth =FirebaseAuth.getInstance();

        Query db= FirebaseDatabase.getInstance().getReference().child("Timetable").child(auth.getCurrentUser().getUid())
                .child("Subjects").orderByChild("subjectDay").equalTo("Monday");

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot subjectSnapshot: snapshot.getChildren()){
                    subjectModel subject= new subjectModel();

                    subject.setSubjectCode(subjectSnapshot.child("subjectCode").getValue().toString());
                    subject.setSubjectName(subjectSnapshot.child("subjectName").getValue().toString());
                    subject.setSubjectTime(subjectSnapshot.child("subjectTime").getValue().toString());
                    subject.setSubjectClass(subjectSnapshot.child("subjectClass").getValue().toString());
                    subject.setSubjectType(subjectSnapshot.child("subjectType").getValue().toString());
                    subject.setSubjectDay(subjectSnapshot.child("subjectDay").getValue().toString());
                    subjectModelList.add(subject);

                }
                subjectAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });

        return monday;
    }
}