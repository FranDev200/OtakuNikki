package com.example.otakunikki;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ActividadInicial extends AppCompatActivity {
    private Button registro, inicioSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_incial);

        registro = findViewById(R.id.btnRegistrar);
        inicioSesion = findViewById(R.id.btnIniciarSesion);

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