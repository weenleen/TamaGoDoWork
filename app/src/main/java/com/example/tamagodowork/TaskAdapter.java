package com.example.tamagodowork;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    Context context;
    ArrayList<Task> taskList;
    /*
    public TaskAdapter(ArrayList<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }
    */

    public TaskAdapter(Context context, ArrayList<Task> taskList) {
        this.taskList = taskList;
        this.context = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView taskName, taskDesc, taskDeadline;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.taskName = itemView.findViewById(R.id.taskName);
            this.taskDesc = itemView.findViewById(R.id.taskDesc);
            this.taskDeadline = itemView.findViewById(R.id.taskDeadline);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(this.context).inflate(R.layout.task_item, parent, false)
                );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.taskName.setText(taskList.get(position).taskName);
        holder.taskDesc.setText(taskList.get(position).taskDesc);
        holder.taskDeadline.setText(taskList.get(position).taskDeadline);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


}
