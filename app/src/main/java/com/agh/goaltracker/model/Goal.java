package com.agh.goaltracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "goals")
public class Goal {
    @PrimaryKey
    @ColumnInfo(name = "goal_id")
    private int goalId;

    @ColumnInfo(name = "title")
    private String title;

    public Goal(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
