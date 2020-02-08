package com.agh.goaltracker.model.source;

import com.agh.goaltracker.model.Goal;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class DefaultGoalRepository implements GoalRepository {
    GoalDataSource localGoalDataSource;

    public DefaultGoalRepository(GoalDataSource localGoalDataSource) {
        this.localGoalDataSource = localGoalDataSource;
    }

    @Override
    public LiveData<List<Goal>> observeObjects() {
        MutableLiveData<List<Goal>> g = new MutableLiveData<>();
        ArrayList<Goal> goalArrayList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            StringBuilder title = new StringBuilder();
            for (int j = 0; j < i; j++) {
                title.append(i);
            }
            goalArrayList.add(new Goal(title.toString()));
        }
        g.setValue(goalArrayList);
        return g;
    }
}
