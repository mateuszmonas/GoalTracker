package com.agh.goaltracker.addgoal.ui.addgoal;

import com.agh.goaltracker.model.source.GoalRepository;

import androidx.lifecycle.ViewModel;

public class AddGoalViewModel extends ViewModel {
    private GoalRepository goalRepository;

    public AddGoalViewModel(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }
}
