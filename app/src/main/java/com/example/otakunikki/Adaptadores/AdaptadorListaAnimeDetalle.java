package com.example.otakunikki.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otakunikki.Actividades.ActividadVistaDetalleListaAnime;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater li = LayoutInflater.from(context);
            convertView = li.inflate(R.layout.item_anime_lista_detalle, parent, false);
        }

        TextView tvTituloAnime = convertView.findViewById(R.id.tvTituloAnimeDetalle);
        TextView tvNumEpisodios = convertView.findViewById(R.id.tvNumEpisodiosDetalle);
        TextView tvEnEmision = convertView.findViewById(R.id.tvEnEmisionDetalle);
        ImageView imgPortada = convertView.findViewById(R.id.imgPortadaAnimeDetalle);
        ImageButton imgFavorito = convertView.findViewById(R.id.imgFavoritoDetalle);

        Anime anime = listaAnimes.get(position);

        tvTituloAnime.setText(anime.getTitulo());
        tvNumEpisodios.setText(anime.getListaEpisodios().size() + "");

        Picasso.get().load(anime.getImagenGrande()).into(imgPortada);

        if (anime.isEnEmision()) {
            tvEnEmision.setText("En emisión ●");
        } else {
            tvEnEmision.setText("Finalizado ●");
        }

        // Permitir que los ítems sean clickeables en el ListView
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ActividadVistaDetalleListaAnime) {
                    ((ActividadVistaDetalleListaAnime) context).abrirDetalleAnime(anime);
                }
            }
        });

        return convertView;
    }


}
