package c.proyecto.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import c.proyecto.adapters.AdvertsRecyclerViewAdapter;
import c.proyecto.adapters.PrestacionesAdapter;
import c.proyecto.adapters.PrestacionesDetalladasAdapter;
import c.proyecto.fragments.DetallesAnuncioFragment;
import c.proyecto.mvp_models.AdvertsFirebaseManager;
import c.proyecto.mvp_models.MessagesFirebaseManager;
import c.proyecto.mvp_models.UsersFirebaseManager;
import c.proyecto.mvp_views_interfaces.AdvertsDetailsActivityOps;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.MessagePojoWithoutAnswer;
import c.proyecto.pojo.Usuario;
import c.proyecto.mvp_presenters.AdvertsDetailsPresenter;
import c.proyecto.pojo.MessagePojo;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetallesAnuncioActivity extends AppCompatActivity implements AdvertsDetailsActivityOps, DetallesAnuncioFragment.IDetallesAnuncioFragmentListener, DetallesAnuncioFragment.OnDetallesAnuncioFragmentClic, ViewPagerEx.OnPageChangeListener, PrestacionesAdapter.IPrestacionAdapter, OnMapReadyCallback {

    private static final String EXTRA_ANUNCIO = "anuncio";
    private static final String EXTRA_ADVERT_TYPE = "advert_type";
    private static final String EXTRA_USER = "user";
    private static final String EXTRA_OPEN_FROM_CHAT = "opem_from_chat";

    private AdvertsDetailsPresenter mPresenter;
    private Anuncio anuncio;
    private int advertType;
    private Usuario currentUser, userPropietario;
    private BroadcastReceiver receiverAnuncioEliminado, receiverAnuncioModificado;
    private MessagePojo messagePojoAux;

    private Toolbar toolbar;
    private RelativeLayout groupImagenes, groupProgressBar;
    private SliderLayout slider;
    private ImageView imgTipoVivienda, imgCamas, imgMessage, imgEdit, imgSubscribe;
    private LinearLayout emptyViewPrestaciones;

    private CircleImageView imgAvatar;
    private TextView lblNombre, lblPrecio, lblTamano, lblTipoVivienda, lblCamas, lblNumCamas, lblNumToilets, lblDescripcionNoDisponible, lblDescripcion;
    private RecyclerView rvPrestaciones;
    private PrestacionesAdapter mPrestacionesAdapter;
    private GoogleMap mGoogleMap;
    private boolean openFromChat;

    public static void start(Context context, Anuncio anuncio, int advertType, Usuario u, boolean openFromChat) {
        Intent intent = new Intent(context, DetallesAnuncioActivity.class);
        intent.putExtra(EXTRA_ANUNCIO, anuncio);
        intent.putExtra(EXTRA_ADVERT_TYPE, advertType);
        intent.putExtra(EXTRA_USER, u);
        intent.putExtra(EXTRA_OPEN_FROM_CHAT, openFromChat);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_anuncio);
        currentUser = getIntent().getParcelableExtra(EXTRA_USER);

        receiverAnuncioEliminado = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Anuncio a = intent.getParcelableExtra(MainActivity.EXTRA_ANUNCIO_ELIMINADO);
                if (anuncio.getKey().equals(a.getKey()))
                    showAdvertHasBeenRemovedDialog();
            }
        };

        receiverAnuncioModificado = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Anuncio a = intent.getParcelableExtra(MainActivity.EXTRA_ANUNCIO_ELIMINADO);
                if (anuncio.getKey().equals(a.getKey())) {
                    anuncio = a;
                    updateAdvert(anuncio);
                }
            }
        };

        anuncio = getIntent().getParcelableExtra(EXTRA_ANUNCIO);
        openFromChat = getIntent().getBooleanExtra(EXTRA_OPEN_FROM_CHAT, false);
        advertType = getIntent().getIntExtra(EXTRA_ADVERT_TYPE, -1);
        mPresenter = AdvertsDetailsPresenter.getPresentador(this);
        mPresenter.setAdvertsManager(new AdvertsFirebaseManager(mPresenter, currentUser));
        mPresenter.setMessagesManager(new MessagesFirebaseManager(mPresenter, currentUser));
        mPresenter.setUsersManager(new UsersFirebaseManager(mPresenter));
        mPresenter.getMessageIfConverExist(anuncio);

        configToolbar();
    }

    private void configToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void messageIfConverExistObtained(MessagePojo m) {
        messagePojoAux = m;
        if (advertType != AdvertsRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS)
            mPresenter.advertPublisherRequested(anuncio.getAnunciante());
        else {
            userPropietario = currentUser;
            initViews();
            confRecyclerview();
            confMap();
            bindData();
        }
    }

    @Override
    public void onAdvertPublisherRequestedResponsed(Usuario u) {
        userPropietario = u;
        initViews();
        confRecyclerview();
        confMap();
        bindData();
    }

    private void initViews() {
        emptyViewPrestaciones = (LinearLayout) findViewById(R.id.emptyViewPrestaciones);
        groupProgressBar = (RelativeLayout) findViewById(R.id.groupProgressBar);
        groupImagenes = (RelativeLayout) findViewById(R.id.groupImagenes);
        lblNombre = (TextView) findViewById(R.id.lblNombre);
        slider = (SliderLayout) findViewById(R.id.slider);
        imgAvatar = (CircleImageView) findViewById(R.id.imgAvatar);
        lblPrecio = (TextView) findViewById(R.id.lblPrecio);
        lblTamano = (TextView) findViewById(R.id.lblTamano);
        imgTipoVivienda = (ImageView) findViewById(R.id.imgTipoVivienda);
        lblTipoVivienda = (TextView) findViewById(R.id.lblTipoVivienda);
        imgCamas = (ImageView) findViewById(R.id.imgCamas);
        lblCamas = (TextView) findViewById(R.id.lblCamas);
        lblNumCamas = (TextView) findViewById(R.id.lblNumCamas);
        lblNumToilets = (TextView) findViewById(R.id.lblNumToilets);
        rvPrestaciones = (RecyclerView) findViewById(R.id.rvPrestaciones);
        lblDescripcionNoDisponible = (TextView) findViewById(R.id.lblDescripcionNoDisponible);
        lblDescripcion = (TextView) findViewById(R.id.lblDescripcion);

        imgMessage = (ImageView) findViewById(R.id.imgMessage);

        setImgMessageClickListener();

        imgEdit = (ImageView) findViewById(R.id.imgEdit);
        imgSubscribe = (ImageView) findViewById(R.id.imgSubscribe);

        switch (advertType) {
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
                onImgEditClicked(anuncio, currentUser);
            }
        });
        //Subscribe al usuario al anuncio
        imgSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (advertType == AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS) {
                    onImgSubClicked(anuncio);
                    //Al subscribirse, cambiará el icono a la imagen de desubscribirse y cambiará el advertType al de desubscribirse
                    imgSubscribe.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_unsubscribe));
                    advertType = AdvertsRecyclerViewAdapter.ADAPTER_TYPE_SUBS;
                } else {
                    onImgUnSubClicked(anuncio);
                    //Al subscribirse, cambiará el icono a la imagen de subscribirse y cambiará el advertType al de subcribirse.
                    imgSubscribe.setImageDrawable(getResources().getDrawable(R.drawable.ic_subscribe));
                    advertType = AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS;
                }
            }
        });

        //Muestra el usuario propietario del anuncio
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerPerfilActivity.start(DetallesAnuncioActivity.this, userPropietario);
            }
        });
        lblNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerPerfilActivity.start(DetallesAnuncioActivity.this, userPropietario);
            }
        });
    }

    private void setImgMessageClickListener() {
        if (!openFromChat) {
            imgMessage.setOnClickListener(null);
            if (messagePojoAux == null) {
                imgMessage.setImageResource(R.drawable.ic_message_black);
                imgMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSenMessageDialog();
                    }
                });
            } else {
                imgMessage.setImageResource(R.drawable.ic_chat_black);
                imgMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConversationActivity.start(DetallesAnuncioActivity.this, messagePojoAux, currentUser);
                    }
                });
            }
        } else
            imgMessage.setVisibility(View.GONE);
    }

    private void confRecyclerview() {
        rvPrestaciones.setHasFixedSize(true);
        mPrestacionesAdapter = new PrestacionesAdapter(anuncio.getPrestaciones(), DetallesAnuncioActivity.this);
        rvPrestaciones.setAdapter(mPrestacionesAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(DetallesAnuncioActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvPrestaciones.setLayoutManager(mLayoutManager);
        rvPrestaciones.setItemAnimator(new DefaultItemAnimator());
    }

    private void confSlider() {
        slider.setPresetTransformer(SliderLayout.Transformer.Stack);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.startAutoCycle(Constantes.DELAY_TIME, Constantes.DURATION, false);
        slider.getPagerIndicator().destroySelf();
        slider.addOnPageChangeListener(DetallesAnuncioActivity.this);
        slider.setWillNotCacheDrawing(true);

        DefaultSliderView defaultSliderView;
        LinkedList<String> lista = new LinkedList<>();
        //Ordena la lista de imagenes
        if (anuncio.getImagenes().size() > 0) {
            for (String key : anuncio.getImagenes().keySet())
                if (key.equals(Constantes.FOTO_PRINCIPAL)) // si la key es de la imagen principal, cargo la foto
                    lista.addFirst(anuncio.getImagenes().get(key));
                else
                    lista.add(anuncio.getImagenes().get(key));

            //Introduce imagenes en el slider.
            for (String url : lista) {
                defaultSliderView = new DefaultSliderView(DetallesAnuncioActivity.this);
                defaultSliderView.image(url).setScaleType(BaseSliderView.ScaleType.CenterCrop);
                slider.addSlider(defaultSliderView);
            }
        }

        //No permite que se pueda pasar de página si no hay mas de una imagen.
        if (lista.size() < 2)
            slider.setPagerTransformer(false, new BaseTransformer() {
                @Override
                protected void onTransform(View view, float v) {
                }
            });
    }

    private void confMap() {
        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(DetallesAnuncioActivity.this);
        fm.beginTransaction().replace(R.id.frmMap, mapFragment).commit();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        posicionarMapa();

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                LocalizacionActivity.start(DetallesAnuncioActivity.this, mGoogleMap.getCameraPosition().target, false);
            }
        });
    }

    private void posicionarMapa() {
        mGoogleMap.clear(); // No se le permite al usuario mover el mapa de ninguna forma
        mGoogleMap.getUiSettings().setAllGesturesEnabled(false);
        LatLng lat = new LatLng(anuncio.getLats().getLatitude(), anuncio.getLats().getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, Constantes.ZOOM_ANUNCIO_CON_LOCALIZACION));
        mGoogleMap.addCircle(new CircleOptions().center(lat).radius(Constantes.CIRCLE_RADIUS).fillColor(Constantes.CIRCLE_COLOR).strokeWidth(Constantes.CIRCLE_STROKE_WIDTH));
    }

    private void bindData() {
        String formatPrecio = "%.2f";

        //Se infla un slider cada vez que se actualiza las imagenes
        //Se coloca la View inflada a modo de FrameLayout en el RelativeLayout
        RelativeLayout r = (RelativeLayout) View.inflate(DetallesAnuncioActivity.this, R.layout.slider, null);
        slider = (SliderLayout) r.findViewById(R.id.slider);
        confSlider();

        groupImagenes.removeAllViews();
        groupImagenes.addView(r);

        ((TextView) toolbar.findViewById(R.id.lblTituloAnuncio)).setText(anuncio.getTitulo());
        ((TextView) toolbar.findViewById(R.id.lblLocalizacion)).setText(anuncio.getPoblacion());
        ((TextView) toolbar.findViewById(R.id.lblDireccion)).setText(anuncio.getDireccion() + " " + anuncio.getNumero());

        lblNombre.setText(userPropietario.getNombre());
        if (userPropietario.getFoto() != null)
            Picasso.with(DetallesAnuncioActivity.this).load(userPropietario.getFoto()).error(R.drawable.default_user).fit().centerCrop().into(imgAvatar);
        else
            Picasso.with(DetallesAnuncioActivity.this).load(R.drawable.default_user).fit().centerCrop().into(imgAvatar);

        if (anuncio.getPrestaciones().size() == 0)
            emptyViewPrestaciones.setVisibility(View.VISIBLE);
        else
            emptyViewPrestaciones.setVisibility(View.GONE);

        //Si el precio no tiene decimales, el número será mostrado sin 0  Ej: 10.00 -> 10
        if (anuncio.getPrecio() % 1 == 0)
            formatPrecio = "%.0f";
        lblPrecio.setText(String.format(formatPrecio + " %s/%s", anuncio.getPrecio(), Constantes.MONEDA, Constantes.MENSUAL));
        lblTamano.setText(anuncio.getTamanio() + Constantes.UNIDAD);

        switch (anuncio.getTipo_vivienda()) {
            case Constantes.HABITACION:
                imgTipoVivienda.setImageResource(R.drawable.tipo_habitacion);
                imgCamas.setImageResource(R.drawable.cama);
                lblCamas.setText(Constantes.CAMAS);
                break;
            case Constantes.CASA:
                imgTipoVivienda.setImageResource(R.drawable.tipo_casa);
                imgCamas.setImageResource(R.drawable.tipo_habitacion);
                lblCamas.setText(Constantes.HABITACION);
                break;
            case Constantes.PISO:
                imgTipoVivienda.setImageResource(R.drawable.tipo_piso);
                imgCamas.setImageResource(R.drawable.tipo_habitacion);
                lblCamas.setText(Constantes.HABITACION);
                break;
        }
        lblTipoVivienda.setText(anuncio.getTipo_vivienda());
        lblNumCamas.setText(String.valueOf(anuncio.getHabitaciones_o_camas()));
        lblNumToilets.setText(String.valueOf(anuncio.getNumero_banios()));

        if (anuncio.getDescripcion().isEmpty()) {
            lblDescripcionNoDisponible.setVisibility(View.VISIBLE);
            lblDescripcion.setText("");
        } else {
            lblDescripcionNoDisponible.setVisibility(View.GONE);
            lblDescripcion.setText(anuncio.getDescripcion());
        }
        //Evita que se pueda hacer scroll cuando no haya suficientes items
        if (anuncio.getPrestaciones().size() < 5)
            rvPrestaciones.setOverScrollMode(View.OVER_SCROLL_NEVER);
        else
            rvPrestaciones.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        //Se ha cargado todoo y desaparece la barra de carga.
        groupProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void onPrestacionClicked() {
        mostrarDialogoPrestaciones();
    }

    @Override
    public void onEmptyViewClicked() {

    }

    private void mostrarDialogoPrestaciones() {
        AlertDialog dialog = new AlertDialog.Builder(DetallesAnuncioActivity.this).create();
        View dialogView = View.inflate(DetallesAnuncioActivity.this, R.layout.dialog_prestaciones_detalladas, null);
        dialog.setView(dialogView);
        dialog.setCanceledOnTouchOutside(true);

        RecyclerView rvPrestacionesDialogo = (RecyclerView) dialogView.findViewById(R.id.rvPrestaciones);
        rvPrestacionesDialogo.setAdapter(new PrestacionesDetalladasAdapter(anuncio.getPrestaciones()));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(DetallesAnuncioActivity.this, LinearLayoutManager.VERTICAL, false);
        rvPrestacionesDialogo.setLayoutManager(mLayoutManager);
        rvPrestacionesDialogo.setItemAnimator(new DefaultItemAnimator());

        dialog.show();
        Point boundsScreen = new Point();
        DetallesAnuncioActivity.this.getWindowManager().getDefaultDisplay().getSize(boundsScreen);

        dialog.getWindow().setLayout((int) (boundsScreen.x * Constantes.PORCENTAJE_PANTALLA), WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void showSenMessageDialog() {
        final EditText txtMensaje;
        final AlertDialog dialog = new AlertDialog.Builder(DetallesAnuncioActivity.this).create();
        View dialogView = View.inflate(DetallesAnuncioActivity.this, R.layout.dialog_fragment_send_message, null);
        dialog.setView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle(getString(R.string.title_dialogEnviarMensaje));
        txtMensaje = (EditText) dialogView.findViewById(R.id.txtMensaje);
        dialogView.findViewById(R.id.imgEnviar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(txtMensaje.getText())) {
                    messagePojoAux = new MessagePojoWithoutAnswer();
                    ((MessagePojoWithoutAnswer) messagePojoAux).setReceptor(userPropietario);
                    messagePojoAux.setKeyReceptor(userPropietario.getKey());
                    messagePojoAux.setEmisor(currentUser);
                    messagePojoAux.setTituloAnuncio(anuncio.getTitulo());
                    messagePojoAux.setContenido(txtMensaje.getText().toString());
                    messagePojoAux.setFecha(new Date());
                    onNewMessageClic(messagePojoAux, anuncio.getAnunciante());
                    setImgMessageClickListener();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void showAdvertHasBeenRemovedDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogView = View.inflate(this, R.layout.dialog_removed_advert, null);
        dialog.setView(dialogView);
        dialog.setTitle(getString(R.string.title_AdvertRemovedDialog));
        dialogView.findViewById(R.id.btnAceptar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void onImgEditClicked(Anuncio advert, Usuario user) {
        CrearAnuncio1Activity.startForResult(this, advert, user, CrearAnuncio1Activity.RC_EDITAR_ANUNCIO);
    }

    @Override
    public void updateAdvert(Anuncio anuncio) {
        this.anuncio = anuncio;
        bindData();
        posicionarMapa();

        mPrestacionesAdapter.replaceAll(anuncio.getPrestaciones());

    }

    @Override
    public void onImgSubClicked(Anuncio a) {
        mPresenter.userNewSubRequested(a);
    }

    @Override
    public void onImgUnSubClicked(Anuncio a) {
        mPresenter.unSubRequested(a);
    }

    @Override
    public void onNewMessageClic(MessagePojo m, String keyReceptor) {
        mPresenter.sendNewMessage(m, keyReceptor);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CrearAnuncio1Activity.RC_EDITAR_ANUNCIO:
                    updateAdvert((Anuncio) data.getParcelableExtra(CrearAnuncio1Activity.EXTRA_ANUNCIO_RESULT));
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filtro = new IntentFilter(MainActivity.ACTION_ANUNCIO_ELIMINADO);
        registerReceiver(receiverAnuncioEliminado, filtro);
        IntentFilter filtro2 = new IntentFilter(MainActivity.ACTION_ANUNCIO_MODIFICADO);
        registerReceiver(receiverAnuncioModificado, filtro2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiverAnuncioEliminado);
        unregisterReceiver(receiverAnuncioModificado);
    }

    @Override
    protected void onDestroy() {
        if (slider != null) {
            for (int i = 0; i < anuncio.getImagenes().size(); i++)
                slider.removeSliderAt(i);
            slider.stopAutoCycle();
            slider.removeAllSliders();
            slider.removeOnPageChangeListener(this);
            slider.destroyDrawingCache();
            slider.removeAllViews();
            slider.removeAllViewsInLayout();
            slider = null;
            groupImagenes.removeAllViews();
            groupImagenes.removeAllViewsInLayout();
            groupImagenes = null;
        }
        System.gc();
        super.onDestroy();
    }
}


