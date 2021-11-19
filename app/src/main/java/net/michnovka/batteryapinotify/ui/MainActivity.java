package net.michnovka.batteryapinotify.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;

import net.michnovka.batteryapinotify.R;
import net.michnovka.batteryapinotify.model.configuration.Below;
import net.michnovka.batteryapinotify.model.configuration.Configuration;
import net.michnovka.batteryapinotify.service.BatteryMonitorService;
import net.michnovka.batteryapinotify.util.SharedPreferenceHelper;
import net.michnovka.batteryapinotify.model.configuration.Above;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout mApiURL, mLevelAbove, mLevelBelow, mInterval;
    private CheckBox mLevelAboveCheckBox, mLevelBelowCheckBox;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch mAppStatus;
    private SharedPreferenceHelper sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreference = new SharedPreferenceHelper(this);
        sharedPreference.setStatus(BatteryMonitorService.isRunning);

        initComponents();
        initFields();
    }


    private void initComponents() {
        mApiURL = findViewById(R.id.text_url);
        mLevelAbove = findViewById(R.id.text_above);
        mLevelBelow = findViewById(R.id.text_below);
        mInterval = findViewById(R.id.text_interval);
        mLevelAboveCheckBox = findViewById(R.id.checkBox_Above);
        mLevelBelowCheckBox = findViewById(R.id.checkBox_Below);
        mAppStatus = findViewById(R.id.switch_status);
    }

    private void initFields() {
        Configuration configuration = sharedPreference.getConfiguration();
        if (configuration != null) {
            mApiURL.getEditText().setText(configuration.getUrl());
            mInterval.getEditText().setText(configuration.getIntervalString());
            mLevelAbove.getEditText().setText(configuration.getAbove().getLevelString());
            mLevelBelow.getEditText().setText(configuration.getBelow().getLevelString());
            mLevelAboveCheckBox.setChecked(configuration.getAbove().getEnabled());
            mLevelBelowCheckBox.setChecked(configuration.getBelow().getEnabled());
            mAppStatus.setChecked(sharedPreference.getStatus());
        }
    }

    public void saveData(View view) {
        String url = mApiURL.getEditText().getText().toString();
        String above_level = mLevelAbove.getEditText().getText().toString();
        String below_level = mLevelBelow.getEditText().getText().toString();
        String interval = mInterval.getEditText().getText().toString();
        boolean isAboveLevelEnabled = mLevelAboveCheckBox.isChecked();
        boolean isBelowLevelEnabled = mLevelBelowCheckBox.isChecked();
        boolean status = mAppStatus.isChecked();

        if (TextUtils.isEmpty(url)) {
            mApiURL.setErrorEnabled(true);
            mApiURL.setError("Please enter url");
            return;
        }

        if (TextUtils.isEmpty(above_level)) {
            mLevelAbove.setErrorEnabled(true);
            mLevelAbove.setError("Please enter level");
            return;
        }

        if (TextUtils.isEmpty(below_level)) {
            mLevelBelow.setErrorEnabled(true);
            mLevelBelow.setError("Please enter level");
            return;
        }

        if (TextUtils.isEmpty(interval)) {
            mInterval.setErrorEnabled(true);
            mInterval.setError("Please enter interval");
            return;
        }

        //Above
        Above above = new Above();
        above.setEnabled(isAboveLevelEnabled);
        above.setLevel(Integer.valueOf(above_level));

        //Below
        Below below = new Below();
        below.setEnabled(isBelowLevelEnabled);
        below.setLevel(Integer.valueOf(below_level));

        //Configuration
        Configuration configuration = new Configuration();
        configuration.setUrl(url);
        configuration.setAbove(above);
        configuration.setBelow(below);
        configuration.setInterval(Integer.valueOf(interval));

        if (sharedPreference.saveConfiguration(configuration)) {
            if (sharedPreference.setStatus(status)) {
                Intent intent = new Intent(MainActivity.this, BatteryMonitorService.class);
                if (status) {
                    ContextCompat.startForegroundService(MainActivity.this, intent);
                } else {
                    stopService(intent);
                }
            }
        }

    }

}