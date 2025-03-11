package com.example.otakunikki.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otakunikki.Clases.Genero;
import com.example.otakunikki.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorGeneroGV extends BaseAdapter {

    private Context context;
    private List<Genero> listaGeneros;

    public AdaptadorGeneroGV(Context context, List<Genero> generos) {
        this.context = context;
        this.listaGeneros = generos;
    }

    @Override
    public int getCount() {
        return listaGeneros.size();
    }

    @Override
    public Object getItem(int position) {
        return listaGeneros.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater li = LayoutInflater.from(context);
            convertView = li.inflate(R.layout.item_genero, null);
        }

        TextView tvGenero = convertView.findViewById(R.id.tvGenero);
        ImageView imgGenero = convertView.findViewById(R.id.imgGenero);

        Picasso.get()
                .load(listaGeneros.get(position).getImgGenero())
                .resize(900, 750)
                .into(imgGenero);

        tvGenero.setText(listaGeneros.get(position).getNombreGenero().toString());


        return convertView;
    }


}
