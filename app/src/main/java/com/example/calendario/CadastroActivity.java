package com.example.calendario;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText nomeEditText, emailEditText, telefoneEditText, senhaEditText, confirmarSenhaEditText;
    private Button cadastrarButton;
    private TextView loginTextView;

    private FirebaseAuth auth;
    private DatabaseReference usuariosRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cadastro); // ou activity_cadastro.xml

        auth = FirebaseAuth.getInstance();
        usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios");

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
        cadastrarButton.setOnClickListener(v -> {
            // 1️⃣ Mostra o alerta imediatamente
            new AlertDialog.Builder(CadastroActivity.this)
                    .setTitle("Cadastro realizado com sucesso!")
                    .setMessage("Sua conta foi criada com sucesso! Clique no botão abaixo para voltar à tela de login.")
                    .setPositiveButton("Ir para Login", (dialog, which) -> {
                        // Vai para LoginActivity
                        startActivity(new Intent(CadastroActivity.this, LoginActivity.class));
                        finish();
                    })
                    .setCancelable(false)
                    .show();

            // 2️⃣ Continua com o cadastro no Firebase
            realizarCadastroFirebase();
        });

        loginTextView.setOnClickListener(v -> finish());
    }

    // Função que realiza o cadastro no Firebase normalmente
    private void realizarCadastroFirebase() {
        String nome = nomeEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String telefone = telefoneEditText.getText().toString().trim();
        String senha = senhaEditText.getText().toString().trim();
        String confirmarSenha = confirmarSenhaEditText.getText().toString().trim();

        if (!validarCampos(nome, email, telefone, senha, confirmarSenha)) return;

        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && auth.getCurrentUser() != null) {
                        String userId = auth.getCurrentUser().getUid();
                        String senhaCriptografada = cifraDeCesar(senha, 3);
                        Usuario usuario = new Usuario(nome, email, telefone, senhaCriptografada);
                        usuariosRef.child(userId).setValue(usuario)
                                .addOnFailureListener(e -> Toast.makeText(CadastroActivity.this, "Erro ao salvar dados: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        String mensagem = (task.getException() != null) ? task.getException().getMessage() : "Erro ao criar usuário";
                        Toast.makeText(CadastroActivity.this, mensagem, Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(CadastroActivity.this, "Erro no cadastro: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private boolean validarCampos(String nome, String email, String telefone, String senha, String confirmarSenha) {
        if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Digite um email válido", Toast.LENGTH_SHORT).show();
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

    private String cifraDeCesar(String texto, int chave) {
        StringBuilder resultado = new StringBuilder();
        for (char c : texto.toCharArray()) {
            resultado.append((char) (c + chave));
        }
        return resultado.toString();
    }

    public static class Usuario {
        public String nome, email, telefone, senha;

        public Usuario() {}

        public Usuario(String nome, String email, String telefone, String senha) {
            this.nome = nome;
            this.email = email;
            this.telefone = telefone;
            this.senha = senha;
        }
    }
}
