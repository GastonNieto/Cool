package com.example.gaston.ofertashoy.Presentador;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.gaston.ofertashoy.PrincipalActivity;
import com.example.gaston.ofertashoy.Modelo.Usuario;
import com.example.gaston.ofertashoy.util.Preferencias;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class LoginPresentador {
    private FirebaseUser user;
    private Context context;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    public LoginPresentador(FirebaseUser user, Context context, FirebaseAuth auth, FirebaseFirestore firestore) {
        this.user = user;
        this.context = context;
        this.auth = auth;
        this.firestore = firestore;
    }


    public void userLog() {
        Usuario usuario = new Usuario();
        usuario.setUid(user.getUid());
        usuario.setNombre(user.getDisplayName());
        usuario.setEmail(user.getEmail());
        usuario.setFoto(user.getPhotoUrl().toString());
        Preferencias.setString(context, Preferencias.getKeyUser(), usuario.getUid());
        Bundle bundle = new Bundle();
        Intent intent = new Intent(context, PrincipalActivity.class);
        bundle.putSerializable("usuario", usuario);
        intent.putExtras(bundle);
        Toast.makeText(context, "Bienvenido " + usuario.getNombre(), Toast.LENGTH_LONG).show();
        context.startActivity(intent);

    }

    public void newUser(FirebaseUser user) {
        Usuario usuario = new Usuario();
        assert user != null;
        usuario.setUid(user.getUid());
        usuario.setNombre(user.getDisplayName());
        usuario.setEmail(user.getEmail());
        usuario.setFoto(Objects.requireNonNull(user.getPhotoUrl()).toString());
        Preferencias.setString(context, Preferencias.getKeyUser(), usuario.getUid());
        crearUsuario(usuario);
        Bundle bundle = new Bundle();
        Intent intent = new Intent(context, PrincipalActivity.class);
        bundle.putSerializable("usuario", usuario);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    public void crearUsuario(Usuario u) {
        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("nombre", u.getNombre());
        usuarios.put("email", u.getEmail());
        usuarios.put("foto", u.getFoto());

        firestore.collection("usuarios").document(u.getUid()).collection("datos").document("personales")
                .set(usuarios)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Bien", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("mal", "Error writing document", e);
                    }
                });
    }
}
