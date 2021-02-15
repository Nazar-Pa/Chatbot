package com.tyagiabhinav.dialogflowchatlibrary.database.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.tyagiabhinav.dialogflowchatlibrary.database.db.NoteDatabase;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Food;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Note;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.AppUtils;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NoteRepository {

    private String DB_NAME = "db_task";


    private NoteDatabase noteDatabase;
    public NoteRepository(Context context) {
        noteDatabase = Room.databaseBuilder(context, NoteDatabase.class, DB_NAME).allowMainThreadQueries().build();
    }

//    public void insertTask(String title, Date createdDate,
//                           String description) {
//
//        insertTask(title, createdDate);
//    }

    public void insertTask(String title,
                           Date createdDate, LocalTime startedAt, String duration
                           ) {

        Note note = new Note();
        note.setTitle(title);
        note.setCreatedDate(createdDate);
        note.setStartedAt(startedAt);
        note.setDuration(duration);

        insertTask(note);
    }

    public void insertTask(final Note note) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.daoAccess().insertTask(note);
                return null;
            }
        }.execute();
    }


    public void deleteTask(final int id) {
        final LiveData<Note> task = getTask(id);
        if(task != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    noteDatabase.daoAccess().deleteTask(task.getValue());
                    return null;
                }
            }.execute();
        }
    }

    public void deleteTask(final Note note) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.daoAccess().deleteTask(note);
                return null;
            }
        }.execute();
    }

    public LiveData<Note> getTask(int id) { return noteDatabase.daoAccess().getTask(id);}

    public LiveData<List<Note>> getTasks() {
        return noteDatabase.daoAccess().fetchAllTasks();

    }

    public Note getNoteItem(int id){
        return noteDatabase.daoAccess().getNote(id);
    }

    public int ItemsCount(){
        return noteDatabase.daoAccess().fetchItemsCount();
    }

//    public LiveData<List<Note>> getFoodTasks() {
//        return noteDatabase.daoAccess().fetchAllFoods();
//
//    }

    public LiveData<List<Note>> filterDaily(String calendar) {
        return noteDatabase.daoAccess().fetchUserByUserDOB(calendar);
    }

//    public LiveData<List<Note>> filterFoodDaily(String calendar) {
//        return noteDatabase.daoAccess().fetchFoodbyDate(calendar);
//    }

//    public int DailySumOfPortions(String currentDate) {
//        return noteDatabase.daoAccess().fetchDailySumOfPortions(currentDate);
//    }

    public int DailyNumberOfActivities(String currentDate) {
        return noteDatabase.daoAccess().fetchDailyNumberOfActivities(currentDate);
    }

//    public int DailySumOfPortions() {
//        return noteDatabase.daoAccess().fetchDailySumOfPortions();
//
//    }
}
