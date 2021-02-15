package com.tyagiabhinav.dialogflowchatlibrary.database.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import com.tyagiabhinav.dialogflowchatlibrary.ChatbotActivity;
import com.tyagiabhinav.dialogflowchatlibrary.R;
import com.tyagiabhinav.dialogflowchatlibrary.database.AppConstants;
import com.tyagiabhinav.dialogflowchatlibrary.database.dao.DaoAccess;
import com.tyagiabhinav.dialogflowchatlibrary.database.db.NoteDatabase;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Message;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Note;
import com.tyagiabhinav.dialogflowchatlibrary.database.repository.MessageRepository;
import com.tyagiabhinav.dialogflowchatlibrary.database.repository.NoteRepository;
import com.tyagiabhinav.dialogflowchatlibrary.database.ui.adapter.MessageListAdapter;
import com.tyagiabhinav.dialogflowchatlibrary.database.ui.adapter.NotesListAdapter;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.RecyclerItemClickListener;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.TimestampConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MessageListActivity extends AppCompatActivity implements
        AppConstants, View.OnClickListener {

    private RecyclerView recyclerView1;
    private MessageListAdapter messageListAdapter;
    public Date date;


    private MessageRepository messageRepository;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_message);

        messageRepository = new MessageRepository(getApplicationContext());

        recyclerView1 = findViewById(R.id.listView);

        recyclerView1.setLayoutManager(new LinearLayoutManager(this));



        if (getIntent()!=null) {
            String botMessage = getIntent().getStringExtra("BotMessage");
            String userMessage = getIntent().getStringExtra("UserMessage");
            Date botMessageTime = (Date) getIntent().getSerializableExtra("BotMessageTime");

            updateTaskList();
        }
        updateTaskList();
    }

    private void updateTaskList() {
        messageRepository.getMessageTasks().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(@Nullable List<Message> messages) {
                if(messages.size() > 0) {
                    recyclerView1.setVisibility(View.VISIBLE);
                    if (messageListAdapter == null) {
                        messageListAdapter = new MessageListAdapter(messages);
                        recyclerView1.setAdapter(messageListAdapter);

                    } else messageListAdapter.addTasks(messages);
                } else updateEmptyView();
            }
        });
    }

    private void updateEmptyView() {
        recyclerView1.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {


            String botMessage = data.getStringExtra(INTENT_BOT_MESSAGE);
            String userMessage = data.getStringExtra(INTENT_USER_MESSAGE);
            Date botMessageTime = (Date) data.getSerializableExtra(String.valueOf(INTENT_BOT_MESSAGE_TIME));


            updateTaskList();
        }
    }

    @Override
    public void onClick(View view) {

    }
}
