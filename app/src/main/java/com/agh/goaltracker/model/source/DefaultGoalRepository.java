package com.agh.goaltracker.model.source;

import com.agh.goaltracker.model.Goal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class DefaultGoalRepository implements GoalRepository {
    private GoalDataSource localGoalDataSource;
    private MutableLiveData<Goal> completedGoal = new MutableLiveData<>();
    private MutableLiveData<Set<Integer>> contributingGoalsIds = new MutableLiveData<>(new HashSet<>());

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
    public void contributeToGoal(int goalId, int amount) {
        localGoalDataSource.contributeToGoal(goalId, amount);
        new Thread(() -> {
            Goal goal = localGoalDataSource.getGoal(goalId);
            if (goal.isCompleted()) {
                completedGoal.postValue(goal);
                stopContributingToGoal(goalId);
            }
        }).start();
    }

    @Override
    public void contributeToGoal(int goalId) {
        this.contributeToGoal(goalId, 1);
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
        stopContributingToGoal(goal.getGoalId());
    }

    @Override
    public LiveData<Boolean> observeContributingGoal(int goalId) {
        return Transformations.switchMap(contributingGoalsIds, contributingGoalIds -> new MutableLiveData<>(contributingGoalIds.contains(goalId)));
    }

    @Override
    public LiveData<Set<Integer>> observeContributingGoalsIds() {
        return contributingGoalsIds;
    }

    @Override
    public void startContributingToGoal(int goalId) {
        Set<Integer> values = contributingGoalsIds.getValue();
        values.add(goalId);
        contributingGoalsIds.postValue(values);
    }

    @Override
    public void stopContributingToGoal(int goalId) {
        Set<Integer> values = contributingGoalsIds.getValue();
        values.remove(goalId);
        contributingGoalsIds.postValue(values);
    }

    @Override
    public void removeAllContributingGoalIds() {
        Set<Integer> values = contributingGoalsIds.getValue();
        values.clear();
        contributingGoalsIds.postValue(values);
    }

    @Override
    public LiveData<Goal> observeCompletedGoal() {
        return completedGoal;
    }

    @Override
    public void contributeToGoals(Set<Integer> goalsIds, int amount) {
        localGoalDataSource.contributeToGoals(goalsIds, amount);
        new Thread(() -> {
            List<Goal> goals = localGoalDataSource.getGoals(goalsIds);
            for (Goal goal : goals) {
                if (goal.isCompleted()) {
                    completedGoal.postValue(goal);
                    stopContributingToGoal(goal.getGoalId());
                }
            }
        }).start();
    }

    @Override
    public void contributeToGoals(Set<Integer> goalsIds) {
        contributeToGoals(goalsIds, 1);
    }
}
