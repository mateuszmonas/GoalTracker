package com.agh.goaltracker.goals.ui.goals;

import com.agh.goaltracker.model.Goal;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GoalsViewModel extends ViewModel {
    MutableLiveData<List<Goal>> goals;

    public LiveData<List<Goal>> getGoals() {
        if(goals==null){
            goals = new MutableLiveData<>();
            loadGoals();
        }
        return goals;
    }

    // TODO: 08/02/20 fetch data from repository
    private void loadGoals() {
        ArrayList<Goal> goalArrayList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            StringBuilder title = new StringBuilder();
            for (int j = 0; j < i; j++) {
                title.append(i);
            }
            goalArrayList.add(new Goal(title.toString()));
        }
        goals.postValue(goalArrayList);
    }
}
