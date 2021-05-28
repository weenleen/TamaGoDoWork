package com.example.tamagodowork;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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
    public LocalDateTime getDateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this.taskDeadline), ZoneOffset.UTC);
    }

    public static String getDeadlineString(long milli) {
        LocalDateTime tmp = LocalDateTime.ofInstant(Instant.ofEpochMilli(milli), ZoneOffset.UTC);
        return tmp.format(formatter);
    }

    public String getDeadlineString() {
        return getDeadlineString(this.taskDeadline);
    }

    public String getTimeLeft() {
        LocalDateTime fromDate = LocalDateTime.now();
        LocalDateTime toDate = this.getDateTime();

        int years = (int) ChronoUnit.YEARS.between(fromDate, toDate);
        int months = (int) ChronoUnit.MONTHS.between(fromDate, toDate);
        int days = (int) ChronoUnit.DAYS.between(fromDate, toDate);
        int hours = (int) ChronoUnit.HOURS.between(fromDate, toDate);

        if (years >= 1) {
            return years + " years";
        } else if (months >= 1) {
            return months + " months";
        } else if (days >= 1) {
            return days + " days";
        } else if (hours >= 1) {
            return hours + " hours";
        } else {
            return "less than 1 hour";
        }
    }
}
