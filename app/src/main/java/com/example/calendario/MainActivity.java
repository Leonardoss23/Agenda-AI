package com.example.calendario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define o layout principal
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        // 1. Configura a Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove o título padrão para usar apenas o TextView centralizado
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // 2. Configura o DrawerLayout (Menu Lateral)
        drawerLayout = findViewById(R.id.drawer_layout);

        // ActionBarDrawerToggle liga o ícone do menu hamburger na Toolbar à funcionalidade do DrawerLayout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState(); // Sincroniza o estado para mostrar o ícone de hamburger

        // 3. Configura o clique no Ícone de Perfil (no Toolbar)
        ImageView profileIcon = findViewById(R.id.profile_icon);
        profileIcon.setOnClickListener(view -> {
            // Verifica se usuário está logado
            if (isUserLoggedIn()) {
                // Se estiver logado, mostra o PerfilFragment
                navigateToFragment(new PerfilFragment());
            } else {
                // Se NÃO estiver logado, vai para LoginActivity
                startLoginActivity();
            }
        });

        // 4. Configura os cliques dos BOTÕES CUSTOMIZADOS (no Menu Lateral)
        // Obtém as referências dos botões do menu lateral que estão dentro do LinearLayout
        Button btnAgenda = findViewById(R.id.btn_agenda);
        Button btnTarefas = findViewById(R.id.btn_tarefas);
        Button btnPerfil = findViewById(R.id.btn_perfil);
        Button btnConcluidas = findViewById(R.id.btn_concluidas);

        // Define os listeners de clique para cada botão
        btnAgenda.setOnClickListener(v -> navigateToFragment(new CalendarioFragment()));
        btnTarefas.setOnClickListener(v -> navigateToFragment(new AgendaFragment()));
        btnPerfil.setOnClickListener(v -> {
            // Verifica se usuário está logado
            if (isUserLoggedIn()) {
                navigateToFragment(new PerfilFragment());
            } else {
                startLoginActivity();
            }
        });
        btnConcluidas.setOnClickListener(v -> {
            showConcluidas();
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        // 5. Carrega o Fragmento do Calendário como tela inicial, se ainda não estiver carregado
        if (savedInstanceState == null) {
            navigateToFragment(new CalendarioFragment());
        }
    }

    private void showConcluidas() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ConcluidasFragment());
        transaction.commit();
    }

    /**
     * Função auxiliar para substituir o fragmento no container e fechar o drawer.
     */
    private void navigateToFragment(androidx.fragment.app.Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // Substitui o fragmento atual
                .commit();

        // Fecha o menu lateral após a navegação
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * Lida com o botão "Voltar" (Back) do dispositivo.
     * Se o menu lateral estiver aberto, ele o fecha em vez de sair da Activity.
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Verifica se o usuário está logado
     */
    private boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean("is_logged_in", false);
    }

    /**
     * Redireciona para a tela de login
     */
    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        // Não chama finish() para que o usuário possa voltar ao calendário
    }
}