package com.agh.goaltracker.model.source.local;

import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.model.source.GoalDataSource;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class GoalLocalDataSource implements GoalDataSource {

    @Override
    public LiveData<List<Goal>> observeGoals() {
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
