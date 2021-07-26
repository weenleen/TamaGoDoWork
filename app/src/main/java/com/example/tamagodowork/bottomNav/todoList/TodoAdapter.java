package com.example.tamagodowork.bottomNav.todoList;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    public enum AdapterType { TASK_LIST, SCHEDULE }

    private final MainActivity main;
    private final ArrayList<Todo> todoList;
    private final AdapterType adapterType;

    public TodoAdapter(MainActivity main, ArrayList<Todo> todoList, AdapterType adapterType) {
        this.todoList = todoList;
        this.main = main;
        this.adapterType = adapterType;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView todoName, todoDeadline;
        private final ImageButton todoAction;
        private final Todo.Status status;

        public ViewHolder(@NonNull View itemView, Todo.Status status) {
            super(itemView);
            this.todoName = itemView.findViewById(R.id.taskName);
            this.todoDeadline = itemView.findViewById(R.id.taskDeadline);
            this.todoAction = itemView.findViewById(R.id.taskAction);
            this.status = status;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return this.todoList.get(position).getStatus().getNum();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Todo.Status.OVERDUE.getNum()) { // overdue
            return new ViewHolder(
                    LayoutInflater.from(this.main).inflate(R.layout.todo_item_overdue, parent, false),
                    Todo.Status.OVERDUE);
        } else {
            return new ViewHolder(
                    LayoutInflater.from(this.main).inflate(R.layout.todo_item_ongoing, parent, false),
                    Todo.Status.ONGOING);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Todo todo = todoList.get(position);

        TodoAdapter adapter = this;

        holder.todoName.setText(todo.getName());
        GradientDrawable indicator = (GradientDrawable) AppCompatResources.getDrawable(main,
                R.drawable.button_color_picker_small);
        if (indicator != null) {
            indicator.setColor(ContextCompat.getColor(main, todo.getColourId()));
            holder.todoName.setCompoundDrawablesWithIntrinsicBounds(indicator, null, null, null);
        }

        // dialog
        holder.itemView.setOnClickListener(v -> {
            DialogTodoDetails dialog = DialogTodoDetails.newInstance(main, todoList.get(position));
            dialog.show(main.getSupportFragmentManager(), DialogTodoDetails.TAG);
        });


        Animation anim;

        if (holder.status == Todo.Status.ONGOING) {
            String deadlineStr;
            if (this.adapterType == AdapterType.TASK_LIST) {
                deadlineStr = main.getString(R.string.time_left_display, todo.getDateTimeLeft());
            } else {
                deadlineStr = todo.getTimeString();
            }
            holder.todoDeadline.setText(deadlineStr);
            anim = AnimationUtils.loadAnimation(main, R.anim.anim_todo_complete);
        } else {
            anim = AnimationUtils.loadAnimation(main, R.anim.anim_todo_dismiss);
        }

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                todoList.remove(position);
                adapter.notifyDataSetChanged();
                main.userDoc.collection("Todos")
                        .document(String.valueOf(todo.getKey())).delete()
                        .addOnSuccessListener(unused -> {
                            if (holder.status == Todo.Status.ONGOING) main.incrementXP();
                        });
            }
        });

        holder.todoAction.setOnClickListener(v -> holder.itemView.startAnimation(anim));
    }

    @Override
    public int getItemCount() {
        return this.todoList.size();
    }

    public void clear() {
        this.todoList.clear();
        this.notifyDataSetChanged();
    }
}