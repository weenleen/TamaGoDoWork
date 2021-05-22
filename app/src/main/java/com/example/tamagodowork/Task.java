package com.example.tamagodowork;

/**
 * Supposed to be the logic of each task in the task list
 */
public class Task {

    public String taskName, taskDesc, taskDeadline;
    public Task() {

    }
    public Task(String taskName, String taskDesc, String deadline) {
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.taskDeadline = deadline;
    }

    // added getters
    public String getTaskName() {
        return this.taskName;
    }

    public String getTaskDesc() {
        return this.taskDesc;
    }

    public String getTaskDeadline() {
        return this.taskDeadline;
    }

    // added setters
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDeadline(String taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }
}
