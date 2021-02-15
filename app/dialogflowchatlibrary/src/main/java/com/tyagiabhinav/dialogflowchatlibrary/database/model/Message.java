package com.tyagiabhinav.dialogflowchatlibrary.database.model;

import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


import com.tyagiabhinav.dialogflowchatlibrary.database.util.TimestampConverter;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;


@Entity
public class Message implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "is_friend_message")
    private boolean isFriendMsg;

    @ColumnInfo(name = "bot_message")
    private String botMessage;

    @ColumnInfo(name = "user_message")
    private String userMessage;

    @ColumnInfo(name = "bot_message_time")
    @TypeConverters({TimestampConverter.class})
    private Date botMessageTime;

//    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
//    private byte[] viewImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean isFriendMsg(){ return isFriendMsg;}

    public void setFriendMsg(boolean friendMsg) { isFriendMsg = friendMsg;}

    public String getBotMessage(){ return botMessage;}

    public void setBotMessage(String botMessage) { this.botMessage = botMessage;}

    public  String getUserMessage(){ return userMessage;}

    public void setUserMessage(String userMessage) { this.userMessage = userMessage;}

    public Date getBotMessageTime() { return botMessageTime;}

    public void setBotMessageTime(Date botMessageTime) { this.botMessageTime = botMessageTime;}

//    public byte[] getViewImage() { return viewImage;}
//
//    public void setViewImage(byte[] viewImage) {this.viewImage = viewImage;}

}







