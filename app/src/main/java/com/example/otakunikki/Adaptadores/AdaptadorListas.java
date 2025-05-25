package com.example.otakunikki.Adaptadores;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otakunikki.Clases.ListaAnime;
import com.example.otakunikki.Clases.Traductor;
import com.example.otakunikki.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdaptadorListas extends BaseAdapter {

    private List<ListaAnime> listadelistasAnimes;
    private Context context;
    private String idioma;

    public AdaptadorListas(Context context, List<ListaAnime> listadelistasAnimes, String idioma) {
        this.context = context;
        this.listadelistasAnimes = listadelistasAnimes;
        this.idioma = idioma;
    }

    @Override
    public int getCount() {
        return listadelistasAnimes.size();
    }

    @Override
    public Object getItem(int position) {
        return listadelistasAnimes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView tvTitulo, tvNroAnimes, tvFecha;
        ImageView imgAnime0, imgAnime1, imgAnime2, imgAnime3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lista_animes, parent, false);
            holder = new ViewHolder();

            holder.tvTitulo = convertView.findViewById(R.id.tvTituloLista);
            holder.tvNroAnimes = convertView.findViewById(R.id.tvNroAnimesGuardados);
            holder.tvFecha = convertView.findViewById(R.id.tvFechaModificacion);
            holder.imgAnime0 = convertView.findViewById(R.id.imgAnime0);
            holder.imgAnime1 = convertView.findViewById(R.id.imgAnime1);
            holder.imgAnime2 = convertView.findViewById(R.id.imgAnime2);
            holder.imgAnime3 = convertView.findViewById(R.id.imgAnime3);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ListaAnime lista = listadelistasAnimes.get(position);

        // Título, cantidad, fecha
        holder.tvTitulo.setText(lista.getNombreLista());
        holder.tvNroAnimes.setText(lista.getListaAnimes().size() + " animes");
        holder.tvFecha.setText(lista.getFechaModificacion());

        // Limpiar imágenes antes de cargar nuevas
        holder.imgAnime0.setImageDrawable(null);
        holder.imgAnime1.setImageDrawable(null);
        holder.imgAnime2.setImageDrawable(null);
        holder.imgAnime3.setImageDrawable(null);

        for (int i = 0; i < lista.getListaAnimes().size(); i++) {
            if (lista.getListaAnimes().get(i).getImagenGrande() == null) continue;

            switch (i) {
                case 0:
                    Picasso.get().load(lista.getListaAnimes().get(i).getImagenGrande()).into(holder.imgAnime0);
                    break;
                case 1:
                    Picasso.get().load(lista.getListaAnimes().get(i).getImagenGrande()).into(holder.imgAnime1);
                    break;
                case 2:
                    Picasso.get().load(lista.getListaAnimes().get(i).getImagenGrande()).into(holder.imgAnime2);
                    break;
                case 3:
                    Picasso.get().load(R.drawable.flecha_derecha)
                            .resize(50, 50)
                            .centerCrop()
                            .into(holder.imgAnime3);
                    break;
            }
        }

        // Traducción (opcional, ya lo hacías)
        Traductor.traducirTexto(holder.tvNroAnimes.getText().toString(), "es", idioma, textoTraducido -> holder.tvNroAnimes.setText(textoTraducido));
        Traductor.traducirTexto(holder.tvFecha.getText().toString(), "es", idioma, textoTraducido -> holder.tvFecha.setText(textoTraducido));

        return convertView;
    }
}


