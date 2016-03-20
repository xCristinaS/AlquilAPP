package c.proyecto.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import c.proyecto.R;
import c.proyecto.models.Anuncio;


public class DetallesAnuncioFragment extends Fragment {

    private static final String ARG_ANUNCIO = "anuncio";
    private Anuncio mAnuncio;
    private ImageView imgFoto;
    private ImageView imgAvatar;
    private TextView lblNombre;
    private TextView lblPrecio;
    private TextView lblTamano;
    private ImageView imgTipoVivienda;
    private TextView lblTipoVivienda;
    private ImageView imgCamas;
    private TextView lblCamas;
    private TextView lblNumCamas;
    private TextView lblNumHuespedes;
    private RecyclerView rvPrestaciones;
    private TextView lblDescripcionNoDisponible;
    private TextView lblDescripcion;


    public static DetallesAnuncioFragment newInstance(Anuncio anuncio) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ANUNCIO, anuncio);

        DetallesAnuncioFragment fragment = new DetallesAnuncioFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalles_anuncio, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        mAnuncio = getArguments().getParcelable(ARG_ANUNCIO);

        //Comprobar si es mi anuncio.

        mostrarDatos();

    }

    private void initViews() {
        Activity act = getActivity();

        imgFoto = (ImageView) act.findViewById(R.id.imgFoto);
        imgAvatar = (ImageView) act.findViewById(R.id.imgAvatar);
        lblNombre = (TextView) act.findViewById(R.id.lblNombre);
        lblPrecio = (TextView) act.findViewById(R.id.lblPrecio);
        lblTamano = (TextView) act.findViewById(R.id.lblTamano);
        imgTipoVivienda = (ImageView) act.findViewById(R.id.imgTipoVivienda);
        lblTipoVivienda = (TextView) act.findViewById(R.id.lblTipoVivienda);
        imgCamas = (ImageView) act.findViewById(R.id.imgCamas);
        lblCamas = (TextView) act.findViewById(R.id.lblCamas);
        lblNumCamas = (TextView) act.findViewById(R.id.lblNumCamas);
        lblNumHuespedes = (TextView) act.findViewById(R.id.lblNumero);
        rvPrestaciones = (RecyclerView) act.findViewById(R.id.rvPrestaciones);
        lblDescripcionNoDisponible = (TextView) act.findViewById(R.id.lblDescripcionNoDisponible);
        lblDescripcion = (TextView) act.findViewById(R.id.lblDescripcion);
    }

    private void mostrarDatos() {
        Picasso.with(getActivity()).load(mAnuncio.)
    }
}
