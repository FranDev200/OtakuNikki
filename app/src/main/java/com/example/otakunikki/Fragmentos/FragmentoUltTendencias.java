package com.example.otakunikki.Fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.otakunikki.Actividades.ActividadVistaDetalleAnime;
import com.example.otakunikki.Adaptadores.AdaptadorAnimesGV;
import com.example.otakunikki.Clases.Anime;
import com.example.otakunikki.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentoUltTendencias extends Fragment {

    private static final String TAG = "EXPLORAR (TENDENCIAS)";
    private RequestQueue requestQueue;
    private StringRequest miStringRequest;
    private String url = "https://api.jikan.moe/v4/seasons/now";
    //--------------------------------------------------------------
    private GridView miGridView;
    private AdaptadorAnimesGV miAdaptador;
    private List<Anime> listaAnimesTemporada;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragmento_ult_tendencias, container, false);

        miGridView = vista.findViewById(R.id.gvUltTendencias);

        /*Adaptador y GridView para las últimas tendencias*/
        miGridView = vista.findViewById(R.id.gvUltTendencias);
        listaAnimesTemporada = new ArrayList<Anime>();
        miAdaptador = new AdaptadorAnimesGV(getActivity(), listaAnimesTemporada);
        miGridView.setAdapter(miAdaptador);

        cargarAnimesTendencia();

        miGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CompletarInfoAnimeIndividual(listaAnimesTemporada.get(position));
                abrirVistaDeDetalleAnime(position);
            }
        });

        /**RETRASO LA EJECUCION DEL CODIGO PARA QUE LOS METODOS DE ARRIBA TENGAN TIEMPO DE CARGAR
         * LA INFO DE LA PETICION A LA API YA QUE SE EJECUTA DE FORMA ASINCRONA Y SI SE HACE ALGO CON
         * LA LISTA INMEDIATAMENTE DESPUÉS DARA FALLO POR NULLPOINTEREXCEPTION**/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!listaAnimesTemporada.isEmpty()) {
                    Log.i(TAG, "--> LISTA: Tamaño de la lista de tendencias: " + listaAnimesTemporada.size());

                } else {
                    Log.i(TAG, "--> LISTA: Aún no hay datos, esperando...");
                }
            }
        }, 1500); // Espera 1,5 segundos antes de revisar la lista
        return vista;

    }

    private void abrirVistaDeDetalleAnime(int position) {

        if (listaAnimesTemporada == null || listaAnimesTemporada.isEmpty()) {
            Toast.makeText(getActivity(), "Cargando datos, intenta de nuevo", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(getActivity().getApplicationContext(), ActividadVistaDetalleAnime.class);
        intent.putExtra("Anime", listaAnimesTemporada.get(position));
        startActivity(intent);
    }

    private void cargarAnimesTendencia() {

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        int numeroAleatorio = (int) (Math.random() * 3) + 1;

        miStringRequest = new StringRequest(Request.Method.GET, url +"?page="+numeroAleatorio, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray dataArray = jsonResponse.getJSONArray("data");

                    if (dataArray.length() == 0) {
                        //Toast.makeText(getActivity().getApplicationContext(), "No hay datos disponibles", Toast.LENGTH_LONG).show();
                        return;
                    }

                    for (int i = 0; i < dataArray.length(); i++) {
                        if(listaAnimesTemporada.size() == 25){

                            /*int nroAleatorio = (int) (Math.random() * 25);

                            String imgTendencia =
                            Picasso.get().load(listaAnimesTemporada.get(nroAleatorio).getImagenMediana()).into();
                            tvTituloInicio.setText(listaAnimesTemporada.get(nroAleatorio).getTitulo());
                            tvSinopsisInicio.setText(listaAnimesTemporada.get(nroAleatorio).getSynopsis());
                            */
                        }

                        JSONObject animeObject = dataArray.getJSONObject(i);
                        int id = animeObject.getInt("mal_id");
                        String titulo = animeObject.optString("title","Titulo no disponible");
                        String synopsis = animeObject.optString("synopsis", "Synopsis no disponible");
                        double score = animeObject.optDouble("score", 0);
                        String trailer = animeObject.getJSONObject("trailer").optString("url", "Trailer no disponible");
                        String imagenGrande = animeObject.getJSONObject("images").getJSONObject("jpg").optString("large_image_url", "Foto no disponible");
                        String imagenMediana = animeObject.getJSONObject("images").getJSONObject("jpg").optString("image_url", "Foto no disponible");
                        String imagenPeqenia =animeObject.getJSONObject("images").getJSONObject("jpg").optString("small_image_url", "Foto no disponible");
                        String estado = animeObject.optString("status", "");

                        // Obtenemos de la lista de géneros
                        JSONArray datosGeneros = animeObject.optJSONArray("genres");
                        List<String> listaGeneros = new ArrayList<>();

                        if (datosGeneros != null) {
                            for (int l = 0; l < datosGeneros.length(); l++) {
                                JSONObject generoObj = datosGeneros.getJSONObject(l);
                                listaGeneros.add(generoObj.optString("name", "Género no disponible"));

                            }
                        }

                        // Creamos el objeto Anime
                        boolean enEmision = !estado.equals("Finished Airing");
                        Anime anime = new Anime(id, titulo,synopsis, score,trailer,imagenGrande,imagenMediana,imagenPeqenia,null, listaGeneros, enEmision, 0, "");

                        if (!listaAnimesTemporada.contains(anime)) {
                            listaAnimesTemporada.add(anime);
                        }

                        Log.i(TAG, "Id: " + id + "++ Titulo: " + titulo + " ++ ImagenG: " + imagenGrande);
                    }

                    // Notificar al adaptador después de agregar los datos
                    miAdaptador.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "Error procesando el JSON de tendencias", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "Error al obtener las últimas tendencias: " + error.getMessage());
            }
        });

        requestQueue.add(miStringRequest);

    }

    private void CompletarInfoAnimeIndividual(Anime anime){
        RequestQueue rqAnimes = Volley.newRequestQueue(getActivity().getApplicationContext());
        String urlAnime = "https://api.jikan.moe/v4/anime/" + anime.getId();

        StringRequest mrqAnimes = new StringRequest(Request.Method.GET, urlAnime,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i(TAG, "Completando info: " + response);
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

                            anime.setSynopsis(sinopsis);
                            anime.setPuntuacion(puntuacion);
                            anime.setTrailer(trailer);
                            anime.setImagenPequenia(imagenPequenia);
                            anime.setImagenMediana(imagenMediana);
                            Log.i(TAG, anime.getPuntuacion() + " ++ " + anime.getTitulo());

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity().getApplicationContext(), "Error procesando los detalles del anime", Toast.LENGTH_LONG).show();
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