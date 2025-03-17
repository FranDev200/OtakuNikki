package com.example.otakunikki.Fragmentos;

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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otakunikki.Actividades.ActividadVistaDetalleListaAnime;
import com.example.otakunikki.Actividades.SeleccionPerfil;
import com.example.otakunikki.Adaptadores.AdaptadorListas;
import com.example.otakunikki.Clases.Anime;
import com.example.otakunikki.Clases.ListaAnime;
import com.example.otakunikki.Clases.Perfil;
import com.example.otakunikki.Clases.Usuario;
import com.example.otakunikki.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class FragmentoListas extends Fragment {

    List<ListaAnime> lista_de_listasAnimes;
    AdaptadorListas miAdaptador;
    ListaAnime listaSeleccionada;
    private ListView miListView;
    private TextView tvNroListas;
    private ImageButton imgBtnAgregarLista;
    private ListaAnime listaAEliminar = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragmento_listas, container, false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtener usuario actual
        FirebaseUser usuario = mAuth.getCurrentUser();

        // Recuperar el nombre del perfil desde SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("NombrePerfil", Context.MODE_PRIVATE);
        String nombrePerfil = preferences.getString("PerfilSeleccionado", "Perfil no encontrado");

        tvNroListas = vista.findViewById(R.id.tvNroListas);
        miListView = vista.findViewById(R.id.lvListasAnimes);
        imgBtnAgregarLista = vista.findViewById(R.id.imgBtnAgregarLista);

        lista_de_listasAnimes = new ArrayList<>();
        miAdaptador = new AdaptadorListas(getActivity().getApplicationContext(), lista_de_listasAnimes);
        miListView.setAdapter(miAdaptador);

        CargarDatos(usuario, db, nombrePerfil);

        miListView.setOnItemClickListener((parent, view, position, id) -> {
            listaSeleccionada = lista_de_listasAnimes.get(position);
            Intent intent = new Intent(requireContext(), ActividadVistaDetalleListaAnime.class);
            intent.putExtra("ListaAnimeSeleccionada", listaSeleccionada);
            startActivity(intent);
        });

        miListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
                listaSeleccionada = lista_de_listasAnimes.get(position);
                String nombreLista = listaSeleccionada.getNombreLista();
                builder.setTitle("¿Estás seguro de borrar la lista " +  nombreLista + "?\n")
                        .setIcon(R.drawable.eliminar);


                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        Toast.makeText(requireContext(), "Has elegido no borrar", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EliminarLista(nombreLista, usuario, nombrePerfil);
                    }
                });

                builder.create();
                builder.show();
                return true;
            }
        });

        imgBtnAgregarLista.setOnClickListener(v -> {
            // Inflar el diseño personalizado del diálogo
            LayoutInflater inflater1 = LayoutInflater.from(getActivity());
            View view = inflater1.inflate(R.layout.alert_personalizado, null);

            EditText etNombreLista = view.findViewById(R.id.etNombreLista);
            Button btnAceptarLista = view.findViewById(R.id.btnAceptarLista);
            Button btnCancelarLista = view.findViewById(R.id.btnCancelarLista);

            // Crear el AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(view);

            AlertDialog dialog = builder.create();
            dialog.show();

            btnAceptarLista.setOnClickListener(v1 -> {
                String nombreLista = etNombreLista.getText().toString().trim();
                if (!nombreLista.isEmpty()) {
                    AgregarListaAnime(nombreLista, usuario, nombrePerfil);

                    dialog.dismiss();
                } else {
                    etNombreLista.setError("El nombre no puede estar vacío");
                }
            });
            btnCancelarLista.setOnClickListener(v1 -> dialog.dismiss());
        });

        return vista;
    }

    private void EliminarLista(String nombreLista, FirebaseUser usuario, String nombrePerfil) {
        if (usuario != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userId = usuario.getUid();

            db.collection("Usuarios").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Usuario usuarioActual = documentSnapshot.toObject(Usuario.class);
                            if (usuarioActual != null) {
                                List<Perfil> listaPerfiles = usuarioActual.getListaPerfiles();
                                for (Perfil perfil : listaPerfiles) {
                                    if (perfil.getNombrePerfil().equals(nombrePerfil)) {
                                        List<ListaAnime> listasAnimes = perfil.getListasAnimes();


                                        for (ListaAnime lista : listasAnimes) {
                                            if (lista.getNombreLista().equals(nombreLista)) {
                                                listaAEliminar = lista;
                                                break;
                                            }
                                        }

                                        if(listaAEliminar.getNombreLista().equalsIgnoreCase("Favoritos")){
                                            Toast.makeText(getActivity().getApplicationContext(), "Esta lista no se puede eliminar", Toast.LENGTH_SHORT).show();
                                            return;
                                        }else{
                                            if (listaAEliminar != null) {
                                                listasAnimes.remove(listaAEliminar); // Eliminar de Firestore

                                                db.collection("Usuarios").document(userId)
                                                        .update("listaPerfiles", listaPerfiles)
                                                        .addOnSuccessListener(aVoid -> {
                                                            Toast.makeText(getActivity(), "Lista eliminada correctamente", Toast.LENGTH_SHORT).show();

                                                            // Eliminar de la lista local
                                                            lista_de_listasAnimes.remove(listaAEliminar);

                                                            // Recargar datos desde Firebase para asegurar sincronización
                                                            CargarDatos(usuario, db, nombrePerfil);
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Toast.makeText(getActivity(), "Error al eliminar lista: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        });
                                            } else {
                                                Toast.makeText(getActivity(), "Lista no encontrada", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        return;
                                    }
                                }
                                Toast.makeText(getActivity(), "Perfil no encontrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Error obteniendo usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getActivity(), "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
        }
    }

    private void AgregarListaAnime(String nombreLista, FirebaseUser usuario, String nombrePerfil) {
        if (usuario != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userId = usuario.getUid();

            db.collection("Usuarios").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Usuario usuarioActual = documentSnapshot.toObject(Usuario.class);
                            if (usuarioActual != null) {
                                // Buscar el perfil que coincida con el nombre seleccionado
                                List<Perfil> listaPerfiles = usuarioActual.getListaPerfiles();
                                for (Perfil perfil : listaPerfiles) {
                                    if (perfil.getNombrePerfil().equals(nombrePerfil)) {

                                        // Verificar si ya existe una lista con el mismo nombre
                                        boolean listaExiste = false;
                                        for (ListaAnime lista : perfil.getListasAnimes()) {
                                            if (lista.getNombreLista().equalsIgnoreCase(nombreLista)) {
                                                listaExiste = true;
                                                break; // No es necesario seguir buscando
                                            }
                                        }

                                        if (listaExiste) {
                                            Toast.makeText(getActivity(), "La lista ya existe", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Crear y agregar nueva lista
                                            ListaAnime nuevaListaAnime = new ListaAnime(nombreLista);
                                            perfil.getListasAnimes().add(nuevaListaAnime);

                                            // Guardar cambios en Firestore
                                            db.collection("Usuarios").document(userId)
                                                    .update("listaPerfiles", listaPerfiles)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Toast.makeText(getActivity(), "Lista agregada correctamente", Toast.LENGTH_SHORT).show();

                                                        // Actualizar UI
                                                        lista_de_listasAnimes.add(nuevaListaAnime);
                                                        miAdaptador.notifyDataSetChanged();
                                                        tvNroListas.setText(lista_de_listasAnimes.size() + " /11 listas");
                                                    })
                                                    .addOnFailureListener(e ->
                                                            Toast.makeText(getActivity(), "Error al agregar lista: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                                    );
                                        }

                                        return; // Salimos del bucle tras encontrar el perfil
                                    }
                                }

                                // Si no encontró el perfil
                                Toast.makeText(getActivity(), "Perfil no encontrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(getActivity(), "Error obteniendo usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        } else {
            Toast.makeText(getActivity(), "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
        }
    }

    private void CargarDatos(FirebaseUser usuario, FirebaseFirestore db, String nombrePerfil) {
        if (usuario != null) {
            String userId = usuario.getUid();

            db.collection("Usuarios").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Usuario usuarioActual = documentSnapshot.toObject(Usuario.class);
                            if (usuarioActual != null) {
                                List<Perfil> listaPerfiles = usuarioActual.getListaPerfiles();
                                for (Perfil perfil : listaPerfiles) {
                                    if (perfil.getNombrePerfil().equals(nombrePerfil)) {
                                        lista_de_listasAnimes.clear();
                                        lista_de_listasAnimes.addAll(perfil.getListasAnimes());
                                        getActivity().runOnUiThread(() -> {
                                            miAdaptador.notifyDataSetChanged();
                                        });
                                        if (lista_de_listasAnimes.isEmpty()) {
                                            tvNroListas.setText("No tienes listas de animes.");
                                        } else {
                                            tvNroListas.setText(lista_de_listasAnimes.size() + " /11 listas");
                                        }

                                        miAdaptador.notifyDataSetChanged();
                                        return;
                                    }
                                }
                                Toast.makeText(getActivity(), "Perfil no encontrado", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error: No se encontró el usuario", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Error obteniendo usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getActivity(), "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
        }
    }
}
