package com.example.tamagodowork;

import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewTask";
    public static AddNewTask newInstance() {
        return new AddNewTask();
    }
}
