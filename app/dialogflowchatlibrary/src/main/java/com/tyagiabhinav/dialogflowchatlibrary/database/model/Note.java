package com.tyagiabhinav.dialogflowchatlibrary.database.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.tyagiabhinav.dialogflowchatlibrary.database.util.TimeStampConvert;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.TimestampConverter;

import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;


@Entity
public class Note implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;


    @ColumnInfo(name = "title")
    private String title;


    @ColumnInfo(name = "activity_created_date")
    @TypeConverters({TimestampConverter.class})
    private Date createdDate;



    @ColumnInfo(name = "activity_started_at")
    @TypeConverters({TimestampConverter.class})
    private LocalTime startedAt;


    @ColumnInfo(name = "duration")
    private String duration;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Date getCreatedDate() { return createdDate;    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }


    public LocalTime getStartedAt() { return startedAt; }

    public void setStartedAt(LocalTime startedAt) {
        this.startedAt = startedAt;
    }


    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


}







