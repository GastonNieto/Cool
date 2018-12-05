package com.example.gaston.ofertashoy.Adaptadores;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gaston.ofertashoy.R;
import com.example.gaston.ofertashoy.Modelo.Tienda;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterOfertas extends RecyclerView.Adapter<AdapterOfertas.OfertasViewHolder>{
List<Tienda> tiendaList;

    public AdapterOfertas(List<Tienda> tiendaList) {
        this.tiendaList = tiendaList;
    }

    @NonNull
    @Override
    public OfertasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_comercios_ofertas,parent,false);

        return new OfertasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfertasViewHolder holder, int position) {
        Tienda tienda = tiendaList.get(position);
        Picasso.get().load(tienda.getImagen()).into(holder.ivcomercioofertaimagen);
        holder.tvcomercioofertanombre.setText(tienda.getNombre());
        holder.tvcomercioofertahorario.setText(tienda.getHorario());
    }

    @Override
    public int getItemCount() {
        return tiendaList.size();
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
