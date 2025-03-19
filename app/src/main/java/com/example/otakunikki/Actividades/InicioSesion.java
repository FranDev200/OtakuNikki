package com.example.otakunikki.Actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.otakunikki.Clases.Traductor;
import com.example.otakunikki.Clases.Usuario;
import com.example.otakunikki.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class InicioSesion extends AppCompatActivity {

    private EditText etEmail, etContrasenya;
    private TextView tvRegistro;
    private Button btnInicioSesion;
    private CheckBox chkMantenerSesion;
    private FirebaseAuth mAuthInicioSesion;
    private ImageButton imgBtnRegistro, btnSeleccionIdi;
    private SharedPreferences estadoSesion;
    private static final String TAG = "InicioSesion";
    private String idioma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        SharedPreferences preferences = getSharedPreferences("Idiomas", MODE_PRIVATE);
        idioma = preferences.getString("idioma", "es");
        setLocale(idioma); // Aplica el idioma al inicio

        // Inicializar componentes de UI
        etEmail = findViewById(R.id.etEmailInicioSesion);
        tvRegistro = findViewById(R.id.tvRegistro);
        etContrasenya = findViewById(R.id.etPwdInicioSesion);
        btnInicioSesion = findViewById(R.id.BtnInicioSesion);
        chkMantenerSesion = findViewById(R.id.chkMantenerSesion);
        imgBtnRegistro = findViewById(R.id.imgBtnRegistro);
        btnSeleccionIdi = findViewById(R.id.btnSeleccionIdioma);
        TraduccionControles();

        btnSeleccionIdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarSelectorIdioma();
            }
        });



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

    private void setLocale(String idioma) {
        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.setLocale(locale);

        //getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }

    private void mostrarSelectorIdioma() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_seleccion_idioma, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        TextView tvSelecciona;
        ImageButton btnEspanol, btnIngles, btnJapones;

        tvSelecciona = view.findViewById(R.id.tvSeleccionaUnIdioma);
        btnEspanol = view.findViewById(R.id.btnEspanol);
        btnIngles = view.findViewById(R.id.btnIngles);
        btnJapones = view.findViewById(R.id.btnJapones);

        btnEspanol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarIdiomaApp("es");
                dialog.dismiss();
            }
        });

        btnIngles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarIdiomaApp("en");
                dialog.dismiss();
            }
        });

        btnJapones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarIdiomaApp("ja");
                dialog.dismiss();
            }
        });

        Traductor.traducirTexto(tvSelecciona.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvSelecciona.setText(textoTraducido);
            }
        });

        dialog.show();
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

                        if (usuario != null && usuario.getIdioma() != null) {
                            String idioma = usuario.getIdioma();
                            cambiarIdiomaApp(idioma);
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

    private void cambiarIdiomaApp(String idioma) {
        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.setLocale(locale);

        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        // Guardar el idioma en SharedPreferences para persistencia
        SharedPreferences.Editor editor = getSharedPreferences("Idiomas", MODE_PRIVATE).edit();
        editor.putString("idioma", idioma);
        editor.apply();
        // Reiniciar la actividad para aplicar los cambios
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    private void abrirSeleccionPerfil(Usuario usuario) {
        Intent intent = new Intent(getApplicationContext(), SeleccionPerfil.class);
        intent.putExtra("Usuario", usuario);
        startActivity(intent);
        finish();
    }

    private void TraduccionControles(){
        /**Traducimos los controloes**/
        Traductor.traducirTexto(etEmail.getHint().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                etEmail.setHint(textoTraducido);
            }
        });

        Traductor.traducirTexto(etContrasenya.getHint().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                etContrasenya.setHint(textoTraducido);
            }
        });

        Traductor.traducirTexto(chkMantenerSesion.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                chkMantenerSesion.setText(textoTraducido);
            }
        });

        Traductor.traducirTexto(btnInicioSesion.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                btnInicioSesion.setText(textoTraducido);
            }
        });

        Traductor.traducirTexto(tvRegistro.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvRegistro.setText(textoTraducido);
            }
        });
    }


}
