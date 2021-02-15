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
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Note;
import com.tyagiabhinav.dialogflowchatlibrary.database.repository.NoteRepository;
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

public class NotesListActivity extends AppCompatActivity implements
        AppConstants, View.OnClickListener {

    private RecyclerView recyclerView1;
    private NotesListAdapter notesListAdapter;
    public Date date;
    public String stringDate;
    public TextView textView;

    private NoteRepository noteRepository;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        noteRepository = new NoteRepository(getApplicationContext());

        recyclerView1 = findViewById(R.id.task_list);
        //recyclerView1.setLayoutManager(new StaggeredGridLayoutManager(1 , StaggeredGridLayoutManager.VERTICAL));
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        final TextView Day = findViewById(R.id.day);
        textView = findViewById(R.id.textView);
        final TextView all = findViewById(R.id.all);



        Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog picker = new DatePickerDialog(NotesListActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    date = DateFor.parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    Log.d("Nazar", date.toString());
                                    DateFor = new SimpleDateFormat("dd MMMM");
                                    stringDate = DateFor.format(date);
                                    textView.setText(stringDate);
                                    noteRepository.filterDaily(TimestampConverter.dateToTimestamp(date)).observe(NotesListActivity.this, new Observer<List<Note>>() {
                                        @Override
                                        public void onChanged(List<Note> notes) {
                                            ((NotesListAdapter) recyclerView1.getAdapter()).addTasks(notes);
                                        }
                                    });
//                                    noteRepository.getTasks().observe(NotesListActivity.this, new Observer<List<Note>>() {
//                                        @Override
//                                        public void onChanged(List<Note> notes) {
//                                            System.out.println();
//                                        }
//                                    });
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, year, month, day);
                picker.show();
            }

        });



        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTaskList();
            }

        });



        if (getIntent()!=null) {
            String title = getIntent().getStringExtra("Title");
            Date date = (Date) getIntent().getSerializableExtra("Date");
            LocalTime startedAt = (LocalTime) getIntent().getSerializableExtra("StartedAt");
            String duration = getIntent().getStringExtra("Duration");
            if (date != null)  {
                DateFormat df = new SimpleDateFormat("dd MMMM");
                textView.setText(df.format(date));
            }

            noteRepository.insertTask(title, date, startedAt, duration);
            updateTaskList();
        }
        updateTaskList();
    }

    private void updateTaskList() {
        noteRepository.getTasks().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                if(notes.size() > 0) {
                    recyclerView1.setVisibility(View.VISIBLE);
                    if (notesListAdapter == null) {
                        notesListAdapter = new NotesListAdapter(notes);
                        recyclerView1.setAdapter(notesListAdapter);

                    } else notesListAdapter.addTasks(notes);
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

            String title = data.getStringExtra(INTENT_TITLE);
            Date createdDate = (Date) data.getSerializableExtra(String.valueOf(INTENT_DATE));
            LocalTime startedAt = (LocalTime) data.getSerializableExtra(INTENT_STARTED_AT);
            String duration = data.getStringExtra(INTENT_DURATION);
                noteRepository.insertTask(title, createdDate, startedAt, duration);
            updateTaskList();
        }
    }

    @Override
    public void onClick(View view) {

    }
}
