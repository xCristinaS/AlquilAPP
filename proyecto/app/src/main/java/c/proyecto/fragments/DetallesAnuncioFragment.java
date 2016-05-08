package c.proyecto.fragments;


import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Transformers.BaseTransformer;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.LinkedList;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.activities.ConversationActivity;
import c.proyecto.activities.LocalizacionActivity;
import c.proyecto.activities.VerPerfilActivity;
import c.proyecto.adapters.AdvertsRecyclerViewAdapter;
import c.proyecto.adapters.PrestacionesAdapter;
import c.proyecto.adapters.PrestacionesDetalladasAdapter;
import c.proyecto.mvp_presenters.ConversationPresenter;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Usuario;
import c.proyecto.pojo.MessagePojo;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetallesAnuncioFragment extends Fragment implements PrestacionesAdapter.IPrestacionAdapter, OnMapReadyCallback, ViewPagerEx.OnPageChangeListener {

    public interface IDetallesAnuncioFragmentListener {
        void onImgEditClicked(Anuncio advert, Usuario user);
    }

    public interface OnDetallesAnuncioFragmentClic {
        void onImgSubClicked(Anuncio a);
        void onImgUnSubClicked(Anuncio a);
        void onNewMessageClic(MessagePojo m, String keyReceptor);
    }

    private static final String ARG_ANUNCIO = "anuncio";
    private static final String ARG_ADVERT_TYPE = "advert_type";
    private static final String ARG_USER_ANUNCIANTE = "mUserAnunciante";
    private static final String ARG_CURRENT_USER = "usuarioLogueado";
    private static final String ARG_MESSAGE = "mensaje_si_existe_conversacion";

    private RelativeLayout shapeComentario, groupImagenes;
    private SliderLayout slider;
    private ImageView imgTipoVivienda,imgCamas,imgMessage, imgEdit, imgSubscribe;

    private CircleImageView imgAvatar;
    private TextView lblNombre, lblPrecio, lblTamano, lblTipoVivienda, lblCamas, lblNumCamas, lblNumToilets, lblDescripcionNoDisponible, lblDescripcion;
    private RecyclerView rvPrestaciones;
    private PrestacionesAdapter mPrestacionesAdapter;

    private Anuncio mAnuncio;
    private Usuario mUserAnunciante, mCurrentUser;
    private int mAdverType;
    private GoogleMap mGoogleMap;
    private MessagePojo mMessage;

    private IDetallesAnuncioFragmentListener mListener;
    private OnDetallesAnuncioFragmentClic mListenerClick;

    public static DetallesAnuncioFragment newInstance(Anuncio anuncio, int advertType, Usuario user, Usuario currentUser, MessagePojo message) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ANUNCIO, anuncio);
        args.putInt(ARG_ADVERT_TYPE, advertType);
        args.putParcelable(ARG_USER_ANUNCIANTE, user);
        args.putParcelable(ARG_CURRENT_USER, currentUser);
        args.putParcelable(ARG_MESSAGE, message);
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
        mAdverType = getArguments().getInt(ARG_ADVERT_TYPE);
        mUserAnunciante = getArguments().getParcelable(ARG_USER_ANUNCIANTE);
        mCurrentUser = getArguments().getParcelable(ARG_CURRENT_USER);
        mMessage = getArguments().getParcelable(ARG_MESSAGE);
        initViews();
        confRecyclerview();
        confMap();
        bindData();
    }

    private void initViews() {
        groupImagenes = (RelativeLayout) getView().findViewById(R.id.groupImagenes);
        lblNombre = (TextView) getView().findViewById(R.id.lblNombre);
        slider = (SliderLayout) getView().findViewById(R.id.slider);
        imgAvatar = (CircleImageView) getView().findViewById(R.id.imgAvatar);
        lblPrecio = (TextView) getView().findViewById(R.id.lblPrecio);
        lblTamano = (TextView) getView().findViewById(R.id.lblTamano);
        imgTipoVivienda = (ImageView) getView().findViewById(R.id.imgTipoVivienda);
        lblTipoVivienda = (TextView) getView().findViewById(R.id.lblTipoVivienda);
        imgCamas = (ImageView) getView().findViewById(R.id.imgCamas);
        lblCamas = (TextView) getView().findViewById(R.id.lblCamas);
        lblNumCamas = (TextView) getView().findViewById(R.id.lblNumCamas);
        lblNumToilets = (TextView) getView().findViewById(R.id.lblNumToilets);
        rvPrestaciones = (RecyclerView) getView().findViewById(R.id.rvPrestaciones);
        lblDescripcionNoDisponible = (TextView) getView().findViewById(R.id.lblDescripcionNoDisponible);
        lblDescripcion = (TextView) getView().findViewById(R.id.lblDescripcion);
        shapeComentario = (RelativeLayout) getView().findViewById(R.id.shapeComentario);

        imgMessage = (ImageView) getView().findViewById(R.id.imgMessage);
        if (mMessage == null)
            imgMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSenMessageDialog();
                }
            });
        else {
            imgMessage.setImageResource(R.drawable.ic_chat);
            imgMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConversationActivity.start(getContext(), mMessage, mCurrentUser);
                }
            });
        }

        imgEdit = (ImageView) getView().findViewById(R.id.imgEdit);
        imgSubscribe = (ImageView) getView().findViewById(R.id.imgSubscribe);

        switch (mAdverType) {
            case AdvertsRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS:
                imgEdit.setVisibility(View.VISIBLE);
                imgMessage.setVisibility(View.GONE);
                break;
            case AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS:
                imgSubscribe.setVisibility(View.VISIBLE);
                break;
            case AdvertsRecyclerViewAdapter.ADAPTER_TYPE_SUBS:
                imgSubscribe.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_unsubscribe));
                imgSubscribe.setVisibility(View.VISIBLE);
                break;
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
                if (mAdverType == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS){
                    mListenerClick.onImgSubClicked(mAnuncio);
                    //Al subscribirse, cambiará el icono a la imagen de desubscribirse y cambiará el advertType al de desubscribirse
                    imgSubscribe.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_unsubscribe));
                    mAdverType = AdvertsRecyclerViewAdapter.ADAPTER_TYPE_SUBS;
                }
                else {
                    mListenerClick.onImgUnSubClicked(mAnuncio);
                    //Al subscribirse, cambiará el icono a la imagen de subscribirse y cambiará el advertType al de subcribirse.
                    imgSubscribe.setImageDrawable(getResources().getDrawable(R.drawable.ic_subscribe));
                    mAdverType = AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS;
                }
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

    private void confSlider() {
        slider.setPresetTransformer(SliderLayout.Transformer.Stack);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.startAutoCycle(Constantes.DELAY_TIME, Constantes.DURATION, false);
        slider.getPagerIndicator().destroySelf();
        slider.addOnPageChangeListener(this);

        DefaultSliderView defaultSliderView;
        LinkedList<String> lista = new LinkedList<>();
        //Ordena la lista de imagenes
        if (mAnuncio.getImagenes().size() > 0) {
            for (String key : mAnuncio.getImagenes().keySet())
                if (key.equals(Constantes.FOTO_PRINCIPAL)) // si la key es de la imagen principal, cargo la foto
                    lista.addFirst(mAnuncio.getImagenes().get(key));
                else
                    lista.add(mAnuncio.getImagenes().get(key));

            //Introduce imagenes en el slider.
            for(String url : lista){
                defaultSliderView = new DefaultSliderView(getContext());
                defaultSliderView.image(url).setScaleType(BaseSliderView.ScaleType.CenterCrop);
                slider.addSlider(defaultSliderView);
            }
        }

        //No permite que se pueda pasar de página si no hay mas de una imagen.
        if(lista.size() < 2)
            slider.setPagerTransformer(false, new BaseTransformer() {
                @Override
                protected void onTransform(View view, float v) {
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


    private void confMap() {
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment =  SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);
        fm.beginTransaction().replace(R.id.frmMap, mapFragment).commit();
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        mGoogleMap = map;
        posicionarMapa();

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                LocalizacionActivity.start(getActivity(), map.getCameraPosition().target, false);
            }
        });
    }
    private void posicionarMapa(){
        mGoogleMap.clear();
        //No se le permite al usuario mover el mapa de ninguna forma
        mGoogleMap.getUiSettings().setAllGesturesEnabled(false);
        LatLng lat = new LatLng(mAnuncio.getLats().getLatitude(), mAnuncio.getLats().getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, Constantes.ZOOM_ANUNCIO_CON_LOCALIZACION));
        mGoogleMap.addCircle(new CircleOptions().center(lat).radius(Constantes.CIRCLE_RADIUS).fillColor(Constantes.CIRCLE_COLOR).strokeWidth(Constantes.CIRCLE_STROKE_WIDTH));
    }

    private void bindData() {
        //Se infla un slider cada vez que se actualiza las imagenes
        //Se coloca la View inflada a modo de FrameLayout en el RelativeLayout
        RelativeLayout r = (RelativeLayout) View.inflate(getContext(), R.layout.slider, null);
        slider = (SliderLayout) r.findViewById(R.id.slider);
        confSlider();

        groupImagenes.removeAllViews();
        groupImagenes.addView(r);


        //Si no hay ninguna prestación se le cambiará el color al shape del comentario al color del fondo
        if (mAnuncio.getPrestaciones().size() == 0) {
            rvPrestaciones.setVisibility(View.GONE);
            shapeComentario.setBackgroundColor(getResources().getColor(android.R.color.white));
        }else
            shapeComentario.setBackgroundColor(getResources().getColor(R.color.colorShape));

        lblNombre.setText(mUserAnunciante.getNombre());
        if (mUserAnunciante.getFoto() != null)
            Picasso.with(getActivity()).load(mUserAnunciante.getFoto()).error(R.drawable.default_user).fit().centerCrop().into(imgAvatar);
        else
            Picasso.with(getActivity()).load(R.drawable.default_user).fit().centerCrop().into(imgAvatar);

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
        lblNumToilets.setText(String.valueOf(mAnuncio.getNumero_banios()));

        if (mAnuncio.getDescripcion().isEmpty()){
            lblDescripcionNoDisponible.setVisibility(View.VISIBLE);
            lblDescripcion.setText("");
        }
        else{
            lblDescripcionNoDisponible.setVisibility(View.GONE);
            lblDescripcion.setText(mAnuncio.getDescripcion());
        }
        //Evita que se pueda hacer scroll cuando no haya suficientes items
        if(mAnuncio.getPrestaciones().size()<5)
            rvPrestaciones.setOverScrollMode(View.OVER_SCROLL_NEVER);
        else
            rvPrestaciones.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
    }

    @Override
    public void onPrestacionClicked() {
        mostrarDialogoPrestaciones();
    }

    @Override
    public void onEmptyViewClicked() {

    }

    private void mostrarDialogoPrestaciones() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        View dialogView = View.inflate(getActivity(), R.layout.dialog_prestaciones_detalladas, null);
        dialog.setView(dialogView);
        dialog.setCanceledOnTouchOutside(true);

        RecyclerView rvPrestacionesDialogo = (RecyclerView) dialogView.findViewById(R.id.rvPrestaciones);
        rvPrestacionesDialogo.setAdapter(new PrestacionesDetalladasAdapter(mAnuncio.getPrestaciones()));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvPrestacionesDialogo.setLayoutManager(mLayoutManager);
        rvPrestacionesDialogo.setItemAnimator(new DefaultItemAnimator());

        dialog.show();
        Point boundsScreen = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(boundsScreen);

        dialog.getWindow().setLayout((int) (boundsScreen.x * Constantes.PORCENTAJE_PANTALLA), WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void showSenMessageDialog(){
        final EditText txtMensaje;
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        View dialogView = View.inflate(getActivity(), R.layout.dialog_fragment_send_message, null);
        dialog.setView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle("Enviar mensaje");
        txtMensaje = (EditText) dialogView.findViewById(R.id.txtMensaje);
        dialogView.findViewById(R.id.imgEnviar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(txtMensaje.getText())) {
                    mListenerClick.onNewMessageClic(new MessagePojo(mCurrentUser, mAnuncio.getTitulo(), txtMensaje.getText().toString(), new Date()), mAnuncio.getAnunciante());
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onAttach(Context context) {
        mListener = (IDetallesAnuncioFragmentListener) context;
        mListenerClick = (OnDetallesAnuncioFragmentClic) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        mListener = null;
        mListenerClick = null;
        slider.stopAutoCycle();
        super.onDetach();
    }

    public void setmAnuncio(Anuncio anuncio) {
        mAnuncio = anuncio;
        bindData();
        posicionarMapa();

        mPrestacionesAdapter.replaceAll(anuncio.getPrestaciones());
        //Si cuando ha terminado de editar el anuncio tiene prestaciones, se mostrará el hueco de prestaciones
        //sino se ocultará

        if (anuncio.getPrestaciones().size() > 0){
            shapeComentario.setBackgroundColor(getResources().getColor(R.color.colorShape));
            rvPrestaciones.setVisibility(View.VISIBLE);
        }
        else{
            shapeComentario.setBackgroundColor(getResources().getColor(android.R.color.white));
            rvPrestaciones.setVisibility(View.GONE);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
