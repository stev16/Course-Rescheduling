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
import com.example.coursereschedule.replacementClassDetails;

import java.util.List;

public class replacementTimeAdapter extends RecyclerView.Adapter<replacementTimeAdapter.replacementTimeViewHolder> {

    Context context;
    List<timeModel> timeModelList;
    int lastClickedPosition = -1;

    public replacementTimeAdapter(Context context, List<timeModel> timeModelList) {
        this.context = context;
        this.timeModelList = timeModelList;

    }

    @NonNull
    @Override
    public replacementTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new replacementTimeAdapter.replacementTimeViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_time_recyclerview_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull replacementTimeAdapter.replacementTimeViewHolder holder, @SuppressLint("RecyclerView") int position) {

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

                replacementClassDetails.replacementTime = String.valueOf(timeModelList.get(position).getTimeOn());

            }
        });

    }

    @Override
    public int getItemCount() {
        return timeModelList.size();
    }

    public class replacementTimeViewHolder extends RecyclerView.ViewHolder {

        TextView timeText;

        public replacementTimeViewHolder(@NonNull View timeView) {
            super(timeView);

            timeText = timeView.findViewById(R.id.timeText);

        }
    }
}
