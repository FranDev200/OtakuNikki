package com.example.otakunikki.Foro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otakunikki.Clases.Anime;
import com.example.otakunikki.Clases.HiloForo;
import com.example.otakunikki.R;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdaptadorForo extends BaseAdapter {
    private List<HiloForo> listaForo;
    private Context context;

    public AdaptadorForo(List<HiloForo> listaForo, Context context) {
        this.listaForo = listaForo;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listaForo.size(); // Devuelve el tamaño real de la lista
    }

    @Override
    public Object getItem(int position) {
        return listaForo.get(position); // Devuelve el objeto de la posición correspondiente
    }

    @Override
    public long getItemId(int position) {
        return position; // Devuelve el índice de la lista como ID del item
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = LayoutInflater.from(context);
        convertView = li.inflate(R.layout.item_foro,parent, false);

        TextView tvTituloForo = convertView.findViewById(R.id.tvTituloForo);
        TextView tvUsuarioForo = convertView.findViewById(R.id.tvUsuarioForo);
        TextView tvFechaForo = convertView.findViewById(R.id.tvFechaForo);
        TextView tvComentarioForo = convertView.findViewById(R.id.tvComentarioForo);

        HiloForo hilo = listaForo.get(position);

        tvTituloForo.setText(hilo.getTitulo());
        tvUsuarioForo.setText(hilo.getUsuario());
        tvFechaForo.setText(convertirTimestampAFecha(hilo.getFecha()));
        tvComentarioForo.setText(hilo.getComentario());
        return convertView;
    }
    public String convertirTimestampAFecha(Timestamp timestamp) {
        if (timestamp != null) {
            // Obtener el objeto Date del Timestamp
            Date date = timestamp.toDate();

            // Formatear la fecha a un formato legible
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            return sdf.format(date); // "14/04/2025 17:42"
        }
        return ""; // Si el timestamp es nulo, retorna una cadena vacía
    }
}
