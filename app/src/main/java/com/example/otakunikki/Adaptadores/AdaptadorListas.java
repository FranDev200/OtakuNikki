package com.example.otakunikki.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otakunikki.Clases.ListaAnime;
import com.example.otakunikki.R;
import com.squareup.picasso.Picasso;

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
        ImageView imgAnime0 = view.findViewById(R.id.imgAnime0);
        ImageView imgAnime1 = view.findViewById(R.id.imgAnime1);
        ImageView imgAnime2 = view.findViewById(R.id.imgAnime2);
        ImageView imgAnime3 = view.findViewById(R.id.imgAnime3);



        for (int i = 0; i < listadelistasAnimes.get(position).getListaAnimes().size(); i++){
            if(listadelistasAnimes.get(position).getListaAnimes().size() != 0){
                switch (i){
                    case 0:
                        Picasso.get()
                                .load(listadelistasAnimes.get(position).getListaAnimes().get(i).getImagenGrande())
                                .into(imgAnime0);
                        break;
                    case 1:
                        Picasso.get()
                                .load(listadelistasAnimes.get(position).getListaAnimes().get(i).getImagenGrande())
                                .into(imgAnime1);
                        break;
                    case 2:
                        Picasso.get()
                                .load(listadelistasAnimes.get(position).getListaAnimes().get(i).getImagenGrande())
                                .into(imgAnime2);
                        break;
                    case 3:
                        Picasso.get()
                                .load(R.drawable.flecha_derecha)
                                .resize(50, 50)
                                .centerCrop()
                                .into(imgAnime3);
                        break;
                }
            }
        }

        tvTitulo.setText(listadelistasAnimes.get(position).getNombreLista());
        tvNroAnimes.setText(listadelistasAnimes.get(position).getListaAnimes().size() + " animes");
        //tvFecha.setText(listadelistasAnimes.get(position).getFechaModificacion());

        return view;
    }

}
