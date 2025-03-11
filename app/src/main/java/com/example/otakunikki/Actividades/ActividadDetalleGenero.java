package com.example.otakunikki.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.otakunikki.Adaptadores.AdaptadorAnimesGV;
import com.example.otakunikki.Clases.Anime;
import com.example.otakunikki.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActividadDetalleGenero extends AppCompatActivity {
    private static final String TAG = "DETALLE_GENERO";
    private ImageButton imgRetroceso;
    private GridView gvAnimes;
    private AdaptadorAnimesGV adaptadorAnimes;
    private List<Anime> listaAnimes;
    private int idGenero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_detalle_genero);

        imgRetroceso = findViewById(R.id.imgRetroceso);
        gvAnimes = findViewById(R.id.gvGeneros);
        listaAnimes = new ArrayList<>();
        adaptadorAnimes = new AdaptadorAnimesGV(this, listaAnimes);
        gvAnimes.setAdapter(adaptadorAnimes);

        // Obtener el ID del género desde el intent
        idGenero = getIntent().getIntExtra("IdGenero", -1);

        if (idGenero != -1) {
            cargarAnimesPorGenero(idGenero);
        } else {
            Toast.makeText(this, "Error al obtener el ID del género", Toast.LENGTH_SHORT).show();
        }

        imgRetroceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void cargarAnimesPorGenero(int idGenero) {
        String url = "https://api.jikan.moe/v4/anime?genres=" + idGenero;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray dataArray = jsonResponse.getJSONArray("data");

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject animeJson = dataArray.getJSONObject(i);
                        int id = animeJson.optInt("mal_id", 0);
                        String titulo = animeJson.optString("title", "Sin título");
                        String imagenG = animeJson.getJSONObject("images").getJSONObject("jpg").optString("large_image_url", "");

                        Anime anime = new Anime(id, titulo, "", 0d, "",  imagenG, "", "",null, null, false);
                        
                        // Evitar duplicados y agregarlo a la lista
                        if (!listaAnimes.contains(anime)) {
                            listaAnimes.add(anime);
                        }
                        Log.i(TAG, id +" ++ "+ " --> Anime cargado: " + titulo +  " ++ "+ imagenG );
                    }

                    adaptadorAnimes.notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(ActividadDetalleGenero.this, "Error procesando JSON", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error al obtener animes por género", error);
            }
        });

        requestQueue.add(stringRequest);
    }
}
