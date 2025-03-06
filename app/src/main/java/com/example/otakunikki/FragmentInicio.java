package com.example.otakunikki;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentInicio extends Fragment {
    private ImageView imgFotoPrincipal;

    private RecyclerView lvhAnimeRecomendaciones;
    private AdaptadorLVHorAnimeMenuPrincipal adpatdorAnimeRecomendado;
    private List<Anime> listaAnimesRecomendados;
    private RequestQueue rqAnimesRecomendados;
    private StringRequest mrqAnimesRecomendados;
    private String urlRecomendados = "https://api.jikan.moe/v4/recommendations/anime";
    private BottomNavigationView menu_navegador;

    private RecyclerView lvhAnimesTemporada;
    private AdaptadorLVHorAnimeMenuPrincipal adaptadorAnimeTemporada;
    private List<Anime> listaAnimeTemporada;
    private RequestQueue rqAnimesTemporada;
    private StringRequest mrqAnimesTemporada;
    private String urlAnimeTemporada = "https://api.jikan.moe/v4/seasons/now";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_inicio, container, false);


        imgFotoPrincipal = vista.findViewById(R.id.imgFotoPrincipal);
        /**ADAPTADOR Y LISTVIEW PARA RECOMENDACIONES**/
        lvhAnimeRecomendaciones = vista.findViewById(R.id.lvRecomendaciones);
        lvhAnimeRecomendaciones.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        listaAnimesRecomendados = new ArrayList<Anime>();
        adpatdorAnimeRecomendado = new AdaptadorLVHorAnimeMenuPrincipal(getActivity(), listaAnimesRecomendados);
        lvhAnimeRecomendaciones.setAdapter(adpatdorAnimeRecomendado);

        /**ADAPTADOR Y LISTVIEW PARA TEMPORADA**/
        lvhAnimesTemporada = vista.findViewById(R.id.lvAnimesTemporada);
        lvhAnimesTemporada.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        listaAnimeTemporada = new ArrayList<Anime>();
        adaptadorAnimeTemporada = new AdaptadorLVHorAnimeMenuPrincipal(getActivity(), listaAnimeTemporada);
        lvhAnimesTemporada.setAdapter(adaptadorAnimeTemporada);

        CargarAnimesRecomendados();
        CargarAnimesTemporada();



        return vista;
    }

    private void CargarAnimesTemporada() {
        rqAnimesTemporada = Volley.newRequestQueue(getActivity().getApplicationContext());

        mrqAnimesTemporada = new StringRequest(Request.Method.GET, urlAnimeTemporada, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("INFO", response); // Ver la respuesta JSON

                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray dataArray = jsonResponse.getJSONArray("data");

                    if (dataArray.length() == 0) {
                        Toast.makeText(getActivity().getApplicationContext(), "No hay datos disponibles", Toast.LENGTH_LONG).show();
                        return;
                    }

                    for (int i = 0; i < dataArray.length(); i++) {
                        if(listaAnimeTemporada.size() == 10){
                            int nroAleatorio = (int) (Math.random() * 10);
                            Picasso.get().load(listaAnimeTemporada.get(nroAleatorio).getImagenPequenia()).into(imgFotoPrincipal);
                            break;
                        }

                        JSONObject animeObject = dataArray.getJSONObject(i);
                        int id = animeObject.getInt("mal_id");
                        String titulo = animeObject.getString("title");
                        String imagenGrande = animeObject.getJSONObject("images").getJSONObject("jpg").getString("large_image_url");
                        String imagenPeqenia =animeObject.getJSONObject("images").getJSONObject("jpg").getString("small_image_url");
                        Anime anime = new Anime(id, titulo, "", 0, imagenGrande, "", imagenPeqenia, null);

                        if (!listaAnimeTemporada.contains(anime)) {
                            listaAnimeTemporada.add(anime);
                        }
                    }

                    // Notificar al adaptador después de agregar los datos
                    adaptadorAnimeTemporada.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "Error procesando el JSON de temporada", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "Error al obtener animes de temporada: " + error.getMessage());
            }
        });

        rqAnimesTemporada.add(mrqAnimesTemporada);
    }

    private void CargarAnimesRecomendados() {
        rqAnimesRecomendados = Volley.newRequestQueue(getActivity().getApplicationContext());

        mrqAnimesRecomendados = new StringRequest(Request.Method.GET, urlRecomendados, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("INFO", response.toString());

                    // Parseamos el JSON de recomendaciones
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray dataArray = jsonResponse.getJSONArray("data");

                    // Verificamos si la respuesta tiene datos
                    if (dataArray.length() == 0) {
                        Toast.makeText(getActivity().getApplicationContext(), "No hay datos disponibles", Toast.LENGTH_LONG).show();
                        return;
                    }

                    for (int i = 0; i < dataArray.length(); i++) {
                        if(listaAnimesRecomendados.size() == 10){
                            break;
                        }
                        JSONObject animeObject = dataArray.getJSONObject(i);
                        JSONArray entryArray = animeObject.getJSONArray("entry");

                        // Iteramos sobre los elementos en "entry"
                        for (int j = 0; j < entryArray.length(); j++) {
                            JSONObject animeEntry = entryArray.getJSONObject(j);

                            // Accedemos a la información del anime
                            int id = animeEntry.getInt("mal_id");
                            String titulo = animeEntry.getString("title");
                            String imagenGrande = animeEntry.getJSONObject("images").getJSONObject("jpg").getString("large_image_url");

                            // Realizamos una solicitud para obtener la sinopsis del anime usando su mal_id
                            String urlDetallesAnime = "https://api.jikan.moe/v4/anime/" + id;

                            Anime anime = new Anime(id, titulo, "", 0, imagenGrande, "", "", null);
                            if(listaAnimesRecomendados.contains(anime) ){

                            }else{
                                listaAnimesRecomendados.add(anime);

                                if(listaAnimesRecomendados.size() == 10){
                                    adpatdorAnimeRecomendado.notifyDataSetChanged();
                                    break;
                                }
                            }

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "Error procesando el JSON de recomendaciones", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "Error al obtener recomendaciones: " + error.getMessage());
            }
        });

        // Añadimos la solicitud a la cola de solicitudes
        rqAnimesRecomendados.add(mrqAnimesRecomendados);
    }
}