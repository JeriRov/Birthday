package com.example.birthday;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MyService extends Service {
    String LOG_TAG = "Kostia_Birthday";
    DBHelper dbHelper;

    public MyService() {
        dbHelper = new DBHelper(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "start command at service");
        //Для початку візьмемо сьогоднішню дату
        String currentDate = new SimpleDateFormat("dd.MM", Locale.getDefault()).format(new Date());
        //Тепер список
        ArrayList<String> peoples = new ArrayList<String>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("birthday_database_kostia", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int dataIndex = c.getColumnIndex("data");
            int nameIndex = c.getColumnIndex("name");
            do {
                if(c.getString(dataIndex).substring(0,c.getString(dataIndex).lastIndexOf(".")).equals(currentDate)){
                    peoples.add(c.getString(nameIndex));
                    Log.d(LOG_TAG, "Today birthday have: " + c.getString(nameIndex));
                }
            } while (c.moveToNext());
        } else
        c.close();
        if(peoples.size()>0){
            String names = "";
            for(int i = 0; i < peoples.size();i++){
                names+=peoples.get(i)+", ";
            }
            names = names.substring(0, names.length() - 2);

            Log.d(LOG_TAG, "names " + names);
            //Створення каналу
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationManager mNotificationManager = (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);
                        String id = "birthday_channel_01";
                NotificationChannel mChannel = new NotificationChannel(id, "birthday channel",
                        NotificationManager.IMPORTANCE_LOW);
                mChannel.enableLights(true);
                mNotificationManager.createNotificationChannel(mChannel);

                Notification.Builder builder =
                        new Notification.Builder(this, id)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setAutoCancel(true)
                                .setContentTitle("День народження")
                                .setContentText("Сьогоні у " + names);
                Notification notification = builder.build();

                mNotificationManager.notify(1, notification);
            }else{
                //Тут можна старим способом
                Notification.Builder builder =
                        new Notification.Builder(this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setAutoCancel(true)
                                .setContentTitle("День народження")
                                .setContentText("Сьогодні у " + names);
                Notification notification = builder.build();

                NotificationManager notificationManager = (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, notification);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}