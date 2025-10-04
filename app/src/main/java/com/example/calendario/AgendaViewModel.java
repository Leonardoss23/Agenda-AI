package com.example.calendario;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class AgendaViewModel extends AndroidViewModel {

    private TaskRepository repository;
    private LiveData<List<Task>> urgentTasks;
    private LiveData<List<Task>> normalTasks;
    private LiveData<List<Task>> completedTasks;

    public AgendaViewModel(Application application) {
        super(application);
        repository = new TaskRepository(application);
        urgentTasks = repository.getUrgentTasks();
        normalTasks = repository.getNormalTasks();
        completedTasks = repository.getCompletedTasks();
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
        repository.insert(task);
    }

    public void update(Task task) {
        repository.update(task);
    }

    // ✅ MÉTODO CORRIGIDO
    public void markAsCompleted(int taskId) {
        repository.markAsCompleted(taskId);
    }
}