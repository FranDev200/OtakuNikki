package com.example.otakunikki.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otakunikki.Adaptadores.AdaptadorListaAnimeDetalle;
import com.example.otakunikki.Clases.Anime;
import com.example.otakunikki.Clases.ListaAnime;
import com.example.otakunikki.R;

import java.util.ArrayList;
import java.util.List;

public class ActividadVistaDetalleListaAnime extends AppCompatActivity {

    ListaAnime listaSeleccionada;
    AdaptadorListaAnimeDetalle miAdaptador;
    private EditText etTituloLista;
    private TextView tvNroAnimesLista;
    private ListView lvAnimesLista;
    private ImageButton imgRetroceso, imgAniadirAnime;
    private List<Anime> listaAnime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_vista_detalle_lista_anime);

        listaSeleccionada = getIntent().getParcelableExtra("ListaAnimeSeleccionada");

        etTituloLista = findViewById(R.id.etTituloListaDetalle);
        tvNroAnimesLista = findViewById(R.id.nroAnimesLista);
        lvAnimesLista = findViewById(R.id.lvAnimesGuardadosLista);
        imgRetroceso = findViewById(R.id.imgRetroceso);

        etTituloLista.setText(listaSeleccionada.getNombreLista());
        tvNroAnimesLista.setText(listaSeleccionada.getListaAnimes().size() + " animes");
        listaAnime = new ArrayList<>();
        listaAnime.addAll(listaSeleccionada.getListaAnimes());
        for(Anime aux : listaAnime){
            Log.i("ANIMES", aux.getId() +"@@@@@@@"+aux.getTitulo());
        }


        miAdaptador = new AdaptadorListaAnimeDetalle(getApplicationContext(),listaAnime);
        lvAnimesLista.setAdapter(miAdaptador);

        miAdaptador.notifyDataSetChanged();


       lvAnimesLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Log.i("DEBUG_CLICK", "Item clickeado en posición: " + position); // Verificar en Logcat
               Anime animeSeleccionado = listaSeleccionada.getListaAnimes().get(position);

               abrirDetalleAnime(animeSeleccionado, listaSeleccionada.getNombreLista());
           }
       });


        imgRetroceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    public void abrirDetalleAnime(Anime animeSeleccionado,String nombreLista) {
        if (animeSeleccionado != null) {
            Intent intent = new Intent(ActividadVistaDetalleListaAnime.this, ActividadVistaDetalleAnime.class);
            intent.putExtra("Anime", animeSeleccionado);
            intent.putExtra("NombreLista", nombreLista);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error al abrir el anime", Toast.LENGTH_SHORT).show();
        }
    }

}