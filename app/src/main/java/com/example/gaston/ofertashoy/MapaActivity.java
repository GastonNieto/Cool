package com.example.gaston.ofertashoy;

import android.net.Uri;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.gaston.ofertashoy.fragment.MapFragment;

public class MapaActivity extends AppCompatActivity implements  MapFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        Fragment fragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorMapa, fragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
