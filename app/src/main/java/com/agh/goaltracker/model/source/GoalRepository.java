package com.agh.goaltracker.model.source;

import com.agh.goaltracker.model.Goal;

import java.util.List;
import java.util.Set;

import androidx.lifecycle.LiveData;

public interface GoalRepository {

    LiveData<List<Goal>> observeGoals();

    void saveGoal(Goal goal);

    LiveData<Goal> observeGoal(int goalId);

    Goal getGoal(int goalId);

    void updateGoal(Goal goal);

    void increaseProgress(int goalId, int amount);

    void deleteGoals(List<Goal> goals);

    void deleteGoal(Goal goal);

    LiveData<Boolean> observeContributingGoal(int goalId);

    LiveData<Set<Integer>> observeContributingGoalsIds();

    Set<Integer> getContributingGoalsIds();

    void startContributingToGoal(int goalId);

    void stopContributingToGoal(int goalId);
}
