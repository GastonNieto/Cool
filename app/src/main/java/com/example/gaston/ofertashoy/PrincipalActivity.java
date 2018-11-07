package com.example.gaston.ofertashoy;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaston.ofertashoy.fragment.CategoriasFragment;
import com.example.gaston.ofertashoy.fragment.ListaComprasFragment;
import com.example.gaston.ofertashoy.fragment.favoritosFragment;
import com.example.gaston.ofertashoy.Modelo.Lista;
import com.example.gaston.ofertashoy.Modelo.Usuario;
import com.example.gaston.ofertashoy.util.Preferencias;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView tvnombreuser, tvemailuser;
    ImageView ivfotouser;
    CircleImageView circleImageView;
    Usuario usuario;
    String nombre, email, foto;
    FirebaseFirestore db;
    NavigationView navigationView;
    FloatingActionButton fab;
    public static int navItemIndex = 0;
    private boolean shouldLoadHomeFragOnBackPress = true;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = FirebaseFirestore.getInstance();
        Bundle bundle = getIntent().getExtras();
        usuario = new Usuario();
        if (bundle != null) {
            usuario = (Usuario) bundle.getSerializable("usuario");
            nombre = usuario.getNombre();
            email = usuario.getEmail();
            foto = usuario.getFoto();
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(getApplicationContext(),CrearItemActivity.class);
                startActivity(intent);*/
                nuevoitem();
            }
        });
        fab.hide();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View v = navigationView.getHeaderView(0);
        tvnombreuser = v.findViewById(R.id.tvNombreUser);
        tvnombreuser.setText(nombre);
        tvemailuser = v.findViewById(R.id.tvEmailUser);
        tvemailuser.setText(email);
        circleImageView = v.findViewById(R.id.ivFotoUser);

        Picasso.get().load(foto).into(circleImageView);
        navigationView.setCheckedItem(R.id.nav_camera);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                navigationView.setCheckedItem(R.id.nav_camera);
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_left).replace(R.id.contenedor,
                        new CategoriasFragment(), "inicio").commit();
                fab.hide();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.principal, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_left);
        List<Fragment> a = getSupportFragmentManager().getFragments();
        switch (item.getItemId()) {
            case R.id.nav_camera:

                if (a.isEmpty()) {
                    transaction.replace(R.id.contenedor, new LocalFragment(), "inicio").commit();
                    navItemIndex = 0;

                } else {
                    for (Fragment asd : getSupportFragmentManager().getFragments()) {
                        String as = asd.getTag();
                        Log.e("fragmentos", as);
                        if (!as.equals("inicio")) {
                            transaction.replace(R.id.contenedor, new LocalFragment(), "inicio").commit();
                            navItemIndex = 0;
                            fab.hide();
                        }
                    }
                }

                break;
            case R.id.nav_gallery:

                for (Fragment asd : getSupportFragmentManager().getFragments()) {
                    String as = asd.getTag();
                    if (!as.equals("favoritos")) {
                        transaction.replace(R.id.contenedor, new favoritosFragment(), "favoritos").commit();
                        navItemIndex = 1;
                        fab.hide();

                    }

                }

                break;

            case R.id.nav_slideshow:
                for (Fragment asd : getSupportFragmentManager().getFragments()) {
                    String as = asd.getTag();
                    if (!as.equals("Lista")) {
                        transaction.replace(R.id.contenedor, new ListaComprasFragment(), "Lista").commit();
                        navItemIndex = 3;
                        fab.show();

                    }

                }
            case R.id.Mitienda:
                for (Fragment asd : getSupportFragmentManager().getFragments()) {
                    String as = asd.getTag();
                    if (!as.equals("Mitienda")) {
                        transaction.replace(R.id.contenedor, new MiTiendoFragment(), "Lista").commit();
                        navItemIndex = 3;
                        fab.show();

                    }

                }

                break;
            case R.id.nav_send:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(PrincipalActivity.this, "User Signed Out", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }

    public void nuevoitem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.nuevo_item, null);
        Button btnAgregar, btnCancelar;
        final EditText etNombre, etCantidad;

        etNombre = view.findViewById(R.id.nombreitem);
        etCantidad = view.findViewById(R.id.cantidaditem);
        btnAgregar = view.findViewById(R.id.btnAgregarItem);
        btnCancelar = view.findViewById(R.id.btnCancelarItem);
        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog a = builder.create();
        a.show();
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = Preferencias.getString(context, Preferencias.getKeyUser());
                Lista lista = new Lista();
                if (etNombre.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Ingrese un nombre", Toast.LENGTH_LONG).show();
                } else if (etCantidad.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Ingrese la cantidad", Toast.LENGTH_LONG).show();
                } else {
                    lista.setNombre(etNombre.getText().toString());
                    lista.setCantidad(Integer.parseInt((etCantidad.getText().toString())));
                    Map<String, Object> Lista = new HashMap<>();
                    Lista.put("nombre", lista.getNombre());
                    Lista.put("cantidad", lista.getCantidad());
                    db.collection("usuarios").document(uid).collection("listacompras").add(Lista)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(context, "Se agrego un nuevo item", Toast.LENGTH_SHORT).show();
                                    etNombre.setText(null);
                                    etCantidad.setText(null);
                                    etNombre.requestFocus();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("mal", "Error writing document", e);
                        }
                    });
                }
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.dismiss();
            }
        });
    }
}

