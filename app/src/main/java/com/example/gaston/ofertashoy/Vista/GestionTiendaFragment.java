package com.example.gaston.ofertashoy.Vista;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.gaston.ofertashoy.Interfaces.GestionTiendaInterface;
import com.example.gaston.ofertashoy.Modelo.Tienda;
import com.example.gaston.ofertashoy.Presentador.GestionTiendaPresentador;
import com.example.gaston.ofertashoy.R;
import com.example.gaston.ofertashoy.util.Preferencias;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.fragment.app.Fragment;

import androidx.fragment.app.Fragment;
import androidx.transition.TransitionManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GestionTiendaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GestionTiendaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GestionTiendaFragment extends Fragment implements View.OnClickListener, GestionTiendaInterface.view {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //UI
    private ImageButton ibmor;
    private LinearLayout a, linaeremailgetiontienda;
    private Animation animationUp;
    private Animation animationDown;
    private ViewGroup transitionsContainer;
    private MaterialButton btneditarmitienda;
    private ImageView ivimagengestiontienda;
    private TextView tvnombregestiontienda, tvdirecciongestiontienda, tvtelgestiontienda,
            tvdescripciongestiontienda, tvhorariogestiontienda, tvemailgestiontienda, tvdesplargagestiontienda;

    //variables/constantes
    private String Usuario;
    private Tienda tiendafull;
    private Context context;
    private FirebaseFirestore firestore;
    private StorageReference storage;
    private GestionTiendaInterface.presenter presenter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GestionTiendaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GestionTiendaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GestionTiendaFragment newInstance(String param1, String param2) {
        GestionTiendaFragment fragment = new GestionTiendaFragment();
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
        View view = inflater.inflate(R.layout.fragment_gestion_tienda, container, false);
        context = getContext();
        storage = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        Usuario = Preferencias.getString(getContext(), Preferencias.getKeyUser());
        presenter = new GestionTiendaPresentador(this);
        presenter.referenciasPresenter(firestore, storage, context);
        presenter.extraerPresenter();
        ini(view);

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

    public void ini(View view) {
        ivimagengestiontienda = view.findViewById(R.id.ivImagenGestionTienda);
        tvnombregestiontienda = view.findViewById(R.id.tvnombreGestionTienda);
        tvdescripciongestiontienda = view.findViewById(R.id.tvDescripcionGestionTienda);
        tvhorariogestiontienda = view.findViewById(R.id.tvHorarioGestionTienda);
        tvdirecciongestiontienda = view.findViewById(R.id.tvDireccionGestionTienda);
        tvtelgestiontienda = view.findViewById(R.id.tvTelGestionTienda);
        tvemailgestiontienda = view.findViewById(R.id.tvEmailGestionTienda);
        tvdesplargagestiontienda = view.findViewById(R.id.tvDespLargaGestionTienda);
        linaeremailgetiontienda = view.findViewById(R.id.linearGestionTienda);
        btneditarmitienda = view.findViewById(R.id.btnEditarTienda);
        a = view.findViewById(R.id.linearMasInfo);
        transitionsContainer = view.findViewById(R.id.linearGestionTienda);
        ibmor = view.findViewById(R.id.ibtnMasInfo);
        a.setVisibility(View.GONE);
        ibmor.setOnClickListener(this);
        btneditarmitienda.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnEditarTienda:
                Bundle bundle = new Bundle();
                Intent intent = new Intent(getContext(), EditarTiendaActivity.class);
                bundle.putSerializable("tiendacargada", tiendafull);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.ibtnMasInfo:
                collapseExpandTextView();
                break;
        }
    }


    void collapseExpandTextView() {
        if (a.isShown()) {
            a.setVisibility(View.GONE);
            TransitionManager.beginDelayedTransition(transitionsContainer);

            ibmor.animate().rotation(0);
        } else {
            a.setVisibility(View.VISIBLE);
            ibmor.animate().rotation(180);
            TransitionManager.beginDelayedTransition(transitionsContainer);

        }

    }

    @Override
    public void extraerView(Tienda tienda) {
        if (tienda == null) {
            linaeremailgetiontienda.setVisibility(View.GONE);
        } else {
            linaeremailgetiontienda.setVisibility(View.VISIBLE);
            tiendafull = tienda;
            Glide.with(getActivity().getApplicationContext()).load(tienda.getImagen()).into(ivimagengestiontienda);
            tvnombregestiontienda.setText(tienda.getNombre());
            tvdesplargagestiontienda.setText(tienda.getDescripcionlarga());
            tvdescripciongestiontienda.setText(tienda.getDescripcion());
            tvhorariogestiontienda.setText(tienda.getHorario());
            tvdirecciongestiontienda.setText(tienda.getDireccion());
            tvtelgestiontienda.setText(tienda.getTelefono().get(0));
            if (tienda.getTelefono().size() == 2) {
                String telefono1 = tienda.getTelefono().get(0);
                String telefono2 = telefono1 + " - " + tienda.getTelefono().get(1);
                tvtelgestiontienda.setText(telefono2);
            }
            if (tienda.getEmail() != null) {
                tvemailgestiontienda.setText(tienda.getEmail());
            } else {
                linaeremailgetiontienda.setVisibility(View.GONE);

            }
        }
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
}
