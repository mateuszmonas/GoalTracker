package com.agh.goaltracker.model.source.local;

import com.agh.goaltracker.model.Goal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Goal.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})

public abstract class GoalDatabase extends RoomDatabase {
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public abstract GoalDao goalDao();

    public static RoomDatabase.Callback CREATE_TRIGGERS = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            db.execSQL("" +
                    "create trigger TRIG_contributing_to_completed_goal " +
                    "after update on goals " +
                    "when new.total_goal<new.current_progress and new.total_goal <> 0 " +
                    "begin " +
                    "update goals " +
                    "set current_progress=total_goal " +
                    "where goal_id=new.goal_id; " +
                    "end");
            db.execSQL("" +
                    "create trigger TRIG_contributing_to_failed_goal " +
                    "before update on goals " +
                    "when old.due_date<CAST((julianday('now') - 2440587.5)*86400000 AS INTEGER) " +
                    "begin " +
                    "SELECT RAISE(IGNORE); " +
                    "end");
        }
    };
}
