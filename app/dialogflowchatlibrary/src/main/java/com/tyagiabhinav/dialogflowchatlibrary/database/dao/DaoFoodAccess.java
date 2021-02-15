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
public interface DaoFoodAccess {


    @Insert
    Long insertTaskFood(Food food);


    @Query("SELECT * FROM Food ORDER BY food_created_date desc")
    LiveData<List<Food>> fetchAllFoods();


    @Query("SELECT * FROM Food WHERE food_created_date=:calendar")
    LiveData<List<Food>> fetchFoodbyDate(String calendar);


    @Query("SELECT SUM(portion) FROM Food WHERE food_created_date =:currentDate")
    int fetchDailySumOfPortions(String currentDate);


    @Query("SELECT * FROM Food WHERE id =:id")
    Food getFood(int id);



    @Query("SELECT * FROM Food WHERE id =:taskId")
    LiveData<Food> getFoodTask(int taskId);


    @Query("SELECT COUNT(id) FROM Food")
    int fetchItemsCount();


    @Update
    void updateFoodTask(Food food);


    @Delete
    void deleteTask(Food food);

    @Delete
    void deleteFood(Food food);

//    @Query("DELETE FROM Food WHERE id =:taskId")
//    void deleteByUserId(int taskId);

}
