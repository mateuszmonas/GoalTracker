package com.agh.goaltracker.model.source.local;

import com.agh.goaltracker.model.Goal;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = Goal.class, version = 1, exportSchema = false)
public abstract class GoalDatabase extends RoomDatabase {
    public abstract GoalDao goalDao();
}
