package com.example.gaston.ofertashoy.Modelo;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gaston.ofertashoy.R;
import com.example.gaston.ofertashoy.VistapreviaInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class TiendaVistapreviaModel implements VistapreviaInterface.model {
    private VistapreviaInterface.Presenter presenter;
    private String a;
    private FirebaseFirestore firestores;
    private StorageReference reference;
    private Context cont;
    private UploadTask uploadTask;
    private ProgressBar progressBar;
    private Dialog dialog;
    int abierto = 0;
    int asd = 0;
    public TiendaVistapreviaModel(VistapreviaInterface.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void cargarmodel(final Tienda tienda, Uri uri) {
        final StorageReference fotoref = reference.child(tienda.getPropietario()).child("Tienda Imagen").child("Imagen");

        uploadTask = fotoref.putFile(uri);


        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                if(asd==0){
                    progres(taskSnapshot);
                }else{
                    asd = 0;
                }
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri uri1 = taskSnapshot.getDownloadUrl();
                Map<String, Object> mitienda = new HashMap<>();
                mitienda.put("nombre", tienda.getNombre());
                mitienda.put("telefono", tienda.getTelefono());
                mitienda.put("propietario", tienda.getPropietario());
                mitienda.put("descripcion", tienda.getDescripcion());
                mitienda.put("descripcionlarga", tienda.getDescripcionLarga());
                mitienda.put("horario", tienda.getHorario());
                mitienda.put("direccion", tienda.getDireccion());
                mitienda.put("email", tienda.getEmail());
                mitienda.put("categorias", tienda.getCategoria());
                mitienda.put("imagen", uri1.toString());
                firestores.collection("comercios").document("categorias").collection("borrar").document(tienda.getPropietario())
                        .set(mitienda)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Bien", "DocumentSnapshot successfully written!");

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

    @Override
    public void referenciasmodel(FirebaseFirestore firestore, StorageReference mStorageRef, Context context) {
        firestores = firestore;
        reference = mStorageRef;
        cont = context;
    }

    public void progres(final UploadTask.TaskSnapshot taskSnapshot) {
        if (abierto == 0) {
            dialog = new Dialog(cont);
            dialog.setContentView(R.layout.pogressbar_mitienda);
            dialog.setCancelable(false);
            progressBar = dialog.findViewById(R.id.pbTienda);
            MaterialButton btnpbCancelar = dialog.findViewById(R.id.btnPbCancelar);
            btnpbCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    asd =1;
                    abierto = 0;
                    uploadTask.cancel();
                    dialog.dismiss();

                }
            });
            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
            progressBar.setProgress((int) progress);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
            abierto = 1;
        } else {
            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
            progressBar.setProgress((int) progress);
            if (progress == 100.0) {
                abierto = 0;
                dialog.dismiss();
            }
        }

    }
}
