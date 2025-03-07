package com.example.otakunikki;

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
import android.widget.ImageView;
import android.widget.Toast;

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

public class FragmentoTodoElAnime extends Fragment {
    private static final String TAG = "EXPLORAR (TODO)";
    private RequestQueue requestQueue;
    private StringRequest miStringRequest;
    //--------------------------------------------------------------
    private GridView miGridView;
    private AdaptadorAnimesGV miAdaptador;
    private ArrayList<Anime> listaAnimes;
    private Anime anime;

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
        Log.d("FragmentoTodoElAnime", "onResume llamado");
        cargarN_AnimesAleatorios(25);

        /**RETRASO LA EJECUCION DEL CODIGO PARA QUE LOS METODOS DE ARRIBA TENGAN TIEMPO DE CARGAR
         * LA INFO DE LA PETICION A LA API YA QUE SE EJECUTA DE FORMA ASINCRONA Y SI SE HACE ALGO CON
         * LA LISTA INMEDIATAMENTE DESPUÉS DARA FALLO POR NULLPOINTEREXCEPTION**/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!listaAnimes.isEmpty()) {
                    Log.i(TAG, "Tamaño de la lista  de todo el anime: " + listaAnimes.size());
                } else {
                    Log.i(TAG, "Aún no hay datos, esperando...");
                }
            }
        }, 1500); // Espera 1,5 segundos antes de revisar la lista

        miGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                abrirVistaDeDetalleAnime(position);
            }
        });
        return vista;
    }

    private void abrirVistaDeDetalleAnime(int position) {

        Intent intent = new Intent(getActivity().getApplicationContext(), ActividadVistaDetalleAnime.class);
        intent.putExtra("Id", listaAnimes.get(position).getId());
        intent.putExtra("Titulo", listaAnimes.get(position).getTitulo());
        intent.putExtra("ImagenG", listaAnimes.get(position).getImagenGrande());
        intent.putExtra("ImagenP", listaAnimes.get(position).getImagenPequenia());
        startActivity(intent);
    }

    private void cargarN_AnimesAleatorios(int numAnimes) {

        //Hemos creado un filtro oara cada vez que entras al fragmento que te muestre 25 animes aleatorios de los diferentes generos que añadimos
        /*
         * g-> todas la edades
         * pg->niños
         * pg13-> a partir de 13 años
         * r17->17+ (violencia)
         * */
        String[] filtro = {"g", "pg", "pg13", "r17"};
        int nro = (int) (Math.random() * 3 + 0);
        String url = "https://api.jikan.moe/v4/anime?rating=" + filtro[nro];

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        //Solo queremos mostar 25 random
        miStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objeto = new JSONObject(response);

                    JSONArray animeArray = objeto.getJSONArray("data");

                    for (int j = 0; j < animeArray.length(); j++) {
                        JSONObject objetoAnime = animeArray.getJSONObject(j);

                        int id = objetoAnime.optInt("mal_id", 0);
                        String titulo = objetoAnime.optString("title", "Sin titulo");
                        String imagenGrande = objetoAnime.getJSONObject("images").getJSONObject("jpg").optString("large_image_url", "Sin imagen");
                        String imagenPequenya = objetoAnime.getJSONObject("images").getJSONObject("jpg").optString("small_image_url", "Sin imagen");

                        Anime anime = new Anime(id, titulo, "", 0, imagenGrande, "", imagenPequenya, null);

                        // Evitar duplicados y agregarlo a la lista
                        if (!listaAnimes.contains(anime)) {
                            listaAnimes.add(anime);
                            miAdaptador.notifyDataSetChanged();
                        }

                        Log.i(TAG, "Anime cargado: " + titulo);

                    }

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