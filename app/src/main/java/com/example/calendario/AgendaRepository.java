package com.example.calendario;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class AgendaRepository {

    private TaskDao taskDao;
    private LiveData<List<Task>> urgentTasks;
    private LiveData<List<Task>> normalTasks;

    public AgendaRepository(Application application) {
        TaskDatabase db = TaskDatabase.getInstance(application); // Método correto
        taskDao = db.taskDao();
        urgentTasks = taskDao.getUrgentTasks();
        normalTasks = taskDao.getNormalTasks();
    }

    public LiveData<List<Task>> getUrgentTasks() {
        return urgentTasks;
    }

    public LiveData<List<Task>> getNormalTasks() {
        return normalTasks;
    }

    public void insert(Task task) {
        TaskDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.insert(task); // Método correto (sem "Task" no final)
        });
    }
}