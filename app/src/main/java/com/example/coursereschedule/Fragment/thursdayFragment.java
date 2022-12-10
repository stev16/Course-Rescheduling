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

public class thursdayFragment extends Fragment {

    private RecyclerView subjectViewThursday;
    List<subjectModel> subjectModelList;
    com.example.coursereschedule.Adapter.subjectAdapter subjectAdapter;
    FirebaseAuth auth;

    public thursdayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View thursday = inflater.inflate(R.layout.fragment_thursday, container, false);

        subjectViewThursday=thursday.findViewById(R.id.subjectViewThursday);
        subjectViewThursday.setLayoutManager(new LinearLayoutManager(thursday.getContext()));
        subjectModelList= new ArrayList<>();
        subjectAdapter = new subjectAdapter(thursday.getContext(), subjectModelList);
        subjectViewThursday.setAdapter(subjectAdapter);

        auth = FirebaseAuth.getInstance();

        Query db= FirebaseDatabase.getInstance().getReference().child("Timetable").child(auth.getCurrentUser().getUid())
                .child("Subjects").orderByChild("subjectDay").equalTo("Thursday");

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





        return thursday;
    }
}