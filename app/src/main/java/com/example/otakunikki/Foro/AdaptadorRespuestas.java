package com.example.otakunikki.Foro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otakunikki.Clases.HiloForo;
import com.example.otakunikki.R;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AdaptadorRespuestas extends RecyclerView.Adapter<AdaptadorRespuestas.ViewHolder> {

    private List<HiloForo> listaRespuestas;

    public AdaptadorRespuestas(List<HiloForo> listaRespuestas) {
        this.listaRespuestas = listaRespuestas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foro, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HiloForo respuesta = listaRespuestas.get(position);
        holder.tvTitulo.setVisibility(View.GONE); // Las respuestas no tienen título
        holder.tvUsuario.setText(respuesta.getUsuario());
        holder.tvFecha.setText(convertirTimestampAFecha(respuesta.getFecha()));
        holder.tvComentario.setText(respuesta.getComentario());
    }

    @Override
    public int getItemCount() {
        return listaRespuestas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvUsuario, tvFecha, tvComentario;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloForo);
            tvUsuario = itemView.findViewById(R.id.tvUsuarioForo);
            tvFecha = itemView.findViewById(R.id.tvFechaForo);
            tvComentario = itemView.findViewById(R.id.tvComentarioForo);
        }
    }

    private String convertirTimestampAFecha(Timestamp timestamp) {
        if (timestamp != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            return sdf.format(timestamp.toDate());
        }
        return "";
    }
}
