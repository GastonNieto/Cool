package com.example.gaston.ofertashoy.Presentador;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gaston.ofertashoy.Modelo.Tienda;
import com.example.gaston.ofertashoy.Modelo.TiendaVistapreviaModel;
import com.example.gaston.ofertashoy.R;
import com.example.gaston.ofertashoy.Vista.TiendaVistapreviaActivity;
import com.example.gaston.ofertashoy.VistapreviaInterface;
import com.example.gaston.ofertashoy.util.Preferencias;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

public class MiTiendaPresentador implements View.OnClickListener, View.OnTouchListener {
    private FirebaseFirestore firestore;
    private StorageReference mStorageRef;
    private UploadTask uploadTask;
    private Context context;
    private Uri filepath;

    private TextInputLayout tlnombretienda, tltelefono1, tltelefono2, tlemail, tldireccion, tlhorario, tldespcorta, tldesplarga, tlcagetorias, tlimagetienda;
    private TextInputEditText etnombretienda, ettelefono1, ettelefono2, etemail, etdireccion, ethorario, etdepcorta, etdesplarga, etcategorias;
    private MaterialButton btndialogcancelar, btndialogaceptar, btnguardarmitienda;
    private AppCompatImageView ivimagentienda;
    private Tienda mitienda = new Tienda();
    private ArrayList<String> telefonos;
    private ArrayList<String> categorias = new ArrayList<>();
    private ChipGroup cgcategoria;
    private Chip cpropayaccesorios, cpalimentos, cpservicios, cptecnologia, cphogar, cpvehiculo;
    private Dialog dialog;
    private ProgressBar progressBar;
    int test = 0;
    int abierto = 0;

    public MiTiendaPresentador(FirebaseFirestore firestore, StorageReference mStorageRef, Context context) {
        this.firestore = firestore;
        this.mStorageRef = mStorageRef;
        this.context = context;
    }

    public void referencias(View view) {

        btnguardarmitienda = view.findViewById(R.id.btnGuardarTienda);

        cgcategoria = view.findViewById(R.id.cgCategorias);

        tlnombretienda = view.findViewById(R.id.tlNombreTienda);
        etnombretienda = view.findViewById(R.id.etNombreTienda);

        tlcagetorias = view.findViewById(R.id.tlCategorias);
        etcategorias = view.findViewById(R.id.etCategorias);

        tlimagetienda = view.findViewById(R.id.tlImagenTienda);
        ivimagentienda = view.findViewById(R.id.ivImagenTienda);

        tltelefono1 = view.findViewById(R.id.tlTelefono1);
        ettelefono1 = view.findViewById(R.id.etTelefono1);

        tltelefono2 = view.findViewById(R.id.tlTelefono2);
        ettelefono2 = view.findViewById(R.id.etTelefono2);

        tlemail = view.findViewById(R.id.tlEmail);
        etemail = view.findViewById(R.id.etEmail);

        tldireccion = view.findViewById(R.id.tlDireccion);
        etdireccion = view.findViewById(R.id.etDireccion);

        tlhorario = view.findViewById(R.id.tlHorario);
        ethorario = view.findViewById(R.id.etHorario);

        tldespcorta = view.findViewById(R.id.tlDespCorta);
        etdepcorta = view.findViewById(R.id.etDespCorta);

        tldesplarga = view.findViewById(R.id.tlDespLarga);
        etdesplarga = view.findViewById(R.id.etDespLarga);

        btnguardarmitienda.setOnClickListener(this);
        etcategorias.setOnTouchListener(this);
    }

    public void setfoto(Uri uri) {
        filepath = uri;
        Glide.with(context).load(filepath).into(ivimagentienda);


    }

    public void tomarDatos(ArrayList<String> arrayList) {
        test = 0;
        telefonos = new ArrayList<>();
        mitienda = new Tienda();
        String s = Preferencias.getString(context, Preferencias.getKeyUser());
        mitienda.setPropietario(s);
        if (etnombretienda.getText().toString().trim().isEmpty()) {
            tlnombretienda.setHelperTextEnabled(false);
            tlnombretienda.setErrorEnabled(true);
            tlnombretienda.setError("*Error: Ingrese un nombre");

        } else {
            tlnombretienda.setError(null);
            tlnombretienda.setErrorEnabled(false);
            tlnombretienda.setHelperText("*Obligatorio");
            tlnombretienda.setHelperTextEnabled(true);
            mitienda.setNombre(etnombretienda.getText().toString().trim());
            Log.w("lila", mitienda.getNombre());
            test = test + 1;
        }
        if (categorias.isEmpty()) {
            tlcagetorias.setHelperTextEnabled(false);
            tlcagetorias.setErrorEnabled(true);
            tlcagetorias.setError("*Error: Seleccione categoria/as");
        } else {
            tlcagetorias.setError(null);
            tlcagetorias.setErrorEnabled(false);
            tlcagetorias.setHelperText("*Obligatorio");
            tlcagetorias.setHelperTextEnabled(true);
            mitienda.setCategoria(arrayList);
            test = test + 1;
        }
        if (filepath == null) {
            tlimagetienda.setHelperTextEnabled(false);
            tlimagetienda.setErrorEnabled(true);
            tlimagetienda.setError("*Error: Cargue una imagen");
        } else {
            tlimagetienda.setError(null);
            tlimagetienda.setErrorEnabled(false);
            tlimagetienda.setHelperText("*Obligatorio");
            tlimagetienda.setHelperTextEnabled(true);
            //TODO
            test = test + 1;
        }
        if (ettelefono1.getText().toString().trim().isEmpty()) {
            tltelefono1.setHelperTextEnabled(false);
            tltelefono1.setErrorEnabled(true);
            tltelefono1.setError("*Error: Ingrese un telefono/celular");

        } else {
            tltelefono1.setError(null);
            tltelefono1.setErrorEnabled(false);
            tltelefono1.setHelperText("*Obligatorio");
            tltelefono1.setHelperTextEnabled(true);
            telefonos.add(ettelefono1.getText().toString().trim());
            mitienda.setTelefono(telefonos);
            test = test + 1;

        }if (!ettelefono2.getText().toString().trim().isEmpty()){
            telefonos.add(ettelefono2.getText().toString().trim());
            mitienda.setTelefono(telefonos);
        }
        if (!etemail.getText().toString().trim().isEmpty()) {
            mitienda.setEmail(etemail.getText().toString().trim());
        }

        if (etdireccion.getText().toString().trim().isEmpty()) {
            tldireccion.setHelperTextEnabled(false);
            tldireccion.setErrorEnabled(true);
            tldireccion.setError("*Error: Ingrese una dirección");

        } else {
            tldireccion.setError(null);
            tldireccion.setErrorEnabled(false);
            tldireccion.setHelperText("*Obligatorio");
            tldireccion.setHelperTextEnabled(true);
            mitienda.setDireccion(etdireccion.getText().toString().trim());
            test = test + 1;

        }
        if (ethorario.getText().toString().trim().isEmpty()) {
            tlhorario.setHelperTextEnabled(false);
            tlhorario.setErrorEnabled(true);
            tlhorario.setError("*Error: Ingrese días y horarios de apertura y cierre");

        } else {
            tlhorario.setError(null);
            tlhorario.setErrorEnabled(false);
            tlhorario.setHelperText("*Obligatorio");
            tlhorario.setHelperTextEnabled(true);
            mitienda.setHorario(ethorario.getText().toString().trim());
            test = test + 1;

        }
        if (etdepcorta.getText().toString().trim().isEmpty()) {
            tldespcorta.setHelperTextEnabled(false);
            tldespcorta.setErrorEnabled(true);
            tldespcorta.setError("*Error: Ingrese una descripción corta sobre la tienda");

        } else {
            tldespcorta.setError(null);
            tldespcorta.setErrorEnabled(false);
            tldespcorta.setHelperText("*Obligatorio");
            tldespcorta.setHelperTextEnabled(true);
            mitienda.setDescripcion(etdepcorta.getText().toString().trim());
            test = test + 1;

        }
        if (etdesplarga.getText().toString().trim().isEmpty()) {
            tldesplarga.setHelperTextEnabled(false);
            tldesplarga.setErrorEnabled(true);
            tldesplarga.setError("*Error: Ingrese una descripción larga sobre la tienda");

        } else {
            tldesplarga.setError(null);
            tldesplarga.setErrorEnabled(false);
            tldesplarga.setHelperText("*Obligatorio");
            tldesplarga.setHelperTextEnabled(true);
            mitienda.setDescripcionLarga(etdesplarga.getText().toString().trim());
            test = test + 1;

        }
        if (test == 8) {

            Bundle bundle = new Bundle();
            Intent intent = new Intent(context, TiendaVistapreviaActivity.class);
            bundle.putSerializable("tienda", mitienda);
            bundle.putParcelable("uri", filepath);
            intent.putExtras(bundle);
            context.startActivity(intent);

        }
    }

    public void categoriaDialog() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.categorias);
        btndialogcancelar = dialog.findViewById(R.id.btnDialogCancel);
        btndialogaceptar = dialog.findViewById(R.id.btnDialogAceptar);
        dialogReferencias(dialog);
        btndialogaceptar.setOnClickListener(this);
        btndialogcancelar.setOnClickListener(this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.show();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDialogCancel:
                dialog.dismiss();
                break;
            case R.id.btnDialogAceptar:
                dialogAceptar();
                break;
            case R.id.btnGuardarTienda:
                tomarDatos(categorias);


                }


        }


    public void dialogReferencias(Dialog dialog) {

        cpropayaccesorios = dialog.findViewById(R.id.cpRopayaccesorios);
        cpalimentos = dialog.findViewById(R.id.cpAlimentos);
        cpservicios = dialog.findViewById(R.id.cpServicios);
        cptecnologia = dialog.findViewById(R.id.cpTecnologia);
        cphogar = dialog.findViewById(R.id.cpHogar);
        cpvehiculo = dialog.findViewById(R.id.cpVehiculos);
        if (!categorias.isEmpty()) {
            for (String a : categorias) {
                if (a.equals(cpropayaccesorios.getText().toString())) {
                    cpropayaccesorios.setChecked(true);
                }
                if (a.equals(cpalimentos.getText().toString())) {
                    cpalimentos.setChecked(true);
                }
                if (a.equals(cpservicios.getText().toString())) {
                    cpservicios.setChecked(true);
                }
                if (a.equals(cptecnologia.getText().toString())) {
                    cptecnologia.setChecked(true);
                }
                if (a.equals(cphogar.getText().toString())) {
                    cphogar.setChecked(true);
                }
                if (a.equals(cpvehiculo.getText().toString())) {
                    cpvehiculo.setChecked(true);
                }

            }
        }
    }


    public void dialogAceptar() {
        categorias.clear();
        cgcategoria.removeAllViews();
        if (cpropayaccesorios.isChecked()) {
            categorias.add(cpropayaccesorios.getText().toString());
            final Chip chip = new Chip(context);
            chip.setText(cpropayaccesorios.getText().toString());
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < categorias.size(); ) {
                        String aux = categorias.get(i);
                        if (aux.equals(chip.getText().toString())) {
                            categorias.remove(i);
                            cgcategoria.removeView(v);
                        }
                        ++i;
                    }

                }
            });
            cgcategoria
                    .addView(chip);
        }
        if (cpalimentos.isChecked()) {
            categorias.add(cpalimentos.getText().toString());
            final Chip chip = new Chip(context);
            chip.setText(cpalimentos.getText().toString());
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < categorias.size(); ) {
                        String aux = categorias.get(i);
                        if (aux.equals(chip.getText().toString())) {
                            categorias.remove(i);
                            cgcategoria.removeView(v);
                        }
                        ++i;
                    }

                }

            });
            cgcategoria
                    .addView(chip);
        }
        if (cpservicios.isChecked()) {
            categorias.add(cpservicios.getText().toString());
            final Chip chip = new Chip(context);
            chip.setText(cpservicios.getText().toString());
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < categorias.size(); ) {
                        String aux = categorias.get(i);
                        if (aux.equals(chip.getText().toString())) {
                            categorias.remove(i);
                            cgcategoria.removeView(v);
                        }
                        ++i;
                    }

                }

            });
            cgcategoria
                    .addView(chip);
        }
        if (cptecnologia.isChecked()) {
            categorias.add(cptecnologia.getText().toString());
            final Chip chip = new Chip(context);
            chip.setText(cptecnologia.getText().toString());
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < categorias.size(); ) {
                        String aux = categorias.get(i);
                        if (aux.equals(chip.getText().toString())) {
                            categorias.remove(i);
                            cgcategoria.removeView(v);
                        }
                        ++i;
                    }

                }

            });
            cgcategoria
                    .addView(chip);
        }
        if (cphogar.isChecked()) {
            categorias.add(cphogar.getText().toString());
            final Chip chip = new Chip(context);
            chip.setText(cphogar.getText().toString());
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < categorias.size(); ) {
                        String aux = categorias.get(i);
                        if (aux.equals(chip.getText().toString())) {
                            categorias.remove(i);
                            cgcategoria.removeView(v);
                        }
                        ++i;
                    }

                }

            });
            cgcategoria
                    .addView(chip);
        }
        if (cpvehiculo.isChecked()) {
            categorias.add(cpvehiculo.getText().toString());
            final Chip chip = new Chip(context);
            chip.setText(cpvehiculo.getText().toString());
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < categorias.size(); ) {
                        String aux = categorias.get(i);
                        if (aux.equals(chip.getText().toString())) {
                            categorias.remove(i);
                            cgcategoria.removeView(v);
                        }
                        ++i;
                    }

                }

            });
            cgcategoria
                    .addView(chip);
        }
        dialog.dismiss();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.etCategorias:
                etcategorias.setSelectAllOnFocus(true);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    categoriaDialog();
                    return true;


                }
                break;
        }

        return false;
    }

    public boolean checkConnection() {
        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);


        NetworkInfo ninfo = cManager.getActiveNetworkInfo();


        if (ninfo != null && ninfo.isConnected())

        {
            isOnlineNet();
            Toast.makeText(context, "Available", Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(context, "Not Available", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com.ar");

            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

}