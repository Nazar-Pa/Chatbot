package com.tyagiabhinav.dialogflowchatlibrary;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.charts.CircularGauge;
import com.anychart.core.axes.Circular;
import com.anychart.core.gauge.pointers.Bar;
import com.anychart.enums.Anchor;
import com.anychart.graphics.vector.Fill;
import com.anychart.graphics.vector.SolidFill;
import com.anychart.graphics.vector.text.HAlign;
import com.anychart.graphics.vector.text.VAlign;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2beta1.Context;
import com.google.cloud.dialogflow.v2beta1.DetectIntentResponse;
import com.google.cloud.dialogflow.v2beta1.EventInput;
import com.google.cloud.dialogflow.v2beta1.QueryInput;

import com.google.cloud.dialogflow.v2beta1.QueryResult;
import com.google.cloud.dialogflow.v2beta1.SessionName;
import com.google.cloud.dialogflow.v2beta1.SessionsClient;
import com.google.cloud.dialogflow.v2beta1.SessionsSettings;
import com.google.cloud.dialogflow.v2beta1.TextInput;
import com.google.protobuf.Struct;


import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
//import com.tyagiabhinav.dialogflowchatlibrary.SmileRating;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Food;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Message;
import com.tyagiabhinav.dialogflowchatlibrary.database.model.Note;
import com.tyagiabhinav.dialogflowchatlibrary.database.repository.AlarmRepository;
import com.tyagiabhinav.dialogflowchatlibrary.database.repository.MessageRepository;
import com.tyagiabhinav.dialogflowchatlibrary.database.repository.NoteRepository;
import com.tyagiabhinav.dialogflowchatlibrary.database.repository.FoodRepository;
import com.tyagiabhinav.dialogflowchatlibrary.database.ui.adapter.FoodListAdapter;
import com.tyagiabhinav.dialogflowchatlibrary.database.ui.adapter.MessageListAdapter;
import com.tyagiabhinav.dialogflowchatlibrary.database.ui.adapter.NotesListAdapter;
import com.tyagiabhinav.dialogflowchatlibrary.database.util.TimestampConverter;
import com.tyagiabhinav.dialogflowchatlibrary.networkutil.ChatbotCallback;
import com.tyagiabhinav.dialogflowchatlibrary.networkutil.TaskRunner;
import com.tyagiabhinav.dialogflowchatlibrary.templates.ButtonMessageTemplate;
import com.tyagiabhinav.dialogflowchatlibrary.templates.CarouselTemplate;
import com.tyagiabhinav.dialogflowchatlibrary.templates.TextMessageTemplate;
import com.tyagiabhinav.dialogflowchatlibrary.templateutil.Constants;
import com.tyagiabhinav.dialogflowchatlibrary.templateutil.OnClickCallback;
import com.tyagiabhinav.dialogflowchatlibrary.templateutil.ReturnMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.time.LocalTime;
import static java.time.temporal.ChronoUnit.MINUTES;
//import java.util.Locale;
import java.util.Locale;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;


@RequiresApi(api = Build.VERSION_CODES.O)
public class ChatbotActivity extends AppCompatActivity implements ChatbotCallback, OnClickCallback, SmileRating.OnSmileySelectionListener, SmileRating.OnRatingSelectedListener {

    public static int stepCount = 0;
    public static int food = 0;
    public static int activityAmount = 0;
    public static int foodSize = 0;
    public static int activitySize = 0;
    public static int messageSize = 0;
    public static String how;
    public static boolean fired = false;



    //------------- STEPS
    enum FitActionRequestCode {
        SUBSCRIBE,
        READ_DATA
    }

    private FitnessOptions fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .build();
    private boolean runningQOrLater =
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;
    //------------- STEPS

    private static final String TAG = ChatbotActivity.class.getSimpleName();
    private static final int USER = 10001;
    private static final int BOT = 10002;
    private static final int SPEECH_INPUT = 10070;

    public static String SESSION_ID = "sessionID";


    //UI
    private LinearLayout chatLayout;
    private EditText queryEditText;
    private ImageView chatMic;
    private ImageView sendBtn;

    //Variables
    private SessionsClient sessionsClient;
    private SessionName session;
    public TaskRunner dialogflowTaskRunner;
    private boolean isProgressRunning;
    public Date date;
    public LocalTime startedAt;
    public LocalTime endedAt;
    public LocalTime foodCreatedAt;
    public String duration;
    public long difference;
    Date dialogDate = null;
    Date dialDate = null;
    Date dialActivityDate = null;
    Date dialogActivityDate = null;
    LocalTime dialogTime = null;
    LocalTime dialogActivityStartTime;
    LocalTime dialogActivityEndTime;
    public String stringDate;
    NoteRepository noteRepository;
    FoodRepository foodRepository;
    Messages messages;
    MessageRepository messageRepository;
    AlarmRepository alarmRepository;
    public ZonedDateTime zonedDateTime = null;
    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    //final DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");

    public SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");
    public SimpleDateFormat Datefor = new SimpleDateFormat("dd MMM");

    public static final String SHARED_PREFS = "sharedPrefs";



    public static final String GOAL = "goal";
    public static final String NAME = "name";
    public static final String PROCCESTYPE = "proccestype";
    public static final String ACTIVITYNAME = "activity_name";
    public static final String APPOPENED = "openedFirst";

    public String activity;
    public String dayTime = "Hi";
    public TextMessageTemplate tmt2;
    public TextMessageTemplate tmt3;
    public NestedScrollView scrollview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (getIntent().getBooleanExtra("notification", false)) {
            //DialogflowCredentials.getInstance().setInputStream(getResources().openRawResource(R.raw.test_agent_credentials));

            ChatbotSettings.getInstance().setChatbot( new Chatbot.ChatbotBuilder()
                    .setShowMic(true) // False by Default, True if you want to use Voice input from the user to chat
                    .build());

            ChatbotActivity.SESSION_ID = UUID.randomUUID().toString();
        }


        date = Calendar.getInstance().getTime();

        //------------- STEPS
        checkPermissionsAndRun(FitActionRequestCode.SUBSCRIBE);
        //------------- STEPS

        final TextMessageTemplate tmt4 = new TextMessageTemplate(ChatbotActivity.this, ChatbotActivity.this, Constants.BOT);
        tmt2 = new TextMessageTemplate(ChatbotActivity.this, ChatbotActivity.this, Constants.BOT);
        tmt3 = new TextMessageTemplate(ChatbotActivity.this, ChatbotActivity.this, Constants.BOT);
        setContentView(R.layout.activity_chatbot);
        chatLayout = findViewById(R.id.chatLayout);
        Log.d(TAG, "onCreate: ");

        ChatbotSettings chatSettings = ChatbotSettings.getInstance();
        messageRepository = new MessageRepository(getApplicationContext());
        messageSize = messageRepository.ItemsCount();

        alarmRepository = new AlarmRepository(getApplicationContext());



        //------------- Notifications

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 05);
        c.set(Calendar.MINUTE, 50);
        c.set(Calendar.SECOND, 0);
        startAlarm(c);
        //deliverNotification(c);


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 0);
        morningNotification(calendar);



        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 0);
        noonNotification(calendar);


        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 0);
        dayNotification(calendar);


        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 0);
        afternoonNotification(calendar);


        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 0);
        eveningNotification(calendar);



        //------------- Notifications






        final int random_int = (int)(Math.random() * 4);
        messages = new Messages();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if(sharedPreferences.getString(APPOPENED, "")!=""){
            chatLayout.addView(tmt3.showMessage("...", false));
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            chatLayout.removeViewAt(chatLayout.getChildCount() - 1);
                            chatLayout.addView(tmt4.showMessage(messages.morning[random_int], false));
                        }
                    });
                }
            }, 1500L);

        }






        Date date = new Date();
        SimpleDateFormat timeFor = new SimpleDateFormat("HH");
        int hours = Integer.parseInt(timeFor.format(date));

        if(hours>11 && hours <18){
            dayTime = "Good afternoon";
        } else if(hours>2 && hours <12){
            dayTime = "Good morning";
        } else if(hours>17 || hours<2){
            dayTime = "Good evening";
        }


//        if(hours>6 && hours<23 && sharedPreferences.getBoolean("isFired", false)==false){
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean("isFired", true);
//            editor.apply();
//            //isFired = sharedPreferences.getBoolean("isFired", false);
//        }

        Log.d(TAG, "Nazar isFired is: " + sharedPreferences.getBoolean("isFired", false));

            final LayoutInflater inflater = LayoutInflater.from(this);
        //ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.activity_main_message, (ViewGroup) chatLayout.getRootView(), false);

        final RecyclerView recyclerView = findViewById(R.id.listView);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        messageRepository.getMessageTasks().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(@Nullable final List<Message> messages) {
                TextMessageTemplate tmt = new TextMessageTemplate(ChatbotActivity.this, ChatbotActivity.this, Constants.BOT);
                if(messages.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(new MessageListAdapter(messages));
                    recyclerView.scrollToPosition(messages.size()-1);
                    //recyclerView.scrollTo(0,0);
                    scrollView();
                } else {
                    updateEmptyView(recyclerView);
                }
            }
        });


        queryEditText = findViewById(R.id.queryEditText);
        //recyclerView.scrollTo(0,0);

        KeyboardVisibilityEvent.setEventListener(this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean b) {
                        scrollView();
                    }
                }
        );






        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor));

        Toolbar toolbar = ChatbotSettings.getInstance().getAppToolbar();
        if (toolbar == null) {
            toolbar = findViewById(R.id.toolbar);
            ChatbotSettings.getInstance().setAppToolbar(toolbar);
        }

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }


        sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Send click");
                sendMessage(view);
            }
        });



        Bundle bundle = getIntent().getExtras();
        String sessionID = null;
        if (bundle != null) {
            sessionID = bundle.getString(SESSION_ID);
            if (sessionID == null || sessionID.trim().isEmpty()) {
                sessionID = UUID.randomUUID().toString();
            }
        }

        try {
            init(sessionID);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ChatbotActivity.this, "Error creating a session!", Toast.LENGTH_LONG).show();
        }
        noteRepository = new NoteRepository(getApplicationContext());
        foodRepository = new FoodRepository(getApplicationContext());
    }




    @Override
    public void onRatingSelected(int level, boolean reselected) {

    }

    @Override
    public void onSmileySelected(int smiley, boolean reselected) {
        TextMessageTemplate tmt = new TextMessageTemplate(ChatbotActivity.this, ChatbotActivity.this, Constants.BOT);
        final ConstraintLayout addRatingBarView = findViewById(R.id.ratingBar);
        final ImageView submit = findViewById(R.id.submit);
        switch (smiley) {
            case SmileRating.BAD:
                how = "Bad";
                submit.setVisibility(View.VISIBLE);
                break;
            case SmileRating.GOOD:
                how = "Good";
                submit.setVisibility(View.VISIBLE);
                break;
            case SmileRating.GREAT:
                how = "Great";
                submit.setVisibility(View.VISIBLE);
                break;
            case SmileRating.OKAY:
                how = "Okay";
                submit.setVisibility(View.VISIBLE);
                break;
            case SmileRating.TERRIBLE:
                how = "Terrible";
                submit.setVisibility(View.VISIBLE);
                break;
            case SmileRating.NONE:
                how = "None";
                submit.setVisibility(View.VISIBLE);
                break;
        }
    }




    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(android.content.Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void morningNotification(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(android.content.Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MorningAlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void noonNotification(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(android.content.Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NoonAlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    private void dayNotification(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(android.content.Context.ALARM_SERVICE);
        Intent intent = new Intent(this, DayAlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void afternoonNotification(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(android.content.Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AfternoonAlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void eveningNotification(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(android.content.Context.ALARM_SERVICE);
        Intent intent = new Intent(this, EveningAlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    /**
     * Initializes a custom log class that outputs both to in-app targets and logcat.
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    private void checkPermissionsAndRun(FitActionRequestCode fitActionRequestCode) {
        if (permissionApproved()) {
            Log.d("Nazar", "permission approved");
            fitSignIn(fitActionRequestCode, false);
        } else {
            Log.d("Nazar", "requestRuntimePermissions");
            requestRuntimePermissions(fitActionRequestCode);
        }
    }

    private void requestRuntimePermissions(final FitActionRequestCode requestCode) {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACTIVITY_RECOGNITION);

        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(findViewById(R.id.chatLayout),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(
                                    ChatbotActivity.this,
                                    new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                                    requestCode.ordinal());
                        }
                    }).show();
        } else {
            Log.i(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                    requestCode.ordinal());
        }
    }

    private boolean permissionApproved() {
        if (runningQOrLater) {
            return
                    PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACTIVITY_RECOGNITION);
        } else {
            return true;
        }
    }

    private void fitSignIn(FitActionRequestCode requestCode, boolean openCircularGauge) {
        if (oAuthPermissionsApproved()) {
            performActionForRequestCode(requestCode, openCircularGauge);
        } else {
            GoogleSignIn.requestPermissions(
                    ChatbotActivity.this,
                    requestCode.ordinal(),
                    getGoogleAccount(), fitnessOptions);
        }
    }

    private void performActionForRequestCode(FitActionRequestCode requestCode,  boolean openCircularGauge) {
        switch (requestCode) {
            case READ_DATA:
                readData(openCircularGauge);
                break;
            case SUBSCRIBE:
                subscribe();
                break;
        }
    }




    public void readData(final boolean openCircularGauge) {
        final Date[] date2 = {Calendar.getInstance().getTime()};
        food = foodRepository.DailySumOfPortions(TimestampConverter.dateToTimestamp(date2[0]));
        activityAmount = noteRepository.DailyNumberOfActivities(TimestampConverter.dateToTimestamp(date2[0]));

        Fitness.getHistoryClient(this, getGoogleAccount())
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        int total = 0;
                        if (!dataSet.isEmpty())
                            total = dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                        stepCount = total;

//                        if (openCircularGauge) {
//                            TextMessageTemplate tmtt = new TextMessageTemplate(ChatbotActivity.this, ChatbotActivity.this, Constants.BOT);
//                            chatLayout.addView(tmtt.showMessage("So far you reached " + stepCount + " steps today"));
//                            chatLayout.addView(getCircularGauge());
//
////                            Intent intent = new Intent(ChatbotActivity.this, CircularGaugeActivity.class);
////                            intent.putExtra("step", total);
////                            startActivity(intent);
//                        }
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        Toast.makeText(ChatbotActivity.this, "Steps: " + total, Toast.LENGTH_LONG).show();
                        TextMessageTemplate tmtt = new TextMessageTemplate(ChatbotActivity.this, ChatbotActivity.this, Constants.BOT);
                        TextMessageTemplate tmt2 = new TextMessageTemplate(ChatbotActivity.this, ChatbotActivity.this, Constants.BOT);
                        if(activityAmount!=0 && food!=0 && sharedPreferences.getString(GOAL, "0").equals("0")){
                            chatLayout.addView(tmtt.showMessage("So far you reached " + stepCount + " steps, did " + activityAmount +" activities and ate "+ food +" portions of fruit/vegetables today", true));
                            chatLayout.addView(getCircularGauge());
                            chatLayout.addView(tmt2.showMessage("But I see you haven't set a daily goal yet. Would you like to set it now?", true));

                        }else if(activityAmount==0 && food!=0 && sharedPreferences.getString(GOAL, "0").equals("0")){
                            chatLayout.addView(tmtt.showMessage("So far you reached " + stepCount + " steps, and ate "+ food +" portions of fruit/vegetables today", true));
                            chatLayout.addView(getCircularGauge());
                            chatLayout.addView(tmt2.showMessage("And I see you have neither added an activity nor set a goal. Do you want to set a goal now.", true));

                        } else if(activityAmount!=0 && food==0 && sharedPreferences.getString(GOAL, "0").equals("0")){
                            chatLayout.addView(tmtt.showMessage("So far you reached " + stepCount + " steps, did " + activityAmount +" activities today", true));
                            chatLayout.addView(getCircularGauge());
                            chatLayout.addView(tmt2.showMessage("And I see you have neither entered your fruit/vegetables intake nor set a goal. Do you want to set a goal now.", true));
                        } else if(activityAmount==0 && food==0 && sharedPreferences.getString(GOAL, "0").equals("0")){
                            chatLayout.addView(tmtt.showMessage("So far you reached " + stepCount + " steps today", true));
                            chatLayout.addView(getCircularGauge());
                            chatLayout.addView(tmt2.showMessage("But I see you haven't added any activity, your food intake and set a daily goal yet. Would you like to set a goal now?", true));
                        } else if(activityAmount==0 && food==0 && !sharedPreferences.getString(GOAL, "0").equals("0")){
                            chatLayout.addView(tmtt.showMessage("So far you reached " + stepCount + " steps today", true));
                            chatLayout.addView(getCircularGauge());
                            chatLayout.addView(tmt2.showMessage("But I see you have added neither an activity nor fruit/vegetables intake yet. Just message me to do so.",true));
                        } else if(activityAmount!=0 && food==0 && !sharedPreferences.getString(GOAL, "0").equals("0")){
                            chatLayout.addView(tmtt.showMessage("So far you reached " + stepCount + " steps and added " + activityAmount+ " activities today", true));
                            chatLayout.addView(getCircularGauge());
                            chatLayout.addView(tmt2.showMessage("But I see you haven't added your fruit/vegetables intake yet. Just message me to do so.", true));
                        } else if(activityAmount==0 && food!=0 && !sharedPreferences.getString(GOAL, "0").equals("0")){
                            chatLayout.addView(tmtt.showMessage("So far you reached " + stepCount + " steps and ate " + food + " portions of fruit/vegetables today", true));
                            chatLayout.addView(getCircularGauge());
                            chatLayout.addView(tmt2.showMessage("But I see you haven't added any activity yet. Just message me to do so.", true));
                        } else {
                            chatLayout.addView(tmtt.showMessage("So far you reached " + stepCount + " steps, did " + activityAmount +" activities and ate "+ food +" portions of fruit/vegetables today", true));
                            chatLayout.addView(getCircularGauge());
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "There was a problem getting the step count.", e);
                        Log.d("Nazar", "There was a problem getting the step count." + e.toString());
                    }
                });
    }

    private void subscribe() {
        Fitness.getRecordingClient(this, getGoogleAccount())
                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "Successfully subscribed!");
                            Log.d("Nazar", "succesfull");
                        }
                        else {
                            Log.d("Nazar", "problem subscribe");
                            Log.w(TAG, "There was a problem subscribing.", task.getException());
                        }
                    }
                });
    }


    private boolean oAuthPermissionsApproved() {
        return GoogleSignIn.hasPermissions(getGoogleAccount(), fitnessOptions);
    }

    private GoogleSignInAccount getGoogleAccount() {
        return GoogleSignIn.getAccountForExtension(this, fitnessOptions);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_read_data) {
            fitSignIn(FitActionRequestCode.READ_DATA, false);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //closeDialog();
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == SPEECH_INPUT) {
//            if (resultCode == Activity.RESULT_OK && data != null) {
//                String result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
//                Log.d(TAG, "onActivityResult: " + result);
//                queryEditText.setText(result);
//                send(result);
//            }
//        }

        if (requestCode == FitActionRequestCode.READ_DATA.ordinal() || requestCode == FitActionRequestCode.SUBSCRIBE.ordinal()) {
            switch (resultCode) {
                case RESULT_OK:
                    FitActionRequestCode postSignInAction = FitActionRequestCode.values()[requestCode];
                    performActionForRequestCode(postSignInAction, false);
                    break;
                default:
                    oAuthErrorMsg(requestCode, resultCode);
                    break;
            }
        }
    }

    private void oAuthErrorMsg(int requestCode, int resultCode) {
        String message = "There was an error signing into Fit.";
        Log.e(TAG, message + requestCode + resultCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0) {
            Log.i(TAG, "User interaction was cancelled.");
        } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            FitActionRequestCode fitActionRequestCode = FitActionRequestCode.values()[requestCode];
            fitSignIn(fitActionRequestCode, false);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void OnChatbotResponse(DetectIntentResponse response) {
        removeProcessWaitBubble();
        processResponse(response);
    }

    @Override
    public void OnUserClickAction(ReturnMessage msg) {
        String eventName = msg.getEventName();
        Struct param = msg.getParam();
        if (eventName != null && !eventName.trim().isEmpty()) {
            if (param != null && param.getFieldsCount() > 0) {
                EventInput eventInput = EventInput.newBuilder().setName(eventName).setLanguageCode("en-US").setParameters(param).build();
                send(eventInput, msg.getActionText());
            } else {
                EventInput eventInput = EventInput.newBuilder().setName(eventName).setLanguageCode("en-US").build();
                send(eventInput, msg.getActionText());
            }
        } else {
            send(msg.getActionText());
        }
    }








    private void init(String UUID) throws IOException {
        InputStream credentialStream = DialogflowCredentials.getInstance().getInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialStream);
        String projectId = ((ServiceAccountCredentials) credentials).getProjectId();

        SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
        SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
        sessionsClient = SessionsClient.create(sessionsSettings);
        session = SessionName.of(projectId, UUID);


        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if (ChatbotSettings.getInstance().isAutoWelcome()) {
            send("hi");

        }

    }

    private void sendMessage(View view) {
        String msg = queryEditText.getText().toString();
        if (msg.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter your query!", Toast.LENGTH_LONG).show();
        } else if (msg.equals("0")){
            Toast.makeText(getApplicationContext(), "Please enter correct information!", Toast.LENGTH_LONG).show();
        }
        else {
            Date currentTime = new Date();
            send(msg);
            messageRepository = new MessageRepository(getApplicationContext());
            messageRepository.insertTask(false, null, msg, currentTime);
        }
    }

    private void send(String message) {
        Log.d(TAG, "send: 1");
        TextMessageTemplate tmt = new TextMessageTemplate(getApplicationContext(), ChatbotActivity.this, Constants.USER);
        if (!ChatbotSettings.getInstance().isAutoWelcome()) {
            chatLayout.addView(tmt.showMessage(message, false));
            queryEditText.setText("");
            showProcessWaitBubble();
        } else {
            ChatbotSettings.getInstance().setAutoWelcome(false);
        }
        QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText(message).setLanguageCode("en-US")).build();

        //Log.d(TAG, "Nazar message " + message);
        dialogflowTaskRunner = new TaskRunner(this, session, sessionsClient, queryInput);
        dialogflowTaskRunner.executeChat();
    }

    private void send(EventInput event, String message) {
        Log.d(TAG, "send: 2");
        TextMessageTemplate tmt = new TextMessageTemplate(getApplicationContext(), ChatbotActivity.this, Constants.USER);
        if (!ChatbotSettings.getInstance().isAutoWelcome()) {
            chatLayout.addView(tmt.showMessage(message, false));
            queryEditText.setText("");
            showProcessWaitBubble();
        } else {
            ChatbotSettings.getInstance().setAutoWelcome(false);
        }

        QueryInput queryInput = QueryInput.newBuilder().setEvent(event).build();
        dialogflowTaskRunner = new TaskRunner(this, session, sessionsClient, queryInput);
        dialogflowTaskRunner.executeChat();
    }

    private void showProcessWaitBubble() {
        TextMessageTemplate tmt = new TextMessageTemplate(getApplicationContext(), ChatbotActivity.this, Constants.BOT);
        chatLayout.addView(tmt.showMessage("...", false));
        isProgressRunning = true;
        enableDissableChatUI(false);

    }

    private void removeProcessWaitBubble() {
        enableDissableChatUI(true);
        if (isProgressRunning && chatLayout != null && chatLayout.getChildCount() > 0) {
            chatLayout.removeViewAt(chatLayout.getChildCount() - 1);
            isProgressRunning = false;
        }
    }

    public void showWaitingView(TextMessageTemplate t){

        chatLayout.addView(t.showMessage("...", false));
        isProgressRunning = true;
        enableDissableChatUI(false);
    }

    public void removeWaitingView(){
        chatLayout.removeViewAt(chatLayout.getChildCount() - 1);
    }

    private String getIntentOfCancel(DetectIntentResponse response1){
        QueryResult queryResult1 = response1.getQueryResult();
        String intentN = queryResult1.getIntent().getDisplayName();
        return intentN;
    }

    String intentN;





    @RequiresApi(api = Build.VERSION_CODES.O)
    private void processResponse(DetectIntentResponse response) {

        QueryResult queryResult = response.getQueryResult();
        Log.d(TAG, "processResponse");
        if (response != null) {
            List<Context> contextList = response.getQueryResult().getOutputContextsList();
            int layoutCount = chatLayout.getChildCount();
            SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");
            SimpleDateFormat Datefor = new SimpleDateFormat("dd MMM");
            final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String intentName = queryResult.getIntent().getDisplayName();
                Log.d(TAG, "Nazar instent is cancellll yes " + intentName);
                intentN = intentName;
                foodSize = foodRepository.ItemsCount();
                activitySize = noteRepository.ItemsCount();
                final Date[] date2 = {Calendar.getInstance().getTime()};
                food = foodRepository.DailySumOfPortions(TimestampConverter.dateToTimestamp(date2[0]));
                activityAmount = noteRepository.DailyNumberOfActivities(TimestampConverter.dateToTimestamp(date2[0]));
                final TextMessageTemplate tmt = new TextMessageTemplate(ChatbotActivity.this, ChatbotActivity.this, Constants.BOT);
                final TextMessageTemplate tmt3 = new TextMessageTemplate(ChatbotActivity.this, ChatbotActivity.this, Constants.BOT);
                CarouselTemplate crt = new CarouselTemplate(ChatbotActivity.this, ChatbotActivity.this, Constants.BOT);
                final Date currenTime = new Date();

                SimpleDateFormat timeFor = new SimpleDateFormat("HH");
                final int hours = Integer.parseInt(timeFor.format(currenTime));
                final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                Struct object = queryResult.getParameters();
                Calendar calendar = Calendar.getInstance();
                switch (intentName) {
                    case "AddActivity":
                        if(object.getFieldsCount() == 0){
                            chatLayout.addView(tmt.showMessage(response));
                        } else {
                            ZonedDateTime zoneActivityStartTime = null;
                            ZonedDateTime zoneActivityEndTime = null;
                            DateTimeFormatter formatt = DateTimeFormatter.ofPattern("HH:mm");
                            activity = queryResult.getParameters().getFieldsMap().get("activity_type").getStringValue();
                            String dateActivity = queryResult.getParameters().getFieldsMap().get("date").getStringValue();
                            String startingtime = queryResult.getParameters().getFieldsMap().get("starting_time").getStringValue();
                            String endingtime = queryResult.getParameters().getFieldsMap().get("ending_time").getStringValue();

                            if (dateActivity!=null && !dateActivity.isEmpty()) {
                                try {
                                    dialActivityDate = formatter.parse(dateActivity);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                            if(startingtime!=null && !startingtime.isEmpty()){
                                zoneActivityStartTime = ZonedDateTime.parse(startingtime);
                                dialogActivityStartTime = LocalTime.parse(zoneActivityStartTime.format(formatt));
                            }

                            if(endingtime!=null && !endingtime.isEmpty()){
                                zoneActivityEndTime = ZonedDateTime.parse(endingtime);
                                dialogActivityEndTime = LocalTime.parse(zoneActivityEndTime.format(formatt));
                            }

                            if(activity!="" && dateActivity!="" && startingtime!="" && endingtime!=""){
                                chatLayout.addView(tmt.showMessage("You got " + activity + " on " + simpleDateformat.format(dialActivityDate) + ", at " + zoneActivityStartTime.format(formatt)  +
                                        ". Hit SAVE to save your entry.", true)); // move focus to text view to automatically make it scroll up if softfocus
                                chatLayout.addView(getAddActivityView(activity, dateActivity, zoneActivityStartTime.format(formatt), zoneActivityEndTime.format(formatt)));
                                scrollView();
                            }else if(activity==""){
                                chatLayout.addView(tmt.showMessage(response));
                                chatLayout.addView(askingActivity());
                                scrollView();
                            } else if(dateActivity==""){
                                chatLayout.addView(tmt.showMessage(response));
                                chatLayout.addView(askingActivityDate());
                                scrollView();
                            } else{
                                chatLayout.addView(tmt.showMessage(response));
                                scrollView();
                            }

                            if(activity!=""){
                                ConstraintLayout activityNameAsking = findViewById(R.id.activityNameAsking);
                                if(activityNameAsking!=null){
                                    layoutDelete(activityNameAsking);
                                }
                            }

                            if(dateActivity!=""){
                                ConstraintLayout activityDateAsking = findViewById(R.id.date_asking);
                                if(activityDateAsking!=null){
                                    layoutDelete(activityDateAsking);
                                }
                            }
                            //scrollView();
                        }

                        break;
                    case "addFoodEntry":
                        if(object.getFieldsCount() == 0){
                            chatLayout.addView(tmt.showMessage(response));
                        } else {
                            DecimalFormat format = new DecimalFormat("#");
                            final DateTimeFormatter formattFood = DateTimeFormatter.ofPattern("HH:mm");
                            final String portion = format.format(contextList.get(0).getParameters().getFieldsMap().get("portion").getNumberValue());
                            final String date = contextList.get(0).getParameters().getFieldsMap().get("date").getStringValue();
                            String time = contextList.get(0).getParameters().getFieldsMap().get("time").getStringValue();

                            if (date != null && !date.isEmpty()) {
                                try {
                                    dialDate = formatter.parse(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (time != null && !time.isEmpty()) {
                                zonedDateTime = ZonedDateTime.parse(time);
                                dialogTime = LocalTime.parse(zonedDateTime.format(formattFood));
                            }
                            if (portion != "" && date != "" && time != "") {
                                chatLayout.addView(tmt.showMessage("You got " + portion + " portions of fruit/vegetables on " + simpleDateformat.format(dialDate) + ", " + Datefor.format(dialDate) + ". Click SAVE to save your entry.", true));
                                chatLayout.addView(getAddFoodView(portion, date, zonedDateTime.format(formattFood)));
                                scrollView();
                            } else if (portion.equals("0")) {
                                chatLayout.addView(tmt.showMessage(response));
                            } else if (date == "" || date == null) {
                                chatLayout.addView(tmt.showMessage(response));
                                chatLayout.addView(askingActivityDate());
                                scrollView();
                            } else {
                                chatLayout.addView(tmt.showMessage(response));
                            }

                            if(date!=""){
                                ConstraintLayout foodDateAsking = findViewById(R.id.date_asking);
                                if(foodDateAsking!=null){
                                    layoutDelete(foodDateAsking);
                                }
                            }
                        }
                        scrollView();
                        queryEditText.requestFocus();
                        break;
                    case "getCalories":
                        chatLayout.addView(tmt.showMessage(response)); // move focus to text view to automatically make it scroll up if softfocus
                        queryEditText.requestFocus();
                        break;
                    case "getSteps":
                        //chatLayout.addView(tmt.showMessage("..."));
                        //((ViewGroup)chatLayout.getParent()).removeView(chatLayout);
                        //chatLayout.removeView((View) chatLayout.getC);
                        fitSignIn(FitActionRequestCode.READ_DATA, true);
                        queryEditText.requestFocus();
                        break;
                    case "ActivateMotivationIntent":
                        break;
                    case "myFoodProgress":
                        if(foodSize>0){
                            if(food>0){
                                chatLayout.addView(tmt.showMessage(sharedPreferences.getString(NAME, "Hey") + " your dietary progress is showing. So far you added " + food
                                        + " portions of fruit/vegetables today.", true));
                                chatLayout.addView(getMyFoodprogressview()); // move focus to text view to automatically make it scroll up if softfocus
                                scrollView();
                                queryEditText.requestFocus();
                            } else {
                                chatLayout.addView(tmt.showMessage(sharedPreferences.getString(NAME, "Hey") + " your dietary progress is showing. So far you haven't added any portions of fruit/vegetables today. Do you want to add one now?", true));
                                chatLayout.addView(getMyFoodprogressview()); // move focus to text view to automatically make it scroll up if softfocus
                                scrollView();
                                queryEditText.requestFocus();
                            }
                        }
                        else {
                            chatLayout.addView(tmt.showMessage("You have not saved any food entry yet. Do you want to add one now?", true));
                            queryEditText.requestFocus();
                        }
                        break;
                    case "myActivityprogress":
                        if(activitySize>0){
                            if(activityAmount>0){
                                chatLayout.addView(tmt.showMessage(sharedPreferences.getString(NAME, "Hey") + " your activity progress is showing. So far you did " + activityAmount + " activities today", true));
                                chatLayout.addView(getMyActivityProgressView()); // move focus to text view to automatically make it scroll up if softfocus
                                scrollView();
                                queryEditText.requestFocus();
                            } else {
                                chatLayout.addView(tmt.showMessage(sharedPreferences.getString(NAME, "Hey") + " your activity progress is showing. So far you haven't added any activity today. Do you want to add one now?", true));
                                chatLayout.addView(getMyActivityProgressView()); // move focus to text view to automatically make it scroll up if softfocus
                                scrollView();
                                queryEditText.requestFocus();
                            }
                        } else {
                            chatLayout.addView(tmt.showMessage("You have not saved any activity entry yet. Do you want to add one now?", true));
                            queryEditText.requestFocus();
                        }
                        break;
                    case "MyProgress":
                        String progressType = queryResult.getParameters().getFieldsMap().get("progress_type").getStringValue();
                        if(progressType!="" && !progressType.isEmpty()){SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(PROCCESTYPE, progressType);
                            editor.apply();
                            if(progressType.equals("Food")){
                                if(foodSize>0){
                                    chatLayout.addView(tmt.showMessage(sharedPreferences.getString(NAME, "Hey") + " your dietary progress is showing. So far you added " + food
                                            + " portions of fruit/vegetables today.", true));
                                    chatLayout.addView(getMyFoodprogressview()); // move focus to text view to automatically make it scroll up if softfocus
                                    scrollView();
                                }
                                else {
                                    chatLayout.addView(tmt.showMessage("You have not saved any food entry yet. Do you want to add one now?", true));
                                    queryEditText.requestFocus();
                                }
                            } else if(progressType.equals("Activity")){
                                if(activitySize>0){
                                    chatLayout.addView(tmt.showMessage(sharedPreferences.getString(NAME, "Hey") + " your activity progress is showing. So far you did" + activityAmount + " activities today", true));
                                    chatLayout.addView(getMyActivityProgressView()); // move focus to text view to automatically make it scroll up if softfocus
                                    scrollView();
                                    queryEditText.requestFocus();
                                } else {
                                    chatLayout.addView(tmt.showMessage("You have not saved any activity entry yet. Do you want to add one now?", true));
                                    queryEditText.requestFocus();
                                }
                            }
                        }else{
                            ButtonMessageTemplate bmt = new ButtonMessageTemplate(ChatbotActivity.this, ChatbotActivity.this, Constants.BOT);
                            chatLayout.addView(bmt.showMessage(response));
                            queryEditText.setEnabled(false);
                        }
                        break;
                    case "Capture Android Event":
                        break;
                    case "addStepGoal":
                        chatLayout.addView(tmt.showMessage(response)); // move focus to text view to automatically make it scroll up if softfocus
                        chatLayout.addView(getAddGoalView());
                        //scrollView();
                        final EditText numberofsteps = findViewById(R.id.numberofsteps);
                        queryEditText.setEnabled(false);
                        if(numberofsteps!=null){
                            numberofsteps.requestFocus();
                        }
                        break;
                    case "Default Welcome Intent":
                        if(sharedPreferences.getString(APPOPENED, "")==""){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(APPOPENED, "openedFirstTime");
                            editor.apply();
                            Date currentTime = new Date();
                            messageRepository.insertTask(true, "Greetings \uD83D\uDD90 I am a bot will help you track your physical activity and food journaling. What is your name?", null, currentTime);
                            queryEditText.requestFocus();
                        } else if(sharedPreferences.getString(NAME, "")=="" && sharedPreferences.getString(APPOPENED, "")!=null){
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            chatLayout.addView(tmt.showMessage("...", false));
                                            new Timer().schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            chatLayout.removeViewAt(chatLayout.getChildCount() - 1);
                                                            chatLayout.addView(tmt3.showMessage(dayTime + ", how can I help you now?", false));
                                                            if(hours>6 && hours<12 && sharedPreferences.getBoolean("isFired", false)==false){
                                                                chatLayout.addView(getRatingBar());
                                                                scrollView();
                                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                editor.putBoolean("isFired", true);
                                                                editor.apply();

                                                            }

                                                            queryEditText.requestFocus();
                                                        }
                                                    });
                                                }
                                            }, 400L);

                                        }
                                    });
                                }
                            }, 1000L);

                            queryEditText.requestFocus();
                        }

                        if(sharedPreferences.getString(NAME, "")!="" && sharedPreferences.getString(APPOPENED, "")!=null){
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            chatLayout.addView(tmt.showMessage("...", false));
                                            new Timer().schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            chatLayout.removeViewAt(chatLayout.getChildCount() - 1);
                                                            chatLayout.addView(tmt3.showMessage(dayTime + " " + sharedPreferences.getString(NAME, "") + ". How can I help you?", false));
                                                            if(hours>6 && hours<12 && sharedPreferences.getBoolean("isFired", false)==false){
                                                                chatLayout.addView(getRatingBar());
                                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                editor.putBoolean("isFired", true);
                                                                editor.apply();

                                                            }

                                                            queryEditText.requestFocus();
                                                        }
                                                    });
                                                }
                                            }, 400L);

                                        }
                                    });
                                }
                            }, 1000L);
                        }
                        queryEditText.requestFocus();
                        break;
                    case "UserGaveName":
                        String nameDialog = queryResult.getParameters().getFieldsMap().get("name").getStringValue();
                        if(nameDialog!=null && !nameDialog.isEmpty()){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(NAME, nameDialog);
                            editor.apply();
                            chatLayout.addView(tmt.showMessage("Nice to meet you " + sharedPreferences.getString(NAME, "") + ". Track your physical activity and food journaling with me. You can set up a daily step goal, " +
                                    "add an activity/food. You have also chance to see your logging afterwards. Where you want to start from? You can start by setting number of steps goal to reach each day. Do you " +
                                            "want to do that?", true
                                    ));
                            queryEditText.requestFocus();
                        }
                        break;
                    case "Capabilities":
                        //chatLayout.addView(tmt.showMessage("I am chatbot"));
                        chatLayout.addView(tmt2.showMessage("I am chatbot and help you to track your physical activity and food journaling with me. You can set up a daily step goal, " +
                                "add an activity/food. You have also chance to see your logging afterwards. Where you want to start from? You can start by setting number of steps goal to reach " +
                                "each day", true));
                        //chatLayout.addView(crt.showMessage(response));
                        queryEditText.requestFocus();
                        break;
                    case "AddActivity - yes":
                        chatLayout.addView(tmt.showMessage(sharedPreferences.getString(NAME, "Hey") + " your activity progress is showing. So far you did " + activityAmount + " activities today", true));
                        chatLayout.addView(getMyActivityProgressView()); // move focus to text view to automatically make it scroll up if softfocus
                        scrollView();
                        queryEditText.requestFocus();
                        break;
                    case "addFoodEntry - yes":
                        chatLayout.addView(tmt.showMessage(sharedPreferences.getString(NAME, "Hey") + " your dietary progress is showing. So far you added " + food
                                + " portions of fruit/vegetables today.", true));
                        chatLayout.addView(getMyFoodprogressview()); // move focus to text view to automatically make it scroll up if softfocus
                        scrollView();
                        queryEditText.requestFocus();
                        break;
                    case "Cancel":
                        final ConstraintLayout addFoodLayout = findViewById(R.id.addFood);
                        final ConstraintLayout addActivityLayout = findViewById(R.id.addActivity);
                        final ConstraintLayout addGoalLayout = findViewById(R.id.add_goal);
                        final ConstraintLayout layoutAskingDate = findViewById(R.id.date_asking);
                        final ConstraintLayout activityNameAsking = findViewById(R.id.activityNameAsking);

                        if(addFoodLayout!=null){
                            //Log.d(TAG, "Nazar instent is cancellll yes " + intentName);
                            layoutDelete(addFoodLayout);
                        } else if(addActivityLayout!=null){
                            layoutDelete(addActivityLayout);
                        } else if(addGoalLayout!=null){
                            layoutDelete(addGoalLayout);
                        } else if(layoutAskingDate!=null){
                            layoutDelete(layoutAskingDate);
                        }else if(activityNameAsking!=null){
                            //Log.d(TAG, "Nazar instent is cancellll yes " + intentName);
                            layoutDelete(activityNameAsking);
                        }
                        //Log.d(TAG, "Nazar instent is cancellll yes " + intentName);
                        chatLayout.addView(tmt.showMessage(response));
                        queryEditText.requestFocus();
                        break;
                    case "Default Fallback Intent - custom":
                    case "SayingOkay":
                        //chatLayout.addView(getLikeHand());
                        chatLayout.addView(tmt.showMessage("Very good", true));
                        queryEditText.requestFocus();
                        break;
                    case "myFoodProgress - yes":
                        QueryInput queryInput2 = QueryInput.newBuilder().setText(TextInput.newBuilder().setText("want to add food").setLanguageCode("en-US")).build();
                        dialogflowTaskRunner = new TaskRunner(ChatbotActivity.this, session, sessionsClient, queryInput2);
                        dialogflowTaskRunner.executeChat();
                        break;
                    case "myActivityprogress - yes":
                        QueryInput queryInput3 = QueryInput.newBuilder().setText(TextInput.newBuilder().setText("want to add activity").setLanguageCode("en-US")).build();
                        dialogflowTaskRunner = new TaskRunner(ChatbotActivity.this, session, sessionsClient, queryInput3);
                        dialogflowTaskRunner.executeChat();
                        break;
                    case "UserGaveName - yes":
                        QueryInput queryInput4 = QueryInput.newBuilder().setText(TextInput.newBuilder().setText("want to set up a goal").setLanguageCode("en-US")).build();
                        dialogflowTaskRunner = new TaskRunner(ChatbotActivity.this, session, sessionsClient, queryInput4);
                        dialogflowTaskRunner.executeChat();
                        queryEditText.requestFocus();
                        break;
                    case "getSteps - yes":
                        QueryInput queryInput5 = QueryInput.newBuilder().setText(TextInput.newBuilder().setText("want to set up a goal").setLanguageCode("en-US")).build();
                        dialogflowTaskRunner = new TaskRunner(ChatbotActivity.this, session, sessionsClient, queryInput5);
                        dialogflowTaskRunner.executeChat();
                        queryEditText.requestFocus();
                        break;
                    case "null":
                        break;
                    case "MyProgress - yes":
                        if(sharedPreferences.getString(PROCCESTYPE, "").equals("Food")){
                            QueryInput queryInput6 = QueryInput.newBuilder().setText(TextInput.newBuilder().setText("want to add food").setLanguageCode("en-US")).build();
                            dialogflowTaskRunner = new TaskRunner(ChatbotActivity.this, session, sessionsClient, queryInput6);
                            dialogflowTaskRunner.executeChat();
                        } else {
                            QueryInput queryInput6 = QueryInput.newBuilder().setText(TextInput.newBuilder().setText("want to add activity").setLanguageCode("en-US")).build();
                            dialogflowTaskRunner = new TaskRunner(ChatbotActivity.this, session, sessionsClient, queryInput6);
                            dialogflowTaskRunner.executeChat();
                        }
                        queryEditText.requestFocus();
                        break;
                    case "Default Welcome Intent - noName":
                        chatLayout.addView(tmt.showMessage("Nice to meet you. Track your physical activity and food journaling with me. You can set up a daily step goal, " +
                                "add an activity/food. You have also chance to see your logging afterwards. Where you want to start from? You can start by setting number of steps goal to reach each day. Do you " +
                                "want to do that?", true
                        ));
                        queryEditText.requestFocus();
                        break;
                    case "Default Welcome Intent - noName - yes":
                        QueryInput queryInput7 = QueryInput.newBuilder().setText(TextInput.newBuilder().setText("want to set a goal").setLanguageCode("en-US")).build();
                        dialogflowTaskRunner = new TaskRunner(ChatbotActivity.this, session, sessionsClient, queryInput7);
                        dialogflowTaskRunner.executeChat();
                        break;
                    case "Default Fallback Intent":
                        chatLayout.addView(tmt.showMessage("I missed what you said \uD83D\uDE14. Please message me about the things that I am able to help you with.", true));
                        queryEditText.requestFocus();
                        break;
                    default:
                        chatLayout.addView(tmt.showMessage(response)); // move focus to text view to automatically make it scroll up if softfocus
                        queryEditText.requestFocus();
                }
        }
    }





    public void layoutDelete(final ConstraintLayout constraintLayout){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        constraintLayout.animate()
                                .alpha(0f)
                                .setDuration(500)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        constraintLayout.setVisibility(View.GONE);
                                    }
                                });
                    }
                });
            }
        }, 600L);


    }




    private ConstraintLayout getRatingBar() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        final ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.rating_bar, null, false);
        final TextMessageTemplate tmt = new TextMessageTemplate(getApplicationContext(), ChatbotActivity.this, Constants.BOT);
        final TextMessageTemplate tmtU = new TextMessageTemplate(getApplicationContext(), ChatbotActivity.this, Constants.USER);

        final SmileRating mSmileRating = layout.findViewById(R.id.ratingView);
        mSmileRating.setOnSmileySelectionListener(this);
        mSmileRating.setOnRatingSelectedListener(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ImageView submit = layout.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final ConstraintLayout constraintLayout = findViewById(R.id.ratingBar);
                Log.d(TAG, "Nazar: " + how);
                layoutDelete(constraintLayout);
                chatLayout.addView(tmtU.showMessage( how +" is selected", false));
                chatLayout.addView(tmt.showMessage("Happy that you've " + how +" slept", true));
            }
        });


        return layout;
    }




    private ConstraintLayout askingActivityDate(){
        final LayoutInflater inflater = LayoutInflater.from(this);
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.date_asking, null, false);
        final Button today = layout.findViewById(R.id.today);
        final Button yesterday = layout.findViewById(R.id.yesterday);
        final TextMessageTemplate tmt = new TextMessageTemplate(getApplicationContext(), ChatbotActivity.this, Constants.USER);

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatLayout.addView(tmt.showMessage("Today is selected", false));
                final ConstraintLayout askActivityLayout = findViewById(R.id.date_asking);

                QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText("today").setLanguageCode("en-US")).build();
                dialogflowTaskRunner = new TaskRunner(ChatbotActivity.this, session, sessionsClient, queryInput);
                dialogflowTaskRunner.executeChat();

                if(askActivityLayout!=null){
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    askActivityLayout.animate()
                                            .alpha(0f)
                                            .setDuration(300)
                                            .setListener(new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    askActivityLayout.setVisibility(View.GONE);
                                                }

                                            });

                                }
                            });
                        }
                    }, 600L);
                }


            }


        });

        yesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatLayout.addView(tmt.showMessage("Yesterday is selected", false));
                final ConstraintLayout askActivityLayout = findViewById(R.id.date_asking);

                QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText("yesterday").setLanguageCode("en-US")).build();
                dialogflowTaskRunner = new TaskRunner(ChatbotActivity.this, session, sessionsClient, queryInput);
                dialogflowTaskRunner.executeChat();

                if(askActivityLayout!=null){
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    askActivityLayout.animate()
                                            .alpha(0f)
                                            .setDuration(300)
                                            .setListener(new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    askActivityLayout.setVisibility(View.GONE);
                                                }

                                            });

                                }
                            });
                        }
                    }, 600L);
                }

            }
        });


        return layout;
    }






    private ConstraintLayout askingActivity(){
        final LayoutInflater inflater = LayoutInflater.from(this);
        final ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.activity_asking, null, false);

        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final Button lastActivity1 = layout.findViewById(R.id.lastActivity1);
        final Button lastActivity2 = layout.findViewById(R.id.lastActivity2);
        final TextMessageTemplate tmt = new TextMessageTemplate(getApplicationContext(), ChatbotActivity.this, Constants.USER);

        String activityType = sharedPreferences.getString(ACTIVITYNAME, "");

        if(activityType != "" && !activityType.equals(lastActivity2.getText().toString())){
            lastActivity1.setText(activityType);
        }

        final ConstraintLayout askActivityLayout = findViewById(R.id.activityNameAsking);
        lastActivity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chatLayout.addView(tmt.showMessage(lastActivity1.getText().toString() + " is selected", false));

                QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText(lastActivity1.getText().toString()).setLanguageCode("en-US")).build();
                dialogflowTaskRunner = new TaskRunner(ChatbotActivity.this, session, sessionsClient, queryInput);
                dialogflowTaskRunner.executeChat();

                if(askActivityLayout!=null){
                    layoutDelete(askActivityLayout);
                }

            }


        });

        lastActivity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatLayout.addView(tmt.showMessage(lastActivity2.getText().toString() + " is selected", false));

                QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText(lastActivity2.getText().toString()).setLanguageCode("en-US")).build();
                dialogflowTaskRunner = new TaskRunner(ChatbotActivity.this, session, sessionsClient, queryInput);
                dialogflowTaskRunner.executeChat();
                if(askActivityLayout!=null){
                    layoutDelete(askActivityLayout);
                }
            }
        });

        if(activity!=""){
            if(askActivityLayout!=null){
                layoutDelete(askActivityLayout);
            }
        }


        return layout;
    }


    private ConstraintLayout getAddGoalView() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        final ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.add_goal, null, false);

        final TextMessageTemplate tmt = new TextMessageTemplate(ChatbotActivity.this, ChatbotActivity.this, Constants.BOT);
        final TextMessageTemplate tmt2 = new TextMessageTemplate(ChatbotActivity.this, ChatbotActivity.this, Constants.USER);
        final TextMessageTemplate tmt3 = new TextMessageTemplate(ChatbotActivity.this, ChatbotActivity.this, Constants.BOT);
        final EditText numberofsteps = layout.findViewById(R.id.numberofsteps);
        final Date[] date2 = {Calendar.getInstance().getTime()};
        food = foodRepository.DailySumOfPortions(TimestampConverter.dateToTimestamp(date2[0]));
        activityAmount = noteRepository.DailyNumberOfActivities(TimestampConverter.dateToTimestamp(date2[0]));
        //queryEditText.setEnabled(false);
        //numberofsteps.requestFocus();
        messageRepository = new MessageRepository(getApplicationContext());
        layout.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(GOAL, numberofsteps.getText().toString());
                editor.apply();
                chatLayout.addView(tmt.showMessage(sharedPreferences.getString(NAME, "Hey") +" you set a goal to reach " + sharedPreferences.getString(GOAL, "55") + " steps daily.", true));

                layoutDelete(layout);

                if(activityAmount==0 && food==0){
                    chatLayout.addView(tmt3.showMessage("I see you have added neither activity nor food intake yet. Message me if you want to add these as well. You can try commands like 'add activity' or 'add food'", true));
                } else if(activityAmount!=0 && food==0){
                    chatLayout.addView(tmt3.showMessage("And I see you haven't added any fruit/vegetables intake yet. Message me if you want to to do so.", true));
                } else if(activityAmount==0 && food!=0){
                    chatLayout.addView(tmt3.showMessage("And I see you haven't added any activity yet. Message me if you want to to do so.", true));
                }
                queryEditText.setEnabled(true);
                queryEditText.requestFocus();

            }
        });

        layout.findViewById(R.id.cancel2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutDelete(layout);
                chatLayout.addView(tmt2.showMessage("Cancel is selected.", false));
                chatLayout.addView(tmt3.showMessage("Ok setting goal was cancelled.", true));
                queryEditText.setEnabled(true);
                queryEditText.requestFocus();
            }
        });

        return layout;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private ConstraintLayout getAddActivityView(final String activityText, final String dateDialogflow, final String startTimeDialog, final String endTimeDialog) {
        final LayoutInflater inflater = LayoutInflater.from(this);
        final ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.add_activity, null, false);
        final TextMessageTemplate tmt = new TextMessageTemplate(ChatbotActivity.this, ChatbotActivity.this, Constants.BOT);
        Date currentDate = new Date();
        final EditText title = layout.findViewById(R.id.title);
        final Button Date = layout.findViewById(R.id.date);
        SimpleDateFormat DateFor = new SimpleDateFormat("dd MMM");
        String stringCurrentDate = DateFor.format(currentDate);
        Date.setText("Date: " + stringCurrentDate);
        Button startTime = layout.findViewById(R.id.startTime);
        SimpleDateFormat timeFor = new SimpleDateFormat("HH:mm");
        String stringCurrentTime = timeFor.format(currentDate);
        startTime.setText("Start: " + stringCurrentTime);
        Button endTime = layout.findViewById(R.id.endTime);
        Date oneHourLater = new Date(System.currentTimeMillis() + 3600 * 1000);
        //timeFor = new SimpleDateFormat("HH:mm");
        String stringOneHourLater = timeFor.format(oneHourLater);
        endTime.setText("End: " + stringOneHourLater);

        if (dateDialogflow!=null && !dateDialogflow.isEmpty()) {
            try {
                dialogActivityDate = formatter.parse(dateDialogflow);
                Date.setText("Date: " + DateFor.format(dialogActivityDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else
            Date.setText("Date: " + stringCurrentDate);


        if (startTimeDialog!=null && !startTimeDialog.isEmpty()) {
            dialogActivityStartTime = LocalTime.parse(startTimeDialog);
            startTime.setText("Start: " + startTimeDialog);
        } else
            startTime.setText("Start: " + stringCurrentTime);


        if (endTimeDialog!=null && !endTimeDialog.isEmpty()) {
            dialogActivityEndTime = LocalTime.parse(endTimeDialog);
            endTime.setText("End: " + endTimeDialog);

        } else
            endTime.setText("End: " + stringOneHourLater);


        if(dialogActivityStartTime!=null && dialogActivityEndTime!=null){
            difference = dialogActivityStartTime.until(dialogActivityEndTime, MINUTES);
            if(difference>60){
                long hour = difference/60;
                long minutes = difference - hour*60;
                duration = hour+"" + " hours " + minutes+"" + " minutes";
            } else {
                duration = difference+" minutes";
            }
        }

        if(activityText == ""){
            title.setText("");
        } else {
            title.setText(activityText);}

        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker((Button) v);
            }
        });
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker((Button) v);
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerForEndedAt((Button) v);
            }
        });

        layout.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteRepository.insertTask(title.getText().toString(), dialogActivityDate, dialogActivityStartTime, duration);
                chatLayout.addView(tmt.showMessage("Your entry was saved as " + duration + " of " + title.getText().toString() + " on " + simpleDateformat.format(dialogActivityDate) + ", "
                        + Datefor.format(dialogActivityDate) +
                        " at " + dialogActivityStartTime + ". You can check your activity logging any time. Do you want to see it now?", true));
                //chatLayout.addView(tmt.showMessage("Your entry was saved. You can check your activity logging any time. Do you want to see it now?"));
                queryEditText.requestFocus();

                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(ACTIVITYNAME, title.getText().toString());
                editor.apply();

                layoutDelete(layout);

            }
        });

        layout.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText("cancel").setLanguageCode("en-US")).build();
                dialogflowTaskRunner = new TaskRunner(ChatbotActivity.this, session, sessionsClient, queryInput);
                dialogflowTaskRunner.executeChat();

                layoutDelete(layout);

            }
        });

        return layout;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private ConstraintLayout getAddFoodView(final String portionText, final String dateDialogflow, final String timeDialogflow) {
        final TextMessageTemplate tmt = new TextMessageTemplate(ChatbotActivity.this, ChatbotActivity.this, Constants.BOT);
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();

        final LayoutInflater inflater = LayoutInflater.from(this);
        final ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.add_food, (ViewGroup) chatLayout.getRootView(), false);
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layout.getLayoutParams();
//        params.leftMargin = 150;
//        params.rightMargin = 24;
//        params.topMargin = 24;
//        params.bottomMargin = 24;
//        layout.setLayoutParams(params);

        final EditText portion = layout.findViewById(R.id.portion);
        final Button Date = layout.findViewById(R.id.date);
        Button time = layout.findViewById(R.id.time);
        SimpleDateFormat timeFor = new SimpleDateFormat("HH:mm");
        String stringCurrentTime = timeFor.format(currentDate);
        SimpleDateFormat DateFor = new SimpleDateFormat("dd MMM");
        String stringCurrentDate = DateFor.format(currentDate);


        if (dateDialogflow!=null && !dateDialogflow.isEmpty()) {
            try {
                dialogDate = formatter.parse(dateDialogflow);
                Date.setText("Date: " + DateFor.format(dialogDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else
            Date.setText("Date: " + stringCurrentDate);



        if (timeDialogflow!=null && !timeDialogflow.isEmpty()) {
            dialogTime = LocalTime.parse(timeDialogflow);
            time.setText("Time: " + timeDialogflow);
        } else
            time.setText("Time: " + stringCurrentTime);


        if(Integer.parseInt(portionText) == 0){
            portion.setText("");
        } else {
        portion.setText(portionText);}

        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker((Button) v);
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFoodTimePicker((Button) v);
            }
        });

        layout.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                foodRepository.insertTask(Integer.parseInt(portion.getText().toString()), dialogDate, dialogTime);
                chatLayout.addView(tmt.showMessage("Your entry was saved as " + portion.getText().toString() + " portions of fruit/vegetables on " + simpleDateformat.format(dialogDate) + ", " + Datefor.format(dialogDate) +
                        " at " + dialogTime + ". You can check your food logging any time. Do you want to see it now?", true));

                queryEditText.requestFocus();

                layoutDelete(layout);

            }
        });

        layout.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText("cancel").setLanguageCode("en-US")).build();
                dialogflowTaskRunner = new TaskRunner(ChatbotActivity.this, session, sessionsClient, queryInput);
                dialogflowTaskRunner.executeChat();

                layoutDelete(layout);

            }
        });


        return layout;
    }



    private ConstraintLayout getMyActivityProgressView(){

        final Date[] date = {Calendar.getInstance().getTime()};
        final NotesListAdapter notesListAdapter = null;
        final LayoutInflater inflater = LayoutInflater.from(this);
        final ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.activity_note_list, null, false);
        final RecyclerView recyclerView = layout.findViewById(R.id.task_list);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1 , StaggeredGridLayoutManager.VERTICAL));

        final TextView Day =  layout.findViewById(R.id.day);
        final TextView textView =  layout.findViewById(R.id.textView);
        final TextView all =  layout.findViewById(R.id.all);
        final Date currenTime = new Date();

        Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog picker = new DatePickerDialog(ChatbotActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    date[0] = DateFor.parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    DateFor = new SimpleDateFormat("dd MMMM");
                                    stringDate = DateFor.format(date[0]);
                                    textView.setTextSize(14);
                                    textView.setText(stringDate);
                                    noteRepository.filterDaily(TimestampConverter.dateToTimestamp(date[0])).observe(ChatbotActivity.this, new Observer<List<Note>>() {
                                        @Override
                                        public void onChanged(List<Note> notes) {
                                            ((NotesListAdapter) recyclerView.getAdapter()).addTasks(notes);
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
                noteRepository.getTasks().observe(ChatbotActivity.this, new Observer<List<Note>>() {
                    @Override
                    public void onChanged(@Nullable List<Note> notes){
                        recyclerView.setVisibility(View.VISIBLE);
                        ((NotesListAdapter) recyclerView.getAdapter()).addTasks(notes);
                        textView.setTextSize(18);
                        textView.setText("All");
                    }
                });
            }
        });


        //updateActivityTaskList(recyclerView, notesListAdapter);

        activitySize = noteRepository.ItemsCount();

        noteRepository.getTasks().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                TextMessageTemplate tmt = new TextMessageTemplate(ChatbotActivity.this, ChatbotActivity.this, Constants.BOT);
                if(notes.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(new NotesListAdapter(notes));
                } else {
                    updateEmptyView(recyclerView);
                    recyclerView.setVisibility(View.INVISIBLE);
                    layout.setVisibility(View.GONE);
                    chatLayout.addView(tmt.showMessage("You have no activity entry anymore", true));
                }
            }
        });

        return layout;
    }




    private ConstraintLayout getMyFoodprogressview() {
        QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText("null").setLanguageCode("en-US")).build();
        dialogflowTaskRunner = new TaskRunner(ChatbotActivity.this, session, sessionsClient, queryInput);
        dialogflowTaskRunner.executeChat();
        final FoodListAdapter foodListAdapter = null;
        final Date[] date = {Calendar.getInstance().getTime()};
        final LayoutInflater inflater = LayoutInflater.from(this);
        final ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.activity_food_list, null, false);
        final RecyclerView recyclerView = layout.findViewById(R.id.task_list);
        final LinearLayout navigation = layout.findViewById(R.id.navigation);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1 , StaggeredGridLayoutManager.VERTICAL));
        final Date currenTime = new Date();

        final TextView Day =  layout.findViewById(R.id.day);
        final TextView textView =  layout.findViewById(R.id.textView);
        final TextView all =  layout.findViewById(R.id.all);


        Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog picker = new DatePickerDialog(ChatbotActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    date[0] = DateFor.parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    DateFor = new SimpleDateFormat("dd MMMM");
                                    stringDate = DateFor.format(date[0]);
                                    textView.setTextSize(14);
                                    textView.setText(stringDate);
                                    foodRepository.filterFoodDaily(TimestampConverter.dateToTimestamp(date[0])).observe(ChatbotActivity.this, new Observer<List<Food>>() {
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
                foodRepository.getFoodTasks().observe(ChatbotActivity.this, new Observer<List<Food>>() {
                    @Override
                    public void onChanged(@Nullable List<Food> foods){
                        recyclerView.setVisibility(View.VISIBLE);
                        ((FoodListAdapter) recyclerView.getAdapter()).addTasks(foods);
                        textView.setTextSize(18);
                        textView.setText("All");
                    }
                });
            }
        });


        foodRepository.getFoodTasks().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(@Nullable List<Food> foods) {
                TextMessageTemplate tmt = new TextMessageTemplate(ChatbotActivity.this, ChatbotActivity.this, Constants.BOT);
                if(foods.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(new FoodListAdapter(foods));
                } else {
                    updateEmptyView(recyclerView);
                    recyclerView.setVisibility(View.INVISIBLE);
                    layout.setVisibility(View.GONE);
                    chatLayout.addView(tmt.showMessage("You have no food entry anymore", true));
                }
            }
        });


        if(foodSize==0){
            recyclerView.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
        }


        return layout;
    }


    private void updateActivityTaskList(final RecyclerView recyclerView, final NotesListAdapter notesListAdapter) {
        noteRepository.getTasks().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                TextMessageTemplate tmt = new TextMessageTemplate(ChatbotActivity.this, ChatbotActivity.this, Constants.BOT);
                if(notes.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(new NotesListAdapter(notes));
                } else{
                    updateEmptyView(recyclerView);
                    chatLayout.addView(tmt.showMessage("You have not saved any activity entry yet", true));

                }

            }
        });
    }


    private void updateFoodTaskList(final RecyclerView recyclerView, final FoodListAdapter foodListAdapter) {
        foodRepository.getFoodTasks().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(@Nullable List<Food> foods) {

                if(foods.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    if (foodListAdapter == null) {
                        Log.d("Nazar", " foodListAdapter is null");
                        recyclerView.setAdapter(new FoodListAdapter(foods));

                    } else foodListAdapter.addTasks(foods);
                } else updateEmptyView(recyclerView);
            }
        });
    }

    private void updateEmptyView(RecyclerView recyclerView) {
        //recyclerView.setVisibility(View.GONE);
        LinearLayout linearLayout = findViewById(R.id.navigation);
        //final LayoutInflater inflater = LayoutInflater.from(this);
        //ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.activity_note_list, null, false);
        //linearLayout.setVisibility(View.GONE);
        //findViewById(R.id.botIcon1).setVisibility(View.GONE);
    }




    private void showDatePicker(final Button button) {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog picker = new DatePickerDialog(ChatbotActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        dialogDate = null;
                        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            date = DateFor.parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            dialogDate = date;
                            dialogActivityDate = date;
                            DateFor = new SimpleDateFormat("dd MMMM");
                            stringDate = DateFor.format(date);
                            button.setText(stringDate);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        picker.show();

    }




    private void showFoodTimePicker(final Button button) {
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ChatbotActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(selectedMinute<10){
                    startedAt = LocalTime.parse(selectedHour + ":0" + selectedMinute);
                    foodCreatedAt = LocalTime.parse(selectedHour + ":0" + selectedMinute);
                    dialogTime = foodCreatedAt;
                    dialogActivityStartTime = startedAt;
                    button.setText(selectedHour + ":" + selectedMinute);
                } else {
                    startedAt = LocalTime.parse(selectedHour + ":" + selectedMinute);
                    foodCreatedAt = LocalTime.parse(selectedHour + ":" + selectedMinute);
                    dialogTime = foodCreatedAt;
                    dialogActivityStartTime = startedAt;
                    button.setText(selectedHour + ":" + selectedMinute);
                }

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.show();

    }




    private void showTimePicker(final Button button) {
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ChatbotActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(selectedMinute<10){
                    startedAt = LocalTime.parse(selectedHour + ":0" + selectedMinute);
                    if(endedAt!=null){
                        difference = startedAt.until(endedAt, MINUTES);
                        if(difference>60){
                            long hour = difference/60;
                            long minutes = difference - hour*60;
                            duration = hour+"" + " hours " + minutes+"" + " minutes";
                        } else {
                            duration = difference+" minutes";
                        }
                    } else{
                        difference = startedAt.until(dialogActivityEndTime, MINUTES);
                        if(difference>60){
                            long hour = difference/60;
                            long minutes = difference - hour*60;
                            duration = hour+"" + " hours " + minutes+"" + " minutes";
                        } else {
                            duration = difference+" minutes";
                        }
                    }
                    foodCreatedAt = LocalTime.parse(selectedHour + ":0" + selectedMinute);
                    dialogTime = foodCreatedAt;
                    dialogActivityStartTime = startedAt;
                    button.setText(selectedHour + ":0" + selectedMinute);
                } else {

                    startedAt = LocalTime.parse(selectedHour + ":" + selectedMinute);
                    if (endedAt != null) {
                        difference = startedAt.until(endedAt, MINUTES);
                        if (difference > 60) {
                            long hour = difference / 60;
                            long minutes = difference - hour * 60;
                            duration = hour + "" + " hours " + minutes + "" + " minutes";
                        } else {
                            duration = difference + " minutes";
                        }
                    } else {
                        difference = startedAt.until(dialogActivityEndTime, MINUTES);
                        if (difference > 60) {
                            long hour = difference / 60;
                            long minutes = difference - hour * 60;
                            duration = hour + "" + " hours " + minutes + "" + " minutes";
                        } else {
                            duration = difference + " minutes";
                        }
                    }
                    foodCreatedAt = LocalTime.parse(selectedHour + ":" + selectedMinute);
                    dialogTime = foodCreatedAt;
                    dialogActivityStartTime = startedAt;
                    button.setText(selectedHour + ":" + selectedMinute);
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.show();

    }

    private void showTimePickerForEndedAt(final Button button) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ChatbotActivity.this, new TimePickerDialog.OnTimeSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(selectedMinute<10){
                    endedAt = LocalTime.parse(selectedHour + ":0" + selectedMinute);
                    if (startedAt != null) {
                        difference = startedAt.until(endedAt, MINUTES);
                        if (difference > 60) {
                            long hour = difference / 60;
                            long minutes = difference - hour * 60;
                            duration = hour + "" + " hours " + minutes + "" + " minutes";
                        } else {
                            duration = difference + " minutes";
                        }
                    } else {
                        difference = dialogActivityStartTime.until(endedAt, MINUTES);
                        if (difference > 60) {
                            long hour = difference / 60;
                            long minutes = difference - hour * 60;
                            duration = hour + "" + " hours " + minutes + "" + " minutes";
                        } else {
                            duration = difference + " minutes";
                        }
                    }
                    dialogActivityEndTime = endedAt;
                    button.setText(selectedHour + ":0" + selectedMinute);
                } else {
                    endedAt = LocalTime.parse(selectedHour + ":" + selectedMinute);
                    if (startedAt != null) {
                        difference = startedAt.until(endedAt, MINUTES);
                        if (difference > 60) {
                            long hour = difference / 60;
                            long minutes = difference - hour * 60;
                            duration = hour + "" + " hours " + minutes + "" + " minutes";
                        } else {
                            duration = difference + " minutes";
                        }
                    } else {
                        difference = dialogActivityStartTime.until(endedAt, MINUTES);
                        if (difference > 60) {
                            long hour = difference / 60;
                            long minutes = difference - hour * 60;
                            duration = hour + "" + " hours " + minutes + "" + " minutes";
                        } else {
                            duration = difference + " minutes";
                        }
                    }
                    dialogActivityEndTime = endedAt;
                    button.setText(selectedHour + ":" + selectedMinute);
                }

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.show();

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private ConstraintLayout getCircularGauge() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.activity_chart_common, (ViewGroup) chatLayout.getRootView(), false);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layout.getLayoutParams();

        final Date[] date2 = {Calendar.getInstance().getTime()};


        food = foodRepository.DailySumOfPortions(TimestampConverter.dateToTimestamp(date2[0]));
        activityAmount = noteRepository.DailyNumberOfActivities(TimestampConverter.dateToTimestamp(date2[0]));

        params.leftMargin = 130;
        params.rightMargin = 24;
        params.topMargin = 24;
        params.bottomMargin = 24;

        layout.setLayoutParams(params);

        AnyChartView anyChartView = layout.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(layout.findViewById(R.id.progress_bar));
        TextView textView = layout.findViewById(R.id.textView);
        textView.setText("Goal: " + sharedPreferences.getString(GOAL, "0") + " steps");

        String stepNum = Integer.toString(stepCount);
        //String stepPerc = Integer.toString(stepPercent);

        int a = (stepCount*100)/Integer.parseInt(sharedPreferences.getString(GOAL, "3000"));

        CircularGauge circularGauge = AnyChart.circular();
        circularGauge.data(new SingleValueDataSet(new String[] { String.valueOf(a), String.valueOf(activityAmount*10), String.valueOf(food*10), "93", "90", "100", sharedPreferences.getString(GOAL, "3000")}));
        circularGauge.fill("#56BCDC")
                .stroke(null)

                .padding(0d, 0d, 0d, 0d)
                .margin(0d, 0d, 0d, 0d)
                .background("#56BCDC")
        ;

        circularGauge.startAngle(0d);
        circularGauge.sweepAngle(360d);

        Circular xAxis = circularGauge.axis(0)
                .radius(100d)
                .width(1d)
                .fill((Fill) null);
        xAxis.scale()
                .minimum(0d)
                .maximum(100d)
        ;
        xAxis.ticks("{ interval: 1 }")
                .minorTicks("{ interval: 1 }");
        xAxis.labels().enabled(false);
        xAxis.ticks().enabled(false);
        xAxis.minorTicks().enabled(false);



        circularGauge.label(0d)
                .text("Steps: "+ stepNum)
                .useHtml(true)
                .hAlign(HAlign.CENTER)
                .vAlign(VAlign.MIDDLE)
                //.fontColor("#F6546A")
                .fontSize(16)
        .fontWeight(600)
        .fontColor("#7C1D56");
        circularGauge.label(0d)
                .anchor(Anchor.RIGHT_CENTER)
                .padding(0d, 67d, 0d, 0d)
                .height(17d / 2d + "%")
                .offsetY(95d + "%")
                .offsetX(6d);
        Bar bar0 = circularGauge.bar(0d);
        bar0.dataIndex(0d);
        bar0.radius(64d);
        bar0.width(9d);
        bar0.fill(new SolidFill("#F6546A", 1d));
        bar0.stroke("#F6546A");
        bar0.zIndex(6d);
        Bar bar100 = circularGauge.bar(100d);
        bar100.dataIndex(6d);
        bar100.radius(64d);
        bar100.width(11d);
        bar100.fill(new SolidFill("#F5F4F4", 1d));
        bar100.stroke("1 #050505");
        bar100.zIndex(4d);

        circularGauge.label(1d)
                .text("Activities: "+ activityAmount)
                .useHtml(true)
                .hAlign(HAlign.CENTER)
                .vAlign(VAlign.MIDDLE)
                //.fontColor("#6694e3")
                .fontSize(16)
                .fontWeight(600)
                .fontColor("#7C1D56");
        circularGauge.label(1d)
                .anchor(Anchor.RIGHT_CENTER)
                .padding(0d, 67d, 0d, 0d)
                .height(17d / 2d + "%")
                .offsetY(83d + "%")
                .offsetX(7d);
        Bar bar1 = circularGauge.bar(1d);
        bar1.dataIndex(1d);
        bar1.radius(45d);
        bar1.width(8d);
        bar1.fill(new SolidFill("#a2dba6", 1d));
        bar1.stroke("##a2dba6");
        bar1.zIndex(5d);
        Bar bar101 = circularGauge.bar(101d);
        bar101.dataIndex(5d);
        bar101.radius(45d);
        bar101.width(10d);
        bar101.fill(new SolidFill("#F5F4F4", 1d));
        bar101.stroke("1 #050505");
        bar101.zIndex(4d);

        circularGauge.label(2d)
                .text("Portions: "+ food)
                .useHtml(true)
                .hAlign(HAlign.CENTER)
                .vAlign(VAlign.MIDDLE)
                //.fontColor("#57c983")
                .fontSize(16)
                .fontWeight(600)
                .fontColor("#7C1D56");
        circularGauge.label(2d)
                .anchor(Anchor.RIGHT_CENTER)
                .padding(0d, 67d, 0d, 0d)
                .height(17d / 2d + "%")
                .offsetY(71d + "%")
                .offsetX(8d);
        Bar bar2 = circularGauge.bar(2d);
        bar2.dataIndex(2d);
        bar2.radius(25d);
        bar2.width(7d);
        bar2.fill(new SolidFill("#008080", 1d));
        bar2.stroke("#008080");
        bar2.zIndex(5d);
        Bar bar102 = circularGauge.bar(102d);
        bar102.dataIndex(5d);
        bar102.radius(25d);
        bar102.width(9d);
        bar102.fill(new SolidFill("#F5F4F4", 1d));
        bar102.stroke("1 #050505");
        bar102.zIndex(4d);

        circularGauge.margin(-30d, 0d, -60d, 0d);
        circularGauge.background("#56BCDC");

        circularGauge.title().enabled(false);
        circularGauge.title().hAlign(HAlign.CENTER);
        circularGauge.title()
                .padding(0d, 0d, 0d, 0d)
                .margin(0d, 0d, 20d, 0d);

        anyChartView.setChart(circularGauge);
        return layout;
    }



    private RelativeLayout getLikeHand(){
        final LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.like_hand, null, false);

        return layout;
    }



    public void scrollView(){
        scrollview = findViewById(R.id.chatScrollView);
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                queryEditText.requestFocus();
            }
        });
    }



    private void closeDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatbotActivity.this);
        ChatbotSettings.getInstance().setAppToolbar(null);
        ChatbotSettings.getInstance().setAutoWelcome(true);
//                                super.onBackPressed();
        ChatbotActivity.this.finish();
        alertDialogBuilder
                .create()
                .show();

    }

    private void enableDissableChatUI(boolean bool) {
//        chatMic.setEnabled(bool);
//        chatMic.setClickable(bool);
        queryEditText.setEnabled(bool);
        queryEditText.setClickable(bool);
        sendBtn.setEnabled(bool);
        sendBtn.setClickable(bool);
    }

    private void promptSpeech() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something !");
        try {
            startActivityForResult(intent, SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Sorry! Device does not support speech input", Toast.LENGTH_SHORT).show();
        }
    }

}
