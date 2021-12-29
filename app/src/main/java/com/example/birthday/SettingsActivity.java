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
        
        //Artem (AlarmManager) ->
        String[] data = sPref.getString("timeForAlarm", "00:00").split(":");
        int hours = Integer.parseInt(data[0].trim()), minutes = Integer.parseInt(data[1].trim());
        Log.d(LOG_TAG, hours + " Hours " + minutes + " minutes");
        Calendar c = Calendar.getInstance();// ставимо години і хвилини в календар
        c.set(Calendar.HOUR_OF_DAY, hours);
        c.set(Calendar.MINUTE, minutes);
        c.set(Calendar.SECOND, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE); //врубаємо AlarmManager
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance()))
            c.add(Calendar.DATE, 1); //пересуває на 1 день (як я зрозумів), якщо час який встановили вже був в той день

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent); // https://coderoad.ru/10531734/разница-между-RTC-и-RTC_WAKEUP-в-android
        //AEnd.
        
        finish();
    }
}
