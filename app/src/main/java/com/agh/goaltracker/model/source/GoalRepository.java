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

    /**
     * increase goal progression by given amount
     * @param goalId id of goal to contribute to
     * @param amount amount to contribute
     */
    void contributeToGoal(int goalId, int amount);

    /**
     * increase goal progression by 1
     * @param goalId id of goal to contribute to
     */
    void contributeToGoal(int goalId);

    /**
     * increase all given goals progression by given amount
     * @param goalsIds ids of goals to contribute to
     * @param amount amount to contribute
     */
    void contributeToGoals(Set<Integer> goalsIds, int amount);

    /**
     * increase all given goals progression by given 1
     * @param goalsIds ids of goals to contribute to
     */
    void contributeToGoals(Set<Integer> goalsIds);

    void deleteGoals(List<Goal> goals);

    void deleteGoal(Goal goal);

    LiveData<Boolean> observeContributingGoal(int goalId);

    LiveData<Set<Integer>> observeContributingGoalsIds();

    Set<Integer> getContributingGoalsIds();

    void startContributingToGoal(int goalId);

    void stopContributingToGoal(int goalId);

    void removeAllContributingGoalIds();

    LiveData<Goal> observeCompletedGoal();
}
