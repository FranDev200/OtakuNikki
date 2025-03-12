package com.example.otakunikki.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otakunikki.Clases.Episodio;
import com.example.otakunikki.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorVistaDetalleLV extends RecyclerView.Adapter<AdaptadorVistaDetalleLV.ViewHolder> {

    private List<Episodio> listaEpisodios;
    private Context context;

    public AdaptadorVistaDetalleLV(Context context, List<Episodio> listaEpisodios) {
        this.context = context;
        this.listaEpisodios = listaEpisodios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vista_detalle_episodios, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Episodio episodio = listaEpisodios.get(position);

        holder.tvNumEp.setText(listaEpisodios.get(position).getIdEpisodio() + "");
        holder.tvTitCap.setText(listaEpisodios.get(position).getTitulo());
        holder.tvFecha.setText(listaEpisodios.get(position).getFecha());

        if (listaEpisodios.get(position).isEstaVisto())
            Picasso.get().load(R.drawable.ojo_visto).into(holder.imgBoton); //Si el boolean que comprueba el capitulo está visto = true --> ponemos como imagen el ojo_visto
        else
            Picasso.get().load(R.drawable.ojo_novisto).into(holder.imgBoton); // Justo lo contrario

    }

    @Override
    public int getItemCount() {
        return listaEpisodios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumEp;
        TextView tvTitCap;
        TextView tvFecha;
        ImageButton imgBoton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumEp = itemView.findViewById(R.id.tvNumEp);
            tvTitCap = itemView.findViewById(R.id.tvTitCap);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            imgBoton = itemView.findViewById(R.id.imgOjo);
        }
    }

}
