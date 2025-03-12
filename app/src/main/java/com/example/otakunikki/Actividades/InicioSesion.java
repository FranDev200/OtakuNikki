package com.example.otakunikki.Actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otakunikki.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InicioSesion extends AppCompatActivity {

    private EditText etEmail, etContrasenya;
    private Button btnInicioSesion;
    private CheckBox chkMantenerSesion;
    private SharedPreferences estadoSesion;
    private FirebaseAuth mAuthInicioSesion;
    private ImageButton imgBtnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        // Inicializar FirebaseAuth
        mAuthInicioSesion = FirebaseAuth.getInstance();
        FirebaseUser usuario = mAuthInicioSesion.getCurrentUser();

        // Inicializar SharedPreferences
        estadoSesion = getSharedPreferences("PreferenciaSesion", MODE_PRIVATE);
        boolean recuerdame = estadoSesion.getBoolean("rememberMe", false);

        // Si el usuario ya está autenticado y eligió recordar sesión, ir directamente a SeleccionPerfil
        if (recuerdame && usuario != null) {
            startActivity(new Intent(this, SeleccionPerfil.class));
            finish();
            return; // Salimos del método onCreate
        }

        // Vincular elementos UI
        etEmail = findViewById(R.id.etEmailInicioSesion);
        etContrasenya = findViewById(R.id.etPwdInicioSesion);
        btnInicioSesion = findViewById(R.id.BtnInicioSesion);
        chkMantenerSesion = findViewById(R.id.chkMantenerSesion);
        imgBtnRegistro = findViewById(R.id.imgBtnRegistro);

        // Botón de registro
        imgBtnRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ActividadRegistro.class);
            startActivity(intent);
        });

        // Botón de inicio de sesión
        btnInicioSesion.setOnClickListener(v -> iniciarSesion());
    }

    private void iniciarSesion() {
        String email = etEmail.getText().toString().trim();
        String pwd = etContrasenya.getText().toString().trim();

        // Validaciones básicas
        if (email.isEmpty()) {
            Toast.makeText(this, "Ingrese un correo electrónico", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwd.isEmpty()) {
            Toast.makeText(this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Intentar iniciar sesión en Firebase
        mAuthInicioSesion.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Guardar preferencia de sesión
                        SharedPreferences.Editor editor = estadoSesion.edit();
                        editor.putBoolean("rememberMe", chkMantenerSesion.isChecked());
                        editor.apply();

                        // Redirigir al usuario
                        startActivity(new Intent(getApplicationContext(), SeleccionPerfil.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
