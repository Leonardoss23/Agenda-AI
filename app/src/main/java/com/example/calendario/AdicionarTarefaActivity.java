package com.example.calendario;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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

    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);

        titleEditText = findViewById(R.id.edit_text_title);
        descriptionEditText = findViewById(R.id.edit_text_description);
        estimatedTimeEditText = findViewById(R.id.edit_text_estimated_time);
        dateTextView = findViewById(R.id.text_view_data_selecionada);
        urgentSwitch = findViewById(R.id.switch_urgente);
        saveButton = findViewById(R.id.button_save);

        viewModel = new ViewModelProvider(this).get(AgendaViewModel.class);

        // Inicializa Firestore e pega ID do usuário logado
        db = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        dateTextView.setOnClickListener(v -> showDatePicker());
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

        if (title.isEmpty() || selectedDateInMillis == 0) {
            Toast.makeText(this, "Título e prazo são obrigatórios.", Toast.LENGTH_SHORT).show();
            return;
        }

        int estimatedTime = 0;
        if (!estimatedTimeStr.isEmpty()) {
            try {
                estimatedTime = Integer.parseInt(estimatedTimeStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Tempo estimado deve ser um número válido.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Cria o objeto Task
        Task novaTarefa = new Task(title, description, selectedDateInMillis, isUrgent, false);

        // 1️⃣ Salva localmente no ViewModel
        viewModel.insert(novaTarefa);

        // 2️⃣ Salva no Firestore (coleção "tarefas/userId/minhasTarefas")
        if (userId != null) {
            db.collection("tarefas")
                    .document(userId)
                    .collection("minhasTarefas")
                    .add(novaTarefa)
                    .addOnSuccessListener(docRef -> Toast.makeText(this, "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Erro ao salvar tarefa: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }

        finish();
    }
}
