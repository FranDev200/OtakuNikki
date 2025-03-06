package com.example.otakunikki;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorLVHorAnimeMenuPrincipal extends RecyclerView.Adapter<AdaptadorLVHorAnimeMenuPrincipal.ViewHolder> implements View.OnClickListener {
    private Context context;
    private List<Anime> listaAnimes;

    private View.OnClickListener listener;
    // Constructor
    public AdaptadorLVHorAnimeMenuPrincipal(Context context, List<Anime> listaAnimes) {
        this.context = context;
        this.listaAnimes = listaAnimes;
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
        holder.nombre.setText(anime.getTitulo());
        Picasso.get().load(anime.getImagenGrande()).into(holder.imagen);

        // Asignar el clic con la posición
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                // Pasamos la posición al listener para que pueda usarla
                listener.onClick(v);
            }
        });
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
