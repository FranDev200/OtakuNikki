package com.example.otakunikki.Fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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
    List<Anime> animesLista1;
    List<Anime> animesLista2;
    List<Episodio> listaEpisodios;
    List<String> listaGeneros;
    AdaptadorListas miAdaptador;
    ListaAnime listaSeleccionada;
    private ListView miListView;
    private TextView tvNroListas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragmento_listas, container, false);

        tvNroListas = (TextView) vista.findViewById(R.id.tvNroListas);
        miListView = (ListView) vista.findViewById(R.id.lvListasAnimes);

        listaEpisodios = new ArrayList<>();
        listaEpisodios.add(new Episodio(0, "Episodio1", "", "", false));
        listaEpisodios.add(new Episodio(1, "Episodio2", "", "", false));
        listaEpisodios.add(new Episodio(2, "Episodio3", "", "", false));
        listaEpisodios.add(new Episodio(3, "Episodio4", "", "", false));
        listaEpisodios.add(new Episodio(4, "Episodio5", "", "", false));

        listaGeneros = new ArrayList<>();
        listaGeneros.add("Acción");
        listaGeneros.add("Aventura");
        listaGeneros.add("Drama");
        listaGeneros.add("Fantasía");
        listaGeneros.add("Supernatural");

        Anime anime = new Anime(0, "Solo Leveling", "", 0, "","https://cdn.myanimelist.net/images/anime/1448/147351l.jpg", "", "", listaEpisodios, listaGeneros,true);
        Anime anime2 = new Anime(2, "Kusuriya no Hitorigoto 2nd Season", "", 0, "","https://cdn.myanimelist.net/images/anime/1025/147458l.jpg", "", "", listaEpisodios, listaGeneros,false);
        Anime anime3 = new Anime(3, "Dr. Stone: Science Future", "", 0, "","https://cdn.myanimelist.net/images/anime/1403/146479l.jpg", "", "", listaEpisodios, listaGeneros,false);

        animesLista1 = new ArrayList<Anime>();
        animesLista1.add(anime);
        animesLista1.add(anime2);


        animesLista2 = new ArrayList<Anime>();
        animesLista2.add(new Anime(5, "Solo Leveling", "", 0, "","https://cdn.myanimelist.net/images/anime/1448/147351l.jpg", "", "", listaEpisodios, listaGeneros,false));
        animesLista2.add(anime3);

        // Agregar la lista de animes a la lista principal
        lista_de_listasAnimes = new ArrayList<ListaAnime>();
        lista_de_listasAnimes.add(new ListaAnime("Mi lista 1", animesLista1));
        lista_de_listasAnimes.add(new ListaAnime("Mi lista 2", animesLista2));

        /**NOTIFICAMOS EL CAMBIO**/
        miAdaptador = new AdaptadorListas(getActivity().getApplicationContext(), lista_de_listasAnimes);
        miListView.setAdapter(miAdaptador);

        miAdaptador.notifyDataSetChanged();

        tvNroListas.setText(lista_de_listasAnimes.size() + " /11 listas");

        miListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ListaAnime lista = lista_de_listasAnimes.get(position);
                //Log.i("LISTA", "Nombre de la lista: " + listaSeleccionada.getNombreLista());
                //Log.i("LISTA", "Numero de animes (con el atributo): " + listaSeleccionada.getNroAnimes());
                //Log.i("LISTA", "Numero de animes (con el .size): " + listaSeleccionada.getListaAnimes().size());

                Intent intent = new Intent(getActivity(), ActividadVistaDetalleListaAnime.class);
                intent.putExtra("Lista", lista);
                startActivity(intent);

            }
        });

        return vista;
    }
}