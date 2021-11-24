package net.michnovka.batteryapinotify.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import net.michnovka.batteryapinotify.model.configuration.Configuration;

/**
 * Created by kishon on 18,November,2021
 */
public class SharedPreferenceHelper {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String STATUS_KEY = "STATUS_KEY";
    private static final String BOOT_KAY = "BOOT_KAY";
    private static final String CONFIGURATION_KEY = "CONFIGURATION_KEY";

    public SharedPreferenceHelper(Context context) {
        preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public boolean saveConfiguration(Configuration configuration){
        Gson gson = new Gson();
        String configurationString = gson.toJson(configuration);
        editor.putString(CONFIGURATION_KEY, configurationString);
        return editor.commit();
    }

    public Configuration getConfiguration(){
        Gson gson = new Gson();
        String configurationString = preferences.getString(CONFIGURATION_KEY, null);
        return gson.fromJson(configurationString, Configuration.class);
    }

    public boolean setStatus(boolean value){
        editor.putBoolean(STATUS_KEY, value);
        return editor.commit();
    }

    public boolean getStatus(){
        return preferences.getBoolean(STATUS_KEY, false);
    }

    public boolean setBoot(boolean value){
        editor.putBoolean(BOOT_KAY, value);
        return editor.commit();
    }

    public boolean getBoot(){
        return preferences.getBoolean(BOOT_KAY, false);
    }
}
