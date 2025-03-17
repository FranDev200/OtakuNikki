package com.example.otakunikki.Actividades;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otakunikki.GestionImagenes.AdaptadorFilasImagenes;
import com.example.otakunikki.Clases.Perfil;
import com.example.otakunikki.Clases.Usuario;
import com.example.otakunikki.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnyadirPerfil extends AppCompatActivity {

    private final String TAG = "Añadir Perfil";
    private EditText etNombrePerfil;
    private Button btnConfirmarPerfil;

    private ImageView imgAgregarFoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anyadir_perfil);

        etNombrePerfil = findViewById(R.id.etNombrePerfil);
        btnConfirmarPerfil = findViewById(R.id.btnConfirmarPerfil);
        imgAgregarFoto = findViewById(R.id.imgAgregarFoto);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtener usuario actual
        FirebaseUser usuario = mAuth.getCurrentUser();

        imgAgregarFoto.setOnClickListener(v -> {
            View popupView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_seleccion_imagenes, null);
            PopupWindow popupWindow = new PopupWindow(popupView, RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT, true);

            RecyclerView rvSeleccionImagenes = popupView.findViewById(R.id.rvSeleccionImagenes);
            rvSeleccionImagenes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            // Creo la lista de imágenes para los diferentes animes
            Map<String, List<Integer>> secciones = new LinkedHashMap<>(); //Ordeno por orden de inserccion en el mapa para que sea más fácil de ver
            secciones.put("One piece", Arrays.asList(R.drawable.luffychibi, R.drawable.zorochibi, R.drawable.namichibi));
            secciones.put("Jujutsu Kaisen", Arrays.asList(R.drawable.itadorichibi, R.drawable.satoruchibi));
            secciones.put("Frieren", Arrays.asList(R.drawable.frierenchibi, R.drawable.fernchibi, R.drawable.himmelchibi));
            secciones.put("Haikyū", Arrays.asList(R.drawable.tobiochibi, R.drawable.shoyochibi));

            AdaptadorFilasImagenes seccionAdapter = new AdaptadorFilasImagenes(getApplicationContext(), secciones, imagenResId -> {
                imgAgregarFoto.setImageResource(imagenResId);
                imgAgregarFoto.setTag(imagenResId);
                popupWindow.dismiss();
            });

            rvSeleccionImagenes.setAdapter(seccionAdapter);

            popupWindow.setElevation(10f);
            popupWindow.showAtLocation(imgAgregarFoto, Gravity.CENTER, 0, 0);
        });



        btnConfirmarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usuario != null) {
                    String userId = usuario.getUid();
                    int fotoPerfil = (int) imgAgregarFoto.getTag();
                    Perfil nuevoPerfil = new Perfil(etNombrePerfil.getText().toString(), fotoPerfil);

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
}