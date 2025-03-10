package com.example.otakunikki.ControlDeslizante;

import android.content.Intent;
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


    public BusquedaAnimeFragDeslizante(String animeBuscado) {
        this.animeBuscado = animeBuscado;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.busqueda_filtro_anime, container, false);
        gvBusquedaAnime = vista.findViewById(R.id.gvBusquedaAnime);
        listaAnimes = new ArrayList<Anime>();
        miAdaptador = new AdaptadorAnimesGV(getContext(),listaAnimes);
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
        return  vista;
    }

    private void CargarAnimesBusqueda() {
        rqAnimes = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "https://api.jikan.moe/v4/anime?q="+animeBuscado;

        mrqAnimes = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("INFO INICIO", response.toString());

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


                            // Crear objeto Anime
                            boolean enEmision = !estado.equals("Finished Airing");
                            Anime anime = new Anime(id, titulo, "", 0, imagenGrande, "", "", null, null, enEmision);

                            // Verificación de duplicados
                            if (!listaAnimes.contains(anime)) {
                                listaAnimes.add(anime);

                            }


                    }

                    // Llamar a notifyDataSetChanged() UNA VEZ después de procesar toda la información
                    miAdaptador.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getActivity().getApplicationContext(), "Error procesando el JSON de busqueda", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "Error al obtener la busqueda: " + error.getMessage());
            }
        });

        rqAnimes.add(mrqAnimes);
    }
}
