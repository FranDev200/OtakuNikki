package com.example.otakunikki.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otakunikki.Clases.Anime;
import com.example.otakunikki.Clases.Traductor;
import com.example.otakunikki.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorLVHorAnimeMenuPrincipal extends RecyclerView.Adapter<AdaptadorLVHorAnimeMenuPrincipal.ViewHolder> implements View.OnClickListener {
    private Context context;
    private List<Anime> listaAnimes;
    private String idioma;
    private View.OnClickListener listener;
    // Constructor
    public AdaptadorLVHorAnimeMenuPrincipal(Context context, List<Anime> listaAnimes, String idioma) {
        this.context = context;
        this.listaAnimes = listaAnimes;
        this.idioma = idioma;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el layout de cada item
        View view = LayoutInflater.from(context).inflate(R.layout.item_anime_recomendados, parent, false);

        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Anime anime = listaAnimes.get(position);
        // Establecemos el nombre y la imagen del anime
        if(!idioma.equals("es")){
        Traductor.traducirTexto(anime.getTitulo(), "en", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                holder.nombre.setText(textoTraducido);
            }
        });
        }else{
            holder.nombre.setText(anime.getTitulo());
        }

        Picasso.get().load(anime.getImagenGrande()).into(holder.imagen);


    }

    @Override
    public int getItemCount() {
        return listaAnimes.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;

    }

    /**EVENTO CLICK PARA INTERACTUAR CON EL LVHORIZONTAL**/
    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        ImageView imagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvTitulo);
            imagen = itemView.findViewById(R.id.imgPortadaAnime);
        }
    }
}
