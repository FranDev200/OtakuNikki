package com.example.otakunikki.Foro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.example.otakunikki.Clases.HiloForo;
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

import java.util.ArrayList;
import java.util.List;

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
    private String userID;
    private ListenerRegistration refrescoForo;
    private String mensaje;
    private String botonNegativo;
    private String botonPositivo;
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

        SharedPreferences identificadorUsuario =requireContext().getSharedPreferences("IDUSUARIO", Context.MODE_PRIVATE);
        userID = identificadorUsuario.getString("ID", "Perfil no encontrado");

        Traductor.traducirTexto("Borrar el hilo:", "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                mensaje = textoTraducido;
            }
        });
        Traductor.traducirTexto("Cancelar", "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                botonNegativo = textoTraducido;
            }
        });
        Traductor.traducirTexto("Aceptar", "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                botonPositivo = textoTraducido;
            }
        });

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

        /**Para poder abrir el hilo hacer click corto**/
        lvForo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hilo = listaForo.get(position);
                Intent intent = new Intent(getActivity(), ActividadDetalleHilo.class);
                intent.putExtra("HiloForo", hilo);
                startActivity(intent);

            }
        });

        /**Para poder eliminar el hilo hacer click largo**/
        lvForo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                eliminar(position);
                return true;
            }
        });

        /**METODO PARA CARGAR LOS HILOS DEL FORO**/
        //CargarDatos();
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

    private void eliminar(int posicion) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        builder.setTitle(mensaje+ " " + listaForo.get(posicion).getTitulo())
                .setIcon(R.drawable.eliminar);

        builder.setNegativeButton(botonNegativo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setPositiveButton(botonPositivo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EliminarFBForo(listaForo.get(posicion).getIdHilo());
                adaptadorforo.notifyDataSetChanged();

            }
        });

        builder.create();
        builder.show();
    }

    private void EliminarFBForo(String idHilo) {
        db.collection("Hilos").document(idHilo).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        HiloForo hiloborrar = documentSnapshot.toObject(HiloForo.class);
                        if (hiloborrar != null) {
                            // Eliminar de lista local si es necesario
                            if(hiloborrar.getUserID().equals(userID) && hiloborrar.getUsuario().equals(nombrePerfil))
                            {
                                listaForo.removeIf(hilo -> hilo.getIdHilo().equals(hiloborrar.getIdHilo()));
                                // Eliminar el documento de Firestore
                                db.collection("Hilos").document(idHilo)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> {
                                            Traductor.traducirTexto("Foro eliminado correctamente", "es", idioma, new Traductor.TraduccionCallback() {
                                                @Override
                                                public void onTextoTraducido(String textoTraducido) {
                                                    Toast.makeText(requireContext(),textoTraducido , Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(requireContext(), "Error al eliminar el foro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }
                            else
                            {
                                Toast.makeText(requireContext(), "No puedes eliminar un comentario que no es tuyo", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Error al obtener foro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void AgregarHiloForo(String tituloForo, String mensajeForo ,FirebaseUser usuario, String nombrePerfil) {
        if (usuario == null) {
            Toast.makeText(getActivity(), "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HiloForo hilo = new HiloForo(nombrePerfil, tituloForo, Timestamp.now(), mensajeForo, idioma, userID);

        db.collection("Hilos")
                .add(hilo)
                .addOnSuccessListener(documentReference -> {
                    String idHilo = documentReference.getId();
                    hilo.setIdHilo(idHilo); // en memoria

                    // Actualizar el campo idHilo dentro del documento ya creado
                    documentReference.update("idHilo", idHilo)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firestore", "idHilo actualizado correctamente");

                                //listaForo.add(hilo);
                                //adaptadorforo.notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> Log.w("Firestore", "Error actualizando idHilo", e));
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error al crear hilo", e);
                });
    }


    private void HiloTiempoReal() {
        refrescoForo = db.collection("Hilos")
                .orderBy("fecha", Query.Direction.DESCENDING)
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        Log.w("Firestore", "Error al escuchar colección.", e);
                        return;
                    }

                    if (querySnapshot != null) {
                        listaForo.clear();
                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            HiloForo hiloAux = doc.toObject(HiloForo.class);
                            if (hiloAux != null) {
                                if(hiloAux.getIdHilo() == null)
                                {
                                    hiloAux.setIdHilo(doc.getId());
                                }
                                listaForo.add(hiloAux);
                            }
                        }
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


}