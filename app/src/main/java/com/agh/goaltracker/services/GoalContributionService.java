package com.agh.goaltracker.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.agh.goaltracker.GoalDetailsActivity;
import com.agh.goaltracker.GoalTrackerApplication;
import com.agh.goaltracker.GoalsActivity;
import com.agh.goaltracker.R;
import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.model.source.GoalRepository;

import java.util.Locale;
import java.util.Set;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.LiveData;

import static com.agh.goaltracker.GoalTrackerApplication.CONTRIBUTION_NOTIFICATION_CHANNEL_ID;


public class GoalContributionService extends LifecycleService {
    private static final String EXTRA_GOAL_ID = "GOAL_ID";
    private static final String TAG = "GoalContributionService";
    private static final String ACTION_START_CONTRIBUTING = "START_CONTRIBUTING";
    private static final String ACTION_STOP_CONTRIBUTING = "STOP_CONTRIBUTING";
    private static final int NOTIFICATION_ID = 1;
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

    LiveData<Goal> observedGoal;

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


    // FIXME: 23/02/20 probably should refractor this
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        int goalId = intent.getIntExtra(EXTRA_GOAL_ID, -1);
        final String action = intent.getAction();
        switch (action) {
            case ACTION_START_CONTRIBUTING:
                goalRepository.startContributingToGoal(goalId);
                break;
            case ACTION_STOP_CONTRIBUTING:
                goalRepository.stopContributingToGoal(goalId);
                goalRepository.observeGoal(goalId).removeObserver(this::updateSingleGoalNotification);
                break;
        }

        Notification notification = new NotificationCompat.Builder(this, CONTRIBUTION_NOTIFICATION_CHANNEL_ID)
                // TODO: 23/02/20 change icon
                .setSmallIcon(R.drawable.ic_add_black_24dp)
                .setContentTitle("temp")
                .setContentText("temp")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
        startForeground(NOTIFICATION_ID, notification);


        if (goalRepository.getContributingGoalsIds().isEmpty()) {
            stopSelf();
        } else if (goalRepository.getContributingGoalsIds().size() == 1) {
            observedGoal = goalRepository.observeGoal(goalRepository.getContributingGoalsIds().iterator().next());
            observedGoal.observe(this, this::updateSingleGoalNotification);
        } else if (goalRepository.getContributingGoalsIds().size() > 1) {
            observedGoal.removeObservers(this);
            Intent goalsActivityIntent = GoalsActivity.createIntent(this);
            PendingIntent goalsActivityPendingIntent =
                    PendingIntent.getActivity(this, 0, goalsActivityIntent, 0);

            Notification n = new NotificationCompat.Builder(this, CONTRIBUTION_NOTIFICATION_CHANNEL_ID)
                    // TODO: 23/02/20 change icon
                    .setSmallIcon(R.drawable.ic_add_black_24dp)
                    .setContentTitle(String.format(Locale.getDefault(), "contributing to %d goals", goalRepository.getContributingGoalsIds().size()))
                    .setContentText("Tap to open")
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setContentIntent(goalsActivityPendingIntent)
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, n);
        }


        return START_NOT_STICKY;
    }

    void updateSingleGoalNotification(Goal goal) {
        TaskStackBuilder stackBuilder = TaskStackBuilder
                .create(this)
                .addNextIntent(GoalsActivity.createIntent(this))
                .addNextIntent(GoalDetailsActivity.createIntent(this, goal.getGoalId()));

        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CONTRIBUTION_NOTIFICATION_CHANNEL_ID)
                // TODO: 23/02/20 change icon
                .setSmallIcon(R.drawable.ic_add_black_24dp)
                .setContentTitle(goal.getTitle())
                .setContentText(goal.progressToString())
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public void onDestroy() {
        threadRunning = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }
}
