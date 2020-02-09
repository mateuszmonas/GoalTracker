package com.agh.goaltracker.goals.ui.goals;

import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.model.source.GoalRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class GoalsViewModel extends ViewModel {
    private GoalRepository goalRepository;
    MutableLiveData<Set<GoalsFilterType>> filters= new MutableLiveData<>(new HashSet<>(Collections.singletonList(GoalsFilterType.CURRENT_GOALS)));
    private LiveData<List<Goal>> goals;

    public GoalsViewModel(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
        filters.getValue().add(GoalsFilterType.CURRENT_GOALS);
        goals = this.goalRepository.observeGoals();
    }

    public LiveData<List<Goal>> getGoals() {
        return Transformations.switchMap(filters, new Function<Set<GoalsFilterType>, LiveData<List<Goal>>>() {
            @Override
            public LiveData<List<Goal>> apply(Set<GoalsFilterType> filterList) {
                return Transformations.switchMap(goals, new Function<List<Goal>, LiveData<List<Goal>>>() {
                    @Override
                    public LiveData<List<Goal>> apply(List<Goal> input) {
                        return new MutableLiveData<>(applyFilters(input, filterList));
                    }
                });
            }
        });
    }

    public void setFiltering(GoalsFilterType filterType) {
        Set<GoalsFilterType> currentFilters = filters.getValue();
        if (currentFilters.contains(filterType)) {
            currentFilters.remove(filterType);
        }else{
            currentFilters.add(filterType);
        }
        filters.setValue(currentFilters);
    }

    List<Goal> applyFilters(List<Goal> goals, Set<GoalsFilterType> filterTypes) {
        return goals;
    }

    enum GoalsFilterType{
        CURRENT_GOALS, COMPLETED_GOALS, FAILED_GOALS
    }
}
