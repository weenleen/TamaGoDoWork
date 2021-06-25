package com.example.tamagodowork.bottomNav.todoList;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.tamagodowork.R;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * Supposed to be the logic of each task in the task list
 */
public class Task implements Comparable<Task>, Parcelable {

    public enum Status {
        ONGOING(0), OVERDUE(1);

        private final int num;

        Status(int num) { this.num = num; }

        int getNum() { return this.num; }
    }

    public static final int[] colours = new int[] {
            R.color.peach,
            R.color.yellow,
            R.color.green,
            R.color.blue,
            R.color.purple
    };

    public static final String parcelKey = "TaskParcelKey";

    public static final DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm", Locale.ENGLISH);

    private String taskName, taskDesc, key;
    private Long taskDeadline;
    private Integer colourId;

    @SuppressWarnings("unused")
    public Task() { }

    public Task(String taskName, long deadline, String taskDesc, String key, Integer colourId) {
        this.taskName = taskName;
        this.taskDeadline = deadline;
        this.taskDesc = taskDesc;
        this.key = key;
        this.colourId = colourId;
    }

    protected Task(Parcel in) {
        taskName = in.readString();
        taskDesc = in.readString();
        key = in.readString();
        if (in.readByte() == 0) {
            taskDeadline = null;
        } else {
            taskDeadline = in.readLong();
        }
        if (in.readByte() == 0) {
            colourId = null;
        } else {
            colourId = in.readInt();
        }
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

    public int getColourId() {
        if (this.colourId == null) {
            this.colourId = colours[0];
        }
        return this.colourId;
    }

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

    // others
    @Override
    public int compareTo(Task o) {
        return Long.compare(this.getTaskDeadline(), o.getTaskDeadline());
    }

    public LocalDateTime getDateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this.taskDeadline), ZoneId.systemDefault());
    }

    public static String getDeadlineString(long milli) {
        LocalDateTime tmp = LocalDateTime.ofInstant(Instant.ofEpochMilli(milli), ZoneId.systemDefault());
        return tmp.format(formatter);
    }

    public String getDeadlineString() {
        return getDeadlineString(this.taskDeadline);
    }

    /**
     * Returns the time at which the alarm for this task should ring
     *
     * @param type The type of alarm (1 hour before, 1 day etc).
     * @return The time in millis in which the alarm should ring.
     */
    public long getAlarmTime(int type) {
        long alarmTime = this.taskDeadline;

        switch (type) {
            case 0: // 1 hour
                alarmTime -= Duration.ofHours(1).toMillis();
                break;
            case 1: // 1 day
                alarmTime -= Duration.ofDays(1).toMillis();
                break;
            case 2: // 2 days
                alarmTime -= Duration.ofDays(2).toMillis();
                break;
        }

        return alarmTime;
    }

    /**
     * Returns the string stating the amount of time left to complete a non-overdue task.
     *
     * @return Returns the string stating the amount of time left to complete a non-overdue task.
     */
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

    public Status getStatus() {
        if (this.taskDeadline - System.currentTimeMillis() <= 0) {
            return Status.OVERDUE;
        } else {
            return Status.ONGOING;
        }
    }


    /**
     * Parcelable methods.
     */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(taskName);
        dest.writeString(taskDesc);
        dest.writeString(key);
        if (taskDeadline == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(taskDeadline);
        }
        if (colourId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(colourId);
        }
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }

        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }
    };
}
