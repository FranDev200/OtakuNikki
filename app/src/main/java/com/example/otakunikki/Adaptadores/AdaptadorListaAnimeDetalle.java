package com.example.otakunikki.Adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otakunikki.Clases.Anime;
import com.example.otakunikki.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorListaAnimeDetalle extends BaseAdapter {

    private List<Anime> listaAnimes;
    private Context context;

    public AdaptadorListaAnimeDetalle(Context context, List<Anime> listaAnimes) {
        this.context = context;
        this.listaAnimes = listaAnimes;

    }

    @Override
    public int getCount() {
        return listaAnimes.size();
    }

    @Override
    public Object getItem(int position) {
        return listaAnimes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaAnimes.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater li = LayoutInflater.from(context);
        view = li.inflate(R.layout.item_anime_lista_detalle, null);

        TextView tvTitulo = view.findViewById(R.id.tvTituloListaDetalle);
        TextView tvNumEpisodios = view.findViewById(R.id.tvNumEpisodios);
        TextView tvEnEmision = view.findViewById(R.id.tvEnEmision);
        ImageView imgPortada = view.findViewById(R.id.imgPortadaAnime);
        ImageButton imgFavorito = view.findViewById(R.id.imgFavorito);

        Anime anime = listaAnimes.get(position);

        if (anime != null) {
            tvTitulo.setText(anime.getTitulo() != null ? anime.getTitulo() : "Título no disponible");

            // Comprobar si la lista de episodios no es null ni vacía
            if (anime.getListaEpisodios() != null && !anime.getListaEpisodios().isEmpty()) {
                if (anime.getListaEpisodios().size() == 1) {
                    tvNumEpisodios.setText(anime.getListaEpisodios().size() + " ep");
                } else {
                    tvNumEpisodios.setText(anime.getListaEpisodios().size() + " eps");
                }
            } else {
                tvNumEpisodios.setText("No disponible");
            }

            // Comprobar si el anime está en emisión
            if (anime.isEnEmision()) {
                tvEnEmision.setText("En emisión ●");
                tvEnEmision.setTextColor(context.getResources().getColor(R.color.rojo));
            } else {
                tvEnEmision.setText("Finalizado");
                tvEnEmision.setTextColor(context.getResources().getColor(R.color.black));
            }

            // Verificar si el anime es favorito
            if (anime.getFavorito()) {
                imgFavorito.setImageResource(R.drawable.heart);
            } else {
                imgFavorito.setImageResource(R.drawable.corazon_vacio);
            }

            // Cargar la imagen utilizando Picasso, y verificar que la URL no sea null
            if (anime.getImagenGrande() != null && !anime.getImagenGrande().isEmpty()) {
                Picasso.get().load(anime.getImagenGrande()).into(imgPortada);
            } else {
                 // Imagen por defecto en caso de error
            }
        } else {
            Log.e("AdaptadorListaAnimeDetalle", "El anime en la posición " + position + " es nulo");
        }

        return view;
    }

}
