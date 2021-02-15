package com.tyagiabhinav.dialogflowchatlibrary.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tyagiabhinav.dialogflowchatlibrary.database.model.Alarm;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Index;

import java.util.List;
@Dao
public interface DaoIndexAccess {
    @Insert
    Long insertTask(Index index);

    @Query("SELECT * FROM `Index` ORDER BY id desc")
    LiveData<List<Index>> fetchAllIndexs();



    @Update
    void updateMessageTask(Index index);

    @Query("SELECT `index` FROM `Index` ORDER BY id DESC LIMIT 1")
    int fetchLastIndex();
}
