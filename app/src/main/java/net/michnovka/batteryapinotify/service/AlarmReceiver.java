package net.michnovka.batteryapinotify.service;

import static android.content.Context.POWER_SERVICE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.michnovka.batteryapinotify.model.configuration.Configuration;
import net.michnovka.batteryapinotify.model.infromation.BatteryInformation;
import net.michnovka.batteryapinotify.util.SharedPreferenceHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kishon on 27,November,2021
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";
    private Context context;
    private SharedPreferenceHelper sharedPreferenceHelper;
    private Configuration configuration;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        PowerManager powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelock");
        wakeLock.acquire(180000);

        sharedPreferenceHelper = new SharedPreferenceHelper(context);
        configuration = sharedPreferenceHelper.getConfiguration();

        getBatteryInformation();
    }

    private void getBatteryInformation() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent intent = context.registerReceiver(null, intentFilter);
        try {
            int battery_level = intent.getIntExtra("level", 0);
            int battery_temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            int battery_voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

            //Status
            String battery_status = "No Data";
            if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                battery_status = "Charging";
            }
            if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
                battery_status = "Discharging";
            }
            if (status == BatteryManager.BATTERY_STATUS_FULL) {
                battery_status = "Full";
            }
            if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
                battery_status = "Not Charging";
            }
            if (status == BatteryManager.BATTERY_STATUS_UNKNOWN) {
                battery_status = "Unknown";
            }

            //Charger
            int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            String battery_source = "No Data";
            if (chargePlug == BatteryManager.BATTERY_PLUGGED_AC) {
                battery_source = "AC";
            }
            if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB) {
                battery_source = "USB";
            }

            //Health
            int BHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
            String battery_health = "No Data";
            if (BHealth == BatteryManager.BATTERY_HEALTH_COLD) {
                battery_health = "Cold";
            }
            if (BHealth == BatteryManager.BATTERY_HEALTH_DEAD) {
                battery_health = "Dead";
            }
            if (BHealth == BatteryManager.BATTERY_HEALTH_GOOD) {
                battery_health = "Good";
            }
            if (BHealth == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {
                battery_health = "Over-Voltage";
            }
            if (BHealth == BatteryManager.BATTERY_HEALTH_OVERHEAT) {
                battery_health = "Overheat";
            }
            if (BHealth == BatteryManager.BATTERY_HEALTH_UNKNOWN) {
                battery_health = "Unknown";
            }
            if (BHealth == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {
                battery_health = "Unspecified Failure";
            }


            BatteryInformation batteryInformation = new BatteryInformation();
            batteryInformation.setStatus(battery_status);
            batteryInformation.setSource(battery_source);
            batteryInformation.setLevel(battery_level);
            batteryInformation.setHealth(battery_health);
            batteryInformation.setTemp(battery_temp);
            batteryInformation.setVoltage(battery_voltage);

            if (configuration.getAbove().getEnabled()) {
                if (batteryInformation.getLevel() > configuration.getAbove().getLevel()) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("battery_level_limit", configuration.getAbove().getLevelString());
                    params.put("current_battery_level", String.valueOf(batteryInformation.getLevel()));
                    params.put("battery_limit_type", "upper");
                    Log.d(TAG, "getBatteryInformation: " + params.toString());
                    sendToServer(params);
                }
            }
            if (configuration.getBelow().getEnabled()) {
                if (batteryInformation.getLevel() < configuration.getBelow().getLevel()) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("battery_level_limit", configuration.getBelow().getLevelString());
                    params.put("current_battery_level", String.valueOf(batteryInformation.getLevel()));
                    params.put("battery_limit_type", "lower");
                    Log.d(TAG, "getBatteryInformation: " + params.toString());
                    sendToServer(params);
                }
            }

        } catch (Exception e) {
            Log.v("TAG", "Battery Info Error");
        }
    }

    private void sendToServer(@NonNull HashMap<String, String> params) {
        Log.d(TAG, "sendToServer: " + params.toString());

        StringRequest request = new StringRequest(Request.Method.POST, sharedPreferenceHelper.getConfiguration().getUrl(), new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onErrorResponse: ", error.getLocalizedMessage());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response.statusCode == 200) {
                    Log.d("parseNetworkResponse: ", "data posted!");
                }
                return super.parseNetworkResponse(response);
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }
}
