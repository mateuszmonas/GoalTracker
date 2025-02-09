package com.agh.goaltracker.model.source;

import com.agh.goaltracker.model.Goal;

import java.util.List;
import java.util.Set;

import androidx.lifecycle.LiveData;

public interface GoalDataSource {

    LiveData<List<Goal>> observeGoals();

    void saveGoal(Goal goal);

    LiveData<Goal> observeGoal(int goalId);

    Goal getGoal(int goalId);

    List<Goal> getGoals(Set<Integer> goalsIds);

    void updateGoal(Goal goal);

    void contributeToGoal(int goalId, int amount);

    void contributeToGoals(Set<Integer> goalsIds, int amount);

    void deleteGoals(List<Goal> goals);

    void deleteGoal(Goal goal);
}
