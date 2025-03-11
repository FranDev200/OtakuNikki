package com.example.otakunikki.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.otakunikki.Adaptadores.AdaptadorVistaDetalleLV;
import com.example.otakunikki.Clases.Anime;
import com.example.otakunikki.Clases.Episodio;
import com.example.otakunikki.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActividadVistaDetalleAnime extends AppCompatActivity {
    private String[] filtros = {"Filtrar por:","Más antiguo", "Más reciente"};
    Spinner spFiltro;
    //-------------------------------------------------------------------------
    ImageButton btnRetroceso, btnFavoritos, imgMostrarTexto, btnReproducir;
    ImageView imgAnime;
    TextView tvTituloAnime, tvSinopsisAnime, tvEmision,
            tvPuntuacion, tvGeneros, tvNumEpisodios;
    Button btnAnyadirAnime;
    //-------------------------------------------------------------------------
    ListView lvEpisodios;
    AdaptadorVistaDetalleLV miAdaptadorEp; //El adaptador que usuamos para el lv de episodios
    List<Episodio> listaEpisodios = new ArrayList<Episodio>();
    //-------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_vista_detalle_anime);

        imgAnime = findViewById(R.id.imgAnime);
        imgMostrarTexto = findViewById(R.id.imgMostrarTexto);

        btnRetroceso = findViewById(R.id.btnRetroceso);
        btnFavoritos = findViewById(R.id.btnFavoritos);
        btnReproducir = findViewById(R.id.btnReproducir);
        btnAnyadirAnime = findViewById(R.id.btnAnyadirAnime);

        tvTituloAnime = findViewById(R.id.tvTituloAnime);
        tvSinopsisAnime = findViewById(R.id.tvSinopsisAnime);
        tvEmision = findViewById(R.id.tvEmision);
        tvPuntuacion = findViewById(R.id.tvPuntuacion);
        tvGeneros = findViewById(R.id.tvGeneros);
        tvNumEpisodios = findViewById(R.id.tvNumEpisodios);

        imgMostrarTexto.setOnClickListener(new View.OnClickListener() {
            boolean flag = false;
            @Override
            public void onClick(View v) {
                if(flag){
                    tvSinopsisAnime.setMaxLines(4);
                    flag = false;
                    imgMostrarTexto.setImageResource(R.drawable.plus);
                }else{
                    tvSinopsisAnime.setMaxLines(Integer.MAX_VALUE);
                    flag = true;
                    imgMostrarTexto.setImageResource(R.drawable.menos);
                }
            }
        });
        tvSinopsisAnime.setOnClickListener(new View.OnClickListener() {
            boolean flag = false;
            @Override
            public void onClick(View v) {
                if(flag){
                    tvSinopsisAnime.setMaxLines(4);
                    flag = false;
                    imgMostrarTexto.setImageResource(R.drawable.plus);
                }else{
                    tvSinopsisAnime.setMaxLines(Integer.MAX_VALUE);
                    flag = true;
                    imgMostrarTexto.setImageResource(R.drawable.menos);
                }
            }
        });

        /**ADAPTADOR DE LA LISTA DE EPISODIOS**/
        lvEpisodios = findViewById(R.id.lvEpisodios);
        miAdaptadorEp = new AdaptadorVistaDetalleLV(this, listaEpisodios);
        lvEpisodios.setAdapter(miAdaptadorEp);

        /**CREACION DEL SPINNER**/
        spFiltro = findViewById(R.id.spFiltro);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, filtros);
        spFiltro.setAdapter(adapter);
        spFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (filtros[position].contentEquals("Filtrar por:")) {

                }
                if (filtros[position].contentEquals("Más antiguo")) {

                }
                if (filtros[position].contentEquals("Más reciente")) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //RECOGEMOS TODA LA INFORMACION DEL ANIME MANDADO DESDE LOS FRAGMENTOS
        Anime anime = null;
        anime = getIntent().getParcelableExtra("Anime");
        tvTituloAnime.setText(anime.getTitulo());
        Picasso.get().load(anime.getImagenGrande()).into(imgAnime);
        if(anime.isEnEmision()){
            tvEmision.setText("En emisión ●");
        }else{
            tvEmision.setText("Finalizado ●");
        }

        tvSinopsisAnime.setText(anime.getSynopsis());
        tvPuntuacion.setText(anime.getPuntuacion() + "");

        CompletarInfoAnimeIndividual(anime);
        AgregarListaEpisodios(anime);

        /**BOTON DE RETROCESO**/
        btnRetroceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    /**PENDIENTE DE SI LLEGAREMOS A USAR LA SINOPSIS DEL EPISODIO**/
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
                    tvNumEpisodios.setText(listaEpisodios.size()+"");

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
                            String trailer = animeDetalles.getJSONObject("trailer").optString("url", "Trailer no disponible");
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

                            // Actualizar los campos del objeto Anime
                            anime.setSynopsis(sinopsis);
                            anime.setPuntuacion(puntuacion);
                            anime.setTrailer(trailer);
                            anime.setImagenPequenia(imagenPequenia);
                            anime.setImagenMediana(imagenMediana);
                            anime.setGeneros(listaGeneros);

                            Log.i("INFO INICIO", "### " + anime.getPuntuacion() + " ### " + anime.getTitulo());

                            // Cargar la información a la UI
                            tvSinopsisAnime.setText(anime.getSynopsis());
                            tvPuntuacion.setText(String.valueOf(anime.getPuntuacion()));


                            String generosText = "";
                            for (String genero : listaGeneros) {
                                generosText += genero+" ";
                            }

                            tvGeneros.setText(generosText);

                            /**METO ESTO AQUI PARA PODER CARGAR EL TRAILER SIN DIFICULTAD O PROBLEMAS DE LECTURA
                             * POR EJECUCION ASINCRONA DE LA API USADA.**/
                            if(anime.getTrailer().equals("Trailer no disponible") || anime.getTrailer().contains("null")){
                                btnReproducir.setVisibility(View.GONE);
                            }else {
                                btnReproducir.setVisibility(View.VISIBLE);
                                btnReproducir.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent = new Intent(getApplicationContext(), ReproductorTrailerAnime.class);
                                        intent.putExtra("Trailer", anime.getTrailer());
                                        startActivity(intent);
                                    }
                                });
                            }

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

}