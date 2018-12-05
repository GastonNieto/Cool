package com.example.gaston.ofertashoy.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gaston.ofertashoy.Modelo.Tienda;
import com.example.gaston.ofertashoy.Presentador.TiendaVistapreviaPresentador;
import com.example.gaston.ofertashoy.R;
import com.example.gaston.ofertashoy.Interfaces.VistapreviaInterface;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class TiendaVistapreviaActivity extends AppCompatActivity implements VistapreviaInterface.View, View.OnClickListener {
    private FirebaseFirestore firestore;
    private Context context;
    private StorageReference mStorageRef;
    private LinearLayout linearemailvistaprevia;
    private MaterialButton btnaceptarvistaprevia;
    private TextView tvnombrevistaprevia, tvhorariovistaprevia, tvtelvistaprevia, tvemailvistaprevia, tvdireccionvistaprevia, tvdespcortavistaprevia, tvdesplargavistaprevia;
    private ImageView ivimagenvistaprevia;
    private VistapreviaInterface.Presenter presenter;
    private Tienda tienda;
    private Uri a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda_vistaprevia);
        context = this;
        firestore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        presenter = new TiendaVistapreviaPresentador(this);
        referencias();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle recibido = getIntent().getExtras();
        tienda = null;
        if (recibido != null) {
            a = recibido.getParcelable("uri");
            tienda = (Tienda) recibido.getSerializable("tienda");
            setDatos(tienda, a);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    public void referencias() {
        btnaceptarvistaprevia = findViewById(R.id.btnAceptarVistaprevia);
        linearemailvistaprevia = findViewById(R.id.linearEmailVistaprevia);
        ivimagenvistaprevia = findViewById(R.id.ivImagenVistaprevia);
        tvnombrevistaprevia = findViewById(R.id.tvNombreVistaprevia);
        tvhorariovistaprevia = findViewById(R.id.tvHorarioVistaprevia);
        tvtelvistaprevia = findViewById(R.id.tvTelVistaprevia);
        tvemailvistaprevia = findViewById(R.id.tvEmailVistaprevia);
        tvdireccionvistaprevia = findViewById(R.id.tvDireccionVistaprevia);
        tvdespcortavistaprevia = findViewById(R.id.tvdespcortaVistaprevia);
        tvdesplargavistaprevia = findViewById(R.id.tvDespLargaVistaprevia);
        btnaceptarvistaprevia.setOnClickListener(this);

    }


    public void setDatos(Tienda tienda, Uri a) {
        Glide.with(context).load(a).into(ivimagenvistaprevia);
        tvnombrevistaprevia.setText(tienda.getNombre());
        tvhorariovistaprevia.setText(tienda.getHorario());
        tvtelvistaprevia.setText(tienda.getTelefono().get(0));
        if (tienda.getTelefono().size() == 2) {
            String telefono1 = tienda.getTelefono().get(0);
            String telefono2 = telefono1 + " - " + tienda.getTelefono().get(1);
            tvtelvistaprevia.setText(telefono2);
        }
        if (tienda.getEmail() != null) {
            tvemailvistaprevia.setText(tienda.getEmail());
        } else {
            linearemailvistaprevia.setVisibility(View.GONE);

        }
        tvdireccionvistaprevia.setText(tienda.getDireccion());
        tvdespcortavistaprevia.setText(tienda.getDescripcion());
        tvdesplargavistaprevia.setText(tienda.getDescripcionlarga());


    }

    @Override
    public void pasardatos(Tienda tienda, Uri uri) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAceptarVistaprevia:
                presenter.referenciaspresenter(firestore, mStorageRef, context);
                presenter.cargarpresenter(tienda, a);
                break;
        }
    }
}
