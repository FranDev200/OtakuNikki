package com.example.otakunikki.Foro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.otakunikki.Clases.HiloForo;
import com.example.otakunikki.Clases.ListaAnime;
import com.example.otakunikki.Clases.Perfil;
import com.example.otakunikki.Clases.Traductor;
import com.example.otakunikki.Clases.Usuario;
import com.example.otakunikki.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Foro extends Fragment {

    List<HiloForo> listaForo;
    AdaptadorForo adaptadorforo;
    private ListView lvForo;
    private FloatingActionButton btnAgregarHilo;
    private FirebaseUser usuario;
    private String nombrePerfil;
    private FirebaseFirestore db;
    private String idioma;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_foro, container, false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Obtener usuario actual
        usuario = mAuth.getCurrentUser();

        // Recuperar el nombre del perfil desde SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("NombrePerfil", Context.MODE_PRIVATE);
        nombrePerfil = preferences.getString("PerfilSeleccionado", "Perfil no encontrado");

        SharedPreferences infoIdioma = requireContext().getSharedPreferences("Idiomas", Context.MODE_PRIVATE);
        idioma = infoIdioma.getString("idioma", "es");


        btnAgregarHilo = vista.findViewById(R.id.btnAgregarHilo);
        lvForo = vista.findViewById(R.id.lvForoComunidad);

        listaForo = new ArrayList<>();
        //HiloForo hilo = new HiloForo("Mario", "ONE PIECE", currentDateTime, "Opiniones sobre el ultimo capitulo de manga de one piece?");
        //listaForo.add(hilo);
        adaptadorforo = new AdaptadorForo(listaForo, requireContext());
        lvForo.setAdapter(adaptadorforo);

        /**METODO PARA CARGAR LOS HILOS DEL FORO**/
        CargarDatos();

        btnAgregarHilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflar el diseño personalizado del diálogo
                LayoutInflater inflater1 = LayoutInflater.from(getActivity());
                View view = inflater1.inflate(R.layout.item_ad_agregar_hilo, null);

                EditText etTituloHilo = view.findViewById(R.id.etTituloHilo);
                EditText etMensajeForo = view.findViewById(R.id.etMensajeForo);
                Button btnPublicarHilo = view.findViewById(R.id.btnPublicarHilo);
                Button btnCancelarPublicacion = view.findViewById(R.id.btnCancelarPublicacion);

                /**TRADUCIR CONTROLES YA DEFINIDOS
                Traductor.traducirTexto(etNombreLista.getHint().toString(), "es", idioma, new Traductor.TraduccionCallback() {
                    @Override
                    public void onTextoTraducido(String textoTraducido) {
                        etNombreLista.setHint(textoTraducido);
                    }
                });

                Traductor.traducirTexto(btnAceptarLista.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
                    @Override
                    public void onTextoTraducido(String textoTraducido) {
                        btnAceptarLista.setText(textoTraducido);
                    }
                });

                Traductor.traducirTexto(btnCancelarLista.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
                    @Override
                    public void onTextoTraducido(String textoTraducido) {
                        btnCancelarLista.setText(textoTraducido);
                    }
                });**/

                // Crear el AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);

                AlertDialog dialog = builder.create();
                dialog.show();

                btnPublicarHilo.setOnClickListener(v1 -> {
                    String tituloHilo = etTituloHilo.getText().toString().toUpperCase();
                    String mensajeForo = etMensajeForo.getText().toString();
                    if (!tituloHilo.isEmpty() && !mensajeForo.isEmpty()) {
                        Toast.makeText(getActivity().getApplicationContext(), "Prueba Validacion", Toast.LENGTH_SHORT).show();
                        AgregarHiloForo(tituloHilo, mensajeForo, usuario,nombrePerfil);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Debe rellenar los campos", Toast.LENGTH_SHORT).show();
                    }
                });
                btnCancelarPublicacion.setOnClickListener(v1 -> dialog.dismiss());
            }
        });

        return vista;
    }

    private void AgregarHiloForo(String tituloForo, String mensajeForo ,FirebaseUser usuario, String nombrePerfil) {
        if (usuario == null) {
            Toast.makeText(getActivity(), "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HiloForo hilo = new HiloForo(nombrePerfil, tituloForo, Timestamp.now(), mensajeForo);

        db.collection("hilos")
                .add(hilo)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "Hilo creado con ID: " + documentReference.getId());
                    listaForo.add(hilo);
                    adaptadorforo.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error al crear hilo", e);
                });

    }

    private void CargarDatos() {
        db.collection("hilos")
                .orderBy("fecha", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(hiloDocs -> {
                    listaForo.clear();
                    for (DocumentSnapshot hiloDoc : hiloDocs) {
                        HiloForo hilo = hiloDoc.toObject(HiloForo.class);
                        listaForo.add(hilo);
                    }
                    adaptadorforo.notifyDataSetChanged();
                });
    }
}
/**ESTO ES PARA LEER LAS RESPUESTA EN OTRA ACTIVIDAD
 db.collection("hilos")
 .document(hiloId)
 .collection("respuestas")
 .orderBy("fecha", Query.Direction.ASCENDING)
 .get()
 .addOnSuccessListener(respDocs -> {

 for (DocumentSnapshot respDoc : respDocs) {
 HiloForo respuesta = respDoc.toObject(HiloForo.class);

 }
 adaptadorforo.notifyDataSetChanged();
 //hilo.setRespuestas(respuestas); // aquí usas tu clase sin tocarla
 //Log.d("Firestore", "Hilo con respuestas: " + hilo.getTitulo() + " tiene " + respuestas.size() + " respuestas");
 });**/

/*
        db.collection("foro")
                .document(publicacionId)
                .collection("comentarios")
                .orderBy("fechaHora", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<HiloForo> listaComentarios = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        HiloForo comentario = doc.toObject(HiloForo.class);
                        listaComentarios.add(comentario);
                    }

                    // Aquí actualizas tu RecyclerView o lista
                });
        */