package com.example.gaston.ofertashoy.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gaston.ofertashoy.Adaptadores.AdapterLista;
import com.example.gaston.ofertashoy.R;
import com.example.gaston.ofertashoy.objetos.Lista;
import com.example.gaston.ofertashoy.util.Preferencias;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListaComprasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListaComprasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaComprasFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    AdapterLista adapterLista;
    RecyclerView rvlistascompras;
    List<Lista> listaList;
    FirebaseFirestore db;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ListaComprasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaComprasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaComprasFragment newInstance(String param1, String param2) {
        ListaComprasFragment fragment = new ListaComprasFragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lista_compras, container, false);
        db = FirebaseFirestore.getInstance();
        cargarLista(v);
        return v;
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

    public void cargarLista(View view) {
        listaList = new ArrayList<>();
        rvlistascompras = view.findViewById(R.id.rvListaCompras);
        rvlistascompras.setHasFixedSize(true);
        rvlistascompras.setLayoutManager(new LinearLayoutManager(getContext()));
        rvlistascompras.setFocusable(false);
        adapterLista = new AdapterLista(listaList);
        rvlistascompras.setAdapter(adapterLista);
        String s = Preferencias.getString(getContext(), Preferencias.getKeyUser());
        ListenerRegistration registration = db.collection("usuarios").document(s).collection("listacompras")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                        if (e !=null){
                            return;
                        }
                        listaList.removeAll(listaList);
                        for (DocumentSnapshot doc : queryDocumentSnapshots){
                            Lista l = doc.toObject(Lista.class);
                            l.setId(doc.getId());
                            listaList.add(l);
                        }
                        adapterLista.notifyDataSetChanged();
                    }

                });

    }

}
