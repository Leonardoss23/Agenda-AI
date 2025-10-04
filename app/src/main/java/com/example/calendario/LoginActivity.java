package com.example.calendario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText senhaEditText;
    private Button loginButton;
    private TextView cadastrarTextView;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        // Verificar se usuário já está logado
        if (isUserLoggedIn()) {
            startMainActivity();
            return;
        }

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        emailEditText = findViewById(R.id.editTextEmail);
        senhaEditText = findViewById(R.id.editTextSenha);
        loginButton = findViewById(R.id.buttonLogin);
        cadastrarTextView = findViewById(R.id.textViewCadastrar);
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> fazerLogin());

        cadastrarTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intent);
        });
    }

    private void fazerLogin() {
        String email = emailEditText.getText().toString().trim();
        String senha = senhaEditText.getText().toString().trim();

        if (validarCampos(email, senha)) {
            salvarDadosUsuario(email);
            Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
            startMainActivity();
        }
    }

    private boolean validarCampos(String email, String senha) {
        if (email.isEmpty()) {
            Toast.makeText(this, "Digite seu email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (senha.isEmpty()) {
            Toast.makeText(this, "Digite sua senha", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Digite um email válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean("is_logged_in", false);
    }

    private void salvarDadosUsuario(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_logged_in", true);
        editor.putString("user_email", email);
        editor.putString("user_name", email.split("@")[0]);
        editor.apply();
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}