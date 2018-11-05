package com.example.gaston.ofertashoy.Adaptadores;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaston.ofertashoy.R;
import com.example.gaston.ofertashoy.Modelo.Lista;
import com.example.gaston.ofertashoy.util.Preferencias;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdapterLista extends RecyclerView.Adapter<AdapterLista.ListaViewHolder> {
    private List<Lista> listaList;
    private FirebaseFirestore db;

    public AdapterLista(List<Lista> listaList) {
        this.listaList = listaList;
    }

    @NonNull
    @Override
    public ListaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_lista, parent, false);

        return new ListaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaViewHolder holder, int position) {
        Lista l = listaList.get(position);
        holder.tvnombreitemlist.setText(l.getNombre());
        holder.tvcantidaditem.setText("Cantidad: " + l.getCantidad().toString());
    }

    @Override
    public int getItemCount() {
        return listaList.size();
    }

    public class ListaViewHolder extends RecyclerView.ViewHolder {
        TextView tvnombreitemlist, tvcantidaditem;
        ImageButton btnmodificaritem, btneliminaritem;
        ImageView ivlistacolor;
        View vlistacolor;
        Context context;

        public ListaViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();
            db = FirebaseFirestore.getInstance();
            final String s = Preferencias.getString(context, Preferencias.getKeyUser());
            btnmodificaritem = itemView.findViewById(R.id.btnEditarItem);
            btneliminaritem = itemView.findViewById(R.id.btnEliminarItem);
            tvnombreitemlist = itemView.findViewById(R.id.tvNombreItemLista);
            tvcantidaditem = itemView.findViewById(R.id.tvCantidadItemLista);
            ivlistacolor = itemView.findViewById(R.id.ivListaColor);
            btnmodificaritem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int post = getAdapterPosition();
                    Lista l = listaList.get(post);
                    nuevoitem(itemView, l);
                }
            });
            btneliminaritem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int post = getAdapterPosition();
                    Lista l = listaList.get(post);

                    db.collection("usuarios").document(s).collection("listacompras").document(l.getId())
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

    public void nuevoitem(final View v, final Lista lis) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        LayoutInflater inflater = LayoutInflater.from(v.getContext());
        View view = inflater.inflate(R.layout.nuevo_item, null);
        Button btnAgregar, btnCancelar;
        final EditText etNombre, etCantidad;
        etNombre = view.findViewById(R.id.nombreitem);
        etCantidad = view.findViewById(R.id.cantidaditem);
        btnAgregar = view.findViewById(R.id.btnAgregarItem);
        btnCancelar = view.findViewById(R.id.btnCancelarItem);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog a = builder.create();
        a.show();
        etNombre.setText(lis.getNombre());
        etCantidad.setText(lis.getCantidad().toString());
        Log.e("tag", lis.getId());
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = Preferencias.getString(v.getContext(), Preferencias.getKeyUser());
                Lista lista = new Lista();
                if (etNombre.getText().toString().isEmpty()) {
                    Toast.makeText(v.getContext(), "Ingrese un nombre", Toast.LENGTH_LONG).show();
                } else if (etCantidad.getText().toString().isEmpty()) {
                    Toast.makeText(v.getContext(), "Ingrese la cantidad", Toast.LENGTH_LONG).show();
                } else {

                    lista.setNombre(etNombre.getText().toString());
                    lista.setCantidad(Integer.parseInt((etCantidad.getText().toString())));

                    Map<String, Object> Lista = new HashMap<>();
                    Lista.put("nombre", lista.getNombre());
                    Lista.put("cantidad", lista.getCantidad());
                    db.collection("usuarios").document(uid).collection("listacompras").document(lis.getId()).set(Lista)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(v.getContext(), "Se modifico", Toast.LENGTH_SHORT).show();
                                    etNombre.setText(null);
                                    etCantidad.setText(null);
                                    etNombre.requestFocus();
                                    a.dismiss();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.dismiss();
            }
        });
    }
}
