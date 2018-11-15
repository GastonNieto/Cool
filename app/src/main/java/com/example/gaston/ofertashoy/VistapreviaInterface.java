package com.example.gaston.ofertashoy;

import android.content.Context;
import android.net.Uri;

import com.example.gaston.ofertashoy.Modelo.Tienda;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public interface VistapreviaInterface {
    interface View{
        void pasardatos(Tienda tienda, Uri uri);
    }
    interface Presenter{
        void cargarpresenter (Tienda tienda, Uri uri);
        void referenciaspresenter(FirebaseFirestore firestore, StorageReference mStorageRef, Context context);

    }
    interface model{
        void cargarmodel(Tienda tienda, Uri uri);
        void referenciasmodel(FirebaseFirestore firestore, StorageReference mStorageRef, Context context);


    }
}
