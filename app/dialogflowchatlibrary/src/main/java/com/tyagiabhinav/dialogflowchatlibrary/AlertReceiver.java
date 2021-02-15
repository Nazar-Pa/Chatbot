package com.tyagiabhinav.dialogflowchatlibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


@RequiresApi(api = Build.VERSION_CODES.O)
public class AlertReceiver extends BroadcastReceiver {
    private static final String TAG = AlertReceiver.class.getSimpleName();

    ChatbotActivity chatbotActivity =  new ChatbotActivity();
    final String SHARED_PREFS = chatbotActivity.SHARED_PREFS;

    //boolean isFired = sharedPreferences.getBoolean("isFired", false);

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFired", false);
        editor.apply();
//        boolean isFired = sharedPreferences.getBoolean("isFired", false);
//        Log.d(TAG, "Nazar isFired issssssssss: "+ isFired);


//        NotificationHelper notificationHelper = new NotificationHelper(context);
//        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
//        notificationHelper.getManager().notify(1, nb.build());
    }
}
