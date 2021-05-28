package com.example.tamagodowork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Task> taskList;

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
                LayoutInflater.from(this.context).inflate(R.layout.task_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final String key = taskList.get(position).getKey();
        final String name = taskList.get(position).getTaskName();
        final String deadlineStr = taskList.get(position).getDeadlineString();
        final String desc = taskList.get(position).getTaskDesc();

        holder.taskName.setText(name);
        holder.taskDesc.setText(desc);
        holder.taskDeadline.setText("in " + taskList.get(position).getTimeLeft());

        /** The task details pop up is shown when an item in the list is selected */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskDetailsDial dialog = new TaskDetailsDial(name, deadlineStr, desc, key);
                dialog.show(((AppCompatActivity)context).getSupportFragmentManager(),
                        "Show task details");
            }
        });

        // check box
        CheckBox checkBox = holder.itemView.findViewById(R.id.taskCheckBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.userDoc.collection("Tasks").document(key).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<Void> task) {
                        if (task.isSuccessful()) {
                            // add xp
                            MainActivity.userDoc.update("XP", MainActivity.xp + 10);

                        } else {
                            Toast.makeText(context, "Complete Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.taskList.size();
    }
}
