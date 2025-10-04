package com.example.calendario;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText nomeEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText telefoneEditText;
    private TextInputEditText senhaEditText;
    private TextInputEditText confirmarSenhaEditText;
    private Button cadastrarButton;
    private TextView loginTextView;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cadastro);

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        nomeEditText = findViewById(R.id.editTextNome);
        emailEditText = findViewById(R.id.editTextEmail);
        telefoneEditText = findViewById(R.id.editTextTelefone);
        senhaEditText = findViewById(R.id.editTextSenha);
        confirmarSenhaEditText = findViewById(R.id.editTextConfirmarSenha);
        cadastrarButton = findViewById(R.id.btnCadastrar);
        loginTextView = findViewById(R.id.txtLogin);
    }

    private void setupClickListeners() {
        cadastrarButton.setOnClickListener(v -> realizarCadastro());

        loginTextView.setOnClickListener(v -> finish());
    }

    private void realizarCadastro() {
        String nome = nomeEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String telefone = telefoneEditText.getText().toString().trim();
        String senha = senhaEditText.getText().toString().trim();
        String confirmarSenha = confirmarSenhaEditText.getText().toString().trim();

        if (validarCampos(nome, email, telefone, senha, confirmarSenha)) {
            salvarUsuario(nome, email, telefone);
            Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean validarCampos(String nome, String email, String telefone, String senha, String confirmarSenha) {
        if (nome.isEmpty()) {
            Toast.makeText(this, "Digite seu nome", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Digite seu email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Digite um email válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (telefone.isEmpty()) {
            Toast.makeText(this, "Digite seu telefone", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (senha.isEmpty()) {
            Toast.makeText(this, "Digite sua senha", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (senha.length() < 6) {
            Toast.makeText(this, "A senha deve ter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!senha.equals(confirmarSenha)) {
            Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void salvarUsuario(String nome, String email, String telefone) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_name", nome);
        editor.putString("user_email", email);
        editor.putString("user_phone", telefone);
        editor.apply();
    }
}