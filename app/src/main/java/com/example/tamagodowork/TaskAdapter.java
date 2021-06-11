package com.example.tamagodowork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Task> taskList;

    public TaskAdapter(Context context, ArrayList<Task> taskList) {
        this.taskList = taskList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView taskName, taskDesc, taskDeadline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.taskName = itemView.findViewById(R.id.taskName);
            this.taskDesc = itemView.findViewById(R.id.taskDesc);
            this.taskDeadline = itemView.findViewById(R.id.taskDeadline);
        }
    }

    // Non overdue tasks
    public class ViewHolderNormal extends ViewHolder {
        private final CheckBox checkBox;

        public ViewHolderNormal(@NonNull View itemView) {
            super(itemView);
            this.checkBox = itemView.findViewById(R.id.taskCheckBox);
        }
    }

    // overdue tasks
    public class ViewHolderOverdue extends ViewHolder {
        private final ImageButton imageButton;

        public ViewHolderOverdue(@NonNull View itemView) {
            super(itemView);
            this.imageButton = itemView.findViewById(R.id.overdue_dismiss);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (this.taskList.get(position).isOverdue()) return 1;
        else return 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) { // overdue
            return new ViewHolderOverdue(
                    LayoutInflater.from(this.context).inflate(R.layout.overdue_item, parent, false));
        } else {
            return new ViewHolderNormal(
                    LayoutInflater.from(this.context).inflate(R.layout.task_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String key = taskList.get(position).getKey();
        final String name = taskList.get(position).getTaskName();
        final String deadlineStr = taskList.get(position).getDeadlineString();
        final String desc = taskList.get(position).getTaskDesc();
        final int viewType = holder.getItemViewType();

        holder.taskName.setText(name);
        holder.taskDesc.setText(desc);

        if (viewType == 1) { // overdue

            holder.taskDeadline.setText("OVERDUE");

            // dialogs
            holder.itemView.setOnClickListener(v -> {
                OverdueDetailsDial dialog = new OverdueDetailsDial(name, deadlineStr, desc);
                dialog.show(((AppCompatActivity)context).getSupportFragmentManager(),
                        "Show task details");
            });

            // dismiss button
            ((ViewHolderOverdue) holder).imageButton.setOnClickListener(v -> MainActivity.userDoc.collection("Tasks").document(key).delete());

        } else { // not overdue

            holder.taskDeadline.setText("in " + taskList.get(position).getTimeLeft());

            // dialogs
            holder.itemView.setOnClickListener(v -> {
                TaskDetailsDial dialog = new TaskDetailsDial(name, deadlineStr, desc, key);
                dialog.show(((AppCompatActivity)context).getSupportFragmentManager(),
                        "Show task details");
            });

            // checkbox
            ((ViewHolderNormal) holder).checkBox.setOnClickListener(v ->
                    MainActivity.userDoc.collection("Tasks").document(key).delete()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // add xp
                                    MainActivity.incrXP();
                                } else {
                                    Toast.makeText(context, "Complete Failed", Toast.LENGTH_SHORT).show();
                                }
                            }));
        }
    }

    @Override
    public int getItemCount() {
        return this.taskList.size();
    }
}
