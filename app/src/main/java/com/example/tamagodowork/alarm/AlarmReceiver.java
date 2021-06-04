package com.example.tamagodowork.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.example.tamagodowork.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class AlarmReceiver extends BroadcastReceiver {

    // Intents should have the extra strings
    // "key" : ID of the Task
    // "timeLeft": the type of alarm
    @Override
    public void onReceive(Context context, Intent intent) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference ref = firebaseFirestore.collection("Users").document(Uid)
                .collection("Tasks").document(intent.getStringExtra("key"));

        String taskName = ref.get().getResult().get("taskName", String.class);
        String timeLeft = intent.getStringExtra("timeLeft");
        String message = Task.alarmMessages.get(Integer.parseInt(timeLeft));

        // show the notification
        showNotification(context, taskName, message);

        // delete alarm info from Firestore
        ref.collection("Reminders").document(timeLeft).delete();
    }

    private void showNotification(Context context, String taskName, String message) {
        PendingIntent contentIntent = PendingIntent.getActivity(
                context, 0, new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "default")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(taskName)
                        .setContentText(message);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    public static void cancelAlarm(Context context) {

    }
}