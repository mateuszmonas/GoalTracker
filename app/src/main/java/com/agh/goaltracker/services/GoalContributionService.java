package com.agh.goaltracker.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.agh.goaltracker.GoalTrackerApplication;
import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.model.source.GoalContributionModel;
import com.agh.goaltracker.model.source.GoalRepository;

import java.util.HashSet;
import java.util.Set;

public class GoalContributionService extends Service {
    GoalRepository goalRepository;
    private static final String EXTRA_GOAL_ID = "GOAL_ID";
    private static final String TAG = "GoalContributionService";

    public static Intent createIntent(Context context, int goalId) {
        Intent intent = new Intent(context, GoalContributionService.class);
        intent.putExtra(EXTRA_GOAL_ID, goalId);
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
        GoalContributionModel.getInstance().addContributingGoalId(goalId);
        Log.d(TAG, "onStartCommand: "+ goalId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
