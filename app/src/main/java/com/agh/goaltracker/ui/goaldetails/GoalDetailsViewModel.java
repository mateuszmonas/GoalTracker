package com.agh.goaltracker.ui.goaldetails;

import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.model.source.GoalRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class GoalDetailsViewModel extends ViewModel {
    private GoalRepository goalRepository;
    private MutableLiveData<Integer> goalId = new MutableLiveData<>();
    LiveData<Goal> goal = Transformations.switchMap(goalId, goalId -> goalRepository.observeGoal(goalId));

    private MutableLiveData<Boolean> _isDeleted = new MutableLiveData<>();
    LiveData<Boolean> isDeleted = _isDeleted;

    public GoalDetailsViewModel(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;

    }

    void start(int goalId) {
        this.goalId.setValue(goalId);
    }

    void deleteGoal() {
        goalRepository.deleteGoal(goal.getValue());
        _isDeleted.setValue(true);
    }
}
