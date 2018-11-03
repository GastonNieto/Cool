package com.example.gaston.ofertashoy.Presentador.a;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.gaston.ofertashoy.objetos.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PresentadorLogin {
    private FirebaseAuth a;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private Context context;

    public PresentadorLogin(FirebaseAuth a, FirebaseUser currentUser, FirebaseFirestore db, Context context) {
        this.a = a;
        this.currentUser = currentUser;
        this.db = db;
        this.context = context;
    }

    public void crearUsuario(Usuario u) {
        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("nombre", u.getNombre());
        usuarios.put("email", u.getEmail());
        usuarios.put("foto", u.getFoto());

        db.collection("usuarios").document(u.getUid()).collection("datos").document("personales")
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
