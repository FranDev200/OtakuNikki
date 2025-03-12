package com.example.otakunikki.Actividades;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otakunikki.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActividadInicial extends AppCompatActivity {
    private Button registro, inicioSesion;
    private FirebaseAuth mAuthComprobarUsuario;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_incial);

        registro = findViewById(R.id.btnRegistrar);
        inicioSesion = findViewById(R.id.btnIniciarSesion);

        mAuthComprobarUsuario = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("PreferenciaSesion", MODE_PRIVATE);

        boolean recuerdame = sharedPreferences.getBoolean("Recuerdame", false);
        FirebaseUser usuario = mAuthComprobarUsuario.getCurrentUser();

        if (recuerdame && usuario != null) {
            // Si el usuario eligió recordar sesión, ir directo a seleccion perfil
            startActivity(new Intent(this, SeleccionPerfil.class));
            finish();
        } else {
            // Si no, ir a Inicio sesion
            startActivity(new Intent(this, InicioSesion.class));
            finish();
        }

        inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbrirInicioSesion();
            }
        });

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AbrirRegistro();
            }
        });
    }

    public void AbrirInicioSesion(){
        Intent intent = new Intent(getApplicationContext(), InicioSesion.class);
        startActivity(intent);
    }
    public void AbrirRegistro(){
        Intent intent = new Intent(getApplicationContext(), ActividadRegistro.class);
        startActivity(intent);
    }
}