package com.agh.goaltracker.model.source.local;

import com.agh.goaltracker.model.Goal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Goal.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})

public abstract class GoalDatabase extends RoomDatabase {
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public abstract GoalDao goalDao();
}
