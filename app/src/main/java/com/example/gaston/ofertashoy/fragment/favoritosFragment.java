package com.example.gaston.ofertashoy.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gaston.ofertashoy.Adaptadores.AdapterFavoritos;
import com.example.gaston.ofertashoy.R;
import com.example.gaston.ofertashoy.Modelo.Usuario;
import com.example.gaston.ofertashoy.Modelo.productos;
import com.example.gaston.ofertashoy.util.Preferencias;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link favoritosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link favoritosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class favoritosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    AdapterFavoritos adapterFavoritos;
    RecyclerView rvfavoritos;
    List<productos> productosList;
    FirebaseFirestore db;
    TextView tvnombreusuariofavorito, tvemailusuariofavorito;
    CircleImageView circleImageView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public favoritosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment favoritosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static favoritosFragment newInstance(String param1, String param2) {
        favoritosFragment fragment = new favoritosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);
        circleImageView = view.findViewById(R.id.ivFotoUsuarioFavoritos);
        tvnombreusuariofavorito = view.findViewById(R.id.tvNombreUsuarioFavoritos);
        tvemailusuariofavorito = view.findViewById(R.id.tvEmailUsuarioFavoritos);
        db = FirebaseFirestore.getInstance();
        cargarFavoritos(view);
        String uid = Preferencias.getString(getActivity(), Preferencias.getKeyUser());
        ListenerRegistration registration = db.collection("usuarios").document(uid)
                .collection("datos").document("personales").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("error al leer", "Listen failed.", e);
                            return;
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            Usuario u = documentSnapshot.toObject(Usuario.class);
                            tvemailusuariofavorito.setText(u.getEmail());
                            tvnombreusuariofavorito.setText(u.getNombre());
                            Picasso.get().load(u.getFoto()).into(circleImageView);
                        } else {
                            Log.d("vacio", "Current data: null");
                        }
                    }
                });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void cargarFavoritos(View view) {
        productosList = new ArrayList<>();
        rvfavoritos = view.findViewById(R.id.rvListaFavoritos);
        rvfavoritos.setHasFixedSize(true);
        rvfavoritos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvfavoritos.setFocusable(false);
        adapterFavoritos = new AdapterFavoritos(productosList);
        rvfavoritos.setAdapter(adapterFavoritos);
        String s = Preferencias.getString(getContext(), Preferencias.getKeyUser());
        ListenerRegistration registration = db.collection("usuarios")
                .document(s).collection("favoritos").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {

                            return;
                        }

                        productosList.removeAll(productosList);
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            String s = doc.getId();
                            productos m = doc.toObject(productos.class);
                            m.setId(doc.getId());
                            Date fecha = m.getFecha();
                            Date currentTime = Calendar.getInstance().getTime();
                            Log.e("fechaActual", currentTime.toString());
                            Log.e("fecha", m.getFecha().toString());
                            Log.e("id", s);


                            productosList.add(m);

                        }
                        adapterFavoritos.notifyDataSetChanged();
                    }
                });
    }

}
