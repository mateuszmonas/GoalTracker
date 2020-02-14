package com.agh.goaltracker.model.source;

import com.agh.goaltracker.model.Goal;

import java.util.List;

import androidx.lifecycle.LiveData;

public class DefaultGoalRepository implements GoalRepository {
    private GoalDataSource localGoalDataSource;

    public DefaultGoalRepository(GoalDataSource localGoalDataSource) {
        this.localGoalDataSource = localGoalDataSource;
    }

    @Override
    public LiveData<List<Goal>> observeGoals() {
        return localGoalDataSource.observeGoals();
    }

    @Override
    public void saveGoal(Goal goal) {
        localGoalDataSource.saveGoal(goal);
    }

    @Override
    public void increaseProgress(int goalId) {
        localGoalDataSource.increaseProgress(goalId);
    }

    @Override
    public LiveData<Goal> observeGoal(int goalId) {
        return localGoalDataSource.observeGoal(goalId);
    }

    @Override
    public Goal getGoal(int goalId) {
        return localGoalDataSource.getGoal(goalId);
    }

    @Override
    public void updateGoal(Goal goal) {
        localGoalDataSource.updateGoal(goal);
    }

    @Override
    public void deleteGoals(List<Goal> goals) {
        localGoalDataSource.deleteGoals(goals);
    }

    @Override
    public void deleteGoal(Goal goal) {
        localGoalDataSource.deleteGoal(goal);
    }
}
