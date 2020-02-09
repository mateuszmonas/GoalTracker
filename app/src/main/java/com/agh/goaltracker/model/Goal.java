package com.agh.goaltracker.model;

import java.util.Date;

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

    @Ignore
    public Goal(String title) {
        this.title = title;
        this.dueDate = null;
    }

    public Goal(String title, Date dueDate) {
        this.title = title;
        this.dueDate = dueDate;
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
}
