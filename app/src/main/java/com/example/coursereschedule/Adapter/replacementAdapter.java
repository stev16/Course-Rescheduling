package com.example.coursereschedule.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursereschedule.Email.JavaMailAPI;
import com.example.coursereschedule.Model.replacementModel;
import com.example.coursereschedule.R;
import com.example.coursereschedule.homePage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class replacementAdapter extends RecyclerView.Adapter<replacementAdapter.replacementViewHolder> {


    Context context;
    List<replacementModel> replacementModelList;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    List<String> studentEmail;

    public replacementAdapter(Context context, List<replacementModel> replacementModelList) {
        this.context = context;
        this.replacementModelList = replacementModelList;
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public replacementAdapter.replacementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new replacementViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.replacement_timetable_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull replacementAdapter.replacementViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.subjectCode.setText(replacementModelList.get(position).getSubjectCode());
        holder.subjectName.setText(replacementModelList.get(position).getSubjectName());
        holder.subjectClass.setText(replacementModelList.get(position).getSubjectClass());
        holder.subjectTime.setText(replacementModelList.get(position).getSubjectTime());
        holder.subjectDay.setText(replacementModelList.get(position).getSubjectDay());
        holder.subjectDate.setText(replacementModelList.get(position).getSubjectDate());

        holder.delete_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = FirebaseDatabase.getInstance().getReference();

                String date = replacementModelList.get(position).getSubjectDate();
                String time = replacementModelList.get(position).getSubjectTime();

                String code= replacementModelList.get(position).getSubjectCode();
                String name= replacementModelList.get(position).getSubjectName();

                studentEmail = new ArrayList<>();

                //retrieving student's email
                databaseReference.child("Class").child("Class Enrollment").child(code)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
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

                //conduct deletion to the database
                databaseReference.child("Timetable").child(auth.getCurrentUser().getUid()).child("Replacement")
                        .child(date+", "+time)
                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            databaseReference.child("GeneralClass").child(replacementModelList.get(position).getSubjectClass())
                                    .child(date+", "+time).removeValue();

                            replacementModelList.remove(replacementModelList.get(position));

                            notifyDataSetChanged();
                            Toast.makeText(context, "class deleted", Toast.LENGTH_SHORT).show();


                            //sending email cancellation
                            String CCEmail = String.join(",", studentEmail);
                            String subject = "Update on "+code+" Replacement Class!";
                            String message = "Good Day, "+code+"'s class!\n\n"+
                                    "You have successfully cancelled or conducted your replacement class, with the following details:\n\n"
                                    +"Subject Code: "+code+"\n"
                                    +"Subject Name: "+name+ "\n"
                                    +"Subject Date: "+date+ "\n"
                                    +"Subject Time: "+time+ "\n\n"
                                    +"Wish you have a great day!\n"+"Thank you.\n\n"+
                                    "Regards\n\nCourse Reschedule Team";

                            JavaMailAPI javaMailAPI = new JavaMailAPI(context, homePage.userEmail, CCEmail, subject, message);

                            javaMailAPI.execute();

                        }
                        else {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });


    }

    @Override
    public int getItemCount() {
        return replacementModelList.size();
    }

    public class replacementViewHolder extends RecyclerView.ViewHolder{

        TextView subjectCode;
        TextView subjectName;
        TextView subjectClass;
        TextView subjectTime;
        TextView subjectDay;
        TextView subjectDate;
        ImageView delete_class;

        public replacementViewHolder(@NonNull View replacementView) {
            super(replacementView);

            delete_class= replacementView.findViewById(R.id.delete_imgView);
            subjectCode= replacementView.findViewById(R.id.replacementCode);
            subjectName= replacementView.findViewById(R.id.replacementName);
            subjectClass= replacementView.findViewById(R.id.replacementVenue);
            subjectTime= replacementView.findViewById(R.id.replacementTime);
            subjectDay= replacementView.findViewById(R.id.replacementDay);
            subjectDate= replacementView.findViewById(R.id.replacementDate);
        }
    }

}
