package com.example.calendario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        // Configura Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Configura DrawerLayout (menu lateral)
        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Ícone do perfil no toolbar
        ImageView profileIcon = findViewById(R.id.profile_icon);
        profileIcon.setOnClickListener(view -> {
            if (isUserLoggedIn()) {
                // abre o PerfilFragment quando o usuário estiver autenticado no Firebase
                openFragment(new PerfilFragment());
            } else {
                startLoginActivity();
            }
        });

        // Botões do menu lateral
        Button btnAgenda = findViewById(R.id.btn_agenda);
        Button btnTarefas = findViewById(R.id.btn_tarefas);
        Button btnPerfil = findViewById(R.id.btn_perfil);
        Button btnConcluidas = findViewById(R.id.btn_concluidas);

        btnAgenda.setOnClickListener(v -> openFragment(new CalendarioFragment()));
        btnTarefas.setOnClickListener(v -> openFragment(new AgendaFragment()));
        btnPerfil.setOnClickListener(v -> {
            if (isUserLoggedIn()) {
                openFragment(new PerfilFragment());
            } else {
                startLoginActivity();
            }
        });
        btnConcluidas.setOnClickListener(v -> {
            openFragment(new ConcluidasFragment());
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        // Fragment inicial
        if (savedInstanceState == null) {
            openFragment(new CalendarioFragment());
        }
    }

    private void openFragment(Fragment fragment) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commitAllowingStateLoss(); // previne crash se estado já salvo
            drawerLayout.closeDrawer(GravityCompat.START);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Checagem de login confiável usando FirebaseAuth.
     * Retorna true se existir um usuário autenticado no Firebase.
     */
    private boolean isUserLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
