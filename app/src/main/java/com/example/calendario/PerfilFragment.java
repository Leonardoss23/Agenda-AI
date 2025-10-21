package com.example.calendario;

import android.app.AlertDialog;
import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilFragment extends Fragment {

    private ImageView imgPerfil;
    private ImageButton btnEditarFoto;
    private TextView txtNomeUsuario, txtEmailUsuario, txtTelefone, txtNomeCompleto;
    private Button btnAlterarSenha, btnSair;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference usuariosRef;

    public PerfilFragment() {
        // Construtor vazio obrigatório
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios");

        initViews(view);
        carregarDadosUsuario();
        setupClickListeners();
    }

    private void initViews(View view) {
        imgPerfil = view.findViewById(R.id.imgPerfil);
        btnEditarFoto = view.findViewById(R.id.btnEditarFoto);
        txtNomeUsuario = view.findViewById(R.id.txtNomeUsuario);
        txtEmailUsuario = view.findViewById(R.id.txtEmailUsuario);
        txtTelefone = view.findViewById(R.id.txtTelefone);
        txtNomeCompleto = view.findViewById(R.id.txtNomeCompleto);
        btnAlterarSenha = view.findViewById(R.id.btnAlterarSenha);
        btnSair = view.findViewById(R.id.btnSair);
    }

    private void carregarDadosUsuario() {
        if (user == null) {
            Toast.makeText(requireContext(), "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();

        usuariosRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(requireContext(), "Usuário não encontrado no banco de dados.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String nome = snapshot.child("nome").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String telefone = snapshot.child("telefone").getValue(String.class);

                // Exibir os dados vindos do Firebase
                txtNomeUsuario.setText(nome != null ? nome : "Usuário");
                txtNomeCompleto.setText(nome != null ? nome : "Usuário");
                txtEmailUsuario.setText(email != null ? email : "email@exemplo.com");
                txtTelefone.setText(telefone != null ? telefone : "(00) 00000-0000");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Erro: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners() {
        btnEditarFoto.setOnClickListener(v ->
                Toast.makeText(requireContext(), "Trocar foto (em desenvolvimento)", Toast.LENGTH_SHORT).show()
        );

        btnAlterarSenha.setOnClickListener(v -> {
            if (user != null && user.getEmail() != null) {
                String email = user.getEmail();
                auth.sendPasswordResetEmail(email)
                        .addOnSuccessListener(aVoid ->
                                new AlertDialog.Builder(requireContext())
                                        .setTitle("E-mail enviado!")
                                        .setMessage("Um link para redefinir sua senha foi enviado para:\n" + email)
                                        .setPositiveButton("OK", null)
                                        .show()
                        )
                        .addOnFailureListener(e ->
                                Toast.makeText(requireContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show()
                        );
            } else {
                Toast.makeText(requireContext(), "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            }
        });

        btnSair.setOnClickListener(v -> {
            auth.signOut();
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}
