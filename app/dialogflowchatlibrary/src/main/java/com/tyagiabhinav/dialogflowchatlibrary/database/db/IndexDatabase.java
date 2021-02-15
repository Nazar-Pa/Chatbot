package com.tyagiabhinav.dialogflowchatlibrary.database.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tyagiabhinav.dialogflowchatlibrary.database.dao.DaoAlarmAccess;
import com.tyagiabhinav.dialogflowchatlibrary.database.dao.DaoIndexAccess;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Alarm;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Index;

@Database(entities = {Index.class}, version = 1, exportSchema = false)
public abstract class IndexDatabase extends RoomDatabase {

    public abstract DaoIndexAccess daoIndexAccess();
}
