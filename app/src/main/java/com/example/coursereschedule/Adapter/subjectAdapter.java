package com.example.coursereschedule.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursereschedule.Model.subjectModel;
import com.example.coursereschedule.R;
import com.example.coursereschedule.detailOfClass;


import java.util.List;

public class subjectAdapter extends RecyclerView.Adapter<subjectAdapter.subjectViewHolder> {

    Context context;
    List<subjectModel> subjectModelList;

    public subjectAdapter(Context context, List<subjectModel> subjectModelList) {
        this.context = context;
        this.subjectModelList = subjectModelList;
    }


    @NonNull
    @Override
    public subjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new subjectViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_component, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull subjectViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.subjectCode.setText(subjectModelList.get(position).getSubjectCode());
        holder.subjectName.setText(subjectModelList.get(position).getSubjectName());
        holder.subjectClass.setText(subjectModelList.get(position).getSubjectClass());
        holder.subjectTime.setText(subjectModelList.get(position).getSubjectTime());
        holder.subjectDay.setText(subjectModelList.get(position).getSubjectDay());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, detailOfClass.class);
                intent.putExtra("update", subjectModelList.get(position));
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return subjectModelList.size();
    }

    public class subjectViewHolder extends RecyclerView.ViewHolder {

        TextView subjectCode;
        TextView subjectName;
        TextView subjectClass;
        TextView subjectTime;
        TextView subjectDay;

        public subjectViewHolder(@NonNull View subjectView) {
            super(subjectView);

            subjectCode= subjectView.findViewById(R.id.subjectCode);
            subjectName= subjectView.findViewById(R.id.subjectName);
            subjectClass= subjectView.findViewById(R.id.classRoom);
            subjectTime= subjectView.findViewById(R.id.subjectTime);
            subjectDay= subjectView.findViewById(R.id.subjectDay);
        }
    }

}
