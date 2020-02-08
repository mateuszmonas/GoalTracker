package com.agh.goaltracker;

import android.app.Application;

import com.agh.goaltracker.model.source.DefaultGoalRepository;
import com.agh.goaltracker.model.source.GoalRepository;
import com.agh.goaltracker.model.source.local.GoalLocalDataSource;

public class GoalTrackerApplication extends Application {

    private GoalRepository goalRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        goalRepository = new DefaultGoalRepository(new GoalLocalDataSource());
    }

    public GoalRepository getGoalRepository() {
        return goalRepository;
    }
}
