package com.agh.goaltracker.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;

import com.agh.goaltracker.GoalDetailsActivity;
import com.agh.goaltracker.GoalTrackerApplication;
import com.agh.goaltracker.GoalsActivity;
import com.agh.goaltracker.R;
import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.model.source.GoalRepository;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import static com.agh.goaltracker.GoalTrackerApplication.CONTRIBUTION_NOTIFICATION_CHANNEL_ID;
import static java.util.concurrent.TimeUnit.SECONDS;


public class GoalContributionService extends LifecycleService {
    private static final String TAG = "GoalContributionService";
    private static final String WAKE_LOCK_TAG = "GoalTracker::GoalContributionService";
    private static final String EXTRA_GOAL_ID = "GOAL_ID";
    private static final String ACTION_START_SERVICE = "START_SERVICE";
    private static final String ACTION_STOP_CONTRIBUTING = "STOP_CONTRIBUTING";
    private static final String ACTION_STOP_ALL_CONTRIBUTING = "STOP_ALL_CONTRIBUTING";
    private static final int NOTIFICATION_ID = 1;
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();
    GoalRepository goalRepository;
    LiveData<Set<Integer>> contributingGoalsIds;
    LiveData<Goal> observedGoal = new MutableLiveData<>();
    Set<Integer> goalIds = new HashSet<>();
    final Runnable goalContributionRunnable = new Runnable() {
        public void run() {
            goalRepository.contributeToGoals(goalIds);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    PowerManager.WakeLock wakeLock;
    ScheduledFuture<?> goalContributionRunnableHandle;

    public static Intent createIntent(Context context) {
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
        goalRepository.observeCompletedGoal().observe(this, this::onGoalComplete);
    }

    void startContributionExecutor() {
        if (goalContributionRunnableHandle == null) {
            goalContributionRunnableHandle = scheduler.scheduleAtFixedRate(goalContributionRunnable, 0, 1, SECONDS);
        }
    }

    void stopContributorExecutor() {
        if (goalContributionRunnableHandle != null) {
            goalContributionRunnableHandle.cancel(false);
            goalContributionRunnableHandle = null;
        }
    }

    void onGoalComplete(Goal goal) {
        Toast.makeText(this, goal.getTitle() + " has been completed", Toast.LENGTH_LONG).show();
    }

    void acquireWakeLock() {
        if (wakeLock == null) {
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    WAKE_LOCK_TAG);
        }
        wakeLock.acquire();
    }

    void releaseWakeLock() {
        if (wakeLock != null) {
            wakeLock.release();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
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
        return START_STICKY;
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
                    PendingIntent.getService(this, 0, stopContributionServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(this, CONTRIBUTION_NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(goal.getTitle())
                    .setContentText(goal.progressToString())
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setContentIntent(goalDetailsPendingIntent)
                    .addAction(R.drawable.ic_notification, "Stop", stopContributingPendingIntent)
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }

    void updateNotification(Set<Integer> goalsIds) {
        if (goalsIds.isEmpty()) {
            observedGoal.removeObservers(this);
            stopForeground(true);
            releaseWakeLock();
            stopContributorExecutor();
        } else if (goalsIds.size() == 1) {
            startContributionExecutor();
            Notification notification = new NotificationCompat.Builder(this, CONTRIBUTION_NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("temp")
                    .setContentText("temp")
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();
            startForeground(NOTIFICATION_ID, notification);
            observedGoal = goalRepository.observeGoal(goalsIds.iterator().next());
            observedGoal.observe(this, this::updateSingleGoalNotification);
            acquireWakeLock();
        } else if (goalsIds.size() > 1) {
            startContributionExecutor();
            observedGoal.removeObservers(this);
            Intent goalsActivityIntent = GoalsActivity.createIntent(this);
            PendingIntent goalsActivityPendingIntent =
                    PendingIntent.getActivity(this, 0, goalsActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent stopAllContributionServiceIntent = GoalContributionService.createStopAllContributingIntent(this);
            PendingIntent stopAllContributingPendingIntent =
                    PendingIntent.getService(this, 0, stopAllContributionServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification n = new NotificationCompat.Builder(this, CONTRIBUTION_NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(String.format(Locale.getDefault(), "contributing to %d goals", goalsIds.size()))
                    .setContentText("Tap to open")
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setContentIntent(goalsActivityPendingIntent)
                    .addAction(R.drawable.ic_notification, "Stop all", stopAllContributingPendingIntent)
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, n);
            acquireWakeLock();
        }
    }

    @Override
    public void onDestroy() {
        goalContributionRunnableHandle.cancel(true);
        releaseWakeLock();
        stopContributorExecutor();
        super.onDestroy();
    }
}
