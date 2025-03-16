package com.example.otakunikki.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otakunikki.Clases.Perfil;
import com.example.otakunikki.R;
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
        if(getItem(i).getImagenPerfil() != 0){
            Picasso.get().load(getItem(i).getImagenPerfil()).resize(750, 750).into(imgView);
        }else{
            Picasso.get().load(R.drawable.imgperfil).resize(750, 750).into(imgView);
        }

        return convertView;
    }
}
