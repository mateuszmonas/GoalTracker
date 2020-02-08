package com.agh.goaltracker.goals.ui.goals;

import com.agh.goaltracker.goals.ui.goals.GoalsViewModel;
import com.agh.goaltracker.model.source.GoalRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class GoalsViewModelFactory implements ViewModelProvider.Factory {

    GoalRepository goalRepository;

    public GoalsViewModelFactory(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GoalsViewModel.class)) {
            return (T) new GoalsViewModel(goalRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
