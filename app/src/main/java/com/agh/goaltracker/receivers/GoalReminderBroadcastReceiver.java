package com.agh.goaltracker.receivers;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.agh.goaltracker.GoalDetailsActivity;
import com.agh.goaltracker.GoalsActivity;
import com.agh.goaltracker.R;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class GoalReminderBroadcastReceiver extends BroadcastReceiver {

    public static final String EXTRA_GOAL_ID = "GOAL_ID";
    public static final String EXTRA_GOAL_TITLE = "GOAL_TITLE";

    // TODO: 14/02/20 improve notification design
    @Override
    public void onReceive(Context context, Intent intent) {

        int goalId = intent.getIntExtra(EXTRA_GOAL_ID, -1);
        String title = intent.getStringExtra(EXTRA_GOAL_TITLE);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(GoalsActivity.createIntent(context));
        stackBuilder.addNextIntent(GoalDetailsActivity.createIntent(context, goalId));
        PendingIntent goalDetailsPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_add_black_24dp)
                .setContentTitle(title)
                .setContentText("start contributing now")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(goalDetailsPendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(1, notification);

    }

}
