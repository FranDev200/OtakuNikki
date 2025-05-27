package com.example.otakunikki.Actividades;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.translation.Translator;
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
import com.example.otakunikki.Clases.Traductor;
import com.example.otakunikki.Clases.Usuario;
import com.example.otakunikki.R;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
            tvPuntuacion, tvGeneros, tvNumEpisodios, tvSeleccionSpinner, tvDuracion,
            tvPuntuacionAnime, tvEpisodiosAnime, tvFavoritoAnime;
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
    private Anime anime = null;
    private boolean flag = false;
    private boolean estadoVisto = false;
    private FirebaseUser usuario;
    private String nombrePerfil;
    private String nombreLista;
    private String idioma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_vista_detalle_anime);
        SharedPreferences infoIdioma = getSharedPreferences("Idiomas", MODE_PRIVATE);
        idioma = infoIdioma.getString("idioma", "es");

        /**********************************************************************************************************/
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // Obtener usuario actual
        usuario = mAuth.getCurrentUser();

        // Recuperar el nombre del perfil desde SharedPreferences
        SharedPreferences preferences = getSharedPreferences("NombrePerfil", Context.MODE_PRIVATE);
        nombrePerfil = preferences.getString("PerfilSeleccionado", "Perfil no encontrado");
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
        tvFavoritoAnime = findViewById(R.id.tvAnimeFavorito);
        tvPuntuacionAnime = findViewById(R.id.tvPuntuacionAnimeDetalle);
        tvEpisodiosAnime = findViewById(R.id.tvAnimeEpisodios);

        TraduccionControles();

        lvEpisodios = findViewById(R.id.lvEpisodios);
        anime = getIntent().getParcelableExtra("Anime");

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

        /**CREACION DEL SPINNER**/
        spFiltro = findViewById(R.id.spFiltro);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, filtros);
        spFiltro.setAdapter(adapter);
        spFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    Traductor.traducirTexto(filtros[0], "es", idioma, new Traductor.TraduccionCallback() {
                        @Override
                        public void onTextoTraducido(String textoTraducido) {
                            tvSeleccionSpinner.setText(textoTraducido);
                        }
                    });

                    Collections.reverse(listaEpisodios);
                    miAdaptadorEp.notifyDataSetChanged();
                }
                if (position == 1) {
                    Traductor.traducirTexto(filtros[1], "es", idioma, new Traductor.TraduccionCallback() {
                        @Override
                        public void onTextoTraducido(String textoTraducido) {
                            tvSeleccionSpinner.setText(textoTraducido);
                        }
                    });
                    Collections.reverse(listaEpisodios);
                    miAdaptadorEp.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**ADAPTADOR DE LA LISTA DE EPISODIOS**/
        lvEpisodios.setLayoutManager(new LinearLayoutManager(this));
        miAdaptadorEp = new AdaptadorVistaDetalleLV(this, listaEpisodios, idioma);
        lvEpisodios.setAdapter(miAdaptadorEp);
        nombreLista = getIntent().getStringExtra("NombreLista");

        miAdaptadorEp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posicion = lvEpisodios.getChildAdapterPosition(v);
                Episodio episodio = listaEpisodios.get(posicion);
                if (!episodio.isEstaVisto()) {
                    MarcarCapitulos(nombreLista, usuario, nombrePerfil, anime, episodio, true);
                } else {
                    MarcarCapitulos(nombreLista, usuario, nombrePerfil, anime, episodio, false);
                }
                // ACTUALIZAR EL ESTADO LOCALMENTE
                episodio.setEstaVisto(!episodio.isEstaVisto());

                // NOTIFICAR SOLO EL ELEMENTO MODIFICADO
                miAdaptadorEp.notifyItemChanged(posicion);
            }
        });

        //RECOGEMOS TODA LA INFORMACION DEL ANIME MANDADO DESDE LOS FRAGMENTOS

        VerificarFavorito(usuario, nombrePerfil, anime.getTitulo());
        VerificarVistos(nombreLista, usuario, nombrePerfil, anime);

        /**********************************************/
        tvTituloAnime.setText(anime.getTitulo());
        Picasso.get().load(anime.getImagenGrande()).into(imgAnime);

        if (anime.isFavorito()) {
            btnFavoritos.setImageResource(R.drawable.heart);
        } else {
            btnFavoritos.setImageResource(R.drawable.corazon_vacio);
        }

        CompletarInfoAnimeIndividual(anime);
        AgregarListaEpisodios(anime);

        /**BOTON DE FAVORITO**/
        btnFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    btnFavoritos.setImageResource(R.drawable.corazon_vacio);
                    flag = false; // 🔹 Asegurar que flag se actualice correctamente
                    // Si ya es favorito, eliminar de favoritos
                    EliminarAnimeFav("Favoritos", usuario, nombrePerfil, anime, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {


                        }
                    });
                } else {
                    btnFavoritos.setImageResource(R.drawable.heart);
                    flag = true; // 🔹 Asegurar que flag se actualice correctamente
                    // Si no es favorito, agregarlo
                    AgregarAnimeFav("Favoritos", usuario, nombrePerfil, anime, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {


                        }
                    });
                }
            }
        });


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


    private void AgregarListaEpisodios(Anime anime) {
        RequestQueue rqEpisodio = Volley.newRequestQueue(getApplicationContext());
        String urlEpisodios = "https://api.jikan.moe/v4/anime/" + anime.getId() + "/episodes";
        List<Episodio> listaAux = new ArrayList<>();
        StringRequest mrqEpisodios = new StringRequest(Request.Method.GET, urlEpisodios, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("INFO INICIO", response); // Ver la respuesta JSON

                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray dataArray = jsonResponse.getJSONArray("data");
                    listaAux.clear();
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject episodios = dataArray.getJSONObject(i);
                        int id = episodios.optInt("mal_id", 0);
                        String titulo = episodios.optString("title", "Titulo no disponible");
                        String fecha = episodios.optString("aired", "");
                        String fechaFormateada = "0000-00-00";

                        if (!fecha.isEmpty() && fecha.contains("T")) {
                            fechaFormateada = fecha.split("T")[0];  // Tomar solo la parte YYYY-MM-DD
                        }

                        listaAux.add(new Episodio(id, titulo, "", fechaFormateada, false));

                    }
                    if (anime.getListaEpisodios() == null) {
                        listaEpisodios.addAll(listaAux);
                        anime.setListaEpisodios(listaEpisodios);  // Asignar lista después de llenarla
                        Log.i("INFO EPISODIOS DE ANIME", "Total episodios: " + anime.getListaEpisodios().size());
                    }

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
                            // Obtener el título en japonés o en inglés
                            String tituloO = animeDetalles.optString("title", "Título no disponible");  // Título original (en japonés)
                            String titulo = animeDetalles.optString("title_english", null);  // Intentamos obtener el título en inglés

                            // Si title_english no está presente o está vacío, usar el título japonés
                            if (titulo.equals("null") || titulo.isEmpty()) {
                                titulo = tituloO;
                            }

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
                            List<String> listaGeneros = new ArrayList<>();

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
                                        Traductor.traducirTexto("No se ha proporcionado un video válido", "es", idioma, new Traductor.TraduccionCallback() {
                                            @Override
                                            public void onTextoTraducido(String textoTraducido) {
                                                Toast.makeText(ActividadVistaDetalleAnime.this, textoTraducido, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            });

                            if(!idioma.equals("es")) {
                                // Traducir título
                                Traductor.traducirTexto(titulo, "en", idioma, new Traductor.TraduccionCallback() {
                                    @Override
                                    public void onTextoTraducido(String textoTraducido) {
                                        anime.setTitulo(textoTraducido); // Actualiza el título traducido
                                        tvTituloAnime.setText(textoTraducido); // Actualiza el UI con el título traducido
                                    }
                                });
                            }
                            // Traducir sinopsis
                            Traductor.traducirTexto(sinopsis, "en", idioma, new Traductor.TraduccionCallback() {
                                @Override
                                public void onTextoTraducido(String textoTraducido) {
                                    anime.setSynopsis(textoTraducido); // Actualiza la sinopsis traducida
                                    tvSinopsisAnime.setText(textoTraducido); // Actualiza el UI con la sinopsis traducida
                                }
                            });

                            if (status.equalsIgnoreCase("Finished Airing")) {
                                anime.setEnEmision(false);
                                // Traducir "Finalizado" y cambiar el color
                                Traductor.traducirTexto("Finalizado", "es", idioma, new Traductor.TraduccionCallback() {
                                    @Override
                                    public void onTextoTraducido(String textoTraducido) {
                                        tvEmision.setText(textoTraducido);
                                        tvEmision.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.rojo)); // Usa ContextCompat
                                    }
                                });
                            } else {
                                anime.setEnEmision(true);
                                // Traducir "En emisión ●" y cambiar el color
                                Traductor.traducirTexto("En emisión ●", "es", idioma, new Traductor.TraduccionCallback() {
                                    @Override
                                    public void onTextoTraducido(String textoTraducido) {
                                        tvEmision.setText(textoTraducido);
                                        tvEmision.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.verde_claro)); // Usa ContextCompat
                                    }
                                });
                            }


                            if (status.equalsIgnoreCase("Finished Airing")) {
                                anime.setEnEmision(false);
                                // Traducir "Finalizado" y cambiar el color
                                Traductor.traducirTexto("Finalizado", "es", idioma, new Traductor.TraduccionCallback() {
                                    @Override
                                    public void onTextoTraducido(String textoTraducido) {
                                        tvEmision.setText(textoTraducido);
                                        tvEmision.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.rojo)); // Usa ContextCompat
                                    }
                                });
                            } else {
                                anime.setEnEmision(true);
                                // Traducir "En emisión ●" y cambiar el color
                                Traductor.traducirTexto("En emisión ●", "es", idioma, new Traductor.TraduccionCallback() {
                                    @Override
                                    public void onTextoTraducido(String textoTraducido) {
                                        tvEmision.setText(textoTraducido);
                                        tvEmision.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.verde_claro)); // Usa ContextCompat
                                    }
                                });
                            }


                            // Traducir géneros
                            List<String> listaGenerosTraducidos = new ArrayList<>();
                            for (String genero : listaGeneros) {
                                Traductor.traducirTexto(genero, "en", idioma, new Traductor.TraduccionCallback() {
                                    @Override
                                    public void onTextoTraducido(String textoTraducido) {
                                        listaGenerosTraducidos.add(textoTraducido);
                                        // Después de traducir todos los géneros, actualizar UI
                                        if (listaGenerosTraducidos.size() == listaGeneros.size()) {
                                            String generosText = String.join(" · ", listaGenerosTraducidos);
                                            tvGeneros.setText(generosText);
                                        }
                                    }
                                });
                            }

                            // Actualizar otros detalles del anime sin necesidad de traducir
                            anime.setPuntuacion(puntuacion);
                            anime.setTrailer(videoId);
                            anime.setImagenPequenia(imagenPequenia);
                            anime.setImagenMediana(imagenMediana);
                            anime.setGeneros(listaGeneros);
                            anime.setNroEpisodios(numEpisodios);
                            anime.setDuracionEp(duracionEp);

                            // Actualizar los campos de UI
                            tvPuntuacion.setText(String.valueOf(anime.getPuntuacion()));

                            if (anime.getNroEpisodios() == 0) {
                                tvNumEpisodios.setText("? ep");
                            } else if (anime.getNroEpisodios() == 1) {
                                tvNumEpisodios.setText(anime.getNroEpisodios() + " ep");
                            } else {
                                tvNumEpisodios.setText(anime.getNroEpisodios() + " eps");
                            }

                            tvDuracion.setText(anime.getDuracionEp());

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
                                        if (perfil.getListasAnimes() == null) {
                                            Toast.makeText(getApplicationContext(), "No hay listas", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (perfil.getListasAnimes() == null) {

                                            } else {
                                                //listaAnimes.clear();
                                                listaAnimes.addAll(perfil.getListasAnimes());

                                                miAdaptadorAlertDialog.notifyDataSetChanged();
                                            }

                                        }

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
        //if (listaAnime.getListaAnimes() == null) {
        //  listaAnime.setListaAnimes(new ArrayList<>());
        //}
        if (listaAnime.getListaAnimes().contains(anime)) {
            Traductor.traducirTexto("El anime ya esta en la lista", "es", idioma, new Traductor.TraduccionCallback() {
                @Override
                public void onTextoTraducido(String textoTraducido) {
                    Toast.makeText(getApplicationContext(), textoTraducido, Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            if (listaAnime.getNombreLista().equalsIgnoreCase("Favoritos")) {
                anime.setFavorito(true);
            }
            Traductor.traducirTexto("Anime agregado a la lista", "es", idioma, new Traductor.TraduccionCallback() {
                @Override
                public void onTextoTraducido(String textoTraducido) {
                    Toast.makeText(getApplicationContext(), textoTraducido, Toast.LENGTH_SHORT).show();
                }
            });
            long currentTimeInMillis = System. currentTimeMillis();
            Date currentDate = new Date(currentTimeInMillis);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
            String currentDateTime = sdf.format(currentDate);
            listaAnime.setFechaModificacion(currentDateTime);
            listaAnime.getListaAnimes().add(anime); // Agregamos el anime a la lista
        }
        //Subir los cambios a Firestore de forma segura
        db.collection("Usuarios").document(userId)
                .update("listaPerfiles", listaPerfiles)  // Asegurar que la estructura se mantenga
                .addOnSuccessListener(aVoid -> {

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Error al actualizar Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void AgregarAnimeFav(String nombreLista, FirebaseUser usuario, String nombrePerfil, Anime anime, OnSuccessListener<Void> listener) {
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

                                                if (!animeExiste) {
                                                    anime.setFavorito(true);
                                                    long currentTimeInMillis = System. currentTimeMillis();
                                                    Date currentDate = new Date(currentTimeInMillis);
                                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
                                                    String currentDateTime = sdf.format(currentDate);
                                                    lista.setFechaModificacion(currentDateTime);
                                                    lista.getListaAnimes().add(anime);
                                                }

                                                break;
                                            }
                                        }

                                        // Si la lista no existe, crear una nueva y agregar el anime
                                        if (!listaExiste) {
                                            ListaAnime nuevaListaAnime = new ListaAnime(nombreLista);
                                            long currentTimeInMillis = System. currentTimeMillis();
                                            Date currentDate = new Date(currentTimeInMillis);
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
                                            String currentDateTime = sdf.format(currentDate);
                                            nuevaListaAnime.setFechaModificacion(currentDateTime);
                                            anime.setFavorito(true);
                                            nuevaListaAnime.getListaAnimes().add(anime);
                                            perfil.getListasAnimes().add(nuevaListaAnime);
                                        }

                                        // Guardar la lista de perfiles actualizada en Firestore
                                        db.collection("Usuarios").document(userId)
                                                .update("listaPerfiles", listaPerfiles)
                                                .addOnSuccessListener(aVoid ->
                                                        Log.i("INFO ANIME FAV", "Anime agregado a favoritos")
                                                )
                                                .addOnFailureListener(e ->
                                                        Log.i("ERROR FAV", "Error al actualizar lista: " + e.getMessage())
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

    private void EliminarAnimeFav(String nombreLista, FirebaseUser usuario, String nombrePerfil, Anime anime, OnSuccessListener<Void> listener) {
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
                                                    anime.setFavorito(false);
                                                    long currentTimeInMillis = System. currentTimeMillis();
                                                    Date currentDate = new Date(currentTimeInMillis);
                                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
                                                    String currentDateTime = sdf.format(currentDate);
                                                    lista.setFechaModificacion(currentDateTime);
                                                    lista.getListaAnimes().remove(anime);
                                                }

                                                break;
                                            }
                                        }


                                        // Guardar la lista de perfiles actualizada en Firestore
                                        db.collection("Usuarios").document(userId)
                                                .update("listaPerfiles", listaPerfiles)
                                                .addOnSuccessListener(aVoid ->
                                                        Log.i("ANIME NO FAV", "Anime eliminado de favoritos")
                                                )
                                                .addOnFailureListener(e ->
                                                        Log.i("ERROR ANIME NO FAV", "Error al actualizar lista: " + e.getMessage())
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

    private void VerificarFavorito(FirebaseUser usuario, String nombrePerfil, String tituloAnime) {
        if (usuario != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userId = usuario.getUid();

            db.collection("Usuarios").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Usuario usuarioActual = documentSnapshot.toObject(Usuario.class);
                            if (usuarioActual != null) {
                                // Buscar el perfil del usuario
                                for (Perfil perfil : usuarioActual.getListaPerfiles()) {
                                    if (perfil.getNombrePerfil().equals(nombrePerfil)) {
                                        // Buscar en la lista de favoritos
                                        for (ListaAnime lista : perfil.getListasAnimes()) {
                                            if (lista.getNombreLista().equalsIgnoreCase("Favoritos")) {
                                                for (Anime anime : lista.getListaAnimes()) {
                                                    if (anime.getTitulo().equalsIgnoreCase(tituloAnime)) {
                                                        // Si está en favoritos, marcar el corazón
                                                        flag = true;
                                                        anime.setFavorito(true);
                                                        btnFavoritos.setImageResource(R.drawable.heart); // 🔹 Actualiza el icono aquí
                                                        return;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        // Si no está en favoritos, dejar el corazón vacío
                        flag = false;
                        anime.setFavorito(false);
                        btnFavoritos.setImageResource(R.drawable.corazon_vacio); // 🔹 También actualiza aquí
                    })
                    .addOnFailureListener(e -> Log.e("Firebase", "Error obteniendo favoritos", e));
        }
    }

    private void MarcarCapitulos(String nombreLista, FirebaseUser usuario, String nombrePerfil, Anime anime, Episodio capitulo, boolean visto) {
        if (usuario == null) {
            Toast.makeText(getApplicationContext(), "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = usuario.getUid();

        db.collection("Usuarios").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        Toast.makeText(getApplicationContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Usuario usuarioActual = documentSnapshot.toObject(Usuario.class);
                    if (usuarioActual == null) return;

                    for (Perfil perfil : usuarioActual.getListaPerfiles()) {
                        if (perfil.getNombrePerfil().equals(nombrePerfil)) {
                            for (ListaAnime lista : perfil.getListasAnimes()) {
                                if (lista.getNombreLista().equals(nombreLista)) {
                                    for (Anime a : lista.getListaAnimes()) {
                                        if (a.getId() == anime.getId()) {

                                            List<Episodio> listaEpisodios = a.getListaEpisodios();
                                            for (int iEp = 0; iEp < listaEpisodios.size(); iEp++) {
                                                Episodio ep = listaEpisodios.get(iEp);
                                                if (ep.getIdEpisodio() == capitulo.getIdEpisodio()) {
                                                    ep.setEstaVisto(visto);
                                                    break;
                                                }
                                            }

                                            db.collection("Usuarios").document(userId)
                                                    .set(usuarioActual)
                                                    .addOnSuccessListener(aVoid -> {

                                                    })
                                                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error al actualizar episodio: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error obteniendo usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void VerificarVistos(String nombreLista, FirebaseUser usuario, String nombrePerfil, Anime anime) {
        if (usuario == null) {
            Toast.makeText(getApplicationContext(), "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = usuario.getUid();

        db.collection("Usuarios").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        Toast.makeText(getApplicationContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Usuario usuarioActual = documentSnapshot.toObject(Usuario.class);
                    if (usuarioActual == null) return;

                    for (Perfil perfil : usuarioActual.getListaPerfiles()) {
                        if (perfil.getNombrePerfil().equals(nombrePerfil)) {
                            for (ListaAnime lista : perfil.getListasAnimes()) {
                                if (lista.getNombreLista().equals(nombreLista)) {
                                    for (Anime a : lista.getListaAnimes()) {
                                        if (a.getId() == anime.getId()) {
                                            // ACTUALIZAMOS listaEpisodios CON LOS DATOS DE FIRESTORE
                                            listaEpisodios.clear();
                                            listaEpisodios.addAll(a.getListaEpisodios());
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }

                })
                .addOnFailureListener(e ->
                        Toast.makeText(getApplicationContext(), "Error obteniendo usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void TraduccionControles(){
        /**TRADUCCION TEXTVIEW PUNTUACION, EPISODIOS, FAVORITOS, OPCIONES SPINNER Y BOTON AÑADIR LISTA**/
        Traductor.traducirTexto(tvFavoritoAnime.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvFavoritoAnime.setText(textoTraducido);
            }
        });
        Traductor.traducirTexto(tvPuntuacionAnime.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvPuntuacionAnime.setText(textoTraducido);
            }
        });
        Traductor.traducirTexto(tvEpisodiosAnime.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvEpisodiosAnime.setText(textoTraducido);
            }
        });
        Traductor.traducirTexto(filtros[0], "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                filtros[0] = textoTraducido;
            }
        });
        Traductor.traducirTexto(filtros[1], "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                filtros[1] = textoTraducido;
            }
        });
        Traductor.traducirTexto(btnAnyadirAnime.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                btnAnyadirAnime.setText(textoTraducido);
            }
        });
        /***************************************************************/
    }
}
