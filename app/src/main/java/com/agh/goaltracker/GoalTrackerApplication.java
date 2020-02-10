package com.agh.goaltracker;

import android.app.Application;

import com.agh.goaltracker.model.Goal;
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
//        deleteDatabase("goals"); TODO uncomment
        GoalDatabase goalDatabase = Room.databaseBuilder(getApplicationContext(), GoalDatabase.class, "goals").build();
        goalRepository = new DefaultGoalRepository(new GoalLocalDataSource(goalDatabase.goalDao()));
        goalRepository.saveGoal(new Goal("first"));
        goalRepository.saveGoal(new Goal("second"));
        goalRepository.saveGoal(new Goal("last"));
    }

    public GoalRepository getGoalRepository() {
        return goalRepository;
    }
}
