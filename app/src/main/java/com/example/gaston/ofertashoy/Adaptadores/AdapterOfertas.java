package com.example.gaston.ofertashoy.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gaston.ofertashoy.R;
import com.example.gaston.ofertashoy.Modelo.Comercios;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterOfertas extends RecyclerView.Adapter<AdapterOfertas.OfertasViewHolder>{
List<Comercios>comerciosList;

    public AdapterOfertas(List<Comercios> comerciosList) {
        this.comerciosList = comerciosList;
    }

    @NonNull
    @Override
    public OfertasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_comercios_ofertas,parent,false);

        return new OfertasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfertasViewHolder holder, int position) {
        Comercios comercios = comerciosList.get(position);
        Picasso.get().load(comercios.getLogo()).into(holder.ivcomercioofertaimagen);
        holder.tvcomercioofertanombre.setText(comercios.getNombre());
        holder.tvcomercioofertahorario.setText(comercios.getHorario());
    }

    @Override
    public int getItemCount() {
        return comerciosList.size();
    }

    public class OfertasViewHolder extends RecyclerView.ViewHolder {
        Context context;
        ImageView ivcomercioofertaimagen;
        TextView tvcomercioofertanombre, tvcomercioofertahorario, tvcomercioofertaubicacion;
        public OfertasViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvcomercioofertanombre = itemView.findViewById(R.id.tvComercioOfertaNombre);
            tvcomercioofertahorario = itemView.findViewById(R.id.tvComercioOfertaHorario);
            tvcomercioofertaubicacion = itemView.findViewById(R.id.tvComercioOfertaUbicacion);
            ivcomercioofertaimagen = itemView.findViewById(R.id.ivComercioOfertaImagen);


        }
    }
}
