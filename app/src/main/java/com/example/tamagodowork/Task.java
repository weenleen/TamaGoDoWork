package com.example.tamagodowork;

import java.util.Date;

public class Task {

    public String taskName, taskDesc;
    public Date deadline;

    public Task() {
    }

    public Task(String taskName, String taskDesc, Date deadline) {
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.deadline = deadline;
    }

    public String getDeadline() {
        return this.deadline.toString();
    }
}
