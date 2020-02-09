package com.agh.goaltracker.goals.ui.goals;

import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.model.source.GoalRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class GoalsViewModel extends ViewModel {
    MutableLiveData<Set<GoalsFilterType>> filters = new MutableLiveData<>(new HashSet<>(Collections.singletonList(GoalsFilterType.CURRENT_GOALS)));
    private GoalRepository goalRepository;
    private LiveData<List<Goal>> goals;

    public GoalsViewModel(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
        filters.getValue().add(GoalsFilterType.CURRENT_GOALS);
        goals = this.goalRepository.observeGoals();
    }

    public LiveData<List<Goal>> getGoals() {
        return Transformations.switchMap(filters, filterList -> Transformations.switchMap(goals, goalList -> new MutableLiveData<>(applyFilters(goalList, filterList))));
    }

    public void setFiltering(GoalsFilterType filterType) {
        Set<GoalsFilterType> currentFilters = filters.getValue();
        if (currentFilters.contains(filterType)) {
            currentFilters.remove(filterType);
        } else {
            currentFilters.add(filterType);
        }
        filters.setValue(currentFilters);
    }

    // TODO: 09/02/20 implement filtering
    List<Goal> applyFilters(List<Goal> goals, Set<GoalsFilterType> filterTypes) {
        return goals;
    }

    enum GoalsFilterType {
        CURRENT_GOALS, COMPLETED_GOALS, FAILED_GOALS
    }
}
