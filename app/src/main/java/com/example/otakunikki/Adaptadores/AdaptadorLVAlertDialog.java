package com.example.otakunikki.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otakunikki.Clases.Anime;
import com.example.otakunikki.Clases.ListaAnime;
import com.example.otakunikki.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorLVAlertDialog extends BaseAdapter {

    private List<ListaAnime> listaAnimes;
    private Context context;

    public AdaptadorLVAlertDialog(Context context, List<ListaAnime> listaAnimes) {
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
        return listaAnimes.get(position).getNroAnimes();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater li = LayoutInflater.from(context);
        view = li.inflate(R.layout.item_alertdialog_personalizado, null);

        TextView nombreLista = view.findViewById(R.id.tvNombreListaAlert);
        nombreLista.setText(listaAnimes.get(position).getNombreLista());

        return view;
    }

}
