package com.example.tamagodowork.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.example.tamagodowork.bottomNav.todoList.Todo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AlarmReceiver extends BroadcastReceiver {

    public static final List<String> alarmMessages = List.of(
            "is due in 1 hour",
            "is due in 1 day",
            "is due in 2 days");

    // Intents should have the extras
    // "key" : ID of the Todo
    // "timeLeft": the type of alarm
    @Override
    public void onReceive(Context context, Intent intent) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        int key = intent.getIntExtra("key", Todo.minBound);
        String taskName = intent.getStringExtra("name");
        int alarmType = intent.getIntExtra("alarmType", 0);
        String message = alarmMessages.get(alarmType);

        Log.e("onReceive", "received");

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference ref = firebaseFirestore.collection("Users").document(Uid)
                .collection("Todos").document(String.valueOf(key));

        // show the notification
        showNotification(context, taskName, message);

        // delete alarm info from Firestore
        ref.collection("Reminders").document(String.valueOf(alarmType)).delete();
    }

    private void showNotification(Context context, String name, String message) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(
                context, 0, intent, 0);

        Log.e("notification shown", name);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "tamagodowork")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(name)
                .setContentText(name + " " + message)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(0, mBuilder.build());
    }

    // Intents should have the extras
    // "key" : ID of the Todo
    // "taskName" : name of task
    // "alarmType": the type of alarm
    // "alarmTime": the time in millis the thing should ring
    //
    // "requestCode" : only for edits
    public void setAlarm(Context context, Intent intent) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long currTime = System.currentTimeMillis();
        long alarmTime = intent.getLongExtra("alarmTime", -1);

        if (alarmTime - currTime < 0) return;

        String taskName = intent.getStringExtra("name");
        int alarmType = intent.getIntExtra("alarmType", 0);
        int key = intent.getIntExtra("key", Todo.minBound);
        int requestCode = key * 10 + alarmType;

        Intent i = new Intent(context, AlarmReceiver.class);
        i.putExtra("name", taskName);
        i.putExtra("key", key);
        i.putExtra("alarmType", alarmType);

//        DocumentReference ref = MainActivity.userDoc.collection("Todos")
//                .document(String.valueOf(key));

//        cancelAlarmIfExists(context, requestCode, i);

        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, i,
                PendingIntent.FLAG_ONE_SHOT);

        am.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pi);
        Log.e("setAlarm", "alarm set");
    }

    public void setOrCancel(Context context, Intent intent) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long currTime = System.currentTimeMillis();
        long alarmTime = intent.getLongExtra("alarmTime", -1);

        if (alarmTime - currTime < 0) return;

        String taskName = intent.getStringExtra("name");
        int alarmType = intent.getIntExtra("alarmType", 0);
        int key = intent.getIntExtra("key", Todo.minBound);
        int requestCode = key * 10 + alarmType;

        Intent i = new Intent(context, AlarmReceiver.class);
        i.putExtra("name", taskName);
        i.putExtra("key", key);
        i.putExtra("alarmType", alarmType);

//        DocumentReference ref = MainActivity.userDoc.collection("Todos")
//                .document(String.valueOf(key));

        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, i,
                PendingIntent.FLAG_ONE_SHOT);

        am.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pi);
        Log.e("setAlarm", "alarm set");


    }

    public void cancelAlarmIfExists(Context context, int requestCode, Intent intent) {
        try {
            Log.e("cancel", "cancel if exists");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.cancel(pendingIntent);
        } catch (Exception e) {
            Log.e("cancel", "exception");
            e.printStackTrace();
        }
    }
}