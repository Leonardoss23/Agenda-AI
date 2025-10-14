package com.example.calendario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PerfilFragment extends Fragment {

    private ImageView imgPerfil;
    private ImageButton btnEditarFoto;
    private TextView txtNomeUsuario, txtEmailUsuario, txtNomeCompleto, txtTelefone, txtDataNascimento;
    private Button btnEditarPerfil, btnAlterarSenha, btnSair;

    private SharedPreferences sharedPreferences;

    public PerfilFragment() {
        // Construtor vazio obrigatório
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla o layout do fragment (use o nome do XML que você mandou)
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireActivity().getSharedPreferences("user_prefs", requireActivity().MODE_PRIVATE);

        initViews(view);
        carregarDadosUsuario();
        setupClickListeners();
    }

    private void initViews(View view) {
        imgPerfil = view.findViewById(R.id.imgPerfil);
        btnEditarFoto = view.findViewById(R.id.btnEditarFoto);
        txtNomeUsuario = view.findViewById(R.id.txtNomeUsuario);
        txtEmailUsuario = view.findViewById(R.id.txtEmailUsuario);
        txtNomeCompleto = view.findViewById(R.id.txtNomeCompleto);
        txtTelefone = view.findViewById(R.id.txtTelefone);
        txtDataNascimento = view.findViewById(R.id.txtDataNascimento);
        btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil);
        btnAlterarSenha = view.findViewById(R.id.btnAlterarSenha);
        btnSair = view.findViewById(R.id.btnSair);
    }

    private void carregarDadosUsuario() {
        String nome = sharedPreferences.getString("user_name", "Usuário");
        String email = sharedPreferences.getString("user_email", "email@exemplo.com");
        String telefone = sharedPreferences.getString("user_phone", "(00) 00000-0000");
        String dataNascimento = sharedPreferences.getString("user_birth", "01/01/2000");

        txtNomeUsuario.setText(nome);
        txtEmailUsuario.setText(email);
        txtNomeCompleto.setText(nome);
        txtTelefone.setText(telefone);
        txtDataNascimento.setText(dataNascimento);
    }

    private void setupClickListeners() {
        // Editar foto — placeholder
        btnEditarFoto.setOnClickListener(v ->
                Toast.makeText(requireContext(), "Trocar foto (em desenvolvimento)", Toast.LENGTH_SHORT).show()
        );

        // Editar perfil: abre CadastroActivity em modo edição
        btnEditarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), CadastroActivity.class);
            intent.putExtra("modo_edicao", true);
            startActivity(intent);
        });

        // Alterar senha: aqui abre uma Activity/dialog futuro (placeholder)
        btnAlterarSenha.setOnClickListener(v ->
                Toast.makeText(requireContext(), "Alterar senha (em desenvolvimento)", Toast.LENGTH_SHORT).show()
        );

        // Logout: limpa sessão e volta pro LoginActivity
        btnSair.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // remove todos os dados do usuário (ajuste se quiser preservar algo)
            editor.apply();

            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    // Se você usa cifraDeCesar em outros fragments/activities, pode mover esse método para uma util class.
    private String cifraDeCesar(String texto, int chave) {
        if (texto == null) return "";
        StringBuilder resultado = new StringBuilder();
        for (char c : texto.toCharArray()) {
            resultado.append((char) (c + chave));
        }
        return resultado.toString();
    }
}
