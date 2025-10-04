package com.example.calendario;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class PerfilFragment extends Fragment {

    private TextView textViewNomeUsuario, textViewEmailUsuario;
    private TextView textViewNomeCompleto, textViewTelefone;
    private Button buttonEditarPerfil, buttonAlterarSenha, buttonSair;
    private ImageView imageViewPerfil;

    private SharedPreferences sharedPreferences;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Use o layout do fragment_perfil que criamos anteriormente
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireActivity().getSharedPreferences("user_prefs", requireActivity().MODE_PRIVATE);

        initViews(view);
        setupClickListeners();
        carregarDadosUsuario();
    }

    private void initViews(View view) {
        textViewNomeUsuario = view.findViewById(R.id.txtNomeUsuario);
        textViewEmailUsuario = view.findViewById(R.id.txtEmailUsuario);
        textViewNomeCompleto = view.findViewById(R.id.txtNomeCompleto);
        textViewTelefone = view.findViewById(R.id.txtTelefone);
        buttonEditarPerfil = view.findViewById(R.id.btnEditarPerfil);
        buttonAlterarSenha = view.findViewById(R.id.btnAlterarSenha);
        buttonSair = view.findViewById(R.id.btnSair);
        imageViewPerfil = view.findViewById(R.id.imgPerfil);
    }

    private void setupClickListeners() {
        buttonEditarPerfil.setOnClickListener(v -> editarPerfil());

        buttonAlterarSenha.setOnClickListener(v -> alterarSenha());

        buttonSair.setOnClickListener(v -> confirmarSaida());

        imageViewPerfil.setOnClickListener(v -> selecionarFoto());
    }

    private void carregarDadosUsuario() {
        String nome = sharedPreferences.getString("user_name", "Usuário");
        String email = sharedPreferences.getString("user_email", "email@exemplo.com");
        String telefone = sharedPreferences.getString("user_phone", "(00) 00000-0000");

        textViewNomeUsuario.setText(nome);
        textViewEmailUsuario.setText(email);
        textViewNomeCompleto.setText(nome);
        textViewTelefone.setText(telefone);
    }

    private void selecionarFoto() {
        Toast.makeText(getContext(), "Funcionalidade de foto em desenvolvimento", Toast.LENGTH_SHORT).show();
    }

    private void editarPerfil() {
        Toast.makeText(getContext(), "Editar perfil - em desenvolvimento", Toast.LENGTH_SHORT).show();
    }

    private void alterarSenha() {
        Toast.makeText(getContext(), "Alterar senha - em desenvolvimento", Toast.LENGTH_SHORT).show();
    }

    private void confirmarSaida() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Sair")
                .setMessage("Tem certeza que deseja sair da sua conta?")
                .setPositiveButton("Sim", (dialog, which) -> sairConta())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void sairConta() {
        // Limpar dados de sessão
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_logged_in", false);
        editor.remove("user_name");
        editor.remove("user_email");
        editor.remove("user_phone");
        editor.apply();

        Toast.makeText(getContext(), "Saindo...", Toast.LENGTH_SHORT).show();

        // Navegar para LoginActivity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}