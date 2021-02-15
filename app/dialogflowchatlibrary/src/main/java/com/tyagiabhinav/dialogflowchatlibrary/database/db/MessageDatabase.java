package com.tyagiabhinav.dialogflowchatlibrary.database.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tyagiabhinav.dialogflowchatlibrary.database.dao.DaoMessageAccess;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Message;

@Database(entities = {Message.class}, version = 1, exportSchema = false)
public abstract class MessageDatabase extends RoomDatabase {

    public abstract DaoMessageAccess daoMessageAccess();
}
