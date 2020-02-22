package com.agh.goaltracker.model.source;

import com.agh.goaltracker.model.Goal;

import java.util.List;
import java.util.Set;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

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
    public void increaseProgress(int goalId, int amount) {
        localGoalDataSource.increaseProgress(goalId, amount);
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

    @Override
    public LiveData<Boolean> observeGoalContributing(int goalId) {
        GoalContributionModel goalContributionModel = GoalContributionModel.getInstance();
        return Transformations.switchMap(goalContributionModel.observeContributingGoalsIds(), contributingGoalIds -> new MutableLiveData<>(contributingGoalIds.contains(goalId)));
    }

    @Override
    public Set<Integer> getContributingGoalsIds() {
        GoalContributionModel goalContributionModel = GoalContributionModel.getInstance();
        return goalContributionModel.getContributingGoalsIds();
    }

    @Override
    public void startContributingToGoal(int goalId) {
        GoalContributionModel goalContributionModel = GoalContributionModel.getInstance();
        goalContributionModel.addContributingGoalId(goalId);
    }

    @Override
    public void stopContributingToGoal(int goalId) {
        GoalContributionModel goalContributionModel = GoalContributionModel.getInstance();
        goalContributionModel.removeContributingGoalId(goalId);
    }
}
