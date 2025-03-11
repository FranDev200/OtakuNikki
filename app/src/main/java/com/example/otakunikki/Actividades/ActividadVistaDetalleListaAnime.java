package com.example.otakunikki.Actividades;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otakunikki.Adaptadores.AdaptadorListaAnimeDetalle;
import com.example.otakunikki.Clases.Anime;
import com.example.otakunikki.Clases.ListaAnime;
import com.example.otakunikki.R;

import java.util.ArrayList;
import java.util.List;

public class ActividadVistaDetalleListaAnime extends AppCompatActivity {

    ListaAnime listaSeleccionada;
    List<Anime> lista_de_animes_guardados;
    AdaptadorListaAnimeDetalle miAdaptador;
    private EditText etTituloLista;
    private TextView tvNroAnimesLista;
    private ListView lvAnimesLista;
    private ImageButton imgRetroceso, imgAniadirAnime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_vista_detalle_lista_anime);

        listaSeleccionada = getIntent().getParcelableExtra("Lista");

        etTituloLista = findViewById(R.id.etTituloListaDetalle);
        tvNroAnimesLista = findViewById(R.id.nroAnimesLista);
        lvAnimesLista = findViewById(R.id.lvAnimesGuardadosLista);
        imgRetroceso = findViewById(R.id.imgRetroceso);

        etTituloLista.setText(listaSeleccionada.getNombreLista());
        tvNroAnimesLista.setText(listaSeleccionada.getNroAnimes() + " animes");




        miAdaptador = new AdaptadorListaAnimeDetalle(getApplicationContext(),listaSeleccionada.getListaAnimes() );
        lvAnimesLista.setAdapter(miAdaptador);
        miAdaptador.notifyDataSetChanged();


        imgRetroceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}