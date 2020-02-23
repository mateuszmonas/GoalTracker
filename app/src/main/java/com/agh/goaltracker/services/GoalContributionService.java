package com.agh.goaltracker.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.agh.goaltracker.GoalTrackerApplication;
import com.agh.goaltracker.GoalsActivity;
import com.agh.goaltracker.R;
import com.agh.goaltracker.model.source.GoalRepository;

import java.util.Set;

import androidx.core.app.NotificationCompat;


public class GoalContributionService extends Service {
    private static final String EXTRA_GOAL_ID = "GOAL_ID";
    private static final String TAG = "GoalContributionService";
    private static final String ACTION_START_CONTRIBUTING = "START_CONTRIBUTING";
    private static final String ACTION_STOP_CONTRIBUTING = "STOP_CONTRIBUTING";
    GoalRepository goalRepository;
    boolean threadRunning = true;
    Set<Integer> contributingGoalsIds;
    Thread contributionThread = new Thread(() -> {
        while (threadRunning) {
            for (Integer contributingGoalsId : contributingGoalsIds) {
                goalRepository.increaseProgress(contributingGoalsId, 1);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    public static Intent createStartContributingIntent(Context context, int goalId) {
        Intent intent = new Intent(context, GoalContributionService.class);
        intent.putExtra(EXTRA_GOAL_ID, goalId);
        intent.setAction(ACTION_START_CONTRIBUTING);
        return intent;
    }

    public static Intent createStopContributingIntent(Context context, int goalId) {
        Intent intent = new Intent(context, GoalContributionService.class);
        intent.putExtra(EXTRA_GOAL_ID, goalId);
        intent.setAction(ACTION_STOP_CONTRIBUTING);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        goalRepository = ((GoalTrackerApplication) getApplication()).getGoalRepository();
        contributingGoalsIds = goalRepository.getContributingGoalsIds();
        contributionThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int goalId = intent.getIntExtra(EXTRA_GOAL_ID, -1);
        final String action = intent.getAction();
        switch (action) {
            case ACTION_START_CONTRIBUTING:
                goalRepository.startContributingToGoal(goalId);
                break;
            case ACTION_STOP_CONTRIBUTING:
                goalRepository.stopContributingToGoal(goalId);
                break;
        }

        Intent notificationIntent = GoalsActivity.createIntent(this);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);


        String a = "";
        for (Integer contributingGoalsId : goalRepository.getContributingGoalsIds()) {
            a += contributingGoalsId + " ";
        }
        Notification notification = new NotificationCompat.Builder(this, "CHANNEL_ID")
                // TODO: 23/02/20 change icon
                .setSmallIcon(R.drawable.ic_add_black_24dp)
                .setContentTitle("title")
                .setContentText(a)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        threadRunning = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
