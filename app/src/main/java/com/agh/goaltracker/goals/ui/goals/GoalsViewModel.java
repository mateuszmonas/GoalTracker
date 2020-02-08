package com.agh.goaltracker.goals.ui.goals;

import com.agh.goaltracker.model.Goal;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GoalsViewModel extends ViewModel {
    LiveData<List<Goal>> goals;

    public LiveData<List<Goal>> getGoals() {
        if(goals==null){
            goals = new MutableLiveData<>();
            loadGoals();
        }
        return goals;
    }

    private void loadGoals() {

    }
}
