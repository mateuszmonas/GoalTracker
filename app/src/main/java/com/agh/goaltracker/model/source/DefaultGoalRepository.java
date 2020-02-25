package com.agh.goaltracker.model.source;

import com.agh.goaltracker.model.Goal;

import java.util.List;
import java.util.Set;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class DefaultGoalRepository implements GoalRepository {
    private GoalDataSource localGoalDataSource;
    private MutableLiveData<Goal> completedGoal = new MutableLiveData<>();

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
                GoalContributionModel.getInstance().removeContributingGoalId(goalId);
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
        GoalContributionModel.getInstance().removeContributingGoalId(goal.getGoalId());
    }

    @Override
    public LiveData<Boolean> observeContributingGoal(int goalId) {
        GoalContributionModel goalContributionModel = GoalContributionModel.getInstance();
        return Transformations.switchMap(goalContributionModel.observeContributingGoalsIds(), contributingGoalIds -> new MutableLiveData<>(contributingGoalIds.contains(goalId)));
    }

    @Override
    public Set<Integer> getContributingGoalsIds() {
        GoalContributionModel goalContributionModel = GoalContributionModel.getInstance();
        return goalContributionModel.getContributingGoalsIds();
    }

    @Override
    public LiveData<Set<Integer>> observeContributingGoalsIds() {
        GoalContributionModel goalContributionModel = GoalContributionModel.getInstance();
        return goalContributionModel.observeContributingGoalsIds();
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

    @Override
    public void removeAllContributingGoalIds() {
        GoalContributionModel goalContributionModel = GoalContributionModel.getInstance();
        goalContributionModel.removeAllContributingGoalIds();
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
                    GoalContributionModel.getInstance().removeContributingGoalId(goal.getGoalId());
                }
            }
        }).start();
    }

    @Override
    public void contributeToGoals(Set<Integer> goalsIds) {
        contributeToGoals(goalsIds, 1);
    }
}
