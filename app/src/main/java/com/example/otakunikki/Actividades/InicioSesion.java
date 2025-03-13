package com.example.otakunikki.Actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;  // Importamos Log para depuración
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

    private static final String TAG = "InicioSesion";  // Para logs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        // Inicializar FirebaseAuth
        mAuthInicioSesion = FirebaseAuth.getInstance();
        FirebaseUser usuario = mAuthInicioSesion.getCurrentUser();

        // Inicializar SharedPreferences
        estadoSesion = getSharedPreferences("PreferenciaSesion", MODE_PRIVATE);

        // Verificar si el usuario eligió "Recordarme"
        boolean recuerdame = estadoSesion.getBoolean("Recuerdame", false);

        Log.d(TAG, "Estado 'Recuerdame': " + recuerdame);  // Log para depuración
        Log.i("USUSARIO",usuario.getEmail()+"");
        /*
        // Si el usuario ya está autenticado y eligió "Recordarme", ir directamente a SeleccionPerfil
        if (recuerdame && usuario != null) {
            Log.d(TAG, "Usuario autenticado y 'Recordarme' seleccionado. Redirigiendo a SeleccionPerfil...");
            startActivity(new Intent(this, SeleccionPerfil.class));
            finish(); // Terminar la actividad actual
            return;  // Importante para evitar que el resto del código se ejecute
        } else {
            Log.d(TAG, "No autenticado o 'Recordarme' no seleccionado. Continuando en InicioSesion.");
        }*/

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
        Log.d(TAG, "Intentando iniciar sesión con email: " + email);
        mAuthInicioSesion.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuthInicioSesion.getCurrentUser();
                        if (firebaseUser != null) {
                            // Si inicio de sesión es exitoso, guardar el estado de "Recuerdame"
                             /*SharedPreferences.Editor editor = estadoSesion.edit();
                            editor.putBoolean("Recuerdame", chkMantenerSesion.isChecked());
                            editor.apply(); // No olvides aplicar los cambios
                            */
                            String userId = firebaseUser.getUid();
                            obtenerDatosUsuario(userId);  // Llamamos a la función para recuperar datos
                            Log.d(TAG, "Inicio de sesión exitoso. Guardando preferencias...");

                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // 🔍 Obtener usuario de Firestore
    private void obtenerDatosUsuario(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Usuarios").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Usuario usuario = documentSnapshot.toObject(Usuario.class);
                        if (usuario != null) {
                            abrirSeleccionPerfil(usuario);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Error obteniendo datos", Toast.LENGTH_SHORT).show();
                });
    }

    // 📲 Abrir Selección de Perfil con el usuario
    private void abrirSeleccionPerfil(Usuario usuario) {
        Intent intent = new Intent(getApplicationContext(), SeleccionPerfil.class);
        intent.putExtra("Usuario", usuario);
        startActivity(intent);
        finish(); // Finalizar la actividad actual para evitar que el usuario regrese a la pantalla de inicio sesión
    }
}
