package com.tyagiabhinav.dialogflowchatlibrary.database.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.tyagiabhinav.dialogflowchatlibrary.database.dao.DaoFoodAccess;
import com.tyagiabhinav.dialogflowchatlibrary.database.db.FoodDatabase;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Food;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class FoodRepository {

    private String DB_FOOD_NAME = "db_food_task";

    private FoodDatabase foodDatabase;

    public FoodRepository(Context context) {
        foodDatabase = Room.databaseBuilder(context, FoodDatabase.class, DB_FOOD_NAME).allowMainThreadQueries().build();
    }



    public void insertTask(int portion, Date foodCreatedDate, LocalTime foodCreatedAt) {

        Food food = new Food();
        food.setPortion(portion);
        food.setFoodCreatedDate(foodCreatedDate);
        food.setFoodCreatedAt(foodCreatedAt);

        insertTask(food);
    }

    public void insertTask(final Food food) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                foodDatabase.daoFoodAccess().insertTaskFood(food);
                return null;
            }
        }.execute();
    }


    public void deleteTask(final int id) {
        final LiveData<Food> task = getFoodTask(id);
        if(task != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    foodDatabase.daoFoodAccess().deleteTask(task.getValue());
                    return null;
                }
            }.execute();
        }
    }

    public void deleteTask(final Food food) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                foodDatabase.daoFoodAccess().deleteTask(food);
                return null;
            }
        }.execute();

    }



    private static class deleteFoodAsyncTask extends AsyncTask<Food, Void, Void>{
        private DaoFoodAccess mAsyncTaskDao;

        deleteFoodAsyncTask(DaoFoodAccess dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Food... params){
            mAsyncTaskDao.deleteFood(params[0]);
            return null;
        }
    }


    public void deleteFood(Food food){
        new deleteFoodAsyncTask(foodDatabase.daoFoodAccess()).execute(food);
    }


    public LiveData<Food> getFoodTask(int id) { return foodDatabase.daoFoodAccess().getFoodTask(id);}


    public LiveData<List<Food>> getFoodTasks() {
        return foodDatabase.daoFoodAccess().fetchAllFoods();

    }

    public LiveData<List<Food>> filterFoodDaily(String calendar) {
        return foodDatabase.daoFoodAccess().fetchFoodbyDate(calendar);
    }

    public int DailySumOfPortions(String currentDate) {
        return foodDatabase.daoFoodAccess().fetchDailySumOfPortions(currentDate);
    }

    public Food getFoodItem(int id){
        return foodDatabase.daoFoodAccess().getFood(id);
    }

    public int ItemsCount(){
        return foodDatabase.daoFoodAccess().fetchItemsCount();
    }


    //public void deleteFoodItem(int id){ foodDatabase.daoFoodAccess().deleteByUserId(id);}


}
