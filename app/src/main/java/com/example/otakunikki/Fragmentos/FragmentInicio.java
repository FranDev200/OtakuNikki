package com.example.otakunikki.Fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.otakunikki.Actividades.ActividadVistaDetalleAnime;
import com.example.otakunikki.Adaptadores.AdaptadorLVHorAnimeMenuPrincipal;
import com.example.otakunikki.Clases.Anime;
import com.example.otakunikki.Clases.Episodio;
import com.example.otakunikki.ControlDeslizante.BusquedaAnimeFragDeslizante;
import com.example.otakunikki.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentInicio extends Fragment {
    private ImageView imgFotoPrincipal;
    private ImageButton imgMostrarTexto;
    private EditText etNombreAnimeBusqueda;
    private ImageButton ibNombreAnimeBusqueda;

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
    private int pagina = 0;

    private TextView tvTituloInicio, tvSinopsisInicio;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_inicio, container, false);
        tvTituloInicio = vista.findViewById(R.id.tvTituloInicio);
        tvSinopsisInicio = vista.findViewById(R.id.tvSinopsisInicio);
        imgMostrarTexto = vista.findViewById(R.id.imgMostrarTexto);
        etNombreAnimeBusqueda = vista.findViewById(R.id.etNombreAnimeBusqueda);
        ibNombreAnimeBusqueda = vista.findViewById(R.id.ibNombreAnimeBusqueda);

        /**METODO PARA BUSCAR ANIME Y DESPLEGAR EL FRAGMENTO DESLIZANTE**/
        ibNombreAnimeBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusquedaAnimeFragDeslizante miBusqueda = new BusquedaAnimeFragDeslizante(etNombreAnimeBusqueda.getText().toString());
                miBusqueda.show(requireActivity().getSupportFragmentManager(), "Busqueda");
                etNombreAnimeBusqueda.setText("");
                etNombreAnimeBusqueda.clearFocus();
            }
        });


        /**METODO PARA EXPANDIR EL TEXT VIEW DE SINOPSIS PARA QUE PODAMOS CONTRAER Y EXPANDIR EL CONTROL**/
        imgMostrarTexto.setOnClickListener(new View.OnClickListener() {
            boolean flag = false;
            @Override
            public void onClick(View v) {
                if(flag){
                    tvSinopsisInicio.setMaxLines(4);
                    flag = false;
                    imgMostrarTexto.setImageResource(R.drawable.plus);
                }else{
                    tvSinopsisInicio.setMaxLines(Integer.MAX_VALUE);
                    flag = true;
                    imgMostrarTexto.setImageResource(R.drawable.menos);
                }
            }
        });

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

        /**DESARROLLAR EL METODO PARA RECOGER LOS CAPITULOS**/
        adpatdorAnimeRecomendado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**RECOJO LA POSICION DEL ITEM DEL LISTVIEW PARA PODER LUEGO COMPLETAR EL ANIME Y AGREGARLE LOS EPISODIOS**/
                int position = lvhAnimeRecomendaciones.getChildAdapterPosition(v);  // Obtén la posición del ítem clickeado
                Anime animeSeleccionado = listaAnimesRecomendados.get(position); // Obtén el anime en esa posición
                EnvioInformacionVistaDetalle(animeSeleccionado);
                Log.i("ANIME ID PRUEBA", animeSeleccionado.getId()+"");
            }
        });


        adaptadorAnimeTemporada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**RECOJO LA POSICION DEL ITEM DEL LISTVIEW PARA PODER LUEGO COMPLETAR EL ANIME Y AGREGARLE LOS EPISODIOS**/
                int position = lvhAnimesTemporada.getChildAdapterPosition(v);  // Obtén la posición del ítem clickeado
                Anime animeSeleccionado = listaAnimeTemporada.get(position); // Obtén el anime en esa posición
                EnvioInformacionVistaDetalle(animeSeleccionado);
            }
        });

        CargarAnimesTemporada();
        CargarAnimesRecomendados();



        /**RETRASO LA EJECUCION DEL CODIGO PARA QUE LOS METODOS DE ARRIBA TENGAN TIEMPO DE CARGAR
         * LA INFO DE LA PETICION A LA API YA QUE SE EJECUTA DE FORMA ASINCRONA Y SI SE HACE ALGO CON
         * LA LISTA INMEDIATAMENTE DESPUÉS DARA FALLO POR NULLPOINTEREXCEPTION**/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!listaAnimeTemporada.isEmpty() && !listaAnimesRecomendados.isEmpty()) {
                    Log.i("LISTA", "Tamaño de la lista temporada: " + listaAnimeTemporada.size());
                    Log.i("LISTA", "Tamaño de la lista recomendados: " + listaAnimesRecomendados.size());

                } else {
                    Log.i("LISTA", "Aún no hay datos, esperando...");
                }
            }
        }, 1500); // Espera 1,5 segundos antes de revisar la lista
        return vista;
    }

    private void EnvioInformacionVistaDetalle(Anime anime){
        Intent intent = new Intent(getActivity().getApplicationContext(), ActividadVistaDetalleAnime.class);
        intent.putExtra("Anime", anime);
        startActivity(intent);

    }



    private void CargarAnimesTemporada() {
        rqAnimesTemporada = Volley.newRequestQueue(getActivity().getApplicationContext());


        mrqAnimesTemporada = new StringRequest(Request.Method.GET, urlAnimeTemporada, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("INFO INICIO", response); // Ver la respuesta JSON

                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray dataArray = jsonResponse.getJSONArray("data");

                    if (dataArray.length() == 0) {
                        //Toast.makeText(getActivity().getApplicationContext(), "No hay datos disponibles", Toast.LENGTH_LONG).show();
                        return;
                    }

                    for (int i = 0; i < dataArray.length(); i++) {
                        if(listaAnimeTemporada.size() == 10){
                            int nroAleatorio = (int) (Math.random() * 10);
                            Picasso.get().load(listaAnimeTemporada.get(nroAleatorio).getImagenMediana()).into(imgFotoPrincipal);
                            tvTituloInicio.setText(listaAnimeTemporada.get(nroAleatorio).getTitulo());
                            tvSinopsisInicio.setText(listaAnimeTemporada.get(nroAleatorio).getSynopsis());
                            break;
                        }

                        JSONObject animeObject = dataArray.getJSONObject(i);
                        int id = animeObject.getInt("mal_id");
                        String titulo = animeObject.optString("title","Titulo no disponible");
                        String synopsis = animeObject.optString("synopsis", "Synopsis no disponible");
                        double score = animeObject.optDouble("score", 0);
                        String trailer = animeObject.getJSONObject("trailer").optString("url", "Trailer no disponible");
                        String imagenGrande = animeObject.getJSONObject("images").getJSONObject("jpg").optString("large_image_url", "Foto no disponible");
                        String imagenMediana = animeObject.getJSONObject("images").getJSONObject("jpg").optString("image_url", "Foto no disponible");
                        String imagenPeqenia =animeObject.getJSONObject("images").getJSONObject("jpg").optString("small_image_url", "Foto no disponible");
                        String estado = animeObject.optString("status", "");

                        // Obtencion de la lista de géneros
                        JSONArray datosGeneros = animeObject.optJSONArray("genres");
                        List<String> listaGeneros = new ArrayList<>(); // Reiniciar la lista en cada iteración

                        if (datosGeneros != null) {
                            for (int l = 0; l < datosGeneros.length(); l++) {
                                JSONObject generoObj = datosGeneros.getJSONObject(l);
                                listaGeneros.add(generoObj.optString("name", "Género no disponible"));

                            }
                        }

                        // Crear objeto Anime
                        boolean enEmision = !estado.equals("Finished Airing");
                        Anime anime = new Anime(id, titulo,synopsis, score,trailer,imagenGrande,imagenMediana,imagenPeqenia,null, listaGeneros, enEmision);

                        if (!listaAnimeTemporada.contains(anime)) {
                            listaAnimeTemporada.add(anime);
                        }
                    }

                    // Notificar al adaptador después de agregar los datos
                    adaptadorAnimeTemporada.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getActivity().getApplicationContext(), "Error procesando el JSON de temporada", Toast.LENGTH_LONG).show();
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
                    Log.i("INFO INICIO", response.toString());

                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray dataArray = jsonResponse.getJSONArray("data");

                    if (dataArray.length() == 0) {
                        //Toast.makeText(getActivity().getApplicationContext(), "No hay datos disponibles", Toast.LENGTH_LONG).show();
                        return;
                    }

                    for (int i = 0; i < dataArray.length(); i++) {
                        if (listaAnimesRecomendados.size() == 10) {
                            break;  // Si ya tenemos 10 animes, salimos del ciclo.
                        }

                        JSONObject animeObject = dataArray.getJSONObject(i);
                        JSONArray entryArray = animeObject.getJSONArray("entry");

                        for (int j = 0; j < entryArray.length(); j++) {
                            JSONObject animeEntry = entryArray.getJSONObject(j);

                            int id = animeEntry.getInt("mal_id");
                            String titulo = animeEntry.getString("title");
                            String imagenGrande = animeEntry.getJSONObject("images").getJSONObject("jpg").getString("large_image_url");
                            String estado = animeObject.optString("status", "");


                            // Crear objeto Anime
                            boolean enEmision = !estado.equals("Finished Airing");
                            Anime anime = new Anime(id, titulo, "", 0, "",imagenGrande, "", "", null, null, enEmision);

                            // Verificación de duplicados
                            if (!listaAnimesRecomendados.contains(anime)) {
                                listaAnimesRecomendados.add(anime);
                                if (listaAnimesRecomendados.size() == 10) {
                                    break; // Si ya tenemos 10 elementos, salimos del bucle
                                }
                            }
                        }

                        if (listaAnimesRecomendados.size() == 10) {
                            break; // Salir del primer bucle también
                        }
                    }

                    // Llamar a notifyDataSetChanged() UNA VEZ después de procesar toda la información
                    adpatdorAnimeRecomendado.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getActivity().getApplicationContext(), "Error procesando el JSON de recomendaciones", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "Error al obtener recomendaciones: " + error.getMessage());
            }
        });

        rqAnimesRecomendados.add(mrqAnimesRecomendados);
    }

}