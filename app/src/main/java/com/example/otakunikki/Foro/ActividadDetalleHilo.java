package com.example.otakunikki.Foro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otakunikki.Clases.HiloForo;
import com.example.otakunikki.Clases.Traductor;
import com.example.otakunikki.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ActividadDetalleHilo extends AppCompatActivity {
    private TextView tvTitulo, tvUsuario, tvFecha, tvComentario, tvComentarioTitulo;
    private ListView rvRespuestas;
    private Button btnResponder;
    private ImageButton btnFlechaAtras;

    private HiloForo hiloPrincipal;
    private FirebaseFirestore db;
    private List<HiloForo> listaRespuestas;
    private AdaptadorRespuestasHilo adaptador;

    private String nombrePerfil;
    private String idioma;

    private ListenerRegistration refrescoForo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_detalle_hilo);


        // Recuperar el nombre del perfil desde SharedPreferences
        SharedPreferences preferences = getSharedPreferences("NombrePerfil", Context.MODE_PRIVATE);
        nombrePerfil = preferences.getString("PerfilSeleccionado", "Perfil no encontrado");

        SharedPreferences infoIdioma = getSharedPreferences("Idiomas", Context.MODE_PRIVATE);
        idioma = infoIdioma.getString("idioma", "es");

        ImageButton imgRetroceso = findViewById(R.id.imgRetroceso);
        imgRetroceso.setOnClickListener(v -> getSupportFragmentManager().popBackStack());


        btnFlechaAtras = findViewById(R.id.imgRetroceso);
        tvTitulo = findViewById(R.id.tvTituloHiloDetalle);
        tvUsuario = findViewById(R.id.tvUsuarioHiloDetalle);
        tvFecha = findViewById(R.id.tvFechaHiloDetalle);
        tvComentarioTitulo = findViewById(R.id.tvComentarioTitulo);
        tvComentario = findViewById(R.id.tvComentarioHiloDetalle);
        rvRespuestas = findViewById(R.id.rvRespuestas);
        btnResponder = findViewById(R.id.btnResponder);

        db = FirebaseFirestore.getInstance();
        listaRespuestas = new ArrayList<>();
        hiloPrincipal = getIntent().getParcelableExtra("HiloForo");
        if (hiloPrincipal.getRespuestas() != null)
        {
            listaRespuestas.addAll(hiloPrincipal.getRespuestas());
        }
        adaptador = new AdaptadorRespuestasHilo(listaRespuestas, this, idioma);
        rvRespuestas.setAdapter(adaptador);

        mostrarHiloPrincipal();
        escucharRespuestasTiempoReal();

        btnFlechaAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnResponder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MostrarDialogoRespuesta();
            }
        });


    }

    private void mostrarHiloPrincipal() {
        Traductor.traducirTexto(tvComentarioTitulo.getText().toString(), hiloPrincipal.getIdioma(), idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvComentarioTitulo.setText(textoTraducido);
            }
        });
        Traductor.traducirTexto(btnResponder.getText().toString(), hiloPrincipal.getIdioma(), idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                btnResponder.setText(textoTraducido);
            }
        });
        Traductor.traducirTexto(hiloPrincipal.getTitulo(), hiloPrincipal.getIdioma(), idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvTitulo.setText(textoTraducido);
            }
        });
        tvUsuario.setText(hiloPrincipal.getUsuario());
        tvFecha.setText(convertirTimestampAFecha(hiloPrincipal.getFecha()));
        Traductor.traducirTexto(hiloPrincipal.getComentario(), hiloPrincipal.getIdioma(), idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvComentario.setText(textoTraducido);
            }
        });
    }
    
    private void MostrarDialogoRespuesta() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.item_ad_agregar_hilo, null);
        EditText etTitulo = dialogView.findViewById(R.id.etTituloHilo);
        EditText etMensaje = dialogView.findViewById(R.id.etMensajeForo);
        TextView tvTitulo = dialogView.findViewById(R.id.tvTituloHilo);
        TextView tvMensaje = dialogView.findViewById(R.id.tvInfoParaMensaje);
        Button btnAceptar = dialogView.findViewById(R.id.btnPublicarHilo);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelarPublicacion);

        tvTitulo.setText("Titulo del hilo: " + hiloPrincipal.getTitulo());
        tvMensaje.setText("Introduce respuesta");
        etTitulo.setVisibility(View.GONE); // no es necesario para respuestas

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setView(dialogView);
        final androidx.appcompat.app.AlertDialog dialog = builder.create();

        btnAceptar.setOnClickListener(v -> {
            String comentario = etMensaje.getText().toString();
            if (!comentario.isEmpty()) {
                agregarRespuesta(comentario);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Comentario vacío", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        dialog.show(); // Mostrar solo una vez
    }

    private void agregarRespuesta(String comentario) {
        // Obtener la lista de perfiles del usuario en Firestore
        db.collection("Hilos").document(hiloPrincipal.getIdHilo()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                HiloForo hilos = documentSnapshot.toObject(HiloForo.class);
                if (hilos != null) {

                    listaRespuestas.add(new HiloForo(nombrePerfil, null, Timestamp.now(), comentario, idioma));

                    // Actualizar en Firestore
                    db.collection("Hilos").document(hiloPrincipal.getIdHilo())
                            .update("respuestas", listaRespuestas)
                            .addOnSuccessListener(aVoid -> {

                                adaptador.notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getApplicationContext(), "Error al introducir respuesta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Error al obtener hilo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void escucharRespuestasTiempoReal() {
        refrescoForo = db.collection("Hilos")
                .document(hiloPrincipal.getIdHilo())
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        Log.w("Firestore", "Error al escuchar cambios.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        HiloForo hiloActualizado = documentSnapshot.toObject(HiloForo.class);
                        if (hiloActualizado != null) {
                            List<HiloForo> nuevasRespuestas = hiloActualizado.getRespuestas();
                            listaRespuestas.clear();
                            if (nuevasRespuestas != null) {
                                listaRespuestas.addAll(nuevasRespuestas);
                            }
                            adaptador.notifyDataSetChanged();
                        }
                    }
                });
    }

    private String convertirTimestampAFecha(com.google.firebase.Timestamp timestamp) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(timestamp.toDate());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (refrescoForo != null) {
            refrescoForo.remove();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (refrescoForo != null) {
            refrescoForo.remove();
        }
    }
}
