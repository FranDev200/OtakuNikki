package com.example.otakunikki.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otakunikki.Clases.Episodio;
import com.example.otakunikki.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorVistaDetalleLV extends RecyclerView.Adapter<AdaptadorVistaDetalleLV.ViewHolder> implements View.OnClickListener{

    private List<Episodio> listaEpisodios;
    private Context context;

    private View.OnClickListener listener;

    public AdaptadorVistaDetalleLV(Context context, List<Episodio> listaEpisodios) {
        this.context = context;
        this.listaEpisodios = listaEpisodios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vista_detalle_episodios, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Episodio episodio = listaEpisodios.get(position);

        holder.tvNumEp.setText(episodio.getIdEpisodio() + "");
        holder.tvTitCap.setText(episodio.getTitulo());
        holder.tvFecha.setText(episodio.getFecha());
        if(episodio.isEstaVisto()){
            holder.imgVisto.setImageResource(R.drawable.ojo_visto);
        }else{
            holder.imgVisto.setImageResource(R.drawable.ojo_novisto);

        }
    }

    @Override
    public int getItemCount() {
        return listaEpisodios.size();
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
        TextView tvNumEp;
        TextView tvTitCap;
        TextView tvFecha;
        ImageView imgVisto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumEp = itemView.findViewById(R.id.tvNumEp);
            tvTitCap = itemView.findViewById(R.id.tvTitCap);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            imgVisto = itemView.findViewById(R.id.imgOjo);
        }
    }


}
