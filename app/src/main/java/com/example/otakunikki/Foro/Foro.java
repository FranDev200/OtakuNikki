package com.example.otakunikki.Foro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otakunikki.Actividades.ActividadVistaDetalleListaAnime;
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
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Foro extends Fragment {

    private List<HiloForo> listaForo;
    private HiloForo hilo;
    private AdaptadorForo adaptadorforo;
    private ListView lvForo;
    private FloatingActionButton btnAgregarHilo;
    private FirebaseUser usuario;
    private String nombrePerfil;
    private FirebaseFirestore db;
    private String idioma;
    private TextView tvForo;

    private ListenerRegistration refrescoForo;

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

        tvForo = vista.findViewById(R.id.tvTitulo);
        Traductor.traducirTexto(tvForo.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvForo.setText(textoTraducido);
            }
        });
        btnAgregarHilo = vista.findViewById(R.id.btnAgregarHilo);
        lvForo = vista.findViewById(R.id.lvForoComunidad);

        listaForo = new ArrayList<>();

        adaptadorforo = new AdaptadorForo(listaForo, requireContext(), idioma);
        lvForo.setAdapter(adaptadorforo);

        lvForo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hilo = listaForo.get(position);
                Intent intent = new Intent(getActivity(), ActividadDetalleHilo.class);
                intent.putExtra("HiloForo", hilo);
                startActivity(intent);

            }
        });

        /**METODO PARA CARGAR LOS HILOS DEL FORO**/
        CargarDatos();
        HiloTiempoReal();


        btnAgregarHilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflar el diseño personalizado del diálogo
                LayoutInflater inflater1 = LayoutInflater.from(getActivity());
                View view = inflater1.inflate(R.layout.item_ad_agregar_hilo, null);

                EditText etTituloHilo = view.findViewById(R.id.etTituloHilo);
                EditText etMensajeForo = view.findViewById(R.id.etMensajeForo);
                TextView tvTituloHilo = view.findViewById(R.id.tvTituloHilo);
                TextView tvComentarioHilo = view.findViewById(R.id.tvInfoParaMensaje);
                Button btnPublicarHilo = view.findViewById(R.id.btnPublicarHilo);
                Button btnCancelarPublicacion = view.findViewById(R.id.btnCancelarPublicacion);

                //TRADUCIR CONTROLES YA DEFINIDOS
                Traductor.traducirTexto(etTituloHilo.getHint().toString(), "es", idioma, new Traductor.TraduccionCallback() {
                    @Override
                    public void onTextoTraducido(String textoTraducido) {
                        etTituloHilo.setHint(textoTraducido);
                    }
                });
                Traductor.traducirTexto(etMensajeForo.getHint().toString(), "es", idioma, new Traductor.TraduccionCallback() {
                    @Override
                    public void onTextoTraducido(String textoTraducido) {
                        etMensajeForo.setHint(textoTraducido);
                    }
                });
                Traductor.traducirTexto(tvTituloHilo.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
                    @Override
                    public void onTextoTraducido(String textoTraducido) {
                        tvTituloHilo.setText(textoTraducido);
                    }
                });
                Traductor.traducirTexto(tvComentarioHilo.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
                    @Override
                    public void onTextoTraducido(String textoTraducido) {
                        tvComentarioHilo.setText(textoTraducido);
                    }
                });
                Traductor.traducirTexto(btnPublicarHilo.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
                    @Override
                    public void onTextoTraducido(String textoTraducido) {
                        btnPublicarHilo.setText(textoTraducido);
                    }
                });

                Traductor.traducirTexto(btnCancelarPublicacion.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
                    @Override
                    public void onTextoTraducido(String textoTraducido) {
                        btnCancelarPublicacion.setText(textoTraducido);
                    }
                });

                // Crear el AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);

                AlertDialog dialog = builder.create();
                dialog.show();

                btnPublicarHilo.setOnClickListener(v1 -> {
                    String tituloHilo = etTituloHilo.getText().toString().toUpperCase();
                    String mensajeForo = etMensajeForo.getText().toString();
                    if (!tituloHilo.isEmpty() && !mensajeForo.isEmpty()) {
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
        HiloForo hilo = new HiloForo(nombrePerfil, tituloForo, Timestamp.now(), mensajeForo, idioma);

        db.collection("Hilos")
                .add(hilo)
                .addOnSuccessListener(documentReference -> {
                    String idHilo = documentReference.getId();
                    hilo.setIdHilo(idHilo); // en memoria

                    // Actualizar el campo idHilo dentro del documento ya creado
                    documentReference.update("idHilo", idHilo)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firestore", "idHilo actualizado correctamente");

                                listaForo.add(hilo);
                                adaptadorforo.notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> Log.w("Firestore", "Error actualizando idHilo", e));
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error al crear hilo", e);
                });
    }


    private void CargarDatos() {
        db.collection("Hilos")
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

    private void HiloTiempoReal() {
        if (hilo == null || hilo.getIdHilo() == null) {
            Log.e("Firestore", "El hilo es null o no tiene ID. No se puede escuchar.");
            return;
        }
        refrescoForo = db.collection("Hilos")
                .document(hilo.getIdHilo())
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        Log.w("Firestore", "Error al escuchar cambios.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        HiloForo hiloActualizado = documentSnapshot.toObject(HiloForo.class);
                        if (hiloActualizado != null) {
                            List<HiloForo> respuestas = hiloActualizado.getRespuestas();
                            listaForo.clear();
                            if (respuestas != null) {
                                listaForo.addAll(respuestas);
                            }
                            adaptadorforo.notifyDataSetChanged();
                        }
                    } else {
                        Log.d("Firestore", "Documento no existe o es null");
                        listaForo.clear();
                        adaptadorforo.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (refrescoForo != null) {
            refrescoForo.remove();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (refrescoForo != null) {
            refrescoForo.remove();
        }
    }
}