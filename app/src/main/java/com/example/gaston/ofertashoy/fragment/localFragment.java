package com.example.gaston.ofertashoy.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.gaston.ofertashoy.Adaptadores.AdapterProductos;
import com.example.gaston.ofertashoy.R;
import com.example.gaston.ofertashoy.objetos.Comercios;
import com.example.gaston.ofertashoy.objetos.productos;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link localFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link localFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class localFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    TextView tvnombrelocal, tvdireccionlocal, tvhorariolocal;
    ImageView ivlocal;
    RecyclerView rvoferta;
    AdapterProductos adapterProductos;
    List<productos> productosList;
    FirebaseFirestore db;


    public localFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment localFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static localFragment newInstance(String param1, String param2) {
        localFragment fragment = new localFragment();
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
        View view = inflater.inflate(R.layout.fragment_local, container, false);
        referencias(view);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Comercios id = (Comercios) bundle.getSerializable("comercio");
            tvdireccionlocal.setText(id.getDescripcion());
            tvhorariolocal.setText(id.getHorario());
            tvnombrelocal.setText(id.getNombre());
            Picasso.get().load(id.getLogo()).into(ivlocal);
            productosList = new ArrayList<>();
            rvoferta = view.findViewById(R.id.rvOfertas);
            rvoferta.setHasFixedSize(true);
            rvoferta.setLayoutManager(new LinearLayoutManager(getContext()));
            adapterProductos = new AdapterProductos(productosList);
            rvoferta.setAdapter(adapterProductos);
            db = FirebaseFirestore.getInstance();
            ListenerRegistration documentReference = db.collection("categorias").document("Ropa y Accesorios").collection(id.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    if (e != null) {

                        return;
                    }

                    productosList.removeAll(productosList);
                    for (DocumentSnapshot doc : documentSnapshots) {
                        String s = doc.getId();
                        productos m = doc.toObject(productos.class);
                        m.setId(doc.getId());
                        Double a = m.getPrecio();
                        Date fecha = m.getFecha();
                        Date currentTime = Calendar.getInstance().getTime();
                        Log.e("fechaActual",currentTime.toString());
                        Log.e("fecha",m.getFecha().toString());
                        Log.e("id",s);
                        Log.e("precio", String.valueOf(m.getPrecio()));
                      /*  if (fecha.after(currentTime)){
                            productosList.add(m);
                        }*/
                        productosList.add(m);

                    }

                    adapterProductos.notifyDataSetChanged();
                }

            });
        }
        return  view;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

public  void referencias (View view){
    tvnombrelocal = view.findViewById(R.id.tvNombreLocal);
    tvhorariolocal = view.findViewById(R.id.tvHorarioLocal);
    tvdireccionlocal = view.findViewById(R.id.tvLocalDireccion);
    ivlocal = view.findViewById(R.id.ivLocal);
}
}