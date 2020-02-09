package com.agh.goaltracker.model.source;

import com.agh.goaltracker.model.Goal;

import java.util.List;

import androidx.lifecycle.LiveData;

public interface GoalRepository {

    LiveData<List<Goal>> observeGoals();

    void saveGoal(Goal goal);

}
