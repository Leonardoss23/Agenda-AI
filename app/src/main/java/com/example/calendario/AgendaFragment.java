package com.example.calendario;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AgendaFragment extends Fragment {

    private AgendaViewModel viewModel;
    private TaskAdapter urgentTaskAdapter;
    private TaskAdapter normalTaskAdapter;

    public AgendaFragment() {
        // Construtor público padrão
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agenda, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializa o ViewModel
        viewModel = new ViewModelProvider(this).get(AgendaViewModel.class);

        // Configura o RecyclerView para tarefas urgentes
        RecyclerView urgentRecyclerView = view.findViewById(R.id.urgent_tasks_recycler_view);
        urgentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        urgentTaskAdapter = new TaskAdapter();
        urgentRecyclerView.setAdapter(urgentTaskAdapter);

        // Configura o RecyclerView para tarefas normais
        RecyclerView normalRecyclerView = view.findViewById(R.id.normal_tasks_recycler_view);
        normalRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        normalTaskAdapter = new TaskAdapter();
        normalRecyclerView.setAdapter(normalTaskAdapter);

        // Configura os listeners de clique
        urgentTaskAdapter.setOnItemClickListener(task -> {
            viewModel.markAsCompleted(task.id);
        });

        normalTaskAdapter.setOnItemClickListener(task -> {
            viewModel.markAsCompleted(task.id);
        });

        // Observa as tarefas urgentes e atualiza o adaptador
        viewModel.getUrgentTasks().observe(getViewLifecycleOwner(), tasks -> {
            urgentTaskAdapter.setTasks(tasks);
        });

        // Observa as tarefas normais e atualiza o adaptador
        viewModel.getNormalTasks().observe(getViewLifecycleOwner(), tasks -> {
            normalTaskAdapter.setTasks(tasks);
        });

        // Configura o botão flutuante para adicionar uma nova tarefa
        FloatingActionButton addTaskFab = view.findViewById(R.id.add_task_fab);
        addTaskFab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AdicionarTarefaActivity.class);
            startActivity(intent);
        });
    }
}