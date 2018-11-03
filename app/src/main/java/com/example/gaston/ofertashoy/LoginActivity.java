package com.example.gaston.ofertashoy;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gaston.ofertashoy.objetos.Usuario;
import com.example.gaston.ofertashoy.util.Preferencias;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    public static final int RC_SIGN_IN = 1;
    SignInButton signInButton;
    FirebaseAuth a;
    FirebaseUser currentUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        final List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        db = FirebaseFirestore.getInstance();

        a = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Usuario usuario = new Usuario();
            usuario.setUid(currentUser.getUid());
            usuario.setNombre(currentUser.getDisplayName());
            usuario.setEmail(currentUser.getEmail());
            usuario.setFoto(currentUser.getPhotoUrl().toString());
            Preferencias.setString(this, Preferencias.getKeyUser(), usuario.getUid());
            Bundle bundle = new Bundle();
            Intent intent = new Intent(this, PrincipalActivity.class);
            bundle.putSerializable("usuario", usuario);
            intent.putExtras(bundle);
            startActivity(intent);

            finish();

        }

        signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false).setAvailableProviders(providers).build(), RC_SIGN_IN);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Usuario usuario = new Usuario();
                assert user != null;
                usuario.setUid(user.getUid());
                usuario.setNombre(user.getDisplayName());
                usuario.setEmail(user.getEmail());
                usuario.setFoto(Objects.requireNonNull(user.getPhotoUrl()).toString());
                Preferencias.setString(this, Preferencias.getKeyUser(), usuario.getUid());
                crearUsuario(usuario);
                Bundle bundle = new Bundle();
                Intent intent = new Intent(this, PrincipalActivity.class);
                bundle.putSerializable("usuario", usuario);
                intent.putExtras(bundle);
                startActivity(intent);

                finish();


                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
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
