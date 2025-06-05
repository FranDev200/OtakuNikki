package com.example.otakunikki.Actividades;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otakunikki.Clases.Traductor;
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
    private Button btnConfirmarPerfil, btnCancelarPerfil;
    private TextView tvNuevoPefil;
    private ImageButton imgAgregarFoto;
    private String idioma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anyadir_perfil);

        SharedPreferences preferencesIdi = getSharedPreferences("Idiomas", MODE_PRIVATE);
        idioma = preferencesIdi.getString("idioma", "es");

        etNombrePerfil = findViewById(R.id.etNombrePerfil);
        btnConfirmarPerfil = findViewById(R.id.btnConfirmarPerfil);
        btnCancelarPerfil = findViewById(R.id.btnCancelarPerfil);
        imgAgregarFoto = findViewById(R.id.imgAgregarFoto);
        tvNuevoPefil = findViewById(R.id.tvTituloNuevoPerfil);

        TraduccionControles();

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
            secciones.put("One piece", Arrays.asList(R.drawable.luffychibi, R.drawable.zorochibi, R.drawable.namichibi, R.drawable.chopper, R.drawable.robbinchibi, R.drawable.lawchibi, R.drawable.doflamingochibi, R.drawable.corazonchibi));
            secciones.put("Jujutsu Kaisen", Arrays.asList(R.drawable.itadorichibi, R.drawable.nobarachibi, R.drawable.megumichibi, R.drawable.satoruchibi, R.drawable.tojichibi, R.drawable.kaisenchibi, R.drawable.sukunachibi, R.drawable.nanamichibi));
            secciones.put("Frieren", Arrays.asList(R.drawable.frierenchibi, R.drawable.fernchibi, R.drawable.starckchibi, R.drawable.himmelchibi, R.drawable.heiterchibi, R.drawable.eisenchibi));
            secciones.put("Haikyū", Arrays.asList(R.drawable.hinatachibi , R.drawable.tobiochibii, R.drawable.daichichibi, R.drawable.sugawarachibi, R.drawable.azumanechibi, R.drawable.nishinoyachibi, R.drawable.tanakachibi, R.drawable.tsukishimachibi, R.drawable.yamaguchichibi));
            secciones.put("Black Clover", Arrays.asList(R.drawable.astachibi, R.drawable.yunochibi, R.drawable.noellechibi, R.drawable.yamichibi, R.drawable.finralchibi, R.drawable.magnachibi, R.drawable.luckchibi, R.drawable.gauchechibi, R.drawable.vanessachibi, R.drawable.charmychibi, R.drawable.gordonchibi, R.drawable.greychibi));
            secciones.put("Kimetsu No Yaiba", Arrays.asList(R.drawable.tanjirochibi, R.drawable.nezukochibi, R.drawable.zenitsuchibi, R.drawable.inosukechibi, R.drawable.tomiokachibi, R.drawable.shionobuchibi, R.drawable.rengokuchibi, R.drawable.uzuichibi, R.drawable.tokitochibi, R.drawable.himejimachibi, R.drawable.igurochibi, R.drawable.sanemichibi));
            secciones.put("Naruto", Arrays.asList(R.drawable.narutochibi, R.drawable.sakurachibi, R.drawable.sasukechibi, R.drawable.shikamaruchibi, R.drawable.inochibi, R.drawable.akimichichibi, R.drawable.hyugachibi, R.drawable.aburamechibi, R.drawable.kibachibi, R.drawable.nejichibi, R.drawable.rockleechibi, R.drawable.tentenchibi));
            secciones.put("Dragon Ball Z", Arrays.asList(R.drawable.gokuchibi, R.drawable.gohanchibi, R.drawable.trunkschibi, R.drawable.vegetachibi, R.drawable.yamchachibi, R.drawable.tenchibi, R.drawable.kamichibi, R.drawable.androidechibi, R.drawable.cellchibi, R.drawable.gerochibi ));

            AdaptadorFilasImagenes seccionAdapter = new AdaptadorFilasImagenes(getApplicationContext(), secciones, imagenResId -> {
                imgAgregarFoto.setImageResource(imagenResId);
                imgAgregarFoto.setTag(imagenResId);
                popupWindow.dismiss();
            });

            rvSeleccionImagenes.setAdapter(seccionAdapter);

            popupWindow.setElevation(10f);
            popupWindow.showAtLocation(imgAgregarFoto, Gravity.CENTER, 0, 0);
        });

        btnCancelarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnConfirmarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usuario != null) {
                    String userId = usuario.getUid();
                    String nombrePerfil = etNombrePerfil.getText().toString().trim();

                    if (nombrePerfil.isEmpty()) {
                        Traductor.traducirTexto("El nombre del perfil no puede estar vacío", "es", idioma, new Traductor.TraduccionCallback() {
                            @Override
                            public void onTextoTraducido(String textoTraducido) {
                                Toast.makeText(getApplicationContext(), textoTraducido, Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }

                    db.collection("Usuarios").document(userId)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    Usuario usuarioActual = documentSnapshot.toObject(Usuario.class);
                                    if (usuarioActual != null) {
                                        List<Perfil> listaPerfiles = usuarioActual.getListaPerfiles();

                                        // Validar número máximo de perfiles (máximo 5)
                                        if (listaPerfiles.size() >= 5) {
                                            Traductor.traducirTexto("Número máximo de perfiles alcanzado (5)", "es", idioma, new Traductor.TraduccionCallback() {
                                                @Override
                                                public void onTextoTraducido(String textoTraducido) {
                                                    Toast.makeText(getApplicationContext(), textoTraducido, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            return;
                                        }

                                        // Verificar si el nombre ya existe
                                        for (Perfil perfil : listaPerfiles) {
                                            if (perfil.getNombrePerfil().equalsIgnoreCase(nombrePerfil)) {
                                                Traductor.traducirTexto("Perfil ya existe", "es", idioma, new Traductor.TraduccionCallback() {
                                                    @Override
                                                    public void onTextoTraducido(String textoTraducido) {
                                                        Toast.makeText(getApplicationContext(), textoTraducido, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                return;
                                            }
                                        }

                                        // Determinar imagen de perfil
                                        int fotoPerfil;
                                        if (imgAgregarFoto.getTag()== null) {
                                            fotoPerfil = R.drawable.luffychibi;
                                        } else {
                                            fotoPerfil = (int) imgAgregarFoto.getTag();
                                        }

                                        // Crear y agregar nuevo perfil
                                        Perfil nuevoPerfil = new Perfil(nombrePerfil, fotoPerfil);
                                        listaPerfiles.add(nuevoPerfil);

                                        // Guardar cambios en Firestore
                                        db.collection("Usuarios").document(userId)
                                                .update("listaPerfiles", listaPerfiles)
                                                .addOnSuccessListener(aVoid -> {
                                                    // Obtener datos actualizados del usuario antes de redirigir
                                                    db.collection("Usuarios").document(userId)
                                                            .get()
                                                            .addOnSuccessListener(updatedDocument -> {
                                                                Usuario usuarioActualizado = updatedDocument.toObject(Usuario.class);
                                                                if (usuarioActualizado != null) {
                                                                    Intent intent = new Intent(getApplicationContext(), SeleccionPerfil.class);
                                                                    intent.putExtra("Usuario", usuarioActualizado);
                                                                    startActivity(intent);
                                                                    finish();
                                                                } else {
                                                                    Log.i("ERROR USUARIO", "Error al cargar usuario");
                                                                }
                                                            })
                                                            .addOnFailureListener(e ->
                                                                    Log.i("ERROR USUARIO", "Error al obtener usuario")
                                                            );
                                                })
                                                .addOnFailureListener(e ->
                                                                Log.i("ERROR USUARIO", "Error al obtener usuario " + e.getMessage())
                                                );
                                    }
                                }
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(getApplicationContext(), "Error obteniendo usuario", Toast.LENGTH_SHORT).show()
                            );
                } else {
                    Toast.makeText(getApplicationContext(), "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
    private void TraduccionControles(){
        Traductor.traducirTexto(tvNuevoPefil.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvNuevoPefil.setText(textoTraducido);
            }
        });
        Traductor.traducirTexto(etNombrePerfil.getHint().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                etNombrePerfil.setHint(textoTraducido);
            }
        });
        Traductor.traducirTexto(btnConfirmarPerfil.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                btnConfirmarPerfil.setText(textoTraducido);
            }
        });
        Traductor.traducirTexto(btnCancelarPerfil.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                btnCancelarPerfil.setText(textoTraducido);
            }
        });
    }
}