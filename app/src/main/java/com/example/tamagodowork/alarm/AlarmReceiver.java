package com.example.tamagodowork.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.util.Log;
import android.widget.Toast;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;


public class AlarmReceiver extends BroadcastReceiver {

    public static final List<String> alarmMessages = List.of(
            "is due in 1 hour",
            "is due in 1 day",
            "is due in 2 days");

    // Intents should have the extras
    // "key" : ID of the Task
    // "timeLeft": the type of alarm
    @Override
    public void onReceive(Context context, Intent intent) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

        Log.e("alarmReceive", "HERE ALARM RECEIVED");
        Toast.makeText(context, "Alarm Received", Toast.LENGTH_LONG).show();

        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference ref = firebaseFirestore.collection("Users").document(Uid)
                .collection("Tasks").document(intent.getStringExtra("key"));

        String taskName = intent.getStringExtra("taskName");
        String timeLeft = intent.getStringExtra("timeLeft");
        String message = alarmMessages.get(Integer.parseInt(timeLeft));

        // show the notification
        showNotification(context, taskName, message);

        // delete alarm info from Firestore
        ref.collection("Reminders").document(timeLeft).delete();
    }

    private void showNotification(Context context, String taskName, String message) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(
                context, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "tamagodowork")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(taskName)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(0, mBuilder.build());

        Log.e("notif", "HERE notif sent");
    }

    // Intents should have the extras
    // "key" : ID of the Task
    // "timeLeft": the type of alarm
    // "alarmTime": the time in millis the thing should ring
    public void setAlarm(Context context, Intent intent) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long alarmId = System.currentTimeMillis();
        long alarmTime = intent.getLongExtra("alarmTime", -1);

        if (alarmTime - alarmId < 0) return;

        Intent i = new Intent(context, AlarmReceiver.class);
        i.putExtra("key", intent.getStringExtra("key"));
        i.putExtra("timeLeft", intent.getStringExtra("timeLeft"));
        PendingIntent pi = PendingIntent.getBroadcast(context,
                (int) alarmId, i, PendingIntent.FLAG_ONE_SHOT);

        am.set(AlarmManager.RTC_WAKEUP, alarmTime, pi);

        Toast.makeText(context, "Alarm Set", Toast.LENGTH_LONG).show();
        Log.e("alarmSet", "HERE ALARM SET");

        DocumentReference ref = MainActivity.userDoc.collection("Tasks").document();
        ref.collection("Reminders").document(intent.getStringExtra("timeLeft"))
                .set(Map.of("alarmId", alarmId));
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}