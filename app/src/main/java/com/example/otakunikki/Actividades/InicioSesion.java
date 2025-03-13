package com.example.otakunikki.Actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.otakunikki.Clases.Usuario;
import com.example.otakunikki.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class InicioSesion extends AppCompatActivity {

    private EditText etEmail, etContrasenya;
    private Button btnInicioSesion;
    private CheckBox chkMantenerSesion;
    private FirebaseAuth mAuthInicioSesion;
    private ImageButton imgBtnRegistro;
    private SharedPreferences estadoSesion;
    private static final String TAG = "InicioSesion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        // Inicializar componentes de UI
        etEmail = findViewById(R.id.etEmailInicioSesion);
        etContrasenya = findViewById(R.id.etPwdInicioSesion);
        btnInicioSesion = findViewById(R.id.BtnInicioSesion);
        chkMantenerSesion = findViewById(R.id.chkMantenerSesion);
        imgBtnRegistro = findViewById(R.id.imgBtnRegistro);

        // Inicializar Firebase Auth
        mAuthInicioSesion = FirebaseAuth.getInstance();

        // Inicializar SharedPreferences
        estadoSesion = getSharedPreferences("PreferenciaSesion", MODE_PRIVATE);

        String userId = estadoSesion.getString("userId", null);
        boolean recuerdame = estadoSesion.getBoolean("rememberMe", false);

        if (recuerdame && userId != null) {
            Log.d(TAG, "Restaurando sesión para el usuario con UID: " + userId);
            obtenerDatosUsuario(userId);
        }

        btnInicioSesion.setOnClickListener(v -> iniciarSesion());

        imgBtnRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ActividadRegistro.class);
            startActivity(intent);
        });
    }


    private void restaurarSesion() {

    }

    private void iniciarSesion() {
        String email = etEmail.getText().toString().trim();
        String pwd = etContrasenya.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingrese un correo electrónico", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwd.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Intentando iniciar sesión con email: " + email);
        mAuthInicioSesion.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuthInicioSesion.getCurrentUser();
                        if (firebaseUser != null) {
                            SharedPreferences.Editor editor = estadoSesion.edit();
                            editor.putBoolean("rememberMe", chkMantenerSesion.isChecked());
                            editor.putString("userId", firebaseUser.getUid()); // Guardar UID
                            editor.apply();
                            Log.d(TAG, "Inicio de sesión exitoso. Guardando UID...");

                            obtenerDatosUsuario(firebaseUser.getUid());
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error al iniciar sesión: " + task.getException());
                    }
                });
    }

    private void obtenerDatosUsuario(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Usuarios").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Usuario usuario = documentSnapshot.toObject(Usuario.class);

                        if (usuario == null || usuario.getListaPerfiles() == null) {
                            Log.e(TAG, "Error: Usuario o lista de perfiles es null");
                            Toast.makeText(getApplicationContext(), "Error al recuperar usuario", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Log.d(TAG, "Usuario recuperado con éxito: " + usuario.getUserName());
                        abrirSeleccionPerfil(usuario);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error obteniendo datos: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Error obteniendo datos", Toast.LENGTH_SHORT).show();
                });
    }

    private void abrirSeleccionPerfil(Usuario usuario) {
        Intent intent = new Intent(getApplicationContext(), SeleccionPerfil.class);
        intent.putExtra("Usuario", usuario);
        startActivity(intent);
        finish();
    }
}
