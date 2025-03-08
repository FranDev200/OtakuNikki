package com.example.otakunikki.Actividades;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.otakunikki.Adaptadores.AdaptadorAnimesGV;
import com.example.otakunikki.Adaptadores.AdaptadorListaAnimeDetalle;
import com.example.otakunikki.Clases.Anime;
import com.example.otakunikki.Clases.ListaAnime;
import com.example.otakunikki.R;

import java.util.ArrayList;

public class ActividadVistaDetalleListaAnime extends AppCompatActivity {

    ListaAnime listaCreada;
    AdaptadorListaAnimeDetalle miAdaptador;
    private EditText etTituloLista;
    private TextView tvNroAnimesLista;
    private ListView lvAnimesLista;
    private ImageButton imgRetroceso, imgAniadirAnime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_vista_detalle_lista_anime);

        listaCreada =  getIntent().getParcelableExtra("ListaAnime");

        etTituloLista = findViewById(R.id.etTituloListaDetalle);
        tvNroAnimesLista = findViewById(R.id.nroAnimesLista);
        lvAnimesLista = findViewById(R.id.lvAnimesLista);
        imgRetroceso = findViewById(R.id.imgRetroceso);

        etTituloLista.setText(listaCreada.getNombreLista());
        tvNroAnimesLista.setText(listaCreada.getNroAnimes() + " animes");

        miAdaptador = new AdaptadorListaAnimeDetalle(getApplicationContext(), listaCreada.getListaAnimes());
        lvAnimesLista.setAdapter(miAdaptador);

        imgRetroceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}