package com.example.calendario;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    // Tarefas urgentes pendentes
    @Query("SELECT * FROM task_table WHERE isUrgent = 1 AND isCompleted = 0 ORDER BY date ASC")
    LiveData<List<Task>> getUrgentTasks();

    // Tarefas normais pendentes
    @Query("SELECT * FROM task_table WHERE isUrgent = 0 AND isCompleted = 0 ORDER BY date ASC")
    LiveData<List<Task>> getNormalTasks();

    // Tarefas concluídas
    @Query("SELECT * FROM task_table WHERE isCompleted = 1 ORDER BY completedDate DESC")
    LiveData<List<Task>> getCompletedTasks();

    // ✅ MÉTODO CORRIGIDO - Marcar tarefa como concluída
    @Query("UPDATE task_table SET isCompleted = 1, completedDate = :completionDate WHERE id = :taskId")
    void markTaskAsCompleted(int taskId, long completionDate);
}