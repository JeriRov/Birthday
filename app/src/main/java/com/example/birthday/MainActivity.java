package com.example.birthday;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    LinearLayout infoContainer;
    DBHelper dbHelper;
    String LOG_TAG = "Kostia_Birthday";
    //https://developer.android.com/training/scheduling/alarms
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);

        //
        SharedPreferences sPref = getSharedPreferences("birthday_settings",MODE_PRIVATE);
        Log.d(LOG_TAG, sPref.getString("timeForAlarm", "00:00"));
        //
        infoContainer = findViewById(R.id.info_container);
        updateList();//Обновлення листа при вході
        startService(new Intent(MainActivity.this, MyService.class));
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }

    //Метод для обновлення контейнера
    public void updateList(){
        infoContainer.removeAllViews();
        ArrayList<CompactInfo> list = new ArrayList<>();
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("birthday_database_kostia", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int dataColIndex = c.getColumnIndex("data");
            int nameColIndex = c.getColumnIndex("name");
            int moreColIndex = c.getColumnIndex("more");
            do {
                CompactInfo compactInfo = new CompactInfo(
                        new String[]{c.getString(nameColIndex), c.getString(dataColIndex), c.getString(moreColIndex)});
                list.add(compactInfo);
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();
        for(CompactInfo ci : list){
            Log.d(LOG_TAG, "add " + ci.getName());
            fragTransaction.add(infoContainer.getId(), ci,"frag"+list.indexOf(ci));
        }
        fragTransaction.commit();
    }

    public void insert(String[] data){
        ContentValues cv = new ContentValues();
        //Підключення до БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cv.put("name",data[0]);
        cv.put("data",data[1]);
        cv.put("more",data[2]);
        db.insert("birthday_database_kostia", null, cv);
        updateList();
    }

    //Методи кнопок
    public void add(View v){
        final String[] data = new String[3];
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Нова дата");
        builder.setMessage("Введіть нове ім'я");
        builder.setCancelable(false);
        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setHint("Name");
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Було введено ім'я
                data[0] = input.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Нова людина");
                builder.setMessage("Введіть дату народження\nФормат: день.місяць.рік");
                builder.setCancelable(false);
                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setHint("Data");
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Було введено дату
                        data[1] = input.getText().toString();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Нова людина");
                        builder.setMessage("Введіть додаткову інформацію, чи залиште пустим");
                        builder.setCancelable(false);
                        final EditText input = new EditText(MainActivity.this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        input.setHint("Additional info");
                        builder.setView(input);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                data[2] = input.getText().toString();
                                insert(data);
                                dialog.cancel();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Нажато кнопку Відміна
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Нажато кнопку Відміна
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
               dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Нажато кнопку Відміна
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void remove(View view){
        String names ="";
        ArrayList<Integer>ids = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("birthday_database_kostia", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int idIndex = c.getColumnIndex("id");
            int nameIndex = c.getColumnIndex("name");
            int dataIndex = c.getColumnIndex("data");
            do {
                names+=c.getString(nameIndex) + " " + c.getString(dataIndex) + "><";
                ids.add(c.getInt(idIndex));
            } while (c.moveToNext());
            names = names.substring(0, names.length()-2);
            Log.d(LOG_TAG, "names: "+names);
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Видалення даних");
        builder.setCancelable(false);
        String[] loop = names.split("><");
        Log.d(LOG_TAG, "loop: "+loop.length);
        boolean[] checkedItems = new boolean[loop.length];
        builder.setMultiChoiceItems(loop, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                //Тут можна було опрацювати нажимання на CheckBox
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i = 0; i < checkedItems.length; i++){
                    if(checkedItems[i]){
                        delete(ids.get(i));
                    }
                }
                updateList();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Нажато кнопку Відміна
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void delete(int a){
        Log.d(LOG_TAG, "Delete: " + a);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int delCount = db.delete("birthday_database_kostia", "id = " + a, null);
        Log.d(LOG_TAG, "deleted rows count = " + delCount);
    }

    public void clear(View view){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("birthday_database_kostia", null, null);
        infoContainer.removeAllViews();
    }

    public void update(View view) {
        /*cv.put("name", name);
      cv.put("email", email);
      // обновляем по id
      int updCount = db.update("mytable", cv, "id = ?",
          new String[] { id });
      Log.d(LOG_TAG, "updated rows count = " + updCount);*/
    }
}