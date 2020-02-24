package com.agh.goaltracker.model.source.local;

import com.agh.goaltracker.model.Goal;

import java.util.List;
import java.util.Set;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
interface GoalDao {
    @Query("select * from goals")
    LiveData<List<Goal>> observeGoals();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertGoal(Goal goal);

    @Query("select * from goals where goal_id=:goalId")
    LiveData<Goal> observeGoal(int goalId);

    @Query("select * from goals where goal_id=:goalId")
    Goal getGoal(int goalId);

    @Query("select * from goals where goal_id in (:goalsIds)")
    List<Goal> getGoals(Set<Integer> goalsIds);

    @Query("update goals set current_progress = current_progress + :amount where goal_id=:goalId")
    void increaseGoalProgress(int goalId, int amount);

    @Query("update goals set current_progress = current_progress + :amount where goal_id in (:goalsIds)")
    void increaseGoalsProgress(Set<Integer> goalsIds, int amount);

    @Update
    void updateGoal(Goal goal);

    @Delete
    void deleteGoal(Goal goal);

    @Delete
    void deleteGoals(List<Goal> goals);
}
