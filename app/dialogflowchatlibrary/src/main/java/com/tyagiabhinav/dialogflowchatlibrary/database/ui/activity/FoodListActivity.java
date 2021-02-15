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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import com.tyagiabhinav.dialogflowchatlibrary.ChatbotActivity;
import com.tyagiabhinav.dialogflowchatlibrary.R;
import com.tyagiabhinav.dialogflowchatlibrary.database.AppConstants;
import com.tyagiabhinav.dialogflowchatlibrary.database.dao.DaoAccess;
import com.tyagiabhinav.dialogflowchatlibrary.database.db.NoteDatabase;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Food;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Note;
import com.tyagiabhinav.dialogflowchatlibrary.database.repository.FoodRepository;
import com.tyagiabhinav.dialogflowchatlibrary.database.repository.NoteRepository;
import com.tyagiabhinav.dialogflowchatlibrary.database.ui.adapter.NotesListAdapter;
import com.tyagiabhinav.dialogflowchatlibrary.database.ui.adapter.FoodListAdapter;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.RecyclerItemClickListener;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.TimestampConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FoodListActivity extends AppCompatActivity implements
        AppConstants, View.OnClickListener {

    private RecyclerView recyclerView;
    private FoodListAdapter foodListAdapter;
    private Button floatingActionButton;
    public Date date;
    public String stringDate;

    private NoteRepository noteRepository;
    private FoodRepository foodRepository;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        foodRepository = new FoodRepository(getApplicationContext());


        recyclerView = findViewById(R.id.task_list);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1 , StaggeredGridLayoutManager.VERTICAL));
//        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));

        //floatingActionButton = findViewById(R.id.day);
        final TextView Day = findViewById(R.id.day);
        final TextView textView = findViewById(R.id.textView);
        final TextView all = findViewById(R.id.all);



        Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog picker = new DatePickerDialog(FoodListActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    date = DateFor.parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    Log.d("Nazar", date.toString());
                                    DateFor = new SimpleDateFormat("dd MMMM");
                                    stringDate = DateFor.format(date);
                                    textView.setText(stringDate);
                                    foodRepository.filterFoodDaily(TimestampConverter.dateToTimestamp(date)).observe(FoodListActivity.this, new Observer<List<Food>>() {
                                        @Override
                                        public void onChanged(List<Food> foods) {
                                            ((FoodListAdapter) recyclerView.getAdapter()).addTasks(foods);
                                        }
                                    });
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
            int portion = getIntent().getIntExtra("Portion", -1);
            Date date = (Date) getIntent().getSerializableExtra("Date");
            LocalTime foodCreatedAt = (LocalTime) getIntent().getSerializableExtra("FoodCreatedAt");


            foodRepository.insertTask(portion, date, foodCreatedAt);
            updateTaskList();
        }
        updateTaskList();
    }

    private void updateTaskList() {
        foodRepository.getFoodTasks().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(@Nullable List<Food> foods) {
                if(foods.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    if (foodListAdapter == null) {
                        foodListAdapter = new FoodListAdapter(foods);
                        recyclerView.setAdapter(foodListAdapter);

                    } else foodListAdapter.addTasks(foods);
                } else updateEmptyView();
            }
        });
    }

    private void updateEmptyView() {
        recyclerView.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            int portion = data.getIntExtra(String.valueOf(INTENT_PORTION), -1);
            Date foodCreatedDate = (Date) data.getSerializableExtra(String.valueOf(INTENT_FOOD_DATE));
            LocalTime foodCreatedAt = (LocalTime) data.getSerializableExtra(INTENT_FOOD_CREATED_AT);
            foodRepository.insertTask(portion, foodCreatedDate, foodCreatedAt);
            updateTaskList();
        }
    }

    @Override
    public void onClick(View view) {

    }
}
