package com.tyagiabhinav.dialogflowchatlibrary.database.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import com.tyagiabhinav.dialogflowchatlibrary.database.db.AlarmDatabase;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Alarm;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Food;

import java.util.List;


public class AlarmRepository {

    private String DB_ALARM_NAME = "db_alarm_task";

    private AlarmDatabase alarmDatabase;

    public AlarmRepository(Context context) {
        alarmDatabase = Room.databaseBuilder(context, AlarmDatabase.class, DB_ALARM_NAME).allowMainThreadQueries().build();
    }



    public void insertTask(String lastMillis) {

        Alarm alarm = new Alarm();
        alarm.setLastMillis(lastMillis);

        insertTask(alarm);
    }

    public void insertTask(final Alarm alarm) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                alarmDatabase.daoAlarmAccess().insertTask(alarm);
                return null;
            }
        }.execute();
    }

    public LiveData<List<Alarm>> getAlarmTasks() {
        return alarmDatabase.daoAlarmAccess().fetchAllAlarms();

    }

    public String lastMillis(){
        return alarmDatabase.daoAlarmAccess().fetchLastMillis();
    }


}
