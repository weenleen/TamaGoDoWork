package com.example.tamagodowork;

/**
 * Supposed to be the logic of each task in the task list
 */
public class Task {

    private String taskName, taskDeadline, taskDesc, key;

    public Task() { }

    public Task(String taskName, String deadline, String taskDesc, String key) {
        this.taskName = taskName;
        this.taskDeadline = deadline;
        this.taskDesc = taskDesc;
        this.key = key;
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

    public String getKey() { return key; }

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

    public void setKey(String key) { this.key = key; }
}
