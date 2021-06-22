package com.example.tamagodowork.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            /* Setting the alarm here */
            AlarmReceiver alarm = new AlarmReceiver();
            alarm.setAlarm(context, intent);
        }
    }
}
