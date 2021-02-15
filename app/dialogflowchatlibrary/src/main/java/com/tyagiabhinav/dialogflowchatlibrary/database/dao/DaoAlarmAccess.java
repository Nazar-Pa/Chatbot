package com.tyagiabhinav.dialogflowchatlibrary.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tyagiabhinav.dialogflowchatlibrary.database.model.Alarm;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Food;


import java.util.List;
@Dao
public interface DaoAlarmAccess {

    @Insert
    Long insertTask(Alarm alarm);

    @Query("SELECT * FROM Alarm ORDER BY id desc")
    LiveData<List<Alarm>> fetchAllAlarms();


//    @Query("SELECT * FROM Message ORDER BY bot_message_time asc")
//    LiveData<List<Message>> fetchAllMessages();
//
//
//
//    @Query("SELECT * FROM Message WHERE id =:taskId")
//    LiveData<Message> getMessageTask(int taskId);


    @Update
    void updateMessageTask(Alarm alarm);

    @Query("SELECT last_millis FROM Alarm ORDER BY id DESC LIMIT 1")
    String fetchLastMillis();
}
