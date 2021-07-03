package com.example.tamagodowork.bottomNav.todoList;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.tamagodowork.R;
import com.example.tamagodowork.alarm.AlarmReceiver;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

/**
 * Supposed to be the logic of each Todo in the Todo list
 */
public class Todo implements Comparable<Todo>, Parcelable {

    public static int minBound = 10000000;

    protected Todo(Parcel in) {
        name = in.readString();
        desc = in.readString();
        key = in.readInt();
        if (in.readByte() == 0) {
            deadline = null;
        } else {
            deadline = in.readLong();
        }
        if (in.readByte() == 0) {
            colourId = null;
        } else {
            colourId = in.readInt();
        }
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeInt(key);
        if (deadline == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(deadline);
        }
        if (colourId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(colourId);
        }
    }

    public enum Status {
        ONGOING(0), OVERDUE(1);

        private final int num;

        Status(int num) { this.num = num; }

        int getNum() { return this.num; }
    }

    public static String getReminderString(int num) {
        switch(num) {
            case 0: return "1 hour before";
            case 1: return "1 day before";
            case 2: return "2 days before";
            default: return "";
        }
    }

    public static final int[] colours = new int[] {
            R.color.peach,
            R.color.yellow,
            R.color.green,
            R.color.blue,
            R.color.purple
    };

    public static final String parcelKey = "TodoParcelKey";

    public static final DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm", Locale.ENGLISH);

    public static final DateTimeFormatter timeFormatter
            = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);

    private String name, desc;
    private int key;
    private List<Boolean> reminders;
    private Long deadline;
    private Integer colourId;

    @SuppressWarnings("unused")
    public Todo() { }

//    public Todo(String name, long deadline, String desc, int key, Integer colourId) {
//        this.name = name;
//        this.deadline = deadline;
//        this.desc = desc;
//        this.key = key;
//        this.colourId = colourId;
//        this.reminders = new ArrayList<>();
//    }

    public Todo(String name, long deadline, String desc, int key, Integer colourId, List<Boolean> reminders) {
        this.name = name;
        this.deadline = deadline;
        this.desc = desc;
        this.key = key;
        this.colourId = colourId;
        if (reminders == null) this.reminders = List.of(false, false, false);
        else this.reminders = reminders;
    }

    // added getters
    public String getName() { return this.name; }

    public String getDesc() {
        return this.desc;
    }

    public long getDeadline() { return this.deadline; }

    public int getKey() { return key; }

    public String getKeyStr() {
        return String.valueOf(this.key);
    }

    public int getColourId() {
        if (this.colourId == null) this.colourId = colours[0];
        return this.colourId;
    }

    public List<Boolean> getReminders() {
        if (this.reminders == null) {
            reminders = List.of(false, false, false);
        }
        return reminders;
    }

    public int getReqCode(int alarmType) {
        return this.key * 10 + alarmType;
    }

    public boolean hasReminderAt(int position) {
        if (this.reminders == null) {
            this.reminders = List.of(false, false, false);
            return false;
        } else if (position < 0 || this.reminders.size() <= position) return false;

        return this.reminders.get(position);
    }

    // added setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public void setDesc(String desc) { this.desc = desc; }

    public void setColourId(int colourId) { this.colourId = colourId; }

    public void setKey(int key) { this.key = key; }

    public void setReminders(List<Boolean> reminders) { this.reminders = reminders; }

    // others
    @Override
    public int compareTo(Todo o) {
        return Long.compare(this.getDeadline(), o.getDeadline());
    }

    public LocalDateTime getDateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this.deadline), ZoneId.systemDefault());
    }

    public static String getDeadlineString(long milli) {
        LocalDateTime tmp = LocalDateTime.ofInstant(Instant.ofEpochMilli(milli), ZoneId.systemDefault());
        return tmp.format(formatter);
    }

    public String getDeadlineString() {
        return getDeadlineString(this.deadline);
    }

    public String getTimeString() {
        LocalDateTime tmp = LocalDateTime.ofInstant(Instant.ofEpochMilli(this.deadline), ZoneId.systemDefault());
        return tmp.format(timeFormatter);
    }

    /**
     * Returns the time at which the alarm for this Todo should ring
     *
     * @param type The type of alarm (1 hour before, 1 day etc).
     * @return The time in millis in which the alarm should ring.
     */
    public long getAlarmTime(int type) {
        long alarmTime = this.deadline;

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
     * Returns the string stating the amount of time left to complete a non-overdue Todo.
     *
     * @return Returns the string stating the amount of time left to complete a non-overdue Todo.
     */
    public String getDateTimeLeft() {
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
        if (this.deadline - System.currentTimeMillis() <= 0) {
            return Status.OVERDUE;
        } else {
            return Status.ONGOING;
        }
    }

    public void clearAlarms(Context context) {
        if (reminders == null) return;
        for (int i = 0; i < reminders.size(); i++) {
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            alarmIntent.putExtra("key", key);
            alarmIntent.putExtra("name", name);
            alarmIntent.putExtra("alarmType", i);
            alarmIntent.putExtra("alarmTime", this.getAlarmTime(i));

            new AlarmReceiver().cancelAlarmIfExists(context, this.getReqCode(i), alarmIntent);
        }
    }
}
