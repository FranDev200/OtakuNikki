package com.example.otakunikki.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otakunikki.Adaptadores.AdaptadorVistaDetalleLV;
import com.example.otakunikki.Clases.Episodio;
import com.example.otakunikki.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ActividadVistaDetalleAnime extends AppCompatActivity {
    private String[] filtros = {"Filtrar por:","Más antiguo", "Más reciente"};
    Spinner spFiltro;
    //-------------------------------------------------------------------------
    ImageButton btnRetroceso, btnFavoritos;
    ImageView imgAnime;
    TextView tvTituloAnime, tvSinopsisAnime, tvEmision,
            tvPuntuacion, tvGeneros, tvNumEpisodios;
    Button btnAnyadirAnime;
    //-------------------------------------------------------------------------
    ListView lvEpisodios;
    AdaptadorVistaDetalleLV miAdaptadorEp; //El adaptador que usuamos para el lv de episodios
    ArrayList<Episodio> listaEpisodios;
    //-------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_vista_detalle_anime);

        imgAnime = findViewById(R.id.imgAnime);

        btnRetroceso = findViewById(R.id.btnRetroceso);
        btnFavoritos = findViewById(R.id.btnFavoritos);
        btnAnyadirAnime = findViewById(R.id.btnAnyadirAnime);

        tvTituloAnime = findViewById(R.id.tvTituloAnime);
        tvSinopsisAnime = findViewById(R.id.tvSinopsisAnime);
        tvEmision = findViewById(R.id.tvEmision);
        tvPuntuacion = findViewById(R.id.tvPuntuacion);
        tvGeneros = findViewById(R.id.tvGeneros);
        tvNumEpisodios = findViewById(R.id.tvNumEpisodios);

        lvEpisodios = findViewById(R.id.lvEpisodios);

        spFiltro = findViewById(R.id.spFiltro);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, filtros);
        spFiltro.setAdapter(adapter);
        spFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (filtros[position].contentEquals("Filtrar por:")) {

                }
                if (filtros[position].contentEquals("Más antiguo")) {

                }
                if (filtros[position].contentEquals("Más reciente")) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Bundle bundle = getIntent().getExtras();

        int idAnime = bundle.getInt("Id");
        tvTituloAnime.setText(bundle.getString("Titulo"));
        String urlG = bundle.getString("ImagenG");
        Log.i("INFO", urlG);
        Picasso.get()
                .load(urlG)//Esto es donde va la url de la imagen
                .into(imgAnime); // Aqui lo guardo en el ImageView

        ImageView imgPequenya;//

        Log.d("DEBUG", "URL de la imagen: " + bundle.getString("ImagenP"));


        if (bundle.getBoolean("EnEmision") == true){
            tvEmision.setText("En emision ●");
        }
        else{
            tvEmision.setText("Finalizada ●");
        }

    }
}