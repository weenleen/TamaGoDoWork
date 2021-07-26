package com.example.tamagodowork.bottomNav.todoList;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class DialogTodoDetails extends BottomSheetDialogFragment {

    public static final String TAG = "Todo Details Dialog";

    private BottomSheetDialog dialog;

    private final MainActivity main;
    private final Todo todo;

    /**
     * Private constructor.
     *
     * @param todo The todo that is shown on the dialog.
     */
    private DialogTodoDetails(MainActivity main, Todo todo) {
        this.main = main;
        this.todo = todo;
    }

    /**
     * Sets up all the common views for the dialogs.
     *
     * @param view The content view for the dialogs.
     */
    private void setViews(View view) {
        this.dialog = new BottomSheetDialog(this.main);

        TextView nameView = view.findViewById(R.id.taskName);
        nameView.setText(this.todo.getName());
        GradientDrawable indicator = (GradientDrawable) AppCompatResources.getDrawable(this.main,
                R.drawable.button_color_picker_small);
        if (indicator != null) {
            int color;
            try {
                color = ContextCompat.getColor(main, todo.getColourId());
            } catch (Exception e) {
                todo.setColourKey("PEACH");
                color = ContextCompat.getColor(main, todo.getColourId());
            }
            indicator.setColor(color);
            nameView.setCompoundDrawablesWithIntrinsicBounds(indicator, null, null, null);
        }

        TextView deadlineView = view.findViewById(R.id.taskDeadline);
        deadlineView.setText(this.todo.getDeadlineString());

        TextView descView = view.findViewById(R.id.taskDesc);
        String tmp = this.todo.getDesc();
        if (tmp.contentEquals("")) {
            tmp = "-";
        }
        descView.setText(tmp);

        this.dialog.setContentView(view);
    }

    /**
     * Factory method that determines what type of dialog is shown.
     *
     * @param todo The todo that is shown on the dialog.
     * @return A dialog fragment.
     */
    public static DialogTodoDetails newInstance(MainActivity main, Todo todo) {
        if (todo.getStatus() == Todo.Status.ONGOING) {
            return new OngoingDial(main, todo);
        } else {
            return new OverdueDial(main, todo);
        }
    }

    /**
     * Class for ongoing todos.
     */
    public static class OngoingDial extends DialogTodoDetails {

        public OngoingDial(MainActivity main, Todo todo) {
            super(main, todo);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            View view = View.inflate(super.main, R.layout.dial_todo_ongoing, null);
            super.setViews(view);

            // Reminders
            TextView reminderView = view.findViewById(R.id.taskReminders);
            StringBuilder sb = new StringBuilder();
            sb.append("-");
            for (int i = 0; i < 3; i++) {
                if (super.todo.hasReminderAt(i)) {
                    if (sb.toString().contentEquals("-")) {
                        sb = new StringBuilder();
                    } else {
                        sb.append(", ");
                    }
                    sb.append(Todo.getReminderString(i));
                }
            }
            reminderView.setText(sb.toString());


            // edit button
            Button editBtn = view.findViewById(R.id.edit_button);
            editBtn.setOnClickListener(v -> {
                Intent intent = new Intent(super.main, EditTodoActivity.class);
                intent.putExtra(Todo.parcelKey, super.todo);
                startActivity(intent);
                this.dismiss();
            });

            // delete button
            Button delBtn = view.findViewById(R.id.delete_button);
            delBtn.setOnClickListener(v -> {
                super.todo.clearAlarms(super.main);
                super.main.userDoc.collection("Todos")
                        .document(String.valueOf(super.todo.getKey())).delete()
                        .addOnCompleteListener(task -> this.dismiss());
            });

            return super.dialog;
        }
    }


    /**
     * Class for overdue todos.
     */
    public static class OverdueDial extends DialogTodoDetails {

        public OverdueDial(MainActivity main, Todo todo) {
            super(main, todo);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            View view = View.inflate(super.main, R.layout.dial_todo_overdue, null);
            super.setViews(view);

            return super.dialog;
        }
    }
}
