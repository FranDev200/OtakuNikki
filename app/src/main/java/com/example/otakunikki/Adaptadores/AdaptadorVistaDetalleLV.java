package com.example.otakunikki.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.otakunikki.Clases.Episodio;
import com.example.otakunikki.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorVistaDetalleLV extends BaseAdapter {

    private List<Episodio> listaEpisodios;
    private Context context;

    public AdaptadorVistaDetalleLV(Context context, List<Episodio> listaEpisodios) {
        this.context = context;
        this.listaEpisodios = listaEpisodios;
    }

    @Override
    public int getCount() {
        return listaEpisodios.size();
    }

    @Override
    public Object getItem(int position) {
        return listaEpisodios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaEpisodios.get(position).getIdEpisodio();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_vista_detalle_episodios, parent, false);
        }

        TextView tvNumEp = convertView.findViewById(R.id.tvNumEp);
        TextView tvTitCap = convertView.findViewById(R.id.tvTitCap);
        TextView tvFecha = convertView.findViewById(R.id.tvFecha);
        ImageButton imgBoton = convertView.findViewById(R.id.imgOjo);

        //Recogemos el episodio
        //Episodio episodio = listaEpisodios.get(position);

        tvNumEp.setText(listaEpisodios.get(position).getIdEpisodio() + "");
        tvTitCap.setText(listaEpisodios.get(position).getTitulo());
        tvFecha.setText(listaEpisodios.get(position).getFecha());

        if (listaEpisodios.get(position).isEstaVisto())
            Picasso.get().load(R.drawable.ojo_visto).into(imgBoton); //Si el boolean que comprueba el capitulo está visto = true --> ponemos como imagen el ojo_visto
        else
            Picasso.get().load(R.drawable.ojo_novisto).into(imgBoton); // Justo lo contrario

        return convertView;
    }

}
