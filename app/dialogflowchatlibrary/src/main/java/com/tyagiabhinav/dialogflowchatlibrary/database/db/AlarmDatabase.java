package com.tyagiabhinav.dialogflowchatlibrary.database.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tyagiabhinav.dialogflowchatlibrary.database.dao.DaoAlarmAccess;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Alarm;

@Database(entities = {Alarm.class}, version = 1, exportSchema = false)
public abstract class AlarmDatabase extends RoomDatabase {

    public abstract DaoAlarmAccess daoAlarmAccess();
}

