package com.example.otakunikki.Actividades;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.otakunikki.Clases.Perfil;
import com.example.otakunikki.Clases.Usuario;
import com.example.otakunikki.R;

//FIREBASE
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ActividadRegistro extends AppCompatActivity {
    private String[] paises = {"--Seleccion un pais--", "España", "Estados Unidos", "Japón"};
    private Spinner spnRegion;
    private TextView tvPaisSeleccionado;
    private Button btnConfirmar;
    private ImageView imgIconoUser;
    private ImageButton imgBtnAgregar;
    private EditText etNombreCompleto, etNombreUsuario, etEmail, etPwd, etPwdConfirmacion;
    private CheckBox chkTerminos;
    private String fotoSeleccionada;

    // Declarar un ActivityResultLauncher
    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_registro);
        /**ATRIBUTOS PARA EL REGISTRO DE USUARIO**/
        imgIconoUser = findViewById(R.id.imgIconoUser);
        imgBtnAgregar = findViewById(R.id.imgBtnAgregar);
        etNombreCompleto = findViewById(R.id.etNombreCompleto);
        etNombreUsuario = findViewById(R.id.etNombreUsuario);
        etEmail = findViewById(R.id.etEmail);
        etPwd = findViewById(R.id.etPwd);
        etPwdConfirmacion = findViewById(R.id.etPwdConfirmacion);
        chkTerminos = findViewById(R.id.chkTerminos);

        spnRegion = findViewById(R.id.spnRegion);
        btnConfirmar = findViewById(R.id.btnConfirmar);
        tvPaisSeleccionado = findViewById(R.id.tvPaisSeleccionado);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paises);
        spnRegion.setAdapter(adapter);
        spnRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (paises[position].contentEquals("--Seleccion un pais--")) {
                    tvPaisSeleccionado.setText(paises[position]);
                }
                if (paises[position].contentEquals("España")) {
                    tvPaisSeleccionado.setText((paises[position]));
                }
                if (paises[position].contentEquals("Estados Unidos")) {
                    tvPaisSeleccionado.setText((paises[position]));
                }
                if (paises[position].contentEquals("Japón")) {
                    tvPaisSeleccionado.setText((paises[position]));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**OBJETOS PARA EL USO DE FIREBASE**/
        FirebaseAuth authRegistro = FirebaseAuth.getInstance();
        FirebaseFirestore baseDatos = FirebaseFirestore.getInstance();
        /***************************************/

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etNombreCompleto.getText().toString().isEmpty() || etNombreUsuario.getText().toString().isEmpty()
                        || etEmail.getText().toString().isEmpty() || etPwd.getText().toString().isEmpty()
                        || etPwdConfirmacion.getText().toString().isEmpty() || !chkTerminos.isChecked()) {

                    Toast.makeText(getApplicationContext(), "Rellena todos los campos", Toast.LENGTH_LONG).show();
                } else {
                    String email = etEmail.getText().toString();
                    String nombreCompleto = etNombreCompleto.getText().toString();
                    String nombreUsuario = etNombreUsuario.getText().toString();
                    if(etPwd.getText().toString().equals(etPwdConfirmacion.getText().toString())){
                        String pwd = etPwdConfirmacion.getText().toString();
                        authRegistro.createUserWithEmailAndPassword(email, pwd)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser = authRegistro.getCurrentUser();
                                        if (firebaseUser != null) {
                                            String idUsuario = firebaseUser.getUid();
                                            List<Perfil> listaPerfiles = new ArrayList<Perfil>();
                                            listaPerfiles.add(new Perfil());
                                            // Crear usuario en Firestore
                                            Usuario nuevoUsuario = new Usuario();
                                            baseDatos.collection("Usuarios").document(idUsuario)
                                                    .set(nuevoUsuario)
                                                    .addOnSuccessListener(aVoid -> {
                                                        // Usuario guardado con éxito en Firestore
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        // Error al guardar usuario
                                                    });
                                        }
                                    } else {
                                        // Error en la autenticación
                                    }
                                });
                        abrirSeleccion();
                    }else{
                        Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

    public void abrirSeleccion() {
        Intent intent = new Intent(getApplicationContext(), SeleccionPerfil.class);
        startActivity(intent);
    }
}
