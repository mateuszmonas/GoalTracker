package com.agh.goaltracker.ui.addgoal;

import android.text.TextUtils;

import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.model.source.GoalRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddGoalViewModel extends ViewModel {
    private MutableLiveData<SaveGoalError> _saveTaskErrorMessage = new MutableLiveData<>();
    LiveData<SaveGoalError> saveTaskErrorMessage = _saveTaskErrorMessage;
    private MutableLiveData<Boolean> _finishActivity = new MutableLiveData<>();
    LiveData<Boolean> finishActivity = _finishActivity;
    private GoalRepository goalRepository;

    public AddGoalViewModel(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    public void saveGoal(Goal goal) {
        if (TextUtils.isEmpty(goal.title)) {
            _saveTaskErrorMessage.postValue(SaveGoalError.EMPTY_TITLE);
        } else {
            goalRepository.saveGoal(goal);
            _finishActivity.postValue(true);
        }
    }

    enum SaveGoalError {
        EMPTY_TITLE
    }
}
