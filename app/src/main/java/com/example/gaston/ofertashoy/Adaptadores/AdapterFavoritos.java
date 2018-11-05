package com.example.gaston.ofertashoy.Adaptadores;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gaston.ofertashoy.R;
import com.example.gaston.ofertashoy.Modelo.productos;
import com.example.gaston.ofertashoy.util.Preferencias;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterFavoritos extends RecyclerView.Adapter<AdapterFavoritos.FavoritosViewHolder> {
    List<productos> productosList;
    FirebaseFirestore db;

    public AdapterFavoritos(List<productos> productosList) {
        this.productosList = productosList;
    }

    @NonNull
    @Override
    public FavoritosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_favoritos, parent, false);
        return new FavoritosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritosViewHolder holder, int position) {
        productos p = productosList.get(position);
        Picasso.get().load(p.getImagen()).into(holder.ivfotofavoritos);
        holder.tvnombrefavoritos.setText(p.getNombre());
        holder.tvpreciofavorito.setText("$ "+(p.getPrecio()).toString());
        holder.tvdescripcionfavorito.setText(p.getDescripcion());

    }

    @Override
    public int getItemCount() {
        return productosList.size();
    }

    public class FavoritosViewHolder extends RecyclerView.ViewHolder {
        Context context;
        ImageView ivfotofavoritos;
        ImageButton btnborrarfavorito;
        TextView tvnombrefavoritos, tvpreciofavorito, tvdescripcionfavorito;

        public FavoritosViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            db = FirebaseFirestore.getInstance();
            final String s = Preferencias.getString(context,Preferencias.getKeyUser());
            ivfotofavoritos = itemView.findViewById(R.id.ivFotoFavoritos);
            tvdescripcionfavorito = itemView.findViewById(R.id.tvDescripcionFavorito);
            tvnombrefavoritos = itemView.findViewById(R.id.tvNombreFavoritos);
            tvpreciofavorito = itemView.findViewById(R.id.tvPrecioFavorito);
            btnborrarfavorito = itemView.findViewById(R.id.btnBorrarFavorito);
            btnborrarfavorito.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int post = getAdapterPosition();
                    productos m = productosList.get(post);
                    db.collection("usuarios").document(s).collection("favoritos").document(m.getId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("SEBORRO", "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("NOSEBORRO", "Error deleting document", e);
                                }
                            });
                }
            });

        }
    }
}
