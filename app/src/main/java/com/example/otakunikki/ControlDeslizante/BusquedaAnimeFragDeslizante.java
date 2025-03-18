package com.example.otakunikki.ControlDeslizante;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BusquedaAnimeFragDeslizante extends BottomSheetDialogFragment {
    private String animeBuscado;
    private List<Anime> listaAnimes;
    private AdaptadorAnimesGV miAdaptador;
    private GridView gvBusquedaAnime;
    private RequestQueue rqAnimes;
    private StringRequest mrqAnimes;
    private String idioma;

    public BusquedaAnimeFragDeslizante(String animeBuscado) {
        this.animeBuscado = animeBuscado;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.busqueda_filtro_anime, container, false);
        SharedPreferences infoIdioma = requireContext().getSharedPreferences("Idiomas", Context.MODE_PRIVATE);
        idioma = infoIdioma.getString("idioma", "es");
        gvBusquedaAnime = vista.findViewById(R.id.gvBusquedaAnime);
        listaAnimes = new ArrayList<Anime>();
        miAdaptador = new AdaptadorAnimesGV(getContext(), listaAnimes, idioma);
        gvBusquedaAnime.setAdapter(miAdaptador);

        CargarAnimesBusqueda();

        gvBusquedaAnime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ActividadVistaDetalleAnime.class);
                intent.putExtra("Anime", listaAnimes.get(position));
                startActivity(intent);
            }
        });
        return vista;
    }

    private void CargarAnimesBusqueda() {
        rqAnimes = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "https://api.jikan.moe/v4/anime?q=" + animeBuscado;

        mrqAnimes = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("INFO INICIO", response);

                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray dataArray = jsonResponse.getJSONArray("data");

                    if (dataArray.length() == 0) {
                        Toast.makeText(getActivity().getApplicationContext(), "No hay datos disponibles", Toast.LENGTH_LONG).show();
                        return;
                    }

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject animeObject = dataArray.getJSONObject(i);

                        int id = animeObject.getInt("mal_id");
                        String titulo = animeObject.getString("title");
                        String imagenGrande = animeObject.getJSONObject("images").getJSONObject("jpg").getString("large_image_url");
                        String estado = animeObject.optString("status", "");

                        // Obtener géneros
                        JSONArray datosGeneros = animeObject.optJSONArray("genres");
                        boolean esHentai = false;

                        if (datosGeneros != null) {
                            for (int j = 0; j < datosGeneros.length(); j++) {
                                JSONObject generoObj = datosGeneros.getJSONObject(j);
                                String generoNombre = generoObj.optString("name", "").toLowerCase();

                                if (generoNombre.equals("hentai")) {
                                    esHentai = true;
                                    break; // Si ya sabemos que es Hentai, salimos del bucle
                                }
                            }
                        }

                        // Si el anime es Hentai, lo ignoramos
                        if (esHentai) {
                            Log.i("ANIME OMITIDO", "Omitido por género Hentai: " + titulo);
                            continue;
                        }

                        // Crear objeto Anime
                        boolean enEmision = !estado.equals("Finished Airing");
                        Anime anime = new Anime(id, titulo, "", 0, "", imagenGrande, "", "", null, null, enEmision, 0, "");

                        // Verificación de duplicados
                        if (!listaAnimes.contains(anime)) {
                            listaAnimes.add(anime);
                        }
                    }

                    // Notificar cambios al adaptador
                    miAdaptador.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR JSON", "Error procesando el JSON de búsqueda: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "Error al obtener la búsqueda: " + error.getMessage());
            }
        });

        rqAnimes.add(mrqAnimes);
    }

}
