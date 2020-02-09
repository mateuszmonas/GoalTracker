package com.agh.goaltracker.model.source.local;

import com.agh.goaltracker.model.Goal;

import java.util.List;

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


    @Update
    void updateGoal(Goal goal);

    @Delete
    void deleteGoal(Goal goal);

    @Delete
    void deleteGoals(List<Goal> goals);
}
