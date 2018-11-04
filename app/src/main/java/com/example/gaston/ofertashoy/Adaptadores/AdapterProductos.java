package com.example.gaston.ofertashoy.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaston.ofertashoy.R;
import com.example.gaston.ofertashoy.Modelo.productos;
import com.example.gaston.ofertashoy.util.Preferencias;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterProductos extends RecyclerView.Adapter<AdapterProductos.ProdcutosViewHodel> {
  private  List<productos> productosList;
    private FirebaseFirestore db;

    public AdapterProductos(List<productos> productosList) {
        this.productosList = productosList;
    }

    @Override
    public ProdcutosViewHodel onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_productos,parent,false);
        return new ProdcutosViewHodel(v);
    }

    @Override
    public void onBindViewHolder( ProdcutosViewHodel holder, int position) {
        productos p = productosList.get(position);
        holder.ofertanombre.setText(p.getNombre());
        holder.ofertaprecio.setText("$ "+(p.getPrecio()));
        holder.ofertadescripcion.setText(p.getDescripcion());
        Picasso.get().load(p.getImagen()).into(holder.ofertaimagen);

    }

    @Override
    public int getItemCount() {
        return productosList.size();
    }

    public class ProdcutosViewHodel extends RecyclerView.ViewHolder {
        TextView ofertanombre, ofertaprecio, ofertadescripcion;
        ImageView ofertaimagen;
        ImageButton btnfavoritos;
        Context context;
        public ProdcutosViewHodel(final View itemView) {
            super(itemView);
            context = itemView.getContext();
            db = FirebaseFirestore.getInstance();
            btnfavoritos = itemView.findViewById(R.id.btnFavoritos);
            ofertanombre = itemView.findViewById(R.id.tvOfertaNombre);
            ofertaprecio = itemView.findViewById(R.id.tvOfertaPrecio);
            ofertadescripcion = itemView.findViewById(R.id.tvOfertaDescripcion);
            ofertaimagen = itemView.findViewById(R.id.ivOfertaImagen);
            btnfavoritos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int post = getAdapterPosition();
                    productos m = productosList.get(post);
                    String uid= Preferencias.getString(context,Preferencias.getKeyUser());
                    Map<String, Object> usuarios = new HashMap<>();
                    usuarios.put("id", m.getId());
                    usuarios.put("nombre", m.getNombre());
                    usuarios.put("precio", m.getPrecio());
                    usuarios.put("imagen", m.getImagen());
                    usuarios.put("descripcion", m.getDescripcion());
                    usuarios.put("fecha", m.getFecha());
                    db.collection("usuarios").document(uid).collection("favoritos").document(m.getId())
                            .set(usuarios)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Bien", "DocumentSnapshot successfully written!");
                                    Toast.makeText(itemView.getContext(),"Se agrego a mis deseos",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("mal", "Error writing document", e);
                                }
                            });

                }
            });

        }
    }
}
