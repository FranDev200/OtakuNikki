package com.example.otakunikki.Actividades;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.otakunikki.Adaptadores.AdaptadorLVAlertDialog;
import com.example.otakunikki.Adaptadores.AdaptadorVistaDetalleLV;
import com.example.otakunikki.Clases.Anime;
import com.example.otakunikki.Clases.Episodio;
import com.example.otakunikki.Clases.ListaAnime;
import com.example.otakunikki.Clases.Perfil;
import com.example.otakunikki.Clases.Usuario;
import com.example.otakunikki.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActividadVistaDetalleAnime extends AppCompatActivity {
    private String[] filtros = {"Más antiguo", "Más reciente"};
    Spinner spFiltro;
    YouTubePlayerView youTubePlayerView;
    //-------------------------------------------------------------------------
    ImageButton btnRetroceso, btnFavoritos;
    ImageView imgAnime, imgMostrarTexto;
    TextView tvTituloAnime, tvSinopsisAnime, tvEmision,
            tvPuntuacion, tvGeneros, tvNumEpisodios, tvSeleccionSpinner, tvDuracion;
    Button btnAnyadirAnime;
    //-------------------------------------------------------------------------
    RecyclerView lvEpisodios;
    AdaptadorVistaDetalleLV miAdaptadorEp; //El adaptador que usuamos para el lv de episodios
    List<Episodio> listaEpisodios = new ArrayList<Episodio>();
    //-------------------------------------------------------------------------
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private AdaptadorLVAlertDialog miAdaptadorAlertDialog;
    private List<ListaAnime> listaAnimes;
    Anime anime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_vista_detalle_anime);

        /**********************************************************************************************************/
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // Obtener usuario actual
        FirebaseUser usuario = mAuth.getCurrentUser();

        // Recuperar el nombre del perfil desde SharedPreferences
        SharedPreferences preferences = getSharedPreferences("NombrePerfil", Context.MODE_PRIVATE);
        String nombrePerfil = preferences.getString("PerfilSeleccionado", "Perfil no encontrado");
        /**********************************************************************************************************/

        imgAnime = findViewById(R.id.imgAnime);
        imgMostrarTexto = findViewById(R.id.imgMostrarTexto);

        btnRetroceso = findViewById(R.id.btnRetroceso);
        btnFavoritos = findViewById(R.id.btnFavoritos);
        btnAnyadirAnime = findViewById(R.id.btnAnyadirAnime);

        tvTituloAnime = findViewById(R.id.tvTituloAnime);
        tvSinopsisAnime = findViewById(R.id.tvSinopsisAnime);
        tvEmision = findViewById(R.id.tvEmision);
        tvPuntuacion = findViewById(R.id.tvPuntuacion);
        tvGeneros = findViewById(R.id.tvGeneros);
        tvNumEpisodios = findViewById(R.id.tvNumEpisodios);
        tvSeleccionSpinner = findViewById(R.id.tvSeleccionSpinner);
        tvDuracion = findViewById(R.id.tvDuracionEp);

        lvEpisodios = findViewById(R.id.lvEpisodios);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);


        tvSinopsisAnime.setOnClickListener(new View.OnClickListener() {
            boolean flag = false;

            @Override
            public void onClick(View v) {
                if (flag) {
                    tvSinopsisAnime.setMaxLines(4);
                    flag = false;
                    imgMostrarTexto.setImageResource(R.drawable.flecha_abajo);
                } else {
                    tvSinopsisAnime.setMaxLines(Integer.MAX_VALUE);
                    flag = true;
                    imgMostrarTexto.setImageResource(R.drawable.flecha_arriba);
                }
            }
        });

        /**ADAPTADOR DE LA LISTA DE EPISODIOS**/
        lvEpisodios.setLayoutManager(new LinearLayoutManager(this));
        miAdaptadorEp = new AdaptadorVistaDetalleLV(this, listaEpisodios);
        lvEpisodios.setAdapter(miAdaptadorEp);

        /**CREACION DEL SPINNER**/
        spFiltro = findViewById(R.id.spFiltro);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, filtros);
        spFiltro.setAdapter(adapter);
        spFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (filtros[position].contentEquals("Más antiguo")) {
                    tvSeleccionSpinner.setText("Más antiguo");
                }
                if (filtros[position].contentEquals("Más reciente")) {
                    tvSeleccionSpinner.setText("Más reciente");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //RECOGEMOS TODA LA INFORMACION DEL ANIME MANDADO DESDE LOS FRAGMENTOS

        anime = getIntent().getParcelableExtra("Anime");
        tvTituloAnime.setText(anime.getTitulo());
        Picasso.get().load(anime.getImagenGrande()).into(imgAnime);


        CompletarInfoAnimeIndividual(anime);
        AgregarListaEpisodios(anime);

        /**BOTON DE RETROCESO**/
        btnRetroceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnAnyadirAnime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActividadVistaDetalleAnime.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_personalizado_listas, null);
                builder.setView(dialogView);


                ListView listView = dialogView.findViewById(R.id.lvListasAnimesDialog);

                listaAnimes = new ArrayList<>();
                miAdaptadorAlertDialog = new AdaptadorLVAlertDialog(getApplicationContext(), listaAnimes);
                listView.setAdapter(miAdaptadorAlertDialog);

                RecuperarListas(usuario, nombrePerfil);

                AlertDialog dialog = builder.create();
                dialog.show();

                // Manejar clics en los elementos del ListView
                listView.setOnItemClickListener((parent, view, position, id) -> {
                    AgregarAnimeLista(usuario, nombrePerfil, position, anime);
                    dialog.dismiss(); // Cerrar el diálogo al seleccionar un ítem
                });

            }
        });


    }

    /**
     * PENDIENTE DE SI LLEGAREMOS A USAR LA SINOPSIS DEL EPISODIO
     **/
    private void AgregarSinopsisEpisodios(List<Episodio> episodios, int idAnime) {
        RequestQueue rqSynopsis = Volley.newRequestQueue(getApplicationContext());
        for (Episodio aux : episodios) {
            String urlSynopsis = "https://api.jikan.moe/v4/anime/" + idAnime + "/episodes/" + aux.getIdEpisodio();

            StringRequest mrqSynopsis = new StringRequest(Request.Method.GET, urlSynopsis, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject respuesta = new JSONObject(response);
                        JSONObject synopsisEpisodio = respuesta.getJSONObject("data");
                        String synopsis = synopsisEpisodio.optString("synopsis", "No dispone de synopsis el episodio");
                        aux.setSinopsis(synopsis);

                        Log.i("INFO SYNOPSIS", aux.getTitulo() + " -> " + synopsis);
                    } catch (JSONException e) {
                        Log.e("ERROR JSON", "Error JSON en la synopsis del episodio", e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ERROR VOLLEY", "Error en la petición de synopsis del episodio", error);
                }
            });

            rqSynopsis.add(mrqSynopsis);
        }
    }

    private void AgregarListaEpisodios(Anime anime) {
        RequestQueue rqEpisodio = Volley.newRequestQueue(getApplicationContext());
        String urlEpisodios = "https://api.jikan.moe/v4/anime/" + anime.getId() + "/episodes";

        StringRequest mrqEpisodios = new StringRequest(Request.Method.GET, urlEpisodios, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("INFO INICIO", response); // Ver la respuesta JSON

                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray dataArray = jsonResponse.getJSONArray("data");

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject episodios = dataArray.getJSONObject(i);
                        int id = episodios.optInt("mal_id", 0);
                        String titulo = episodios.optString("title", "Titulo no disponible");
                        String fecha = episodios.optString("aired", "");
                        String fechaFormateada = "0000-00-00";

                        if (!fecha.isEmpty() && fecha.contains("T")) {
                            fechaFormateada = fecha.split("T")[0];  // Tomar solo la parte YYYY-MM-DD
                        }

                        listaEpisodios.add(new Episodio(id, titulo, "", fechaFormateada, false));

                    }
                    miAdaptadorEp.notifyDataSetChanged();
                    anime.setListaEpisodios(listaEpisodios);  // Asignar lista después de llenarla
                    Log.i("INFO EPISODIOS DE ANIME", "Total episodios: " + anime.getListaEpisodios().size());

                } catch (JSONException e) {
                    Log.e("ERROR JSON", "Error al parsear JSON", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR VOLLEY", "Error en la petición de episodios", error);
            }
        });

        rqEpisodio.add(mrqEpisodios);
    }

    private void CompletarInfoAnimeIndividual(Anime anime) {
        RequestQueue rqAnimes = Volley.newRequestQueue(getApplicationContext());
        String urlAnime = "https://api.jikan.moe/v4/anime/" + anime.getId();

        StringRequest mrqAnimes = new StringRequest(Request.Method.GET, urlAnime,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("INFO JSON INICIO", response);
                            JSONObject animeDetallesResponse = new JSONObject(response);
                            JSONObject animeDetalles = animeDetallesResponse.getJSONObject("data");

                            String sinopsis = animeDetalles.optString("synopsis", "Sin sinopsis disponible");
                            double puntuacion = animeDetalles.optDouble("score", 0.0);
                            String url = animeDetalles.getJSONObject("trailer").optString("url", "Trailer no disponible");
                            String videoId = extractYouTubeVideoId(url);
                            String status = animeDetalles.optString("status", "Status no disponible");
                            int numEpisodios = animeDetalles.optInt("episodes", 0);
                            String duracionEp = animeDetalles.optString("duration", "Duración no disponible");
                            String imagenPequenia = animeDetalles.optJSONObject("images")
                                    .optJSONObject("jpg")
                                    .optString("image_url", "URL no disponible");

                            String imagenMediana = animeDetalles.optJSONObject("images")
                                    .optJSONObject("jpg")
                                    .optString("medium_image_url", "URL no disponible");

                            // Obtención de la lista de géneros
                            JSONArray datosGeneros = animeDetalles.optJSONArray("genres");
                            List<String> listaGeneros = new ArrayList<>(); // Reiniciar la lista de géneros en cada iteración

                            if (datosGeneros != null) {
                                for (int l = 0; l < datosGeneros.length(); l++) {
                                    JSONObject generoObj = datosGeneros.getJSONObject(l);
                                    listaGeneros.add(generoObj.optString("name", "Género no disponible"));
                                }
                            }

                            // Asegurarse de que siempre haya géneros, incluso si el array estaba vacío
                            if (listaGeneros.isEmpty()) {
                                listaGeneros.add("Género no disponible");
                            }

                            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                                @Override
                                public void onReady(YouTubePlayer youTubePlayer) {


                                    if (videoId != null && !videoId.isEmpty()) {
                                        youTubePlayer.cueVideo(videoId, 0); // Carga el video pero no lo reproduce
                                    } else {
                                        Toast.makeText(ActividadVistaDetalleAnime.this, "No se ha proporcionado un video válido", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                            // Actualizar los campos del objeto Anime
                            anime.setSynopsis(sinopsis);
                            anime.setPuntuacion(puntuacion);
                            anime.setTrailer(videoId);
                            anime.setImagenPequenia(imagenPequenia);
                            anime.setImagenMediana(imagenMediana);
                            anime.setGeneros(listaGeneros);
                            anime.setNroEpisodios(numEpisodios);
                            anime.setDuracionEp(duracionEp);

                            if (status.contentEquals("Finished Airing")) {
                                anime.setEnEmision(false);
                            } else {
                                anime.setEnEmision(true);
                            }

                            Log.i("INFO INICIO", "### " + anime.getPuntuacion() + " ### " + anime.getTitulo());

                            // Cargar la información a la UI
                            tvSinopsisAnime.setText(anime.getSynopsis());
                            tvPuntuacion.setText(String.valueOf(anime.getPuntuacion()));

                            if (anime.isEnEmision()) {
                                tvEmision.setText("En emisión ●");
                                tvEmision.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.rojo)); // Usa ContextCompat
                            } else {
                                tvEmision.setText("Finalizado");
                                tvEmision.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.moradoDifuminadoIcono)); // Usa ContextCompat
                            }

                            if (anime.getNroEpisodios() == 1) {
                                tvNumEpisodios.setText(anime.getNroEpisodios() + " ep");
                            } else {
                                tvNumEpisodios.setText(anime.getNroEpisodios() + " eps");
                            }

                            tvDuracion.setText(anime.getDuracionEp());

                            String generosText = "";
                            for (String genero : listaGeneros) {
                                generosText += genero + " · ";
                            }

                            tvGeneros.setText(generosText);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error procesando los detalles del anime", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Error al obtener detalles del anime: " + error.getMessage());
                    }
                }
        );

        rqAnimes.add(mrqAnimes);
    }

    public String extractYouTubeVideoId(String youtubeUrl) {
        String videoId = null;
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0) {
            // Expresión regular para extraer el ID del video
            String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(youtubeUrl);
            if (matcher.find()) {
                videoId = matcher.group();
            }
        }
        return videoId;
    }

    /**
     * RECUPERAMOS LAS LISTAS QUE POSEE ESE PERFIL
     **/
    public void RecuperarListas(FirebaseUser usuario, String nombrePerfil) {
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
                                        listaAnimes.clear();
                                        listaAnimes.addAll(perfil.getListasAnimes());

                                        miAdaptadorAlertDialog.notifyDataSetChanged();
                                        return;
                                    }
                                }
                                Toast.makeText(getApplicationContext(), "Perfil no encontrado", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: No se encontró el usuario", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Error obteniendo usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getApplicationContext(), "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
        }
    }

    private void AgregarAnimeLista(FirebaseUser usuario, String nombrePerfil, int position, Anime anime) {
        if (usuario == null) {
            Toast.makeText(getApplicationContext(), "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = usuario.getUid();

        db.collection("Usuarios").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        Toast.makeText(getApplicationContext(), "Error: No se encontró el usuario", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Usuario usuarioActual = documentSnapshot.toObject(Usuario.class);
                    if (usuarioActual == null || usuarioActual.getListaPerfiles() == null) {
                        Toast.makeText(getApplicationContext(), "Error al obtener usuario", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<Perfil> listaPerfiles = usuarioActual.getListaPerfiles();
                    Perfil perfilSeleccionado = null;

                    // 🔍 Buscar el perfil correcto
                    for (Perfil perfil : listaPerfiles) {
                        if (perfil.getNombrePerfil().equals(nombrePerfil)) {
                            perfilSeleccionado = perfil;
                            break;
                        }
                    }

                    if (perfilSeleccionado == null) {
                        Toast.makeText(getApplicationContext(), "Perfil no encontrado", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 🔍 Verificar que la lista de animes existe
                    if (perfilSeleccionado.getListasAnimes() == null || position < 0 || position >= perfilSeleccionado.getListasAnimes().size()) {
                        Toast.makeText(getApplicationContext(), "Índice inválido", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // ⚡ Llamar a la función que actualiza Firestore
                    actualizarFirestore(userId, listaPerfiles, perfilSeleccionado, position, anime);

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Error obteniendo usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void actualizarFirestore(String userId, List<Perfil> listaPerfiles, Perfil perfilSeleccionado, int position, Anime anime) {
        // ⚡ Modificar la lista en memoria antes de subirla a Firestore
        ListaAnime listaAnime = perfilSeleccionado.getListasAnimes().get(position);
        if (listaAnime.getListaAnimes() == null) {
            listaAnime.setListaAnimes(new ArrayList<>());
        }

        listaAnime.getListaAnimes().add(anime); // Agregamos el anime a la lista

        //Subir los cambios a Firestore de forma segura
        db.collection("Usuarios").document(userId)
                .update("listaPerfiles", listaPerfiles)  // Asegurar que la estructura se mantenga
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getApplicationContext(), "Anime agregado exitosamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Error al actualizar Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}

