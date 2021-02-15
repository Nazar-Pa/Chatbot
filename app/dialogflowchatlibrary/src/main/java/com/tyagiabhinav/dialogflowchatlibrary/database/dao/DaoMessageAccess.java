package com.tyagiabhinav.dialogflowchatlibrary.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tyagiabhinav.dialogflowchatlibrary.database.model.Message;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Note;

import java.util.List;

@Dao
public interface DaoMessageAccess {

    @Insert
    Long insertTask(Message message);


    @Query("SELECT * FROM Message ORDER BY bot_message_time asc")
    LiveData<List<Message>> fetchAllMessages();



    @Query("SELECT * FROM Message WHERE id =:taskId")
    LiveData<Message> getMessageTask(int taskId);


    @Update
    void updateMessageTask(Message message);

    @Query("SELECT COUNT(id) FROM Message")
    int fetchItemsCount();

}
