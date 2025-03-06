package com.example.otakunikki;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentoTodoElAnime extends Fragment {
    private static final String TAG = "CONEXIÓN";
    private RequestQueue requestQueue;
    private StringRequest miStringRequest;
    private String url = "https://api.jikan.moe/v4/anime";
    //--------------------------------------------------------------
    private GridView miGridView;
    private AdaptadorAnimesGV miAdaptador;
    private ArrayList<Anime> listaAnimes;
    private ImageView imgFotoPrincipal;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragmento_todo_el_anime, container, false);

        miGridView = vista.findViewById(R.id.gvTodosAnimes);
        listaAnimes = new ArrayList<>();

        miAdaptador = new AdaptadorAnimesGV(listaAnimes, getActivity().getApplicationContext());
        miGridView.setAdapter(miAdaptador);

        cargarInfo();

        return vista;
    }

    private void cargarInfo() {
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        miStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

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
                        String imagenPequenia =animeObject.getJSONObject("images").getJSONObject("jpg").getString("small_image_url");

                        Anime anime = new Anime(id, titulo, "", 0, imagenGrande, "", imagenPequenia, null);

                        if (!listaAnimes.contains(anime)) {
                            listaAnimes.add(anime);
                        }
                    }

                    Log.i(TAG, response); // Ver la respuesta JSON
                    // Notificar al adaptador después de agregar los datos
                    miAdaptador.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Error procesando el JSON de tods los animes", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "Error al obtener animes de temporada: " + error.getMessage());
            }
        });

        requestQueue.add(miStringRequest);

    }
}