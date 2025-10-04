package com.example.calendario;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> urgentTasks;
    private LiveData<List<Task>> normalTasks;
    private LiveData<List<Task>> completedTasks;
    private final ExecutorService executorService;

    public TaskRepository(Application application) {
        TaskDatabase db = TaskDatabase.getInstance(application);
        taskDao = db.taskDao();
        urgentTasks = taskDao.getUrgentTasks();
        normalTasks = taskDao.getNormalTasks();
        completedTasks = taskDao.getCompletedTasks();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Task>> getUrgentTasks() {
        return urgentTasks;
    }

    public LiveData<List<Task>> getNormalTasks() {
        return normalTasks;
    }

    public LiveData<List<Task>> getCompletedTasks() {
        return completedTasks;
    }

    public void insert(Task task) {
        executorService.execute(() -> taskDao.insert(task));
    }

    public void update(Task task) {
        executorService.execute(() -> taskDao.update(task));
    }

    // ✅ MÉTODO CORRIGIDO
    public void markAsCompleted(int taskId) {
        executorService.execute(() -> {
            long completionDate = System.currentTimeMillis();
            taskDao.markTaskAsCompleted(taskId, completionDate);
        });
    }
}