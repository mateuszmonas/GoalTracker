package com.agh.goaltracker.model.source.local;

import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.model.source.GoalDataSource;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class GoalLocalDataSource implements GoalDataSource {
    private GoalDao goalDao;

    public GoalLocalDataSource(GoalDao goalDao) {
        this.goalDao = goalDao;
    }

    @Override
    public LiveData<List<Goal>> observeGoals() {
        return goalDao.observeGoals();
    }

    @Override
    public void saveGoal(Goal goal) {
        goalDao.insert(goal);
    }
}
