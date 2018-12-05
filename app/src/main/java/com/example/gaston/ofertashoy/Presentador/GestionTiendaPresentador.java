package com.example.gaston.ofertashoy.Presentador;

import android.content.Context;

import com.example.gaston.ofertashoy.Interfaces.GestionTiendaInterface;
import com.example.gaston.ofertashoy.Modelo.GestionTiendaModel;
import com.example.gaston.ofertashoy.Modelo.Tienda;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public class GestionTiendaPresentador implements GestionTiendaInterface.presenter {
    private GestionTiendaInterface.view view;
    private GestionTiendaInterface.model model;

    public GestionTiendaPresentador(GestionTiendaInterface.view view) {
        this.view = view;
     model = new GestionTiendaModel(this);
    }

    @Override
    public void extraerPresenter() {
        if (view!=null){
            model.extraerModel();
        }
    }

    @Override
    public void referenciasPresenter(FirebaseFirestore firestore, StorageReference mStorageRef, Context context) {
        if(view!=null){
            model.referenciasModel(firestore, mStorageRef, context);
        }
    }

    @Override
    public void PasarDatosPresenter(Tienda tienda) {
        if(model!=null){
            view.extraerView(tienda);
        }
    }
}
