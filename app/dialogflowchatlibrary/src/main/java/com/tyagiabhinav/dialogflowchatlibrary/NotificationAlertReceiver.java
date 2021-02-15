package com.tyagiabhinav.dialogflowchatlibrary;

import android.app.NativeActivity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;



public class NotificationAlertReceiver extends BroadcastReceiver {
    private static final String TAG = NotificationAlertReceiver.class.getSimpleName();


    @Override
    public void onReceive(Context context, Intent intent) {


        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification("Alarm is working fine");


//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        Intent i = new Intent(context, ChatbotActivity.class);
        i.putExtra("notification", true);
//        stackBuilder.addNextIntent(i);
//        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
//        nb.setContentIntent(resultPendingIntent);




        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);


        nb.setContentIntent(contentIntent);

        notificationHelper.getManager().notify(1, nb.build());
        //notificationHelper.getManager().cancel(1);
        //nb.setDeleteIntent(resultPendingIntent);
    }
}
