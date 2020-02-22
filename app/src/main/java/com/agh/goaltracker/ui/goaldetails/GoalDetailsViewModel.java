package com.agh.goaltracker.ui.goaldetails;

import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.model.source.GoalRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class GoalDetailsViewModel extends ViewModel {
    MutableLiveData<Boolean> _startGoalContributing = new MutableLiveData<>();
    LiveData<Boolean> startGoalContributing = _startGoalContributing;
    private GoalRepository goalRepository;
    private MutableLiveData<Integer> goalId = new MutableLiveData<>();
    LiveData<Goal> goal = Transformations.switchMap(goalId, goalId -> goalRepository.observeGoal(goalId));
    LiveData<Boolean> isGoalContributing = Transformations.switchMap(goalId, goalId -> goalRepository.observeContributingGoal(goalId));
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

    void recordPastProgress(int amount) {
        Goal goal = this.goal.getValue();
        goalRepository.increaseProgress(goal.getGoalId(), amount);
    }

    void startContributing() {
        Goal goal = this.goal.getValue();
        if (goal.isProgressAsMinutes()) {
            _startGoalContributing.setValue(true);
        } else {
            goalRepository.increaseProgress(goal.getGoalId(), 1);
        }
    }

    void stopContributing() {
        _startGoalContributing.setValue(false);
    }

}
