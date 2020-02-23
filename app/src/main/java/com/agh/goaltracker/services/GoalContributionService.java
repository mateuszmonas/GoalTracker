package com.agh.goaltracker.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
<<<<<<< HEAD
import android.os.Handler;
=======
import android.os.IBinder;
>>>>>>> bde03db2d080399c7b2795142147a8d93db1d3ad

import com.agh.goaltracker.GoalDetailsActivity;
import com.agh.goaltracker.GoalTrackerApplication;
import com.agh.goaltracker.GoalsActivity;
import com.agh.goaltracker.R;
import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.model.source.GoalRepository;

<<<<<<< HEAD
import java.util.HashSet;
=======
>>>>>>> bde03db2d080399c7b2795142147a8d93db1d3ad
import java.util.Locale;
import java.util.Set;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.LiveData;
<<<<<<< HEAD
import androidx.lifecycle.MutableLiveData;
=======
>>>>>>> bde03db2d080399c7b2795142147a8d93db1d3ad

import static com.agh.goaltracker.GoalTrackerApplication.CONTRIBUTION_NOTIFICATION_CHANNEL_ID;


public class GoalContributionService extends LifecycleService {
    private static final String EXTRA_GOAL_ID = "GOAL_ID";
    private static final String ACTION_START_SERVICE = "START_SERVICE";
    private static final String ACTION_STOP_CONTRIBUTING = "STOP_CONTRIBUTING";
    private static final String ACTION_STOP_ALL_CONTRIBUTING = "STOP_ALL_CONTRIBUTING";
    private static final int NOTIFICATION_ID = 1;
    GoalRepository goalRepository;
    LiveData<Set<Integer>> contributingGoalsIds;

    LiveData<Goal> observedGoal = new MutableLiveData<>();

    Set<Integer> goalIds = new HashSet<>();

    boolean serviceRunning = true;
    Thread thread = new Thread(() ->
    {
        while (serviceRunning) {
            for (Integer goalId : goalIds) {
                goalRepository.increaseProgress(goalId, 1);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

<<<<<<< HEAD
    public static Intent createIntent(Context context) {
=======
    LiveData<Goal> observedGoal;

    public static Intent createStartContributingIntent(Context context, int goalId) {
>>>>>>> bde03db2d080399c7b2795142147a8d93db1d3ad
        Intent intent = new Intent(context, GoalContributionService.class);
        intent.setAction(ACTION_START_SERVICE);
        return intent;
    }


    public static Intent createStopContributingIntent(Context context, int goalId) {
        Intent intent = new Intent(context, GoalContributionService.class);
        intent.putExtra(EXTRA_GOAL_ID, goalId);
        intent.setAction(ACTION_STOP_CONTRIBUTING);
        return intent;
    }

    public static Intent createStopAllContributingIntent(Context context) {
        Intent intent = new Intent(context, GoalContributionService.class);
        intent.setAction(ACTION_STOP_ALL_CONTRIBUTING);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        goalRepository = ((GoalTrackerApplication) getApplication()).getGoalRepository();
        contributingGoalsIds = goalRepository.observeContributingGoalsIds();
        contributingGoalsIds.observe(this, this::updateNotification);
        contributingGoalsIds.observe(this, this::updateContributingGoalsIdsSet);
        thread.start();
    }


<<<<<<< HEAD
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
=======
    // FIXME: 23/02/20 probably should refactor this
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        int goalId = intent.getIntExtra(EXTRA_GOAL_ID, -1);
>>>>>>> bde03db2d080399c7b2795142147a8d93db1d3ad
        final String action = intent.getAction();

        switch (action) {
            case ACTION_STOP_CONTRIBUTING:
                int goalId = intent.getIntExtra(EXTRA_GOAL_ID, -1);
                goalRepository.stopContributingToGoal(goalId);
                break;
            case ACTION_STOP_ALL_CONTRIBUTING:
                goalRepository.removeAllContributingGoalIds();
                break;
        }

<<<<<<< HEAD
        return START_NOT_STICKY;
    }

    void updateContributingGoalsIdsSet(Set<Integer> contributingGoalsIds) {
        this.goalIds = contributingGoalsIds;
    }

    void updateSingleGoalNotification(Goal goal) {
        if (goal != null) {
            TaskStackBuilder goalDetailsStackBuilder = TaskStackBuilder
                    .create(this)
                    .addNextIntent(GoalsActivity.createIntent(this))
                    .addNextIntent(GoalDetailsActivity.createIntent(this, goal.getGoalId()));

            PendingIntent goalDetailsPendingIntent =
                    goalDetailsStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent stopContributionServiceIntent = GoalContributionService.createStopContributingIntent(this, goal.getGoalId());
            PendingIntent stopContributingPendingIntent =
                    PendingIntent.getService(this, 0, stopContributionServiceIntent, 0);

            Notification notification = new NotificationCompat.Builder(this, CONTRIBUTION_NOTIFICATION_CHANNEL_ID)
                    // TODO: 23/02/20 change icon
                    .setSmallIcon(R.drawable.ic_add_black_24dp)
                    .setContentTitle(goal.getTitle())
                    .setContentText(goal.progressToString())
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setContentIntent(goalDetailsPendingIntent)
                    .addAction(R.drawable.ic_add_black_24dp, "Stop", stopContributingPendingIntent)
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
=======
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

            Intent stopAllContributionServiceIntent = GoalContributionService.createStopAllContributingIntent(this);
            PendingIntent stopAllContributingPendingIntent =
                    PendingIntent.getService(this, 0, stopAllContributionServiceIntent, 0);

            Notification n = new NotificationCompat.Builder(this, CONTRIBUTION_NOTIFICATION_CHANNEL_ID)
                    // TODO: 23/02/20 change icon
                    .setSmallIcon(R.drawable.ic_add_black_24dp)
                    .setContentTitle(String.format(Locale.getDefault(), "contributing to %d goals", goalRepository.getContributingGoalsIds().size()))
                    .setContentText("Tap to open")
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setContentIntent(goalsActivityPendingIntent)
                    .addAction(R.drawable.ic_add_black_24dp, "Stop all", stopAllContributingPendingIntent)
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, n);
        }


        return START_NOT_STICKY;
    }

    void updateSingleGoalNotification(Goal goal) {
        TaskStackBuilder goalDetailsStackBuilder = TaskStackBuilder
                .create(this)
                .addNextIntent(GoalsActivity.createIntent(this))
                .addNextIntent(GoalDetailsActivity.createIntent(this, goal.getGoalId()));

        PendingIntent goalDetailsPendingIntent =
                goalDetailsStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent stopContributionServiceIntent = GoalContributionService.createStopContributingIntent(this, goal.getGoalId());
        PendingIntent stopContributingPendingIntent =
                PendingIntent.getService(this, 0, stopContributionServiceIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CONTRIBUTION_NOTIFICATION_CHANNEL_ID)
                // TODO: 23/02/20 change icon
                .setSmallIcon(R.drawable.ic_add_black_24dp)
                .setContentTitle(goal.getTitle())
                .setContentText(goal.progressToString())
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(goalDetailsPendingIntent)
                .addAction(R.drawable.ic_add_black_24dp, "Stop", stopContributingPendingIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
>>>>>>> bde03db2d080399c7b2795142147a8d93db1d3ad
    }

    void updateNotification(Set<Integer> goalsIds) {
        if (goalsIds.isEmpty()) {
            observedGoal.removeObservers(this);
            stopForeground(true);
        } else if (goalsIds.size() == 1) {
            Notification notification = new NotificationCompat.Builder(this, CONTRIBUTION_NOTIFICATION_CHANNEL_ID)
                    // TODO: 23/02/20 change icon
                    .setSmallIcon(R.drawable.ic_add_black_24dp)
                    .setContentTitle("temp")
                    .setContentText("temp")
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();
            startForeground(NOTIFICATION_ID, notification);
            observedGoal = goalRepository.observeGoal(goalsIds.iterator().next());
            observedGoal.observe(this, this::updateSingleGoalNotification);
        } else if (goalsIds.size() > 1) {
            observedGoal.removeObservers(this);
            Intent goalsActivityIntent = GoalsActivity.createIntent(this);
            PendingIntent goalsActivityPendingIntent =
                    PendingIntent.getActivity(this, 0, goalsActivityIntent, 0);

            Intent stopAllContributionServiceIntent = GoalContributionService.createStopAllContributingIntent(this);
            PendingIntent stopAllContributingPendingIntent =
                    PendingIntent.getService(this, 0, stopAllContributionServiceIntent, 0);

            Notification n = new NotificationCompat.Builder(this, CONTRIBUTION_NOTIFICATION_CHANNEL_ID)
                    // TODO: 23/02/20 change icon
                    .setSmallIcon(R.drawable.ic_add_black_24dp)
                    .setContentTitle(String.format(Locale.getDefault(), "contributing to %d goals", goalsIds.size()))
                    .setContentText("Tap to open")
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setContentIntent(goalsActivityPendingIntent)
                    .addAction(R.drawable.ic_add_black_24dp, "Stop all", stopAllContributingPendingIntent)
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, n);
        }
    }

    @Override
<<<<<<< HEAD
    public void onDestroy() {
        serviceRunning = false;
        super.onDestroy();
=======
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
>>>>>>> bde03db2d080399c7b2795142147a8d93db1d3ad
    }
}
