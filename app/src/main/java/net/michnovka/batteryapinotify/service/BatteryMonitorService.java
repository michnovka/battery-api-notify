package net.michnovka.batteryapinotify.service;

import static net.michnovka.batteryapinotify.App.CHANNEL_ID;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import net.michnovka.batteryapinotify.R;
import net.michnovka.batteryapinotify.model.configuration.Configuration;
import net.michnovka.batteryapinotify.ui.MainActivity;
import net.michnovka.batteryapinotify.util.SharedPreferenceHelper;

/**
 * Created by kishon on 18,November,2021
 */
public class BatteryMonitorService extends Service {

    private static final String TAG = "BatteryMonitorService";
    public static boolean isRunning = false;
    private SharedPreferenceHelper sharedPreferenceHelper;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        isRunning = true;

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(this, MainActivity.class), flags);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Service Running!")
                .addAction(new NotificationCompat.Action(R.drawable.ic_launcher_foreground, "View", pendingIntent))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .build();

        Configuration configuration = sharedPreferenceHelper.getConfiguration();
        startService(configuration);

        startForeground(1, notification);

        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        isRunning = false;
        stopService(); 
        super.onDestroy();
    }

    private void startService(Configuration configuration) {
        Intent myAlarm = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent recurringAlarm = PendingIntent.getBroadcast(getApplicationContext(), 0, myAlarm, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarms.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,configuration.getInterval() * 1000, recurringAlarm);
    }

    private void stopService() {
        Intent myAlarm = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent recurringAlarm = PendingIntent.getBroadcast(getApplicationContext(), 0, myAlarm, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarms.cancel(recurringAlarm);
    }
}
