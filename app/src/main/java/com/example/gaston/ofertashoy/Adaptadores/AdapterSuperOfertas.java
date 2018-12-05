package com.example.gaston.ofertashoy.Adaptadores;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gaston.ofertashoy.PrincipalActivity;
import com.example.gaston.ofertashoy.R;
import com.example.gaston.ofertashoy.fragment.localFragment;
import com.example.gaston.ofertashoy.Modelo.Tienda;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterSuperOfertas extends RecyclerView.Adapter<AdapterSuperOfertas.MarcasViewHolder> {
    List<Tienda> tiendaList;

    public AdapterSuperOfertas(List<Tienda> tiendaList) {
        this.tiendaList = tiendaList;
    }

    @Override
    public MarcasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_superofertas, parent, false);
        return new MarcasViewHolder(view, tiendaList);
    }

    @Override
    public void onBindViewHolder(MarcasViewHolder holder, int position) {
        Tienda m = tiendaList.get(position);
        holder.tvsuperofertanombre.setText(m.getNombre());
        holder.tvsuperofertahorario.setText(m.getDescripcion());
        Picasso.get().load(m.getImagen()).into(holder.imagen);
    }

    @Override
    public int getItemCount() {
        return tiendaList.size();
    }

    public static class MarcasViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView tvsuperofertanombre, tvsuperofertahorario;
        Context context;
        CardView cardView;

        public MarcasViewHolder(final View itemView, final List<Tienda> tiendaList) {
            super(itemView);
            context = itemView.getContext();
            imagen = itemView.findViewById(R.id.ivMarca);
            tvsuperofertanombre = itemView.findViewById(R.id.tvSuperOfertaNombre);
            tvsuperofertahorario = itemView.findViewById(R.id.tvSuperOfertaHorario);
            cardView = itemView.findViewById(R.id.cardSuperofertas);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int post = getAdapterPosition();
                    Tienda m = tiendaList.get(post);
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Bundle bundle = new Bundle();
                    PrincipalActivity.navItemIndex = 2;
                    bundle.putSerializable("comercio", m);
                    localFragment nextFrag = new localFragment();
                    nextFrag.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_left)
                            .replace(R.id.contenedor, nextFrag, "local")
                            .commit();

                }
            });
        }

    }
}
