package com.agh.goaltracker.model.source;

import com.agh.goaltracker.model.Goal;

import java.util.List;

import androidx.lifecycle.LiveData;

public interface GoalDataSource {

    LiveData<List<Goal>> observeGoals();

}
