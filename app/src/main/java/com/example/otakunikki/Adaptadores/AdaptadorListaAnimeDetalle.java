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
import com.example.otakunikki.Clases.Episodio;
import com.example.otakunikki.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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

        /*
        listaEpisodios = new ArrayList<>();
        listaEpisodios.add(new Episodio(0, "Episodio1", "", "", false));
        listaEpisodios.add(new Episodio(1, "Episodio2", "", "", false));
        listaEpisodios.add(new Episodio(2, "Episodio3", "", "", false));
        listaEpisodios.add(new Episodio(3, "Episodio4", "", "", false));
        listaEpisodios.add(new Episodio(4, "Episodio5", "", "", false));

        listaAnimes.get(position).setListaEpisodios(listaEpisodios);*/
        Log.i("LISTA", "=============================================");
        Log.i("LISTA", "Numero de animes en la lista: " + listaAnimes.size());
        Log.i("LISTA", "=============================================");

        Log.i("LISTA", "ID del anime: " + listaAnimes.get(position).getId());
        if(listaAnimes.get(position).getTitulo() != null){
            Log.i("LISTA", "Titulo del anime " + position + ": " + listaAnimes.get(position).getTitulo());
        }else{
            Log.i("LISTA", "Titulo del anime " + position + ": No disponible");
        }

        if(listaAnimes.get(position).getListaEpisodios() != null){
            Log.i("LISTA", "Numero de episodios: " + listaAnimes.get(position).getNroEpisodios());
        }else{
            Log.i("LISTA", "Numero de episodios: No disponible");
        }

        if(listaAnimes.get(position).isEnEmision() == true){
            Log.i("LISTA", "Anime en emision");
        }else if(listaAnimes.get(position).isEnEmision() == false){
            Log.i("LISTA", "Anime finalizado");
        }

        //listaAnimes.get(position).setFavorito(false);
        if(listaAnimes.get(position).getFavorito() == true){
            Log.i("LISTA", "Anime como favorito");
        }else if(listaAnimes.get(position).getFavorito() == false){
            Log.i("LISTA", "Anime no favorito");
        }

        if(listaAnimes.get(position).getGeneros() != null){
            Log.i("LISTA", "Numero de generos del anime: " + listaAnimes.get(position).getGeneros().size());
        }else{
            Log.i("LISTA", "Numeros de generos no disponibles");
        }

        Log.i("LISTA", "=============================================");

        /*
        Anime anime = listaAnimes.get(position);

        if (anime != null) {
            tvTituloAnime.setText(anime.getTitulo() != null ? anime.getTitulo() : "Título no disponible");

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
*/
        return view;
    }

}
