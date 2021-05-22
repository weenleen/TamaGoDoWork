package com.example.tamagodowork;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * For the Recycler View thing??
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private ArrayList<Task> taskList;

    public TaskAdapter(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView taskName, taskDesc, taskDeadline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.taskName = itemView.findViewById(R.id.taskName);
            this.taskDesc = itemView.findViewById(R.id.taskDesc);
            this.taskDeadline = itemView.findViewById(R.id.taskDeadline);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.task_item;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.taskName.setText(taskList.get(position).taskName);
        holder.taskDesc.setText(taskList.get(position).taskDesc);
        holder.taskDeadline.setText(taskList.get(position).taskDeadline);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
