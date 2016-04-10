package c.proyecto.fragments;


import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.activities.ConversationActivity;
import c.proyecto.activities.VerPerfilActivity;
import c.proyecto.adapters.AdvertsRecyclerViewAdapter;
import c.proyecto.adapters.PrestacionesAdapter;
import c.proyecto.adapters.PrestacionesDetalladasAdapter;
import c.proyecto.models.Anuncio;
import c.proyecto.models.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetallesAnuncioFragment extends Fragment implements PrestacionesAdapter.IPrestacionAdapter {


    public interface IDetallesAnuncioFragmentListener {

        void onImgEditClicked(Anuncio advert, Usuario user);
    }

    private static final String ARG_ANUNCIO = "anuncio";

    private static final String ARG_ADVERT_TYPE = "advert_type";
    private static final String ARG_USER_ANUNCIANTE = "mUserAnunciante";

    private static final String ARG_CURRENT_USER = "usuarioLogueado";
    private RelativeLayout shapeComentario;
    private ImageView imgFoto, imgTipoVivienda, imgCamas, imgMessage, imgEdit, imgSubscribe;

    private CircleImageView imgAvatar;
    private TextView lblNombre, lblPrecio, lblTamano, lblTipoVivienda, lblCamas, lblNumCamas, lblNumHuespedes, lblDescripcionNoDisponible, lblDescripcion;
    private RecyclerView rvPrestaciones;
    private PrestacionesAdapter mPrestacionesAdapter;

    private Anuncio mAnuncio;

    private Usuario mUserAnunciante, mCurrentUser;
    private int adverType;


    private IDetallesAnuncioFragmentListener mListener;


    public static DetallesAnuncioFragment newInstance(Anuncio anuncio, int advertType, Usuario user, Usuario currentUser) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ANUNCIO, anuncio);
        args.putInt(ARG_ADVERT_TYPE, advertType);
        args.putParcelable(ARG_USER_ANUNCIANTE, user);
        args.putParcelable(ARG_CURRENT_USER, currentUser);
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
        mAnuncio = getArguments().getParcelable(ARG_ANUNCIO);
        adverType = getArguments().getInt(ARG_ADVERT_TYPE);
        mUserAnunciante = getArguments().getParcelable(ARG_USER_ANUNCIANTE);
        mCurrentUser = getArguments().getParcelable(ARG_CURRENT_USER);
        initViews();
        confRecyclerview();
        bindData();
    }

    private void initViews() {
        lblNombre = (TextView) getView().findViewById(R.id.lblNombre);
        imgFoto = (ImageView) getView().findViewById(R.id.imgFoto);
        imgAvatar = (CircleImageView) getView().findViewById(R.id.imgAvatar);
        lblPrecio = (TextView) getView().findViewById(R.id.lblPrecio);
        lblTamano = (TextView) getView().findViewById(R.id.lblTamano);
        imgTipoVivienda = (ImageView) getView().findViewById(R.id.imgTipoVivienda);
        lblTipoVivienda = (TextView) getView().findViewById(R.id.lblTipoVivienda);
        imgCamas = (ImageView) getView().findViewById(R.id.imgCamas);
        lblCamas = (TextView) getView().findViewById(R.id.lblCamas);
        lblNumCamas = (TextView) getView().findViewById(R.id.lblNumCamas);
        lblNumHuespedes = (TextView) getView().findViewById(R.id.lblNumHuespedes);
        rvPrestaciones = (RecyclerView) getView().findViewById(R.id.rvPrestaciones);
        lblDescripcionNoDisponible = (TextView) getView().findViewById(R.id.lblDescripcionNoDisponible);
        lblDescripcion = (TextView) getView().findViewById(R.id.lblDescripcion);
        shapeComentario = (RelativeLayout) getView().findViewById(R.id.shapeComentario);


        imgMessage = (ImageView) getView().findViewById(R.id.imgMessage);

        imgMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConversationActivity.start(getActivity(), null, mCurrentUser, mAnuncio);
            }
        });
        imgEdit = (ImageView) getView().findViewById(R.id.imgEdit);
        imgSubscribe = (ImageView) getView().findViewById(R.id.imgSubscribe);
        

        if(adverType == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS){
            imgEdit.setVisibility(View.VISIBLE);
            imgSubscribe.setVisibility(View.VISIBLE);
            imgMessage.setVisibility(View.GONE);
        }
        //Permite editar el anuncio
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onImgEditClicked(mAnuncio, mCurrentUser);
            }
        });
        //Subscribe al usuario al anuncio
        imgSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //`---------------------------------------------------------------------  SUBSCRIBIRSE POR HACER
            }
        });


        //Muestra el usuario propietario del anuncio
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerPerfilActivity.start(getActivity(), mUserAnunciante);
            }
        });
        lblNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerPerfilActivity.start(getActivity(), mUserAnunciante);
            }
        });
    }

    private void confRecyclerview() {
        rvPrestaciones.setHasFixedSize(true);
        mPrestacionesAdapter = new PrestacionesAdapter(mAnuncio.getPrestaciones(), this);
        rvPrestaciones.setAdapter(mPrestacionesAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvPrestaciones.setLayoutManager(mLayoutManager);
        rvPrestaciones.setItemAnimator(new DefaultItemAnimator());
    }

    private void bindData() {
        if (mAnuncio.getImagenes().size() > 0) {
            for (String img : mAnuncio.getImagenes().keySet())
                if (img.equals(Constantes.FOTO_PRINCIPAL)) // si la key es de la imagen principal, cargo la foto
                    Picasso.with(getActivity()).load(mAnuncio.getImagenes().get(img)).into(imgFoto);
        } else
            Picasso.with(getActivity()).load(R.drawable.default_user).into(imgFoto);

        //Si no hay ninguna prestación se le cambiará el color al shape del comentario al color del fondo
        if (mAnuncio.getPrestaciones().size() == 0)
            shapeComentario.setBackgroundColor(getResources().getColor(android.R.color.white));

        lblNombre.setText(mUserAnunciante.getNombre());
        if (mUserAnunciante.getFoto() != null)
            Picasso.with(getActivity()).load(mUserAnunciante.getFoto()).error(R.drawable.default_user).into(imgAvatar);
        else
            Picasso.with(getActivity()).load(R.drawable.default_user).into(imgAvatar);

        lblPrecio.setText(String.format("%.2f%s", mAnuncio.getPrecio(), Constantes.MONEDA));
        lblTamano.setText(mAnuncio.getTamanio() + Constantes.UNIDAD);
        switch (mAnuncio.getTipo_vivienda()) {
            case Constantes.HABITACION:
                imgTipoVivienda.setImageResource(R.drawable.habitacion);
                imgCamas.setImageResource(R.drawable.cama);
                lblCamas.setText(Constantes.CAMAS);
                break;
            case Constantes.CASA:
                imgTipoVivienda.setImageResource(R.drawable.casa);
                imgCamas.setImageResource(R.drawable.habitacion);
                lblCamas.setText(Constantes.HABITACION);
                break;
            case Constantes.PISO:
                imgTipoVivienda.setImageResource(R.drawable.piso);
                imgCamas.setImageResource(R.drawable.habitacion);
                lblCamas.setText(Constantes.HABITACION);
                break;
        }
        lblTipoVivienda.setText(mAnuncio.getTipo_vivienda());
        lblNumCamas.setText(String.valueOf(mAnuncio.getHabitaciones_o_camas()));
        //Num Huespedes

        if (mAnuncio.getDescripcion().isEmpty())
            lblDescripcionNoDisponible.setVisibility(View.VISIBLE);
        else
            lblDescripcion.setText(mAnuncio.getDescripcion());


    }

    @Override
    public void onPrestacionClicked() {
        mostrarDialogoPrestaciones();
    }

    @Override
    public void onEmptyViewClicked() {

    }

    private void mostrarDialogoPrestaciones() {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        final View dialogView = View.inflate(getActivity(), R.layout.dialog_prestaciones_detalladas, null);
        dialog.setView(dialogView);
        dialog.setCanceledOnTouchOutside(true);


        rvPrestaciones = (RecyclerView) dialogView.findViewById(R.id.rvPrestaciones);


        rvPrestaciones.setAdapter(new PrestacionesDetalladasAdapter(mAnuncio.getPrestaciones()));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvPrestaciones.setLayoutManager(mLayoutManager);
        rvPrestaciones.setItemAnimator(new DefaultItemAnimator());

        dialog.show();
        Point boundsScreen = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(boundsScreen);

        dialog.getWindow().setLayout((int) (boundsScreen.x * Constantes.PORCENTAJE_PANTALLA), WindowManager.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onAttach(Context context) {
        mListener = (IDetallesAnuncioFragmentListener) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public void setmAnuncio(Anuncio anuncio) {
        mAnuncio = anuncio;
        bindData();
        mPrestacionesAdapter.replaceAll(anuncio.getPrestaciones());
    }
}
