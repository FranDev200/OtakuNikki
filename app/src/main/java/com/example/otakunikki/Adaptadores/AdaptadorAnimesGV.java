package com.example.otakunikki.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otakunikki.Clases.Anime;
import com.example.otakunikki.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorAnimesGV  extends BaseAdapter {

    private List<Anime> listaAnimes;
    private Context context;

    public AdaptadorAnimesGV(Context context, List<Anime> listaAnimes) {
        this.context = context;
        this.listaAnimes = listaAnimes;
    }

    @Override
    public int getCount() {
        return listaAnimes.size();
    }

    @Override
    public Object getItem(int position) {
        return listaAnimes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaAnimes.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater li = LayoutInflater.from(context);
        view = li.inflate(R.layout.item_anime_recomendados, null);

        ImageView img = view.findViewById(R.id.imgPortadaAnime);
        TextView tvImg = view.findViewById(R.id.tvTitulo);

        Picasso.get()
                .load(listaAnimes.get(position).getImagenGrande())
                .into(img);
        tvImg.setText(listaAnimes.get(position).getTitulo().toString());

        return view;
    }
}
