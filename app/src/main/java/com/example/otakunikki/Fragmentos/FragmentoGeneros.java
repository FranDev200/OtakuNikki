package com.example.otakunikki.Fragmentos;

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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.otakunikki.Actividades.ActividadDetalleGenero;
import com.example.otakunikki.Adaptadores.AdaptadorGeneroGV;
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

        miGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Genero generoSeleccionado = listaGeneros.get(position);
                int idGenero = generoSeleccionado.getIdGenero();  // Asegúrate de tener este método en tu clase Genero

                // Crear un Intent para iniciar ActividadDetalleGenero
                Intent intent = new Intent(getActivity(), ActividadDetalleGenero.class);
                intent.putExtra("IdGenero", idGenero);
                startActivity(intent);
            }
        });

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
                        if(listaGeneros.size()==12){
                            break;
                        }

                        JSONObject objetoGenero = animeArray.getJSONObject(j);

                        int id = objetoGenero.optInt("mal_id", 0);
                        String nombreGenero = objetoGenero.optString("name", "Sin género");
                        int numAnimes = objetoGenero.optInt("count", 0);

                        int imagenGenero = obtenerImagenPorNombre(nombreGenero);

                        Genero genero = new Genero(id, nombreGenero, numAnimes, imagenGenero);

                        //Vamos a cargar únicamente 11 géneros diferentes porque hay demasiados y algunos son de un contenido no apropiado para el trabajo
                        if(     nombreGenero.equalsIgnoreCase("action") ||
                                nombreGenero.equalsIgnoreCase("adventure") ||
                                nombreGenero.equalsIgnoreCase("comedy") ||
                                nombreGenero.equalsIgnoreCase("sports") ||
                                nombreGenero.equalsIgnoreCase("drama") ||
                                nombreGenero.equalsIgnoreCase("fantasy") ||
                                nombreGenero.equalsIgnoreCase("horror") ||
                                nombreGenero.equalsIgnoreCase("mystery") ||
                                nombreGenero.equalsIgnoreCase("romance") ||
                                nombreGenero.equalsIgnoreCase("sci-fi") ||
                                nombreGenero.equalsIgnoreCase("suspense")||
                                nombreGenero.equalsIgnoreCase("kids")) {
                            // Evitar duplicados y agregarlo a la lista
                            if (!listaGeneros.contains(genero)) {
                                listaGeneros.add(genero);
                            }
                            Log.i(TAG, "Id: " + id + " Nombre: " + nombreGenero + " Animes del género: " + numAnimes);
                        }
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

    private int obtenerImagenPorNombre(String nombre) {
        if(nombre.equalsIgnoreCase("action"))
            return R.drawable.accion;
        else if(nombre.equalsIgnoreCase("adventure"))
            return R.drawable.aventura;
        else if(nombre.equalsIgnoreCase("comedy"))
            return R.drawable.comedia;
        else if (nombre.equalsIgnoreCase("sports"))
            return R.drawable.deportes;
        else if(nombre.equalsIgnoreCase("drama"))
            return R.drawable.drama;
        else if(nombre.equalsIgnoreCase("fantasy"))
            return R.drawable.fantasia;
        else if(nombre.equalsIgnoreCase("horror"))
            return R.drawable.terror;
        else if(nombre.equalsIgnoreCase("mystery"))
            return R.drawable.misterio;
        else if(nombre.equalsIgnoreCase("romance"))
            return R.drawable.romance;
        else if(nombre.equalsIgnoreCase("sci-fi"))
            return R.drawable.sci_fi;
        else if(nombre.equalsIgnoreCase("suspense"))
            return R.drawable.suspense;
        else if(nombre.equalsIgnoreCase("kids"))
            return R.drawable.kids;
        else
            return R.drawable.accion;

    }

}