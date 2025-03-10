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
import com.example.otakunikki.Clases.Episodio;
import com.example.otakunikki.Clases.ListaAnime;
import com.example.otakunikki.R;

import java.util.ArrayList;
import java.util.List;


public class FragmentoListas extends Fragment {

    List<ListaAnime> lista_de_listasAnimes;
    AdaptadorListas miAdaptador;

    private ListView miListView;
    private TextView tvNroListas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragmento_listas, container, false);

        tvNroListas = (TextView) vista.findViewById(R.id.tvNroListas);
        miListView = (ListView) vista.findViewById(R.id.lvListasAnimes);

        lista_de_listasAnimes = new ArrayList<ListaAnime>();
        // Crear la lista de animes para la primera lista
        List<Episodio> listaEpisodios = new ArrayList<>();
        listaEpisodios.add(new Episodio(0, "", "", "", false));
        listaEpisodios.add(new Episodio(1, "", "", "", false));
        listaEpisodios.add(new Episodio(2, "", "", "", false));
        listaEpisodios.add(new Episodio(3, "", "", "", false));
        listaEpisodios.add(new Episodio(4, "", "", "", false));

        List<String> listaGeneros = new ArrayList<>();
        listaGeneros.add("Acción");


        List<Anime> animesLista1 = new ArrayList<>();
        animesLista1.add(new Anime(0, "Solo Leveling", "", 0, "https://cdn.myanimelist.net/images/anime/1448/147351l.jpg", "", "", listaEpisodios, listaGeneros,false));
        animesLista1.add(new Anime(1, "Sakamoto Days", "", 0, "https://cdn.myanimelist.net/images/anime/1026/146459l.jpg", "", "", listaEpisodios, listaGeneros,false));
        animesLista1.add(new Anime(2, "Kusuriya no Hitorigoto 2nd Season", "", 0, "https://cdn.myanimelist.net/images/anime/1025/147458l.jpg", "", "", listaEpisodios, listaGeneros,false));
        animesLista1.add(new Anime(3, "Dr. Stone: Science Future", "", 0, "https://cdn.myanimelist.net/images/anime/1403/146479l.jpg", "", "", listaEpisodios, listaGeneros,false));
        animesLista1.add(new Anime(4, "Salaryman ga Isekai ni Ittara Shitennou ni Natta Hanashi", "", 0, "https://cdn.myanimelist.net/images/anime/1668/144352l.jpg", "", "", listaEpisodios, listaGeneros,false));

        List<Anime> animesLista2 = new ArrayList<>();
        animesLista2.add(new Anime(0, "Solo Leveling", "", 0, "https://cdn.myanimelist.net/images/anime/1448/147351l.jpg", "", "", listaEpisodios, listaGeneros,false));
        animesLista2.add(new Anime(1, "Sakamoto Days", "", 0, "https://cdn.myanimelist.net/images/anime/1026/146459l.jpg", "", "", listaEpisodios, listaGeneros,false));

        // Agregar la lista de animes a la lista principal
        lista_de_listasAnimes.add(new ListaAnime("Mi lista 1", animesLista1));

        lista_de_listasAnimes.add(new ListaAnime("Mi lista 2", animesLista2));


        miAdaptador = new AdaptadorListas(getActivity(), lista_de_listasAnimes);
        miListView.setAdapter(miAdaptador);

        miAdaptador.notifyDataSetChanged();

        tvNroListas.setText(lista_de_listasAnimes.size() + " /11 listas");

        miListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ListaAnime listaSeleccionada = lista_de_listasAnimes.get(position);

                Intent intent = new Intent(getActivity().getApplicationContext(), ActividadVistaDetalleListaAnime.class);
                intent.putExtra("ListaAnime", listaSeleccionada);
                startActivity(intent);

            }
        });

        return vista;
    }
}