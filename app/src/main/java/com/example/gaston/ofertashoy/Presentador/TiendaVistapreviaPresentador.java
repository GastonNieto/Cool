package com.example.gaston.ofertashoy.Presentador;

import android.content.Context;
import android.net.Uri;

import com.example.gaston.ofertashoy.Modelo.Tienda;
import com.example.gaston.ofertashoy.Modelo.TiendaVistapreviaModel;
import com.example.gaston.ofertashoy.VistapreviaInterface;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;


public class TiendaVistapreviaPresentador implements VistapreviaInterface.Presenter {

    private VistapreviaInterface.View view;
    private VistapreviaInterface.model model;

    public TiendaVistapreviaPresentador(VistapreviaInterface.View view) {
        this.view = view;
        model = new TiendaVistapreviaModel(this);
    }



    @Override
    public void cargarpresenter(Tienda tienda, Uri uri) {
        if(view !=null){
            model.cargarmodel(tienda,uri);
        }
    }

    @Override
    public void referenciaspresenter(FirebaseFirestore firestore, StorageReference mStorageRef, Context context) {
        if(view!=null){
            model.referenciasmodel(firestore,mStorageRef,context);
        }
    }



}
