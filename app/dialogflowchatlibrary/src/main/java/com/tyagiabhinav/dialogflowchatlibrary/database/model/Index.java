package com.tyagiabhinav.dialogflowchatlibrary.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.tyagiabhinav.dialogflowchatlibrary.database.util.TimestampConverter;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Index implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "index")
    private int index;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getIndex(){ return index;}

    public void setIndex(int index) { this.index = index;}


}
