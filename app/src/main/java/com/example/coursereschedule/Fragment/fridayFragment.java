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

public class fridayFragment extends Fragment {

    private RecyclerView subjectViewFriday;
    List<subjectModel> subjectModelList;
    subjectAdapter subjectAdapter;
    FirebaseAuth auth;

    public fridayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View friday = inflater.inflate(R.layout.fragment_friday, container, false);


        subjectViewFriday=friday.findViewById(R.id.subjectViewFriday);
        subjectViewFriday.setLayoutManager(new LinearLayoutManager(friday.getContext()));
        subjectModelList= new ArrayList<>();
        subjectAdapter = new subjectAdapter(friday.getContext(), subjectModelList);
        subjectViewFriday.setAdapter(subjectAdapter);

        auth = FirebaseAuth.getInstance();

        Query db= FirebaseDatabase.getInstance().getReference().child("Timetable").child(auth.getCurrentUser().getUid())
                .child("Subjects").orderByChild("subjectDay").equalTo("Friday");

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




        return friday;
    }
}