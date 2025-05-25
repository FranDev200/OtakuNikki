package com.example.otakunikki.Fragmentos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.example.otakunikki.Adaptadores.AdaptadorListas;
import com.example.otakunikki.Clases.ListaAnime;
import com.example.otakunikki.Clases.Perfil;
import com.example.otakunikki.Clases.Traductor;
import com.example.otakunikki.Clases.Usuario;
import com.example.otakunikki.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FragmentoListas extends Fragment {

    List<ListaAnime> lista_de_listasAnimes;
    AdaptadorListas miAdaptador;
    ListaAnime listaSeleccionada;
    private ListView miListView;
    private TextView tvNroListas, tvMisListas;
    private ImageButton imgBtnAgregarLista;
    private ListaAnime listaAEliminar = null;
    private FirebaseUser usuario;
    private String nombrePerfil;
    private FirebaseFirestore db;
    private String idioma;
    private ListenerRegistration refrescoListas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragmento_listas, container, false);

        tvMisListas = vista.findViewById(R.id.tvMisListas);
        tvNroListas = vista.findViewById(R.id.tvNroListas);
        miListView = vista.findViewById(R.id.lvListasAnimes);
        imgBtnAgregarLista = vista.findViewById(R.id.imgBtnAgregarLista);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Obtener usuario actual
        usuario = mAuth.getCurrentUser();

        // Recuperar el nombre del perfil desde SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("NombrePerfil", Context.MODE_PRIVATE);
        nombrePerfil = preferences.getString("PerfilSeleccionado", "Perfil no encontrado");

        SharedPreferences infoIdioma = requireContext().getSharedPreferences("Idiomas", Context.MODE_PRIVATE);
        idioma = infoIdioma.getString("idioma", "es");

        CargarDatosTiempoReal(usuario, db, nombrePerfil);

        /**TRADUCIR CONTROLES YA DEFINIDOS**/
        Traductor.traducirTexto(tvMisListas.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvMisListas.setText(textoTraducido);
            }
        });

        tvNroListas.setText("0/11");

        lista_de_listasAnimes = new ArrayList<>();
        miAdaptador = new AdaptadorListas(getActivity().getApplicationContext(), lista_de_listasAnimes, idioma);
        miListView.setAdapter(miAdaptador);

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

                // Usar un array para almacenar el mensaje
                final String[] msg = new String[1];
                msg[0] = "¿Estás seguro de borrar la lista " + nombreLista;

                // Traducir el mensaje principal
                Traductor.traducirTexto(msg[0], "es", idioma, new Traductor.TraduccionCallback() {
                    @Override
                    public void onTextoTraducido(String textoTraducido) {
                        msg[0] = textoTraducido;

                        // Traducir el texto del botón "Cancelar"
                        Traductor.traducirTexto("Cancelar", "es", idioma, new Traductor.TraduccionCallback() {
                            @Override
                            public void onTextoTraducido(String textoCancelarTraducido) {
                                // Traducir el texto del botón "Aceptar"
                                Traductor.traducirTexto("Aceptar", "es", idioma, new Traductor.TraduccionCallback() {
                                    @Override
                                    public void onTextoTraducido(String textoAceptarTraducido) {
                                        // Configurar el diálogo con los textos traducidos
                                        builder.setTitle(msg[0] + " ?\n")
                                                .setIcon(R.drawable.eliminar)
                                                .setNegativeButton(textoCancelarTraducido, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.cancel();

                                                    }
                                                })
                                                .setPositiveButton(textoAceptarTraducido, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        EliminarLista(nombreLista, usuario, nombrePerfil);
                                                    }
                                                });

                                        // Mostrar el diálogo
                                        builder.show();
                                    }
                                });
                            }
                        });
                    }
                });

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

            /**TRADUCIR CONTROLES YA DEFINIDOS**/
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
            });

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
                    Traductor.traducirTexto("El nombre no puede estar vacío", "es", idioma, new Traductor.TraduccionCallback() {
                        @Override
                        public void onTextoTraducido(String textoTraducido) {
                            etNombreLista.setError(textoTraducido);
                        }
                    });

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

                                        if (listaAEliminar.getNombreLista().equalsIgnoreCase("Favoritos")) {
                                            Traductor.traducirTexto("Esta lista no se puede eliminar", "es", idioma, new Traductor.TraduccionCallback() {
                                                @Override
                                                public void onTextoTraducido(String textoTraducido) {
                                                    Toast.makeText(getActivity().getApplicationContext(), textoTraducido, Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            return;
                                        } else {
                                            if (listaAEliminar != null) {
                                                listasAnimes.remove(listaAEliminar); // Eliminar de Firestore

                                                db.collection("Usuarios").document(userId)
                                                        .update("listaPerfiles", listaPerfiles)
                                                        .addOnSuccessListener(aVoid -> {

                                                            // Eliminar de la lista local
                                                            lista_de_listasAnimes.remove(listaAEliminar);

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
        if (usuario == null) {
            Toast.makeText(getActivity(), "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = usuario.getUid();

        db.collection("Usuarios").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        Toast.makeText(getActivity(), "Usuario no encontrado en la base de datos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Usuario usuarioActual = documentSnapshot.toObject(Usuario.class);
                    if (usuarioActual == null) {
                        Toast.makeText(getActivity(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Buscar el perfil correspondiente
                    List<Perfil> listaPerfiles = usuarioActual.getListaPerfiles();
                    Perfil perfilSeleccionado = null;

                    for (Perfil perfil : listaPerfiles) {
                        if (perfil.getNombrePerfil().equals(nombrePerfil)) {
                            perfilSeleccionado = perfil;
                            break;
                        }
                    }

                    if (perfilSeleccionado == null) {
                        Toast.makeText(getActivity(), "Perfil no encontrado", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Verificar el número máximo de listas antes de recorrerlas
                    boolean nroMaximo = perfilSeleccionado.getListasAnimes().size() >= 11;
                    if (nroMaximo) {
                        Traductor.traducirTexto("Número máximo de listas alcanzado", "es", idioma, new Traductor.TraduccionCallback() {
                            @Override
                            public void onTextoTraducido(String textoTraducido) {
                                getActivity().runOnUiThread(() ->
                                        Toast.makeText(getActivity(), textoTraducido, Toast.LENGTH_SHORT).show()
                                );
                            }
                        });
                        return; // Salimos si ya hay 11 listas
                    }

                    // Verificar si ya existe una lista con el mismo nombre
                    boolean listaExiste = false;
                    for (ListaAnime lista : perfilSeleccionado.getListasAnimes()) {
                        if (lista.getNombreLista().equalsIgnoreCase(nombreLista)) {
                            listaExiste = true;
                            break;
                        }
                    }

                    if (listaExiste) {
                        Traductor.traducirTexto("Lista ya existe", "es", idioma, new Traductor.TraduccionCallback() {
                            @Override
                            public void onTextoTraducido(String textoTraducido) {
                                getActivity().runOnUiThread(() ->
                                        Toast.makeText(getActivity(), textoTraducido, Toast.LENGTH_SHORT).show()
                                );
                            }
                        });
                        return; // Salimos si la lista ya existe
                    }

                    // Crear y agregar nueva lista
                    ListaAnime nuevaListaAnime = new ListaAnime(nombreLista);
                    long currentTimeInMillis = System. currentTimeMillis();
                    Date currentDate = new Date(currentTimeInMillis);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
                    String currentDateTime = sdf.format(currentDate);
                    nuevaListaAnime.setFechaModificacion(currentDateTime);
                    perfilSeleccionado.getListasAnimes().add(nuevaListaAnime);

                    // Guardar cambios en Firestore
                    db.collection("Usuarios").document(userId)
                            .update("listaPerfiles", listaPerfiles)
                            .addOnSuccessListener(aVoid -> {

                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(getActivity(), "Error al agregar lista: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                            );

                })
                .addOnFailureListener(e ->
                        Toast.makeText(getActivity(), "Error obteniendo usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void CargarDatosTiempoReal(FirebaseUser usuario, FirebaseFirestore db, String nombrePerfil) {
        if (usuario == null) {
            Toast.makeText(getActivity(), "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = usuario.getUid();

        refrescoListas = db.collection("Usuarios").document(userId)
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        Toast.makeText(getActivity(), "Error escuchando cambios: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Usuario usuarioActual = documentSnapshot.toObject(Usuario.class);
                        if (usuarioActual != null && usuarioActual.getListaPerfiles() != null) {

                            List<Perfil> listaPerfiles = usuarioActual.getListaPerfiles();
                            Perfil perfilSeleccionado = null;

                            for (Perfil perfil : listaPerfiles) {
                                if (perfil.getNombrePerfil().equals(nombrePerfil)) {
                                    perfilSeleccionado = perfil;
                                    break;
                                }
                            }

                            if (perfilSeleccionado != null && perfilSeleccionado.getListasAnimes() != null) {
                                // Refrescar siempre desde cero
                                lista_de_listasAnimes.clear();
                                lista_de_listasAnimes.addAll(perfilSeleccionado.getListasAnimes());

                                getActivity().runOnUiThread(() -> {
                                    miAdaptador.notifyDataSetChanged();

                                    if (lista_de_listasAnimes.isEmpty()) {
                                        Traductor.traducirTexto("No tienes listas de animes.", "es", idioma, new Traductor.TraduccionCallback() {
                                            @Override
                                            public void onTextoTraducido(String textoTraducido) {
                                                tvNroListas.setText(textoTraducido);
                                            }
                                        });
                                    } else {
                                        tvNroListas.setText(lista_de_listasAnimes.size() + " /11 listas");
                                    }
                                });

                            } else {
                                Toast.makeText(getActivity(), "No hay listas de animes en el perfil", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getActivity(), "Usuario o perfiles inválidos", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "No se encontró el documento del usuario", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onDestroy() {
        super.onDestroy();
        if (refrescoListas != null) {
            refrescoListas.remove();
        }
    }
}
