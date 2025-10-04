package com.example.calendario;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarioFragment extends Fragment {

    private AgendaViewModel viewModel;
    private TaskAdapter taskAdapter;
    private CalendarView calendarView;
    private long selectedDateInMillis;
    private List<Task> allUrgentTasks = new ArrayList<>();
    private List<Task> allNormalTasks = new ArrayList<>();

    public CalendarioFragment() {
        // Construtor público padrão.
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendario, container, false);

        calendarView = view.findViewById(R.id.calendar_view);
        RecyclerView tasksRecyclerView = view.findViewById(R.id.tasks_recycler_view);
        FloatingActionButton addTaskFab = view.findViewById(R.id.add_task_fab);

        viewModel = new ViewModelProvider(requireActivity()).get(AgendaViewModel.class);

        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter();
        tasksRecyclerView.setAdapter(taskAdapter);

        // Configura o listener de clique
        taskAdapter.setOnItemClickListener(task -> {
            viewModel.markAsCompleted(task.id);
        });

        // Define a data inicial como hoje
        selectedDateInMillis = System.currentTimeMillis();
        calendarView.setDate(selectedDateInMillis);

        // Listener para mudança de data
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth, 0, 0, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            selectedDateInMillis = calendar.getTimeInMillis();
            filterTasksByDate(selectedDateInMillis);
        });

        // FAB para adicionar tarefa
        addTaskFab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AdicionarTarefaActivity.class);
            intent.putExtra("selectedDate", selectedDateInMillis);
            startActivity(intent);
        });

        // Observa as tarefas urgentes
        viewModel.getUrgentTasks().observe(getViewLifecycleOwner(), tasks -> {
            if (tasks != null) {
                allUrgentTasks = tasks;
                filterTasksByDate(selectedDateInMillis);
            }
        });

        // Observa as tarefas normais
        viewModel.getNormalTasks().observe(getViewLifecycleOwner(), tasks -> {
            if (tasks != null) {
                allNormalTasks = tasks;
                filterTasksByDate(selectedDateInMillis);
            }
        });

        return view;
    }

    private void filterTasksByDate(long dateInMillis) {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(allUrgentTasks);
        allTasks.addAll(allNormalTasks);

        // Filtra apenas tarefas do dia selecionado e não concluídas
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : allTasks) {
            if (isSameDay(task.date, dateInMillis) && !task.isCompleted) {
                filteredTasks.add(task);
            }
        }

        taskAdapter.setTasks(filteredTasks);
    }

    private boolean isSameDay(long date1, long date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(date1);
        cal2.setTimeInMillis(date2);

        // Zera as horas para comparar apenas a data
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);

        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
}