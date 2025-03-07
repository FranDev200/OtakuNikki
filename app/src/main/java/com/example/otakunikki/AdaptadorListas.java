package com.example.otakunikki;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorListas extends BaseAdapter {

    private List<ListaAnime> listadelistasAnimes;
    private Context context;

    public AdaptadorListas(Context context, List<ListaAnime> listadelistasAnimes) {
        this.context = context;
        this.listadelistasAnimes = listadelistasAnimes;

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
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater li = LayoutInflater.from(context);
        view = li.inflate(R.layout.item_lista_animes, null);

        TextView tvTitulo = view.findViewById(R.id.tvTituloLista);
        TextView tvNroAnimes = view.findViewById(R.id.tvNroAnimesGuardados);
        TextView tvFecha = view.findViewById(R.id.tvFechaModificacion);


        tvTitulo.setText(listadelistasAnimes.get(position).getNombreLista());
        tvNroAnimes.setText(listadelistasAnimes.get(position).getNroAnimes() + " animes");
        tvFecha.setText(listadelistasAnimes.get(position).getFechaModificacion());

        return view;
    }

}
