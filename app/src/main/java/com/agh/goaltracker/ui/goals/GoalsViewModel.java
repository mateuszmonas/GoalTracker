package com.agh.goaltracker.ui.goals;

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
    MutableLiveData<Set<GoalsFilterType>> _filters = new MutableLiveData<>(new HashSet<>(Collections.singletonList(GoalsFilterType.CURRENT_GOALS)));
    LiveData<Set<GoalsFilterType>> filters = _filters;
    private GoalRepository goalRepository;
    private LiveData<List<Goal>> _goals;
    LiveData<List<Goal>> goals = Transformations.switchMap(_filters, filterList -> Transformations.switchMap(_goals, goalList -> new MutableLiveData<>(applyFilters(goalList, filterList))));

    public GoalsViewModel(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
        _filters.getValue().add(GoalsFilterType.CURRENT_GOALS);
        _goals = this.goalRepository.observeGoals();
    }

    public void delete(Goal goal) { // TODO handle delete error?
        goalRepository.deleteGoal(goal);
    }

    public void setFiltering(GoalsFilterType filterType) {
        Set<GoalsFilterType> currentFilters = _filters.getValue();
        if (currentFilters.contains(filterType)) {
            currentFilters.remove(filterType);
        } else {
            currentFilters.add(filterType);
        }
        _filters.setValue(currentFilters);
    }

    // TODO: 09/02/20 implement filtering
    List<Goal> applyFilters(List<Goal> goals, Set<GoalsFilterType> filterTypes) {
        return goals;
    }

    enum GoalsFilterType {
        CURRENT_GOALS, COMPLETED_GOALS, FAILED_GOALS
    }
}
