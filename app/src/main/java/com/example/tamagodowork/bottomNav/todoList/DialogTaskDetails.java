package com.example.tamagodowork.bottomNav.todoList;

import android.app.Dialog;
import android.content.Context;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

public class DialogTaskDetails extends BottomSheetDialogFragment {

    public static final String TAG = "Task Details Dialog";

    private BottomSheetDialog dialog;

    private Context context;
    private final Task task;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    /**
     * Private constructor.
     *
     * @param task The task that is shown on the dialog.
     */
    private DialogTaskDetails(Task task) {
        this.task = task;
    }

    /**
     * Sets up all the common views for the dialogs.
     *
     * @param view The content view for the dialogs.
     */
    private void setViews(View view) {
        this.dialog = new BottomSheetDialog(this.context);

        TextView nameView = view.findViewById(R.id.taskName);
        nameView.setText(this.task.getTaskName());
        GradientDrawable indicator = (GradientDrawable) AppCompatResources.getDrawable(this.context,
                R.drawable.button_color_picker_small);
        if (indicator != null) {
            int color;
            try {
                color = ContextCompat.getColor(context, task.getColourId());
            } catch (Exception e) {
                task.setColourId(Task.colours[0]);
                color = ContextCompat.getColor(context, Task.colours[0]);
            }
            indicator.setColor(color);
            nameView.setCompoundDrawablesWithIntrinsicBounds(indicator, null, null, null);
        }

        TextView deadlineView = view.findViewById(R.id.taskDeadline);
        deadlineView.setText(this.task.getDeadlineString());

        TextView descView = view.findViewById(R.id.taskDesc);
        String tmp = this.task.getTaskDesc();
        if (tmp.contentEquals("")) {
            tmp = "-";
        }
        descView.setText(tmp);

        this.dialog.setContentView(view);
    }

    /**
     * Factory method that determines what type of dialog is shown.
     *
     * @param task The task that is shown on the dialog.
     * @return A dialog fragment.
     */
    public static DialogTaskDetails newInstance(Task task) {
        if (task.getStatus() == Task.Status.ONGOING) {
            return new OngoingDial(task);
        } else {
            return new OverdueDial(task);
        }
    }

    /**
     * Class for ongoing tasks.
     */
    public static class OngoingDial extends DialogTaskDetails {

        private String reminderTxt = "None";

        public OngoingDial(Task task) {
            super(task);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            View view = View.inflate(super.context, R.layout.dial_task_ongoing, null);
            super.setViews(view);

            final DocumentReference ref = MainActivity.userDoc.collection("Tasks").document(super.task.getKey());

            // Reminders
            TextView reminderView = view.findViewById(R.id.taskReminders);
            for (int i = 0; i < 3; i++) {
                final int index = i;
                ref.collection("Reminders").document(String.valueOf(i)).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot == null || !documentSnapshot.exists()) return;
                            if (reminderTxt.contentEquals("None")) {
                                reminderTxt = Task.getReminderString(index);
                            } else {
                                reminderTxt += ", " + Task.getReminderString(index);
                            }
                            reminderView.setText(reminderTxt);
                        });
            }


            // edit button
            Button editBtn = view.findViewById(R.id.edit_button);
            editBtn.setOnClickListener(v -> {
                Intent intent = new Intent(super.context, EditTaskAct.class);
                intent.putExtra(Task.parcelKey, super.task);
                startActivity(intent);
            });

            // delete button
            Button delBtn = view.findViewById(R.id.delete_button);
            delBtn.setOnClickListener(v -> {
                ref.delete();
                this.dismiss();
            });

            return super.dialog;
        }
    }


    /**
     * Class for overdue tasks.
     */
    public static class OverdueDial extends DialogTaskDetails {

        public OverdueDial(Task task) {
            super(task);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            View view = View.inflate(super.context, R.layout.dial_task_overdue, null);
            super.setViews(view);

            return super.dialog;
        }
    }
}
