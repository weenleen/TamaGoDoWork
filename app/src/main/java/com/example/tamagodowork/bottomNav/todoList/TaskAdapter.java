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
import android.widget.TextView;

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
        private final View taskAction;
        private final Task.Status status;

        public ViewHolder(@NonNull View itemView, Task.Status status) {
            super(itemView);
            this.taskName = itemView.findViewById(R.id.taskName);
            this.taskDesc = itemView.findViewById(R.id.taskDesc);
            this.taskDeadline = itemView.findViewById(R.id.taskDeadline);
            this.taskAction = itemView.findViewById(R.id.taskAction);
            this.status = status;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return this.taskList.get(position).getStatus().getNum();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Task.Status.OVERDUE.getNum()) { // overdue
            return new ViewHolder(
                    LayoutInflater.from(this.context).inflate(R.layout.r_item_task_overdue, parent, false),
                    Task.Status.OVERDUE);
        } else {
            return new ViewHolder(
                    LayoutInflater.from(this.context).inflate(R.layout.r_item_task_ongoing, parent, false),
                    Task.Status.ONGOING);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Task task = taskList.get(position);

        TaskAdapter adapter = this;

        holder.taskName.setText(task.getTaskName());
        GradientDrawable indicator = (GradientDrawable) AppCompatResources.getDrawable(context,
                R.drawable.button_color_picker_small);
        if (indicator != null) {
            indicator.setColor(ContextCompat.getColor(context, task.getColourId()));
            holder.taskName.setCompoundDrawablesWithIntrinsicBounds(indicator, null, null, null);
        }

        holder.taskDesc.setText(task.getTaskDesc());

        // dialog
        holder.itemView.setOnClickListener(v -> {
            DialogTaskDetails dialog = DialogTaskDetails.newInstance(taskList.get(position));
            dialog.show(((AppCompatActivity)context).getSupportFragmentManager(),
                    DialogTaskDetails.TAG);
        });


        Animation anim;

        if (holder.status == Task.Status.ONGOING) {
            holder.taskDeadline.setText(context.getString(R.string.time_left_display,
                    task.getTimeLeft()));
            anim = AnimationUtils.loadAnimation(context, R.anim.anim_task_complete);
        } else {
            anim = AnimationUtils.loadAnimation(context, R.anim.anim_task_dismiss);
        }

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                taskList.remove(position);
                adapter.notifyDataSetChanged();
                MainActivity.userDoc.collection("Tasks")
                        .document(task.getKey()).delete()
                        .addOnSuccessListener(unused -> {
                            if (holder.status == Task.Status.ONGOING) MainActivity.incrXP();
                        });
            }
        });

        holder.taskAction.setOnClickListener(v -> holder.itemView.startAnimation(anim));
    }

    @Override
    public int getItemCount() {
        return this.taskList.size();
    }
}