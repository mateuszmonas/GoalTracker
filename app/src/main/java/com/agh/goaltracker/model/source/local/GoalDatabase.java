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

    public static RoomDatabase.Callback CREATE_TRIGGER_CALLBACK = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            db.execSQL("" +
                    "create trigger TRING_goal_completed " +
                    "after update on goals " +
                    "when new.current_progress>new.total_goal and new.total_goal <> 0 " +
                    "begin " +
                    "update goals " +
                    "set current_progress=total_goal " +
                    "where goal_id=new.goal_id; " +
                    "end");
        }
    };
}
