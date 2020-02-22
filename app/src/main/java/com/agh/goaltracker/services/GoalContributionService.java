package com.agh.goaltracker.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.agh.goaltracker.GoalTrackerApplication;
import com.agh.goaltracker.model.source.GoalRepository;

public class GoalContributionService extends Service {
    GoalRepository goalRepository;
    private static final String EXTRA_GOAL_ID = "GOAL_ID";
    private static final String TAG = "GoalContributionService";

    private static final String ACTION_START_CONTRIBUTING = "START_CONTRIBUTING";
    private static final String ACTION_STOP_CONTRIBUTING = "STOP_CONTRIBUTING";

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
        String a = "";
        for (Integer contributingGoalsId : goalRepository.getContributingGoalsIds()) {
            a += contributingGoalsId + " ";
        }
        Log.d(TAG, "onStartCommand: "+ a);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
