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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentoTodoElAnime extends Fragment {
    private static final String TAG = "EXPLORAR (TODO)";
    private RequestQueue requestQueue;
    private StringRequest miStringRequest;
    private String url = "https://api.jikan.moe/v4/random/anime";
    //--------------------------------------------------------------
    private GridView miGridView;
    private AdaptadorAnimesGV miAdaptador;
    private ArrayList<Anime> listaAnimes;
    @Override
    public void onResume() {
        super.onResume();
        Log.d("FragmentoTodoElAnime", "onResume llamado");
        cargarN_AnimesAleatorios(100);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("FragmentoTodoElAnime", "onCreateView llamado");
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragmento_todo_el_anime, container, false);

        miGridView = vista.findViewById(R.id.gvTodosAnimes);
        listaAnimes = new ArrayList<Anime>();

        miAdaptador = new AdaptadorAnimesGV(getActivity(), listaAnimes);
        miGridView.setAdapter(miAdaptador);

        //cargarN_AnimesAleatorios(1);

        return vista;
    }

    private void cargarN_AnimesAleatorios(int numAnimes) {

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        //Solo queremos mostar 100 random
        for (int i = 0; i < numAnimes; i++) {
            miStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject objeto = new JSONObject(response);

                        JSONObject animeObject = objeto.getJSONObject("data");

                        int id = animeObject.optInt("mal_id", 0);
                        String titulo = animeObject.optString("title", "Sin titulo");
                        String imagenGrande = animeObject.getJSONObject("images").getJSONObject("jpg").optString("large_image_url", "Sin imagen");
                        String imagenPequenya = animeObject.getJSONObject("images").getJSONObject("jpg").optString("small_image_url", "Sin imagen");

                        Anime anime = new Anime(id, titulo, "", 0, imagenGrande, "", imagenPequenya, null);

                        // Evitar duplicados y agregarlo a la lista
                        if (!listaAnimes.contains(anime)) {
                            listaAnimes.add(anime);
                            miAdaptador.notifyDataSetChanged();
                        }

                        Log.i(TAG, "Anime cargado: " + titulo);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error procesando el JSON", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ERROR", "Error al obtener anime aleatorio: " + error.getMessage());
                }
            });

            requestQueue.add(miStringRequest);
        }
    }

}