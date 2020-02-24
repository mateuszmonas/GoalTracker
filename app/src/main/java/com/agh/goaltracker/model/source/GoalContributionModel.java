package com.agh.goaltracker.model.source;

import java.util.HashSet;
import java.util.Set;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

class GoalContributionModel {

    private static GoalContributionModel instance = new GoalContributionModel();
    private MutableLiveData<Set<Integer>> contributingGoalsIds = new MutableLiveData<>(new HashSet<>());

    static GoalContributionModel getInstance() {
        return instance;
    }

    LiveData<Set<Integer>> observeContributingGoalsIds() {
        return contributingGoalsIds;
    }

    Set<Integer> getContributingGoalsIds() {
        return contributingGoalsIds.getValue();
    }

    void addContributingGoalId(int goalId) {
        Set<Integer> values = contributingGoalsIds.getValue();
        values.add(goalId);
        contributingGoalsIds.postValue(values);
    }

    void removeContributingGoalId(int goalId) {
        Set<Integer> values = contributingGoalsIds.getValue();
        values.remove(goalId);
        contributingGoalsIds.postValue(values);
    }

    void removeAllContributingGoalIds() {
        Set<Integer> values = contributingGoalsIds.getValue();
        values.clear();
        contributingGoalsIds.postValue(values);
    }
}
