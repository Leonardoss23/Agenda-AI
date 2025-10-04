package com.example.calendario;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AdicionarTarefaActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText estimatedTimeEditText;
    private TextView dateTextView;
    private Switch urgentSwitch;
    private Button saveButton;

    private AgendaViewModel viewModel;
    private long selectedDateInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);

        // Vincula as visualizações do layout usando os IDs corretos do XML
        titleEditText = findViewById(R.id.edit_text_title);
        descriptionEditText = findViewById(R.id.edit_text_description);
        estimatedTimeEditText = findViewById(R.id.edit_text_estimated_time);
        dateTextView = findViewById(R.id.text_view_data_selecionada);
        urgentSwitch = findViewById(R.id.switch_urgente);
        saveButton = findViewById(R.id.button_save);

        // Inicializa o ViewModel
        viewModel = new ViewModelProvider(this).get(AgendaViewModel.class);

        // Define o clique no TextView para abrir o DatePickerDialog
        dateTextView.setOnClickListener(v -> showDatePicker());

        // Configura o botão de salvar
        saveButton.setOnClickListener(v -> salvarTarefa());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    Calendar c = Calendar.getInstance();
                    c.set(year1, month1, dayOfMonth);
                    selectedDateInMillis = c.getTimeInMillis();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    dateTextView.setText(sdf.format(c.getTime()));
                }, year, month, day);
        datePickerDialog.show();
    }

    private void salvarTarefa() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String estimatedTimeStr = estimatedTimeEditText.getText().toString().trim();
        boolean isUrgent = urgentSwitch.isChecked();

        // Validação básica dos campos
        if (title.isEmpty() || selectedDateInMillis == 0) {
            Toast.makeText(this, "Título e prazo são obrigatórios.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Converte o tempo estimado para int, se não for vazio
        int estimatedTime = 0;
        if (!estimatedTimeStr.isEmpty()) {
            try {
                estimatedTime = Integer.parseInt(estimatedTimeStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Tempo estimado deve ser um número válido.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Cria e salva a nova tarefa
        Task novaTarefa = new Task(title, description, selectedDateInMillis, isUrgent, false);
        viewModel.insert(novaTarefa);

        Toast.makeText(this, "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
