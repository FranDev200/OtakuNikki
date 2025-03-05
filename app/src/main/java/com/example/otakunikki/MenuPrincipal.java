package com.example.otakunikki;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuPrincipal extends AppCompatActivity {
    private RecyclerView listViewHorizontal;
    private AdaptadorLVHorAnimeRecomendaciones adpatdorAnimeRecomendado;
    private List<Anime> listaAnimesRecomendados;
    private RequestQueue requestQueue;
    private StringRequest miStringRequest;
    private String urlRecomendados = "https://api.jikan.moe/v4/recommendations/anime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);

        listViewHorizontal = findViewById(R.id.lvRecomendaciones);
        listViewHorizontal.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listaAnimesRecomendados = new ArrayList<Anime>();
        adpatdorAnimeRecomendado = new AdaptadorLVHorAnimeRecomendaciones(getApplicationContext(), listaAnimesRecomendados);
        listViewHorizontal.setAdapter(adpatdorAnimeRecomendado);
        /*listViewHorizontal.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                // No establecer espaciado (es decir, 0 para todo)
                outRect.set(0, 1, 0, 1);
            }
        });*/
        CargarAnimesRecomendados();

    }

    private void CargarAnimesRecomendados() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        miStringRequest = new StringRequest(Request.Method.GET, urlRecomendados, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("INFO", response.toString());

                    // Parseamos el JSON de recomendaciones
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray dataArray = jsonResponse.getJSONArray("data");

                    // Verificamos si la respuesta tiene datos
                    if (dataArray.length() == 0) {
                        Toast.makeText(getApplicationContext(), "No hay datos disponibles", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Iteramos sobre los datos de "data"
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject animeObject = dataArray.getJSONObject(i);
                        JSONArray entryArray = animeObject.getJSONArray("entry");

                        // Iteramos sobre todos los elementos en "entry"
                        for (int j = 0; j < entryArray.length(); j++) {
                            JSONObject animeEntry = entryArray.getJSONObject(j);

                            // Accedemos a la información del anime
                            int id = animeEntry.getInt("mal_id");
                            String titulo = animeEntry.getString("title");
                            String imagenPequenia = animeEntry.getJSONObject("images").getJSONObject("jpg").getString("small_image_url");

                            // Realizamos una segunda solicitud para obtener la sinopsis del anime usando su mal_id
                            String urlDetallesAnime = "https://api.jikan.moe/v4/anime/" + id;

                            StringRequest animeDetalles = new StringRequest(Request.Method.GET, urlDetallesAnime,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                Log.i("INFO JSON", response);
                                                // Parseamos la respuesta de la solicitud de detalles del anime
                                                JSONObject animeDetallesResponse = new JSONObject(response);
                                                JSONObject animeDetalles = animeDetallesResponse.getJSONObject("data");

                                                /**CON LOS METODOS OPTSTRING Y EL RESTO EVITAMOS LOS VALORES NULOS DE ESTA FORMA CARGAMOS INFO SIEMPRE
                                                 * EN CASO DE SER NULO LE ASIGNAMOS UN VALOR POR DEFECTO**/
                                                String sinopsis = animeDetalles.optString("synopsis", "Sin sinopsis disponible");
                                                double puntuacion = animeDetalles.optDouble("score", 0.0); // Si no existe, 0.0 es el valor predeterminado
                                                String imagenGrande = animeDetalles.optJSONObject("images")
                                                        .optJSONObject("jpg")
                                                        .optString("large_image_url", "URL no disponible");

                                                String imagenMediana = animeDetalles.optJSONObject("images")
                                                        .optJSONObject("jpg")
                                                        .optString("medium_image_url", "URL no disponible");

                                                // Añadimos el anime a la lista
                                                listaAnimesRecomendados.add(new Anime(id, titulo, sinopsis, puntuacion, imagenGrande, imagenMediana, imagenPequenia, null));

                                                // Notificamos al adaptador que se ha actualizado la lista
                                                adpatdorAnimeRecomendado.notifyDataSetChanged();

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

                            // Añadimos la solicitud de detalles del anime a la cola de solicitudes
                            requestQueue.add(animeDetalles);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error procesando el JSON de recomendaciones", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "Error al obtener recomendaciones: " + error.getMessage());
            }
        });

        // Añadimos la solicitud a la cola de solicitudes
        requestQueue.add(miStringRequest);
    }


}