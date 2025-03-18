package com.example.otakunikki.Fragmentos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private String idioma;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragmento_ult_tendencias, container, false);
        SharedPreferences infoIdioma = requireContext().getSharedPreferences("Idiomas", Context.MODE_PRIVATE);
        idioma = infoIdioma.getString("idioma", "es");
        miGridView = vista.findViewById(R.id.gvUltTendencias);

        /*Adaptador y GridView para las últimas tendencias*/
        miGridView = vista.findViewById(R.id.gvUltTendencias);
        listaAnimesTemporada = new ArrayList<Anime>();
        miAdaptador = new AdaptadorAnimesGV(getActivity(), listaAnimesTemporada, idioma);
        miGridView.setAdapter(miAdaptador);

        cargarAnimesTendencia();

        miGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                        Toast.makeText(getActivity().getApplicationContext(), "No hay datos disponibles", Toast.LENGTH_LONG).show();
                        return;
                    }

                    for (int i = 0; i < dataArray.length(); i++) {

                        JSONObject animeObject = dataArray.getJSONObject(i);
                        int id = animeObject.getInt("mal_id");
                        String titulo = animeObject.optString("title","Titulo no disponible");
                        String imagenGrande = animeObject.getJSONObject("images").getJSONObject("jpg").optString("large_image_url", "Foto no disponible");

                        Anime anime = new Anime(id, titulo,"", 0,"",imagenGrande,"","",null, null, false, 0, "");

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

}