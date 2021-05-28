package com.example.tamagodowork;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Supposed to be the logic of each task in the task list
 */
public class Task {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private String taskName, taskDesc, key;
    private long taskDeadline;

    public Task() { }

    public Task(String taskName, long deadline, String taskDesc, String key) {
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

    public long getTaskDeadline() { return this.taskDeadline; }

    public String getKey() { return key; }

    // added setters
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDeadline(long taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public void setKey(String key) { this.key = key; }

    // others
    public static String getDeadlineString(long milli) {
        LocalDateTime tmp = LocalDateTime.ofInstant(Instant.ofEpochMilli(milli), ZoneOffset.UTC);
        return tmp.format(formatter);
    }

    public String getDeadlineString() {
        return getDeadlineString(this.taskDeadline);
    }
}
