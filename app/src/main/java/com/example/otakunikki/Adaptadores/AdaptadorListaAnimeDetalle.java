package com.example.otakunikki.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otakunikki.Clases.Anime;
import com.example.otakunikki.Clases.Episodio;
import com.example.otakunikki.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorListaAnimeDetalle extends BaseAdapter {

    private List<Anime> listaAnimes;
    private Context context;
    List<Episodio> listaEpisodios;

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
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater li = LayoutInflater.from(context);
        view = li.inflate(R.layout.item_anime_lista_detalle, null);

        TextView tvTituloAnime = view.findViewById(R.id.tvTituloAnimeDetalle);
        TextView tvNumEpisodios = view.findViewById(R.id.tvNumEpisodiosDetalle);
        TextView tvEnEmision = view.findViewById(R.id.tvEnEmisionDetalle);
        ImageView imgPortada = view.findViewById(R.id.imgPortadaAnimeDetalle);
        ImageButton imgFavorito = view.findViewById(R.id.imgFavoritoDetalle);

        tvTituloAnime.setText(listaAnimes.get(position).getTitulo());
        tvNumEpisodios.setText(listaAnimes.get(position).getListaEpisodios().size()+"");

        Picasso.get().load(listaAnimes.get(position).getImagenGrande()).into(imgPortada);
        if(listaAnimes.get(position).isEnEmision()){
            tvEnEmision.setText("En emisión ●");
        }else{
            tvEnEmision.setText("Finalizado ●");
        }


        return view;
    }

}
