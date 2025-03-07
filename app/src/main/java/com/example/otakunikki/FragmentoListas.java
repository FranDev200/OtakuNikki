package com.example.otakunikki;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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
        listaAnimes.add(new ListaAnime("Mi lista 1", new ArrayList<Anime>(new Anime(0, "Solo Leveling", "", 0, "@drawable/rin", "", "", new ArrayList<Episodio>()).getId())));

        listaAnimes.add(new ListaAnime("Mi lista 2", new ArrayList<Anime>()));

        listaAnimes.add(new ListaAnime("Mi lista 3", new ArrayList<Anime>()));

        miAdaptador = new AdaptadorListas(getActivity(), listaAnimes);
        miListView.setAdapter(miAdaptador);

        miAdaptador.notifyDataSetChanged();

        tvNroListas.setText(listaAnimes.size() + " /11 listas");


        return vista;
    }
}