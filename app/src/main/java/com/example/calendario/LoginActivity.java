package com.example.calendario;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, senhaEditText;
    private Button loginButton;
    private TextView cadastrarTextView;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login); // ou o XML correto da tela de login

        auth = FirebaseAuth.getInstance();

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
        // 🔹 Força o e-mail para minúsculas antes do login
        String email = emailEditText.getText().toString().trim().toLowerCase();
        String senha = senhaEditText.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        String erro = (task.getException() != null)
                                ? task.getException().getMessage()
                                : "Erro desconhecido ao fazer login";
                        Toast.makeText(this, "Erro no login: " + erro, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
