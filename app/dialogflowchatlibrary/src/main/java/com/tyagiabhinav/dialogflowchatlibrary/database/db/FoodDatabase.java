package com.tyagiabhinav.dialogflowchatlibrary.database.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tyagiabhinav.dialogflowchatlibrary.database.dao.DaoAccess;
import com.tyagiabhinav.dialogflowchatlibrary.database.dao.DaoFoodAccess;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Food;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Note;

@Database(entities = {Food.class}, version = 1, exportSchema = false)
public abstract class FoodDatabase extends RoomDatabase {

    public abstract DaoFoodAccess daoFoodAccess();
}
