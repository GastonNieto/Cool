package com.example.gaston.ofertashoy.Vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.gaston.ofertashoy.Modelo.Tienda;
import com.example.gaston.ofertashoy.R;
import com.example.gaston.ofertashoy.util.Preferencias;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class EditarTiendaActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private Uri filepath;
    private final int PICK_IMAGE = 50;
    Context context;
    private TextInputLayout tlnombretienda, tltelefono1, tltelefono2, tlemail, tldireccion, tlhorario, tldespcorta, tldesplarga, tlcagetorias, tlimagetienda;
    private TextInputEditText etnombretienda, ettelefono1, ettelefono2, etemail, etdireccion, ethorario, etdepcorta, etdesplarga, etcategorias;
    private MaterialButton btndialogcancelar, btndialogaceptar, btnguardarmitienda, btnimagentienda;
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

    private Tienda tienda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_tienda);
        context = this;
        referencias();
        Bundle recibido = getIntent().getExtras();
        tienda = null;
        if(recibido !=null){
            tienda = (Tienda) recibido.getSerializable("tiendacargada");
            cargarDatos(tienda);
        }
    }

    public void recomendacionDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_recomendacion_imagenmitienda);
        MaterialButton btndialogcancelar = dialog.findViewById(R.id.btnDialogRecOk);
        btndialogcancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                abrirGaleria();
            }
        });
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.show();

    }

    public void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filepath = data.getData();
            setfoto(filepath);

        }

    }

    public void referencias() {
        btnimagentienda = findViewById(R.id.btnImagenTienda);
        btnguardarmitienda = findViewById(R.id.btnGuardarTienda);

        cgcategoria = findViewById(R.id.cgCategorias);

        tlnombretienda = findViewById(R.id.tlNombreTienda);
        etnombretienda = findViewById(R.id.etNombreTienda);

        tlcagetorias = findViewById(R.id.tlCategorias);
        etcategorias = findViewById(R.id.etCategorias);

        tlimagetienda = findViewById(R.id.tlImagenTienda);
        ivimagentienda = findViewById(R.id.ivImagenTienda);

        tltelefono1 = findViewById(R.id.tlTelefono1);
        ettelefono1 = findViewById(R.id.etTelefono1);

        tltelefono2 = findViewById(R.id.tlTelefono2);
        ettelefono2 = findViewById(R.id.etTelefono2);

        tlemail = findViewById(R.id.tlEmail);
        etemail = findViewById(R.id.etEmail);

        tldireccion = findViewById(R.id.tlDireccion);
        etdireccion = findViewById(R.id.etDireccion);

        tlhorario = findViewById(R.id.tlHorario);
        ethorario = findViewById(R.id.etHorario);

        tldespcorta = findViewById(R.id.tlDespCorta);
        etdepcorta = findViewById(R.id.etDespCorta);

        tldesplarga = findViewById(R.id.tlDespLarga);
        etdesplarga = findViewById(R.id.etDespLarga);

        btnimagentienda.setOnClickListener(this);
        btnguardarmitienda.setOnClickListener(this);
        etcategorias.setOnTouchListener(this);
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
                break;
            case R.id.btnImagenTienda:
                recomendacionDialog();
                break;

        }
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

    public void setfoto(Uri uri) {
        filepath = uri;
        Glide.with(context).load(uri).into(ivimagentienda);


    }

    public void tomarDatos(ArrayList<String> arrayList) {
        test = 0;
        telefonos = new ArrayList<>();
        mitienda = new Tienda();
        String s = Preferencias.getString(this, Preferencias.getKeyUser());
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
            mitienda.setCategorias(arrayList);
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

        }
        if (!ettelefono2.getText().toString().trim().isEmpty()) {
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
            mitienda.setDescripcionlarga(etdesplarga.getText().toString().trim());
            test = test + 1;

        }
        if (test == 8) {

            Bundle bundle = new Bundle();
            Intent intent = new Intent(this, TiendaVistapreviaActivity.class);
            bundle.putSerializable("tienda", mitienda);
            bundle.putParcelable("uri", filepath);
            intent.putExtras(bundle);
            startActivity(intent);

        }
    }

    public void cargarDatos(Tienda tienda) {
        categorias = tienda.getCategorias();
        for (int j = 0;j<categorias.size();){
            final Chip chip = new Chip(this);
            String aux = categorias.get(j);
            chip.setText(aux);
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
            cgcategoria.addView(chip);
            ++j;
        }

        etnombretienda.setText(tienda.getNombre());
        filepath = Uri.parse(tienda.getImagen());
        Glide.with(this).load(filepath).into(ivimagentienda);
        ettelefono1.setText(tienda.getTelefono().get(0));
        if (tienda.getTelefono().size() == 2) {

            ettelefono2.setText(tienda.getTelefono().get(1));
        }

        etemail.setText(tienda.getEmail());
        etdireccion.setText(tienda.getDireccion());
        ethorario.setText(tienda.getHorario());
        etdepcorta.setText(tienda.getDescripcion());
        etdesplarga.setText(tienda.getDescripcionlarga());

    }

    public void categoriaDialog() {
        dialog = new Dialog(this);
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
            final Chip chip = new Chip(this);
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
            final Chip chip = new Chip(this);
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
            final Chip chip = new Chip(this);
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
            final Chip chip = new Chip(this);
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
            final Chip chip = new Chip(this);
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
            final Chip chip = new Chip(this);
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
}
