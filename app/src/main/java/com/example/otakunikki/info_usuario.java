    package com.example.otakunikki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class info_usuario extends AppCompatActivity {

    private Button btnEliminarPerfil, btnDesconexion, btnCambioPerfil;
    private EditText etNombreUsuario, etCorreoUsuario, etContraseniaUsuario;
    private Spinner spRegion;
    private ImageButton imgPerfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_usuario);

        btnEliminarPerfil = findViewById(R.id.btnEliminarPerfil);
        btnDesconexion = findViewById(R.id.btnDesconexion);
        btnCambioPerfil = findViewById(R.id.btnCambioPerfil);
        etNombreUsuario = findViewById(R.id.etNombreUsuario);
        etCorreoUsuario = findViewById(R.id.etCorreoUsuario);
        etContraseniaUsuario = findViewById(R.id.etContraseniaUsuario);
        spRegion = findViewById(R.id.spRegion);
        imgPerfil = findViewById(R.id.imgPerfil);

        btnCambioPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SeleccionPerfil.class);
                startActivity(intent);
            }
        });

        btnDesconexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActividadInicial.class);
                startActivity(intent);

                // Aquí puedes implementar la lógica para desconectarse del usuario

            }
        });

        btnEliminarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SeleccionPerfil.class);
                startActivity(intent);

                // Aquí puedes implementar la lógica para eliminar el perfil del usuario

            }
        });

    }
}