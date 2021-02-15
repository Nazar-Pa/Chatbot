package com.tyagiabhinav.dialogflowchatlibrary.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tyagiabhinav.dialogflowchatlibrary.database.model.Food;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Note;
import com.tyagiabhinav.dialogflowchatlibrary.database.ui.activity.NotesListActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    Long insertTask(Note note);


    @Query("SELECT * FROM Note ORDER BY activity_created_date desc")
    LiveData<List<Note>> fetchAllTasks();


    @Query("SELECT * FROM Note WHERE activity_created_date=:calendar")
    LiveData<List<Note>> fetchUserByUserDOB(String calendar);


    @Query("SELECT COUNT(title) FROM Note WHERE activity_created_date =:currentDate")
    int fetchDailyNumberOfActivities(String currentDate);


    @Query("SELECT * FROM Note WHERE id =:id")
    Note getNote(int id);

    @Query("SELECT * FROM Note WHERE id =:taskId")
    LiveData<Note> getTask(int taskId);

    @Query("SELECT COUNT(id) FROM Note")
    int fetchItemsCount();


    @Update
    void updateTask(Note note);


    @Delete
    void deleteTask(Note note);


}
