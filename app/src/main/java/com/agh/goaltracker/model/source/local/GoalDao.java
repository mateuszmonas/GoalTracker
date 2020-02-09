package com.agh.goaltracker.model.source.local;

import com.agh.goaltracker.model.Goal;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
interface GoalDao {
    @Query("select * from goals")
    LiveData<List<Goal>> observeGoals();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Goal goal);
}
