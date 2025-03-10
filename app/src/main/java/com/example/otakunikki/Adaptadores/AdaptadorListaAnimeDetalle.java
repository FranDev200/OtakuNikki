package com.example.otakunikki.Adaptadores;

import android.content.Context;
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

        TextView tvTitulo = view.findViewById(R.id.tvTitulo);
        TextView tvNumEpisodios = view.findViewById(R.id.tvNumEpisodios);
        TextView tvEnEmision = view.findViewById(R.id.tvEnEmision);
        ImageView imgPortada = view.findViewById(R.id.imgPortadaAnime);
        ImageButton imgFavorito = view.findViewById(R.id.imgFavorito);


/*        Anime anime = listaAnimes.get(position);
        tvTitulo.setText(anime.getTitulo());

        if(anime.getListaEpisodios().size() != 0){

            if(anime.getListaEpisodios().size() == 1){
                tvNumEpisodios.setText(anime.getListaEpisodios().size() + " ep");
            } else {
                tvNumEpisodios.setText(anime.getListaEpisodios().size() + " eps");
            }
        }

        if (anime.isEnEmision()) {
            tvEnEmision.setText("En emision ●");
            tvEnEmision.setTextColor(R.color.rojo);
        } else {
            tvEnEmision.setText("Finalizado");
            tvEnEmision.setTextColor(R.color.black);
        }

        if(anime.getFavorito() == true){
            imgFavorito.setImageResource(R.drawable.heart);
        } else {
            imgFavorito.setImageResource(R.drawable.corazon_vacio);
        }

        Picasso.get().load(anime.getImagenGrande()).into(imgPortada);
*/
        return view;
    }
}
