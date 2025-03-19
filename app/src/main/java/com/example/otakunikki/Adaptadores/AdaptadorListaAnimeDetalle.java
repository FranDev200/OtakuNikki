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
import com.example.otakunikki.Clases.Traductor;
import com.example.otakunikki.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorListaAnimeDetalle extends BaseAdapter {

    private List<Anime> listaAnimes;
    private Context context;
    List<Episodio> listaEpisodios;
    private String idioma;

    public AdaptadorListaAnimeDetalle(Context context, List<Anime> listaAnimes, String idioma) {
        this.context = context;
        this.listaAnimes = listaAnimes;
        this.idioma = idioma;
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
        ImageView imgFavorito = convertView.findViewById(R.id.imgFavoritoDetalle);

        Anime anime = listaAnimes.get(position);

        tvTituloAnime.setText(anime.getTitulo());
        tvNumEpisodios.setText(anime.getNroEpisodios() + "");

        if (anime.isFavorito()) {
            imgFavorito.setImageResource(R.drawable.heart);
        } else {
            imgFavorito.setImageResource(R.drawable.corazon_vacio);
        }
        Picasso.get().load(anime.getImagenGrande()).into(imgPortada);

        if (anime.isEnEmision()) {
            tvEnEmision.setText("En emisión ●");
        } else {
            tvEnEmision.setText("Finalizado ●");
        }

        /**TRADUCIR CONTROLES YA DEFINIDOS**/
        Traductor.traducirTexto(tvTituloAnime.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvTituloAnime.setText(textoTraducido);
            }
        });

        Traductor.traducirTexto(tvNumEpisodios.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvNumEpisodios.setText(textoTraducido);
            }
        });

        Traductor.traducirTexto(tvEnEmision.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvEnEmision.setText(textoTraducido);
            }
        });

        return convertView;
    }


}
