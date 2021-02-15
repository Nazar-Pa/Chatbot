package com.tyagiabhinav.dialogflowchatlibrary.database.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.tyagiabhinav.dialogflowchatlibrary.database.db.MessageDatabase;
import com.tyagiabhinav.dialogflowchatlibrary.database.db.NoteDatabase;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Message;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Note;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.AppUtils;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MessageRepository {

    private String DB_MESSAGE_NAME = "db_message_task";


    private MessageDatabase messageDatabase;
    public MessageRepository(Context context) {
        messageDatabase = Room.databaseBuilder(context, MessageDatabase.class, DB_MESSAGE_NAME).allowMainThreadQueries().build();
    }


    public void insertTask(boolean isFriendMsg, String botMessage,  String userMessage,
                           Date botMessageTime
    ) {

        Message message = new Message();
        message.setFriendMsg(isFriendMsg);
        message.setBotMessage(botMessage);
        message.setUserMessage(userMessage);
        message.setBotMessageTime(botMessageTime);

        insertTask(message);
    }

    public void insertTask(final Message message) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                messageDatabase.daoMessageAccess().insertTask(message);
                return null;
            }
        }.execute();
    }



    public LiveData<Message> getMessageTask(int id) { return messageDatabase.daoMessageAccess().getMessageTask(id);}

    public LiveData<List<Message>> getMessageTasks() {
        return messageDatabase.daoMessageAccess().fetchAllMessages();

    }

    public int ItemsCount(){
        return messageDatabase.daoMessageAccess().fetchItemsCount();
    }

}
