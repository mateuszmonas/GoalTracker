package com.agh.goaltracker.model;

import com.agh.goaltracker.util.ProgressDrawable;

import java.util.Date;
import java.util.Locale;
import java.util.Objects;

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
    public boolean progressAsTime;

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
        this(title, dueDate, 0);
    }

    public Goal(String title, Date dueDate, int totalGoal) {
        this.title = title;
        this.dueDate = dueDate;
        this.currentProgress = 0;
        this.progressAsTime = false;
        this.totalGoal = totalGoal;
    }

    public Goal(String title, Date dueDate, int totalGoalHours, int totalGoalMinutes) {
        this.title = title;
        this.dueDate = dueDate;
        this.progressAsTime = true;
        this.totalGoal = totalGoalHours * 3600 + totalGoalMinutes * 60;
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

    public boolean isProgressAsTime() {
        return progressAsTime;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public int getTotalGoal() {
        return totalGoal;
    }

    public ProgressDrawable getProgressDrawable() {
        if (totalGoal == 0)
            return ProgressDrawable.WATERING_CAN;
        double completedRatio = (double) currentProgress / totalGoal;
        if (completedRatio < 0.2) {
            return ProgressDrawable.PLANT1;
        } else if (completedRatio < 0.5) {
            return ProgressDrawable.PLANT2;
        } else if (completedRatio < 0.9) {
            return ProgressDrawable.PLANT3;
        } else {
            return ProgressDrawable.PLANT4;
        }

    }

    public String progressToString() {
        String result;
        if (progressAsTime) {
            if (totalGoal > 0) {
                result = String.format(Locale.getDefault(), "%02d:%02d:%02d/%02d:%02d:%02d", currentProgress / 3600, currentProgress % 3600 / 60, currentProgress % 60, totalGoal / 3600, totalGoal % 3600 / 60, totalGoal % 60);
            } else {
                result = String.format(Locale.getDefault(), "%02d:%02d:%02d", currentProgress / 3600, currentProgress % 3600 / 60, currentProgress % 60);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return goalId == goal.goalId &&
                progressAsTime == goal.progressAsTime &&
                currentProgress == goal.currentProgress &&
                totalGoal == goal.totalGoal &&
                Objects.equals(title, goal.title) &&
                Objects.equals(dueDate, goal.dueDate);
    }

    public boolean isFailed() {
        return dueDate != null && dueDate.before(new Date()) && currentProgress < totalGoal;
    }

    public boolean isCompleted() {
        return (totalGoal != 0 && totalGoal <= currentProgress) || (dueDate != null && dueDate.before(new Date()) && totalGoal == 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goalId, title, dueDate, progressAsTime, currentProgress, totalGoal);
    }
}
