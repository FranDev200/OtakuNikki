package com.example.otakunikki.Fragmentos;

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
import com.example.otakunikki.Adaptadores.AdaptadorAnimesGV;
import com.example.otakunikki.Adaptadores.AdaptadorGeneroGV;
import com.example.otakunikki.Clases.Anime;
import com.example.otakunikki.Clases.Genero;
import com.example.otakunikki.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentoGeneros extends Fragment {
    private static final String TAG = "EXPLORAR (GENEROS)";
    private RequestQueue requestQueue;
    private StringRequest miStringRequest;
    //--------------------------------------------------------------
    private GridView miGridView;
    private AdaptadorGeneroGV miAdaptador;
    private List<Genero> listaGeneros;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Comprobamos que en tra en el onCreateView
        //Log.d("FragmentoGeneros", "onCreateView llamado");

        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragmento_generos, container, false);

        miGridView = vista.findViewById(R.id.gvGeneros);
        listaGeneros = new ArrayList<Genero>();

        miAdaptador = new AdaptadorGeneroGV(getActivity(), listaGeneros);
        miGridView.setAdapter(miAdaptador);

        cargar_AnimesGeneros();

        miGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //CompletarInfoAnimeIndividual(listaGeneros.get(position));
                //abrirVistaDeDetalleAnime(position);
            }
        });

        /**RETRASO LA EJECUCION DEL CODIGO PARA QUE LOS METODOS DE ARRIBA TENGAN TIEMPO DE CARGAR
         * LA INFO DE LA PETICION A LA API YA QUE SE EJECUTA DE FORMA ASINCRONA Y SI SE HACE ALGO CON
         * LA LISTA INMEDIATAMENTE DESPUÉS DARA FALLO POR NULLPOINTEREXCEPTION**/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!listaGeneros.isEmpty()) {
                    Log.i(TAG, "Tamaño de la lista  de generos: " + listaGeneros.size());
                } else {
                    Log.i(TAG, "Aún no hay datos, esperando...");
                }
            }
        }, 1500); // Espera 1,5 segundos antes de revisar la lista


        return vista;
    }


    private void cargar_AnimesGeneros() {

        String url = "https://api.jikan.moe/v4/genres/anime";

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        //Solo queremos mostar n generos
        miStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objeto = new JSONObject(response);

                    JSONArray animeArray = objeto.getJSONArray("data");

                    for (int j = 0; j < animeArray.length(); j++) {
                        JSONObject objetoGenero = animeArray.getJSONObject(j);

                        int id = objetoGenero.optInt("mal_id", 0);
                        String nombreGenero = objetoGenero.optString("name", "Sin género");
                        int numAnimes = objetoGenero.optInt("count", 0);

                        String imagenGenero = obtenerImagenPorNombre(nombreGenero);

                        Genero genero = new Genero(id, nombreGenero, numAnimes, imagenGenero);

                        // Evitar duplicados y agregarlo a la lista
                        if (!listaGeneros.contains(genero)) {
                            listaGeneros.add(genero);
                        }

                        Log.i(TAG, "Id: " + id + " Nombre: " +nombreGenero + " Animes del género: " + numAnimes);
                    }

                    miAdaptador.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Error procesando el JSON", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR: " + TAG , "Error al obtener género: " + error.getMessage());
            }
        });

        requestQueue.add(miStringRequest);

    }

    private String obtenerImagenPorNombre(String nombre) {
        if(nombre.equalsIgnoreCase("action"))
            return "https://img.youtube.com/vi/gY5nDXOtv_o/maxresdefault.jpg";
        else if(nombre.equalsIgnoreCase("adventure"))
            return "https://img.youtube.com/vi/-tviZNY6CSw/maxresdefault.jpg";
        else if(nombre.equalsIgnoreCase("comedy"))
            return "https://img.youtube.com/vi/6TN4a0kZuXg/maxresdefault.jpg";
        else if (nombre.equalsIgnoreCase("sports"))
            return "https://img.youtube.com/vi/K4hy46Y-Cf8/maxresdefault.jpg";
        else if(nombre.equalsIgnoreCase("drama"))
            return "https://img.youtube.com/vi/eI8aUqsCovo/maxresdefault.jpg";
        else if(nombre.equalsIgnoreCase("fantasy"))
            return "https://img.youtube.com/vi/xs6xzznC9tY/maxresdefault.jpg";
        else if(nombre.equalsIgnoreCase("horror"))
            return "https://img.youtube.com/vi/5GjS0Vvs8KA/maxresdefault.jpg";
        else if(nombre.equalsIgnoreCase("mystery"))
            return "https://img.youtube.com/vi/35GXUVBRIEM/maxresdefault.jpg";
        else if(nombre.equalsIgnoreCase("romance"))
            return "https://img.youtube.com/vi/XMCqw1vxMnY/maxresdefault.jpg";
        else if(nombre.equalsIgnoreCase("sci-fi"))
            return "https://img.youtube.com/vi/bJVyIXeUznY/maxresdefault.jpg";
        else if(nombre.equalsIgnoreCase("suspense"))
            return "https://img.youtube.com/vi/35GXUVBRIEM/maxresdefault.jpg";
        else
            return "https://img.youtube.com/vi/gY5nDXOtv_o/maxresdefault.jpg";

    }

}