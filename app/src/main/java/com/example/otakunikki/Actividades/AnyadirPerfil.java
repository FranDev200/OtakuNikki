package com.example.otakunikki.Actividades;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otakunikki.Clases.Perfil;
import com.example.otakunikki.Clases.Usuario;
import com.example.otakunikki.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class AnyadirPerfil extends AppCompatActivity {

    private final String TAG = "Añadir Perfil";
    private EditText etNombrePerfil;
    private Button btnConfirmarPerfil;
    private ImageView imgIconoPerfil;
    private String uriImagenPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anyadir_perfil);

        etNombrePerfil = findViewById(R.id.etNombrePerfil);
        btnConfirmarPerfil = findViewById(R.id.btnConfirmarPerfil);
        imgIconoPerfil = findViewById(R.id.imgIconoPerfil);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtener usuario actual
        FirebaseUser usuario = mAuth.getCurrentUser();

        imgIconoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        Object resourceId = imgIconoPerfil.getTag();
        Log.i("RECURSOIMAGEN", resourceId + "");


        btnConfirmarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usuario != null) {
                    String userId = usuario.getUid();

                    Perfil nuevoPerfil = new Perfil(etNombrePerfil.getText().toString(), uriImagenPerfil);

                    db.collection("Usuarios").document(userId)
                            .update("listaPerfiles", FieldValue.arrayUnion(nuevoPerfil))
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getApplicationContext(), "Perfil agregado correctamente", Toast.LENGTH_SHORT).show();

                                // Obtener datos actualizados del usuario antes de redirigir
                                db.collection("Usuarios").document(userId)
                                        .get()
                                        .addOnSuccessListener(documentSnapshot -> {
                                            Usuario usuarioActualizado = documentSnapshot.toObject(Usuario.class);
                                            if (usuarioActualizado != null) {
                                                Intent intent = new Intent(getApplicationContext(), SeleccionPerfil.class);
                                                intent.putExtra("Usuario", usuarioActualizado);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Error al cargar usuario", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(getApplicationContext(), "Error obteniendo usuario", Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getApplicationContext(), "Error al agregar perfil: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            uriImagenPerfil = selectedImageUri.toString();

            if (selectedImageUri != null) {
                imgIconoPerfil.setImageURI(selectedImageUri);

            }
        }
    }

}