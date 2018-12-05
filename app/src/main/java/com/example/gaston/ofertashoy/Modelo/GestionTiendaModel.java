package com.example.gaston.ofertashoy.Modelo;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.example.gaston.ofertashoy.Interfaces.GestionTiendaInterface;
import com.example.gaston.ofertashoy.R;
import com.example.gaston.ofertashoy.util.Preferencias;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class GestionTiendaModel implements GestionTiendaInterface.model {
    private GestionTiendaInterface.presenter presenter;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private Context context;

    public GestionTiendaModel(GestionTiendaInterface.presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void extraerModel() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_cargando);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.show();
        String Usuario = Preferencias.getString(context, Preferencias.getKeyUser());
        DocumentReference documentReference = firestore.collection("comercios").document("categorias")
                .collection("borrar").document(Usuario);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    dialog.dismiss();
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Tienda tienda = snapshot.toObject(Tienda.class);
                    presenter.PasarDatosPresenter(tienda);
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    Tienda tienda = null;
                    presenter.PasarDatosPresenter(tienda);
                    Log.d(TAG, "Current data: null");
                }
            }
        });

        /*get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Tienda tienda = documentSnapshot.toObject(Tienda.class);
                presenter.PasarDatosPresenter(tienda);
               dialog.dismiss();
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });*/
    }

    @Override
    public void referenciasModel(FirebaseFirestore firestore, StorageReference mStorageRef, Context context) {
        this.context = context;
        this.firestore = firestore;
        this.storageReference = mStorageRef;
    }
}
