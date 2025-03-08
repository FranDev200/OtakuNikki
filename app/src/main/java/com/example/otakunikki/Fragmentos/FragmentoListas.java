package com.example.otakunikki.Fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.otakunikki.Actividades.ActividadVistaDetalleListaAnime;
import com.example.otakunikki.Adaptadores.AdaptadorListas;
import com.example.otakunikki.Clases.Anime;
import com.example.otakunikki.Actividades.ListaAnime;
import com.example.otakunikki.R;

import java.util.ArrayList;


public class FragmentoListas extends Fragment {

    ArrayList<ListaAnime> listaAnimes;
    AdaptadorListas miAdaptador;

    private ListView miListView;
    private TextView tvNroListas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragmento_listas, container, false);

        tvNroListas = (TextView) vista.findViewById(R.id.tvNroListas);
        miListView = (ListView) vista.findViewById(R.id.lvListasAnimes);

        listaAnimes = new ArrayList<ListaAnime>();
        // Crear la lista de animes para la primera lista
        ArrayList<Anime> animesLista1 = new ArrayList<>();
        animesLista1.add(new Anime(0, "Solo Leveling", "", 0, "https://cdn.myanimelist.net/images/anime/1448/147351l.jpg", "", "", null, null,false));
        animesLista1.add(new Anime(1, "Sakamoto Days", "", 0, "https://cdn.myanimelist.net/images/anime/1026/146459l.jpg", "", "", null, null,false));
        animesLista1.add(new Anime(2, "Kusuriya no Hitorigoto 2nd Season", "", 0, "https://cdn.myanimelist.net/images/anime/1025/147458l.jpg", "", "", null, null,false));
        animesLista1.add(new Anime(3, "Dr. Stone: Science Future", "", 0, "https://cdn.myanimelist.net/images/anime/1403/146479l.jpg", "", "", null, null,false));
        animesLista1.add(new Anime(4, "Salaryman ga Isekai ni Ittara Shitennou ni Natta Hanashi", "", 0, "https://cdn.myanimelist.net/images/anime/1668/144352l.jpg", "", "", null, null,false));

        ArrayList<Anime> animesLista2 = new ArrayList<>();
        animesLista2.add(new Anime(0, "Solo Leveling", "", 0, "https://cdn.myanimelist.net/images/anime/1448/147351l.jpg", "", "", null, null,false));
        animesLista2.add(new Anime(1, "Sakamoto Days", "", 0, "https://cdn.myanimelist.net/images/anime/1026/146459l.jpg", "", "", null, null,false));

        // Agregar la lista de animes a la lista principal
        listaAnimes.add(new ListaAnime("Mi lista 1", animesLista1));

        listaAnimes.add(new ListaAnime("Mi lista 2", animesLista2));

        listaAnimes.add(new ListaAnime("Mi lista 3", new ArrayList<Anime>()));

        miAdaptador = new AdaptadorListas(getActivity(), listaAnimes);
        miListView.setAdapter(miAdaptador);

        miAdaptador.notifyDataSetChanged();

        tvNroListas.setText(listaAnimes.size() + " /11 listas");

        miListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListaAnime animeSeleccionado = (ListaAnime) parent.getItemAtPosition(position);

                Intent intent = new Intent(view.getContext(), ActividadVistaDetalleListaAnime.class);
                intent.putExtra("ListaAnime", animeSeleccionado);
                startActivity(intent);
            }
        });

        return vista;
    }
}