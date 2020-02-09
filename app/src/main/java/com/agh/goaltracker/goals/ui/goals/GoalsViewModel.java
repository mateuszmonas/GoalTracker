package com.agh.goaltracker.goals.ui.goals;

import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.model.source.GoalRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class GoalsViewModel extends ViewModel {
    private LiveData<List<Goal>> goals;
    private GoalRepository goalRepository;

    public GoalsViewModel(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    public LiveData<List<Goal>> getGoals() {
        if (goals == null) {
            goals = goalRepository.observeObjects();
        }
        return goals;
    }
}
