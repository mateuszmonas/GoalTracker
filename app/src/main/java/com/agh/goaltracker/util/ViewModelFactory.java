package com.agh.goaltracker.util;

import com.agh.goaltracker.model.source.GoalRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private GoalRepository goalRepository;

    public ViewModelFactory(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass.getConstructor(GoalRepository.class).newInstance(goalRepository);
        } catch (Exception e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
    }
}
