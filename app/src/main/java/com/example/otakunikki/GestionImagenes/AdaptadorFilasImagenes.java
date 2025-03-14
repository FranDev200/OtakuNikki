package com.example.otakunikki.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otakunikki.GestionImagenes.AdaptadorImagenes;
import com.example.otakunikki.R;
import java.util.List;
import java.util.Map;

public class AdaptadorFilasImagenes extends RecyclerView.Adapter<AdaptadorFilasImagenes.ViewHolder> {
    private Context context;
    private Map<String, List<Integer>> secciones; // Tema -> Lista de imágenes
    private AdaptadorImagenes.OnItemClickListener listener;

    public AdaptadorFilasImagenes(Context context, Map<String, List<Integer>> secciones, AdaptadorImagenes.OnItemClickListener listener) {
        this.context = context;
        this.secciones = secciones;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_imagenes_filas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String tema = (String) secciones.keySet().toArray()[position];
        holder.tvTituloSeccion.setText(tema);

        // Configurar RecyclerView horizontal para cada fila
        holder.recyclerViewImagenes.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        AdaptadorImagenes imagenAdapter = new AdaptadorImagenes(context, secciones.get(tema), listener);
        holder.recyclerViewImagenes.setAdapter(imagenAdapter);
    }

    @Override
    public int getItemCount() {
        return secciones.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTituloSeccion;
        RecyclerView recyclerViewImagenes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTituloSeccion = itemView.findViewById(R.id.tvTituloSeccion);
            recyclerViewImagenes = itemView.findViewById(R.id.recyclerViewImagenes);
        }
    }
}
