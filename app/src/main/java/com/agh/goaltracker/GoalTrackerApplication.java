package com.agh.goaltracker;

import android.app.Application;

import com.agh.goaltracker.model.source.DefaultGoalRepository;
import com.agh.goaltracker.model.source.GoalRepository;
import com.agh.goaltracker.model.source.local.GoalDatabase;
import com.agh.goaltracker.model.source.local.GoalLocalDataSource;

import androidx.room.Room;

public class GoalTrackerApplication extends Application {

    private GoalRepository goalRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        deleteDatabase("goals");
        GoalDatabase goalDatabase = Room.databaseBuilder(getApplicationContext(), GoalDatabase.class, "goals").build();
        goalRepository = new DefaultGoalRepository(new GoalLocalDataSource(goalDatabase.goalDao()));
    }

    public GoalRepository getGoalRepository() {
        return goalRepository;
    }
}
