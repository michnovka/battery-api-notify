package net.michnovka.batteryapinotify.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.michnovka.batteryapinotify.util.SharedPreferenceHelper;

/**
 * Created by kishon on 23,November,2021
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

            SharedPreferenceHelper sharedPreference = new SharedPreferenceHelper(context);

            if (sharedPreference.getBoot() && sharedPreference.getStatus()) {
                Intent service = new Intent(context, BatteryMonitorService.class);
                context.startService(service);
            }
        }
    }
}
