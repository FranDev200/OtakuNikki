package com.example.otakunikki.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otakunikki.Clases.Usuario;
import com.example.otakunikki.R;

public class InicioSesion extends AppCompatActivity {

    private TextView tvNombre, tvContrasenya;
    private Button btnInicioSesion;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        tvNombre = findViewById(R.id.etNombreUser);
        tvContrasenya = findViewById(R.id.etContrasenya);
        btnInicioSesion = findViewById(R.id.BtnInicioSesion);

        btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirSeleccion();
            }
        });

    }

    public void abrirSeleccion(){
        Intent intent = new Intent(getApplicationContext(), SeleccionPerfil.class);
        startActivity(intent);
    }
}