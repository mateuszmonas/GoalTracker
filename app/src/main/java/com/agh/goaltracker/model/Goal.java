package com.agh.goaltracker.model;

import java.util.Date;
import java.util.Locale;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "goals")
public class Goal {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "goal_id")
    public int goalId;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "due_date")
    public Date dueDate;

    @ColumnInfo(name = "progress_type_as_minutes")
    public boolean progressAsMinutes;

    @ColumnInfo(name = "current_progress")
    public int currentProgress;

    @ColumnInfo(name = "total_goal")
    public int totalGoal;

    @Ignore
    public Goal(String title) {
        this(title, null);
    }

    @Ignore
    public Goal(String title, Date dueDate) {
        this(title, dueDate, false, 0);
    }

    public Goal(String title, Date dueDate, boolean progressAsMinutes, int totalGoal) {
        this.title = title;
        this.dueDate = dueDate;
        this.progressAsMinutes = progressAsMinutes;
        this.currentProgress = 0;
        this.totalGoal = totalGoal;
    }

    public String getTitle() {
        return title;
    }

    public int getGoalId() {
        return goalId;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public boolean isProgressAsMinutes() {
        return progressAsMinutes;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public int getTotalGoal() {
        return totalGoal;
    }

    public String progressToString() {
        String result = "";
        if (progressAsMinutes) {
            if (totalGoal > 0) {
                result = String.format(Locale.getDefault(), "%02d:%02d/%02d:%02d", currentProgress / 60, currentProgress % 60, totalGoal / 60, totalGoal % 60);
            } else {
                result = String.format(Locale.getDefault(), "%02d:%02d", currentProgress / 60, currentProgress % 60);
            }
        } else {
            if (totalGoal > 0) {
                result = String.format(Locale.getDefault(), "%d/%d", currentProgress, totalGoal);
            } else {
                result = String.format(Locale.getDefault(), "%d", currentProgress);
            }
        }
        return result;

    }
}
