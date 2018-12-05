package com.example.gaston.ofertashoy.Interfaces;

import android.content.Context;

import com.example.gaston.ofertashoy.Modelo.Tienda;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public interface GestionTiendaInterface {
    interface view {
        void extraerView(Tienda tienda);

    }

    interface presenter {
        void extraerPresenter();
        void referenciasPresenter(FirebaseFirestore firestore, StorageReference mStorageRef, Context context);
        void PasarDatosPresenter(Tienda tienda);
    }

    interface model {
        void extraerModel();
        void referenciasModel(FirebaseFirestore firestore, StorageReference mStorageRef, Context context);

    }
}
