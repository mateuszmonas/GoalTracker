package com.agh.goaltracker.model.source.local;

import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.model.source.GoalDataSource;

import java.util.List;

import androidx.lifecycle.LiveData;

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
    public Goal getGoal(int goalId) {
        return goalDao.getGoal(goalId);
    }

    @Override
    public void increaseProgress(int goalId) {
        GoalDatabase.databaseWriteExecutor.execute(() -> goalDao.increaseProgress(goalId));
    }

    @Override
    public void saveGoal(Goal goal) {
        GoalDatabase.databaseWriteExecutor.execute(() -> goalDao.insertGoal(goal));
    }

    @Override
    public LiveData<Goal> observeGoal(int goalId) {
        return goalDao.observeGoal(goalId);
    }

    @Override
    public void updateGoal(Goal goal) {
        GoalDatabase.databaseWriteExecutor.execute(() -> goalDao.updateGoal(goal));
    }

    @Override
    public void deleteGoals(List<Goal> goals) {
        GoalDatabase.databaseWriteExecutor.execute(() -> goalDao.deleteGoals(goals));
    }

    @Override
    public void deleteGoal(Goal goal) {
        GoalDatabase.databaseWriteExecutor.execute(() -> goalDao.deleteGoal(goal));
    }
}
