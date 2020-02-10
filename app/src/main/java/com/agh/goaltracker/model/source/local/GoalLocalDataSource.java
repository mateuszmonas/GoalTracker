package com.agh.goaltracker.model.source.local;

import android.os.AsyncTask;

import com.agh.goaltracker.model.Goal;
import com.agh.goaltracker.model.source.GoalDataSource;

import java.util.List;

import androidx.lifecycle.LiveData;

public class GoalLocalDataSource implements GoalDataSource {
    private GoalDao goalDao;

    public GoalLocalDataSource(GoalDao goalDao) {
        this.goalDao = goalDao;
    }

    @Override
    public LiveData<List<Goal>> observeGoals() {
        return goalDao.observeGoals();
    }

    @Override
    public void saveGoal(Goal goal) {
        new InsertAsyncTask(goalDao).execute(goal);
//        goalDao.insertGoal(goal);
    }


    @Override
    public LiveData<Goal> observeGoal(int goalId) {
        return goalDao.observeGoal(goalId);
    }

    @Override
    public void updateGoal(Goal goal) {
        new UpdateAsyncTask(goalDao).execute(goal);
//        goalDao.updateGoal(goal);
    }

    @Override
    public void deleteGoals(List<Goal> goals) {
        goalDao.deleteGoals(goals);
    }

    @Override
    public void deleteGoal(Goal goal) {
        new DeleteGoalAsyncTask(goalDao).execute(goal);
    }

    private static class DeleteGoalAsyncTask extends AsyncTask<Goal, Void, Void> {
        private GoalDao mAsyncTaskDao;

        DeleteGoalAsyncTask(GoalDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Goal... params) {
            mAsyncTaskDao.deleteGoal(params[0]);
            return null;
        }
    }

    private static class InsertAsyncTask extends AsyncTask<Goal, Void, Void> {

        private final GoalDao asyncGoalDao;

        InsertAsyncTask(GoalDao GoalDao) {
            asyncGoalDao = GoalDao;
        }

        @Override
        protected Void doInBackground(final Goal... words) {
            asyncGoalDao.insertGoal(words[0]);
            return null;
        }
    }
    
    private static class UpdateAsyncTask extends AsyncTask<Goal, Void, Void> {

        private final GoalDao asyncGoalDao;

        UpdateAsyncTask(GoalDao GoalDao) {
            asyncGoalDao = GoalDao;
        }

        @Override
        protected Void doInBackground(final Goal... words) {
            asyncGoalDao.updateGoal(words[0]);
            return null;
        }
    }
}
