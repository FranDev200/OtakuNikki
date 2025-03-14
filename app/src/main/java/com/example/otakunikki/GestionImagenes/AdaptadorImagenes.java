package com.example.otakunikki.GestionImagenes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.otakunikki.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorImagenes extends RecyclerView.Adapter<AdaptadorImagenes.ViewHolder> {
    private List<Integer> listaImagenes;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int imagenResId);
    }

    public AdaptadorImagenes(Context context, List<Integer> listaImagenes, OnItemClickListener listener) {
        this.context = context;
        this.listaImagenes = listaImagenes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_imagen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int imagenResId = listaImagenes.get(position);

        holder.imageView.setOnClickListener(v -> listener.onItemClick(imagenResId));
        Picasso.get().load(imagenResId).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return listaImagenes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgItem);
        }
    }
}
