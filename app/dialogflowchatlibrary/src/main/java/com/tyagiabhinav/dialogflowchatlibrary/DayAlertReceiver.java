package com.tyagiabhinav.dialogflowchatlibrary;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.tyagiabhinav.dialogflowchatlibrary.database.repository.IndexRepository;

public class DayAlertReceiver extends BroadcastReceiver {
    private static final String TAG = DayAlertReceiver.class.getSimpleName();
    IndexRepository indexRepository;
    NotificationCompat.Builder nb;
    public int index;

    @Override
    public void onReceive(Context context, Intent intent) {
        indexRepository = new IndexRepository(context);
        Messages messages = new Messages();
        index = indexRepository.lastIndex();
        NotificationHelper notificationHelper = new NotificationHelper(context);
        nb = notificationHelper.getChannelNotification(messages.messages[indexRepository.lastIndex()]);
        Intent i = new Intent(context, ChatbotActivity.class);
        i.putExtra("notification", true);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        nb.setContentIntent(contentIntent);
        notificationHelper.getManager().notify(1, nb.build());

        indexRepository.insertTask(indexRepository.lastIndex()+1);

    }
}
