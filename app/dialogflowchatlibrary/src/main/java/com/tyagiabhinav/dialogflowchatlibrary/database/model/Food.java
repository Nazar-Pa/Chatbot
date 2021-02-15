package com.tyagiabhinav.dialogflowchatlibrary.database.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.tyagiabhinav.dialogflowchatlibrary.database.repository.FoodRepository;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.TimeStampConvert;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.TimestampConverter;

import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;


@Entity
public class Food implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;


    @ColumnInfo(name = "portion")
    private int portion;


    @ColumnInfo(name = "food_created_date")
    @TypeConverters({TimestampConverter.class})
    private Date foodCreatedDate;


    @ColumnInfo(name = "food_created_at")
    @TypeConverters({TimestampConverter.class})
    private LocalTime foodCreatedAt;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getPortion() {
        return portion;
    }

    public void setPortion(int portion) {
        this.portion = portion;
    }

    public Date getFoodCreatedDate() { return foodCreatedDate;    }

    public void setFoodCreatedDate(Date foodCreatedDate) {
        this.foodCreatedDate = foodCreatedDate;
    }


    public LocalTime getFoodCreatedAt() { return foodCreatedAt; }

    public void setFoodCreatedAt(LocalTime foodCreatedAt) {
        this.foodCreatedAt = foodCreatedAt;
    }



//    public Date getEndedAt() { return endedAt; }
//
//    public void setEndedAt(Time endedAt) {
//        this.endedAt = endedAt;
//    }

}







