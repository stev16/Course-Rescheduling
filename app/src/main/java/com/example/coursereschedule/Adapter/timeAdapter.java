package com.example.coursereschedule.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursereschedule.Model.timeModel;
import com.example.coursereschedule.R;
import com.example.coursereschedule.detailOfClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class timeAdapter extends RecyclerView.Adapter<timeAdapter.timeViewHolder> {

    Context context;
    List<timeModel> timeModelList;
    int lastClickedPosition = -1;
    DatabaseReference databaseReference;

    public timeAdapter(Context context, List<timeModel> timeModelList) {
        this.context = context;
        this.timeModelList = timeModelList;

    }

    @NonNull
    @Override
    public timeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new timeViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_time_recyclerview_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull timeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.timeText.setText(timeModelList.get(position).getTimeOn());



        if (lastClickedPosition == position) {
            holder.itemView.setBackground(context.getDrawable(R.drawable.time_selected_bg));
        } else {
            holder.itemView.setBackground(context.getDrawable(R.drawable.time_bg));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lastClickedPosition = position;
                notifyDataSetChanged();

                //transfer time to detail of class for intent
                detailOfClass.newTime = String.valueOf(timeModelList.get(position).getTimeOn());

            }
        });

    }

    @Override
    public int getItemCount() {
        return timeModelList.size();
    }

    public class timeViewHolder extends RecyclerView.ViewHolder {

        TextView timeText;

        public timeViewHolder(@NonNull View timeView) {
            super(timeView);

            timeText = timeView.findViewById(R.id.timeText);

        }
    }
}
