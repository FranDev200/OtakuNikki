package com.example.otakunikki.Actividades;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.otakunikki.Adaptadores.AdaptadorListaAnimeDetalle;
import com.example.otakunikki.Clases.Anime;
import com.example.otakunikki.Clases.ListaAnime;
import com.example.otakunikki.Clases.Perfil;
import com.example.otakunikki.Clases.Usuario;
import com.example.otakunikki.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ActividadVistaDetalleListaAnime extends AppCompatActivity {

    ListaAnime listaSeleccionada;
    AdaptadorListaAnimeDetalle miAdaptador;
    private EditText etTituloLista;
    private TextView tvNroAnimesLista;
    private ListView lvAnimesLista;
    private ImageButton imgRetroceso, imgAniadirAnime;
    private List<Anime> listaAnime;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_vista_detalle_lista_anime);

        /**********************************************************************************************************/
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // Obtener usuario actual
        usuario = mAuth.getCurrentUser();
        // Recuperar el nombre del perfil desde SharedPreferences
        SharedPreferences preferences = getSharedPreferences("NombrePerfil", Context.MODE_PRIVATE);
        String nombrePerfil = preferences.getString("PerfilSeleccionado", "Perfil no encontrado");
        /**********************************************************************************************************/

        listaSeleccionada = getIntent().getParcelableExtra("ListaAnimeSeleccionada");

        etTituloLista = findViewById(R.id.etTituloListaDetalle);
        tvNroAnimesLista = findViewById(R.id.nroAnimesLista);
        lvAnimesLista = findViewById(R.id.lvAnimesGuardadosLista);
        imgRetroceso = findViewById(R.id.imgRetroceso);
        imgAniadirAnime = findViewById(R.id.imgAniadirAnime);

        etTituloLista.setText(listaSeleccionada.getNombreLista());
        tvNroAnimesLista.setText(listaSeleccionada.getListaAnimes().size() + " animes");
        listaAnime = new ArrayList<>();
        listaAnime.addAll(listaSeleccionada.getListaAnimes());
        for (Anime aux : listaAnime) {
            Log.i("ANIMES", aux.getId() + "@@@@@@@" + aux.getTitulo());
        }


        miAdaptador = new AdaptadorListaAnimeDetalle(getApplicationContext(), listaAnime);
        lvAnimesLista.setAdapter(miAdaptador);

        miAdaptador.notifyDataSetChanged();


        lvAnimesLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("DEBUG_CLICK", "Item clickeado en posición: " + position); // Verificar en Logcat
                Anime animeSeleccionado = listaSeleccionada.getListaAnimes().get(position);

                abrirDetalleAnime(animeSeleccionado, listaSeleccionada.getNombreLista());
            }
        });

        lvAnimesLista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Anime anime = listaSeleccionada.getListaAnimes().get(position);
                String nombreAnime = anime.getTitulo();
                AlertDialog.Builder builder = new AlertDialog.Builder(ActividadVistaDetalleListaAnime.this);

                builder.setTitle("¿Estás seguro de borrar el anime " + nombreAnime + "?\n")
                        .setIcon(R.drawable.eliminar);


                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        Toast.makeText(getApplicationContext(), "Has elegido no borrar", Toast.LENGTH_LONG).show();
                    }
                });

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), nombreAnime  + " eliminado.", Toast.LENGTH_LONG).show();
                        listaAnime.remove(anime);

                        // Notificar al adaptador que la lista ha cambiado
                        miAdaptador.notifyDataSetChanged();
                        EliminarAnime(anime, nombrePerfil, listaSeleccionada.getNombreLista());
                    }
                });

                builder.create();
                builder.show();

                return true;
            }
        });

        imgAniadirAnime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Explorar.class);
                startActivityForResult(intent, 1); // El 1 es para ponerle un id a la actividad
                //Utilizo startActivityForResult para que detecte cuando se cierra la actividad y cambie el estado del navegador
            }
        });

        imgRetroceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void EliminarAnime(Anime anime, String nombrePerfil, String nombreLista) {
        if (usuario != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userId = usuario.getUid();

            // Buscar el usuario en Firestore
            db.collection("Usuarios").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Usuario usuarioActual = documentSnapshot.toObject(Usuario.class);
                            if (usuarioActual != null) {
                                // Buscar el perfil que coincida con el nombre seleccionado
                                List<Perfil> listaPerfiles = usuarioActual.getListaPerfiles();
                                for (Perfil perfil : listaPerfiles) {
                                    if (perfil.getNombrePerfil().equals(nombrePerfil)) {

                                        // Verificar si la lista de animes ya existe
                                        boolean listaExiste = false;
                                        for (ListaAnime lista : perfil.getListasAnimes()) {
                                            if (lista.getNombreLista().equals(nombreLista)) {
                                                listaExiste = true;

                                                // Verificar si el anime ya está en la lista
                                                boolean animeExiste = false;
                                                for (Anime a : lista.getListaAnimes()) {
                                                    if (a.getId() == anime.getId()) {
                                                        animeExiste = true;
                                                        break;
                                                    }
                                                }

                                                if (animeExiste) {
                                                    lista.getListaAnimes().remove(anime);
                                                }

                                                break;
                                            }
                                        }

                                        // Guardar la lista de perfiles actualizada en Firestore
                                        db.collection("Usuarios").document(userId)
                                                .update("listaPerfiles", listaPerfiles)
                                                .addOnSuccessListener(aVoid ->
                                                        Toast.makeText(getApplicationContext(), "Anime eliminado", Toast.LENGTH_SHORT).show()
                                                )
                                                .addOnFailureListener(e ->
                                                        Toast.makeText(getApplicationContext(), "Error al eliminar: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                                );

                                        return; // Salimos después de encontrar y modificar el perfil correcto
                                    }
                                }

                                // Si no encontró el perfil
                                Toast.makeText(getApplicationContext(), "Perfil no encontrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Error obteniendo usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getApplicationContext(), "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
        }


    }

    public void abrirDetalleAnime(Anime animeSeleccionado, String nombreLista) {
        if (animeSeleccionado != null) {
            Intent intent = new Intent(ActividadVistaDetalleListaAnime.this, ActividadVistaDetalleAnime.class);
            intent.putExtra("Anime", animeSeleccionado);
            intent.putExtra("NombreLista", nombreLista);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error al abrir el anime", Toast.LENGTH_SHORT).show();
        }
    }


}