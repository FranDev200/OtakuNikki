package com.example.otakunikki;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorPerfilesGridView extends ArrayAdapter<Perfil> {

    public AdaptadorPerfilesGridView(Context context, int resource, List<Perfil> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_perfiles, parent, false);
        }

        TextView nombrePerfil = convertView.findViewById(R.id.tvNombrePerfilSeleccion);
        ImageView imgView = convertView.findViewById(R.id.imgPerfil);

        nombrePerfil.setText(getItem(i).getNombrePerfil());
        Picasso.get().load(getItem(i).getImagenPerfil()).into(imgView);

        return convertView;
    }
}
