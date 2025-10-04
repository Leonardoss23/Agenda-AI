package com.example.calendario;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class Task {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String description;
    public long date;
    public boolean isUrgent;
    public boolean isCompleted;
    public long completedDate; // data em que foi concluída

    public Task(String title, String description, long date, boolean isUrgent, boolean isCompleted) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.isUrgent = isUrgent;
        this.isCompleted = isCompleted;
        this.completedDate = 0;
    }
}

