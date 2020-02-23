package com.agh.goaltracker.model.source;

import java.util.HashSet;
import java.util.Set;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class GoalContributionModel {

    private static GoalContributionModel instance = new GoalContributionModel();
    private MutableLiveData<Set<Integer>> contributingGoalsIds = new MutableLiveData<>(new HashSet<>());

    public static GoalContributionModel getInstance() {
        return instance;
    }

    public LiveData<Set<Integer>> observeContributingGoalsIds() {
        return contributingGoalsIds;
    }

    public Set<Integer> getContributingGoalsIds() {
        return contributingGoalsIds.getValue();
    }

    public void addContributingGoalId(int goalId) {
        Set<Integer> values = contributingGoalsIds.getValue();
        values.add(goalId);
        contributingGoalsIds.postValue(values);
    }

    public void removeContributingGoalId(int goalId) {
        Set<Integer> values = contributingGoalsIds.getValue();
        values.remove(goalId);
        contributingGoalsIds.postValue(values);
    }

    public void removeAllContributingGoalIds() {
        Set<Integer> values = contributingGoalsIds.getValue();
        values.clear();
        contributingGoalsIds.postValue(values);
    }
}
