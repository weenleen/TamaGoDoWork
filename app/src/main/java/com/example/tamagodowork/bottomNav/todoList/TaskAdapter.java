package com.example.tamagodowork.bottomNav.todoList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Task> taskList;

    public TaskAdapter(Context context, ArrayList<Task> taskList) {
        this.taskList = taskList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView taskName, taskDesc, taskDeadline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.taskName = itemView.findViewById(R.id.taskName);
            this.taskDesc = itemView.findViewById(R.id.taskDesc);
            this.taskDeadline = itemView.findViewById(R.id.taskDeadline);
        }
    }

    // Non overdue tasks
    public static class ViewHolderNormal extends ViewHolder {
        private final CheckBox checkBox;

        public ViewHolderNormal(@NonNull View itemView) {
            super(itemView);
            this.checkBox = itemView.findViewById(R.id.taskCheckBox);
        }
    }

    // overdue tasks
    public static class ViewHolderOverdue extends ViewHolder {
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
        final int colourId = taskList.get(position).getColourId();
        final int viewType = holder.getItemViewType();

        TaskAdapter adapter = this;

        holder.taskName.setText(name);
        GradientDrawable indicator = (GradientDrawable) AppCompatResources.getDrawable(context,
                R.drawable.button_color_picker_small);
        if (indicator != null) {
            indicator.setColor(ContextCompat.getColor(context, colourId));
            holder.taskName.setCompoundDrawablesWithIntrinsicBounds(indicator, null, null, null);
        }


        holder.taskDesc.setText(desc);

        if (viewType == 1) { // overdue

            holder.taskDeadline.setText("OVERDUE");

            // dialogs
            holder.itemView.setOnClickListener(v -> {
                OverdueDetailsDial dialog = new OverdueDetailsDial(name, deadlineStr, desc, colourId);
                dialog.show(((AppCompatActivity)context).getSupportFragmentManager(),
                        "Show task details");
            });

            // dismiss button
            ((ViewHolderOverdue) holder).imageButton.setOnClickListener(v -> {
                MainActivity.userDoc.collection("Tasks").document(key).delete();
                adapter.notifyDataSetChanged();
            });

        } else { // not overdue

            holder.taskDeadline.setText("in " + taskList.get(position).getTimeLeft());

            // dialogs
            holder.itemView.setOnClickListener(v -> {
                TaskDetailsDial dialog = new TaskDetailsDial(name, deadlineStr, desc, key, colourId);
                dialog.show(((AppCompatActivity)context).getSupportFragmentManager(),
                        "Show task details");
            });


            // checkbox
            ((ViewHolderNormal) holder).checkBox.setOnClickListener(v -> {
                Animation anim = AnimationUtils.loadAnimation(context, R.anim.anim_task_complete);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        taskList.remove(position);
                        adapter.notifyDataSetChanged();
                        MainActivity.userDoc.collection("Tasks").document(key).delete()
                                .addOnSuccessListener(unused -> MainActivity.incrXP())
                                .addOnFailureListener(e -> Toast.makeText(context, "Complete Failed", Toast.LENGTH_SHORT).show());
                    }
                });

                holder.itemView.startAnimation(anim);
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.taskList.size();
    }
}
