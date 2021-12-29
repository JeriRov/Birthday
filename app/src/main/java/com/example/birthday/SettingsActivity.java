package com.example.birthday;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    TimePicker timePicker;
    String LOG_TAG="Kostia_Birthday";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(DateFormat.is24HourFormat(getApplicationContext()));
    }

    public void saveTime(View view) {
        String loop;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            loop = timePicker.getHour()+":"+timePicker.getMinute();
        }else{
            loop = timePicker.getCurrentHour()+":"+timePicker.getCurrentMinute();
        }
        Log.d(LOG_TAG, "saved time for alarm: "+loop);
        SharedPreferences sPref = getSharedPreferences("birthday_settings",MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("timeForAlarm", loop);
        ed.commit();
        finish();
    }
}
