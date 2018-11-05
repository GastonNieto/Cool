package com.example.gaston.ofertashoy.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gaston.ofertashoy.Adaptadores.AdapterSuperOfertas;
import com.example.gaston.ofertashoy.R;
import com.example.gaston.ofertashoy.Modelo.Comercios;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OfertasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OfertasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfertasFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView rvnegocios, rvofertascomunes, rvropayaccesorios;

    AdapterSuperOfertas adapterMarcas, adapterComercios, adapterRopayAccesorios;
    List<Comercios> comerciosList;
    FirebaseFirestore db;
    List<Comercios> ofertascomunes, ropayaccesorioslist;
    CardView cardView;

    public OfertasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OfertasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OfertasFragment newInstance(String param1, String param2) {
        OfertasFragment fragment = new OfertasFragment();
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
        View view = inflater.inflate(R.layout.fragment_ofertas, container, false);
        cardView = view.findViewById(R.id.superofertas);
        db = FirebaseFirestore.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        comerciosList = new ArrayList<>();
        rvnegocios = view.findViewById(R.id.rvSuperOfertas);
        rvnegocios.setHasFixedSize(true);
        rvnegocios.setLayoutManager(layoutManager);
        adapterMarcas = new AdapterSuperOfertas(comerciosList);
        rvnegocios.setAdapter(adapterMarcas);
        ListenerRegistration documentReference = db.collection("marcas").whereEqualTo("superoferta", "si").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                comerciosList.removeAll(comerciosList);
                for (DocumentSnapshot doc : documentSnapshots) {
                    Comercios m = doc.toObject(Comercios.class);

                    comerciosList.add(m);
                }
                Log.e("vacio","entro por vacio");
                adapterMarcas.notifyDataSetChanged();
            }

        });

        comerciosrecicler(view);
        setRvropayaccesorios(view);
        Date currentTime = Calendar.getInstance().getTime();
        int lunes = Calendar.SATURDAY;


        Log.e("hora", currentTime.toString());
        return view;
    }

    public void comerciosrecicler(View view) {
        ofertascomunes = new ArrayList<>();
        rvofertascomunes = view.findViewById(R.id.rvOfertasComunes);
        rvofertascomunes.setHasFixedSize(true);
        rvofertascomunes.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterComercios = new AdapterSuperOfertas(ofertascomunes);
        rvofertascomunes.setAdapter(adapterComercios);
        ListenerRegistration ofertasreference = db.collection("marcas").whereEqualTo("superoferta", "no").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                ofertascomunes.removeAll(ofertascomunes);
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    Comercios m = doc.toObject(Comercios.class);
                    ofertascomunes.add(m);
                }
                adapterComercios.notifyDataSetChanged();

            }
        });

    }

    public void setRvropayaccesorios(View view) {
        ropayaccesorioslist = new ArrayList<>();
        rvropayaccesorios = view.findViewById(R.id.rvRopayaccesorios);
        rvropayaccesorios.setHasFixedSize(true);
        rvropayaccesorios.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterRopayAccesorios = new AdapterSuperOfertas(ropayaccesorioslist);
        rvropayaccesorios.setAdapter(adapterRopayAccesorios);
        ListenerRegistration a = db.collection("comercios").document("categorias").collection("Ropa y Accesorio").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                ropayaccesorioslist.removeAll(ropayaccesorioslist);
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    Comercios m = doc.toObject(Comercios.class);
                    ropayaccesorioslist.add(m);
                }
                adapterRopayAccesorios.notifyDataSetChanged();

            }
        });

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
}
