package com.example.otakunikki;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuPrincipal extends AppCompatActivity {
    private RecyclerView listViewHorizontal;
    private AdaptadorLVHorAnimeRecomendaciones adpatdorAnimeRecomendado;
    private List<Anime> listaAnimesRecomendados;
    private RequestQueue requestQueue;
    private StringRequest miStringRequest;
    private String urlRecomendados = "https://api.jikan.moe/v4/recommendations/anime";
    private BottomNavigationView menu_navegador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);

        listViewHorizontal = findViewById(R.id.lvRecomendaciones);
        listViewHorizontal.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listaAnimesRecomendados = new ArrayList<Anime>();
        adpatdorAnimeRecomendado = new AdaptadorLVHorAnimeRecomendaciones(getApplicationContext(), listaAnimesRecomendados);
        listViewHorizontal.setAdapter(adpatdorAnimeRecomendado);
        menu_navegador = findViewById(R.id.menu_navegador);

        menu_navegador.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.mnu_foro){
                    abrirForo();
                }else if(item.getItemId() == R.id.mnu_explorar){
                    abrirExplorar();
                }else if(item.getItemId() == R.id.mnu_listas){
                    abrirListas();
                }else if(item.getItemId() == R.id.mnu_cuenta){
                    abrirCuenta();
                }
                return true;
            }
        });

        CargarAnimesRecomendados();


    }

    public void abrirCuenta(){
        Intent intent = new Intent(getApplicationContext(), info_usuario.class);
        startActivity(intent);
    }
    public void abrirForo(){
        Intent intent = new Intent(getApplicationContext(), info_usuario.class);
        startActivity(intent);
    }
    public void abrirExplorar(){
        Intent intent = new Intent(getApplicationContext(), Explorar.class);
        startActivity(intent);
    }
    public void abrirListas(){
        Intent intent = new Intent(getApplicationContext(), info_usuario.class);
        startActivity(intent);
    }

    private void CargarAnimesRecomendados() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        miStringRequest = new StringRequest(Request.Method.GET, urlRecomendados, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("INFO", response.toString());

                    // Parseamos el JSON de recomendaciones
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray dataArray = jsonResponse.getJSONArray("data");

                    // Verificamos si la respuesta tiene datos
                    if (dataArray.length() == 0) {
                        Toast.makeText(getApplicationContext(), "No hay datos disponibles", Toast.LENGTH_LONG).show();
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
                                adpatdorAnimeRecomendado.notifyDataSetChanged();
                                if(listaAnimesRecomendados.size() == 10){
                                    break;
                                }
                            }

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error procesando el JSON de recomendaciones", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "Error al obtener recomendaciones: " + error.getMessage());
            }
        });

        // Añadimos la solicitud a la cola de solicitudes
        requestQueue.add(miStringRequest);
    }
}