package com.agh.goaltracker.model.source.local;

import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.model.source.GoalDataSource;

import java.util.List;
import java.util.Set;

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
    public void contributeToGoal(int goalId, int amount) {
        GoalDatabase.databaseWriteExecutor.execute(() -> goalDao.increaseGoalProgress(goalId, amount));
    }

    @Override
    public void contributeToGoals(Set<Integer> goalsIds, int amount) {
        goalDao.increaseGoalsProgress(goalsIds, amount);
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

    @Override
    public List<Goal> getGoals(Set<Integer> goalsIds) {
        return goalDao.getGoals(goalsIds);
    }
}
