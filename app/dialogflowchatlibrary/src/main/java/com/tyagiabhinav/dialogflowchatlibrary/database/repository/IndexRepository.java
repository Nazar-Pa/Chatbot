package com.tyagiabhinav.dialogflowchatlibrary.database.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.tyagiabhinav.dialogflowchatlibrary.database.db.AlarmDatabase;
import com.tyagiabhinav.dialogflowchatlibrary.database.db.IndexDatabase;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Alarm;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Index;

import java.util.List;

public class IndexRepository {

    private String DB_INDEX_NAME = "db_index_task";

    private IndexDatabase indexDatabase;

    public IndexRepository(Context context) {
        indexDatabase = Room.databaseBuilder(context, IndexDatabase.class, DB_INDEX_NAME).allowMainThreadQueries().build();
    }



    public void insertTask(int lastIndex) {

        Index index = new Index();
        index.setIndex(lastIndex);

        insertTask(index);
    }

    public void insertTask(final Index index) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                indexDatabase.daoIndexAccess().insertTask(index);
                return null;
            }
        }.execute();
    }



    public int lastIndex(){
        return indexDatabase.daoIndexAccess().fetchLastIndex();
    }


}

