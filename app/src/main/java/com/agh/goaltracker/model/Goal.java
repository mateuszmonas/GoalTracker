package com.agh.goaltracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "goals")
public class Goal {
    @PrimaryKey
    @ColumnInfo(name = "goal_id")
    public int goalId;

    @ColumnInfo(name = "title")
    public String title;

    public Goal(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getGoalId() {
        return goalId;
    }
}
