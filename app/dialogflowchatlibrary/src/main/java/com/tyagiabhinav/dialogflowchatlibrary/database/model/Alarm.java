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
public class Alarm implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "last_millis")
    private String lastMillis;

//    @ColumnInfo(name = "app_open_date")
//    @TypeConverters({TimestampConverter.class})
//    private Date lastOpenDate;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getLastMillis(){ return lastMillis;}

    public void setLastMillis(String lastMillis) { this.lastMillis = lastMillis;}

//    public Date getFoodCreatedDate() { return lastOpenDate;    }
//
//    public void setFoodCreatedDate(Date lastOpenDate) {
//        this.lastOpenDate = lastOpenDate;
//    }
}
