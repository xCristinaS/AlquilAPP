package c.proyecto.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.adapters.AdvertsRecyclerViewAdapter;
import c.proyecto.adapters.HuespedesAdapter;
import c.proyecto.adapters.MessagesRecyclerViewAdapter;
import c.proyecto.dialog_fragments.AboutUsDialogFragment;
import c.proyecto.dialog_fragments.FilterDialogFramgent;
import c.proyecto.dialog_fragments.SeleccionPrestacionesDialogFragment;
import c.proyecto.fragments.MessagesFragment;
import c.proyecto.fragments.PrincipalFragment;
import c.proyecto.mvp_models.AdvertsFirebaseManager;
import c.proyecto.mvp_models.MessagesFirebaseManager;
import c.proyecto.mvp_models.UsersFirebaseManager;
import c.proyecto.mvp_presenters.MainPresenter;
import c.proyecto.mvp_views_interfaces.MainActivityOps;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.MessagePojo;
import c.proyecto.pojo.Prestacion;
import c.proyecto.pojo.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements MainActivityOps, AdvertsRecyclerViewAdapter.OnAdapterItemLongClick, AdvertsRecyclerViewAdapter.OnAdapterItemClick, NavigationView.OnNavigationItemSelectedListener, MessagesRecyclerViewAdapter.OnMessagesAdapterItemClick, PrincipalFragment.AllowFilters, FilterDialogFramgent.ApplyFilters, SeleccionPrestacionesDialogFragment.ICallBackOnDismiss, AdvertsRecyclerViewAdapter.OnSubsIconClick, HuespedesAdapter.OnUserSubClick, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String ARG_USUARIO = "usuario_extra";
    private static final String TAG_PRINCIPAL_FRAGMENT = "principal_fragment";
    private static final String TAG_MESSAGES_FRAGMENT = "messages_fragment";
    private static final String TAG_FILTER_DIALOG_FRAMGENT = "filtros_dialog_fragment";
    private static final String TAG_ABOUT_US = "dialog_fragment_about_us";
    private static final int RC_REQUEST_LOCATION = 1000;

    private static MainPresenter mPresenter;
    private Usuario mUser;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private AdvertsRecyclerViewAdapter adapter;
    private CircleImageView imgNavDrawer;
    private TextView txtUserNavDrawer;
    private CircleImageView navHeader;
    private FragmentManager mFragmentManager;
    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationSettingsRequest.Builder mBuilder;
    private LocationRequest mLocationRequest;

    public static void start(Activity a, Usuario u) {
        Intent intent = new Intent(a, MainActivity.class);
        //Cierra todas las actividades anteriores.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ARG_USUARIO, u);
        a.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getIntent().hasExtra(ARG_USUARIO))
            mUser = getIntent().getParcelableExtra(ARG_USUARIO);
        initViews();
    }

    private void initViews() {
        mPresenter = MainPresenter.getPresentador(this);
        mPresenter.setAdvertsManager(new AdvertsFirebaseManager(mPresenter, mUser));
        mPresenter.setMessagesManager(new MessagesFirebaseManager(mPresenter, mUser));
        mPresenter.setUsersManager(new UsersFirebaseManager(mPresenter));

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.frmContenido, new PrincipalFragment(), TAG_PRINCIPAL_FRAGMENT).commit();
        configNavDrawer();
        configLocation();
    }

    private void configNavDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //Listener del menú del navigationDrawer
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        txtUserNavDrawer = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtUserNavDrawer);
        txtUserNavDrawer.setText(String.format("%s %s", mUser.getNombre(), mUser.getApellidos()));
        imgNavDrawer = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.imgNavDrawer);

        //Foto del usuario en el nav drawer.
        if (mUser.getFoto() != null)
            Picasso.with(this).load(mUser.getFoto()).fit().centerCrop().error(R.drawable.default_user).into(imgNavDrawer);
        else
            Picasso.with(this).load(R.drawable.default_user).fit().centerCrop().into(imgNavDrawer);

        navHeader = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.imgNavDrawer);
        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerPerfilActivity.start(MainActivity.this, mUser);
            }
        });
    }

    private void configLocation() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30000);
        mLocationRequest.setFastestInterval(5000);
        mBuilder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        mBuilder.setAlwaysShow(true);

        if (mGoogleApiClient == null)
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void userAdvertHasBeenModified(Usuario user) {
        mUser = user;
        txtUserNavDrawer.setText(String.format("%s %s", mUser.getNombre(), mUser.getApellidos()));
        if (mUser.getFoto() != null)
            Picasso.with(this).load(mUser.getFoto()).fit().centerCrop().error(R.drawable.default_user).into(imgNavDrawer);
    }

    @Override
    public void advertHasBeenObtained(Anuncio a) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).addAdvertToAdapter(a);
    }

    @Override
    public void adverHasBeenModified(Anuncio a) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).replaceAdvertFromAdapter(a);
    }

    @Override
    public void subHasBeenObtained(Anuncio a) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).addSubToAdapter(a);
    }

    @Override
    public void subHasBeenModified(Anuncio a) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).replaceSubFromAdapter(a);
    }

    @Override
    public void userAdvertHasBeenObtained(Anuncio a) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).addUserAdvertToAdapter(a);
    }

    @Override
    public void userAdvertHasBeenModified(Anuncio a) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).replaceUserAdvertFromAdapter(a);
    }

    @Override
    public void removeSub(Anuncio a) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).removeSub(a);
    }

    @Override
    public void removeAdvert(Anuncio a) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).removeAdvert(a);
    }

    @Override
    public void sendAdvertHasBeenRemovedBroadcast() {
        sendBroadcast(new Intent(DetallesAnuncioActivity.ACTION_CLOSE_ACTIVITY));
    }

    //MENU NAV DRAWER
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                //Si el principalFragment no está cargado en el FrameLayout
                if (!(mFragmentManager.findFragmentById(R.id.frmContenido) instanceof PrincipalFragment)) {
                    //Si ya existe se carga
                    if (mFragmentManager.findFragmentByTag(TAG_PRINCIPAL_FRAGMENT) != null)
                        mFragmentManager.beginTransaction().replace(R.id.frmContenido, mFragmentManager.findFragmentByTag(TAG_PRINCIPAL_FRAGMENT)).commit();
                    else
                        mFragmentManager.beginTransaction().replace(R.id.frmContenido, new PrincipalFragment()).commit();
                }

                break;
            case R.id.nav_new_adv:
                //Null = nuevo Anuncio.
                CrearAnuncio1Activity.start(this, mUser);
                break;
            case R.id.nav_edit_profile:
                EditProfileActivity.start(this, mUser);
                break;
            case R.id.nav_messages:
                mPresenter.requestUserMessages(mUser);
                getSupportFragmentManager().beginTransaction().replace(R.id.frmContenido, MessagesFragment.newInstance(false, null), TAG_MESSAGES_FRAGMENT).commit();
                break;
            case R.id.nav_preferences:
                startActivity(new Intent(this, PreferencesActivity.class));
                break;
            case R.id.nav_sign_off:
                InicioActivity.start(this);
                finish();
                break;
            case R.id.nav_about_us:
                new AboutUsDialogFragment().show(getSupportFragmentManager(), TAG_ABOUT_US);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void setAdapterAllowMultiDeletion(AdvertsRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        showFilterIcon();
        showMapIcon();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.limpiar:
                adapter.clearAllSelections();
                adapter.disableMultiDeletionMode();
                toolbar.getMenu().findItem(R.id.eliminar).setVisible(false);
                toolbar.getMenu().findItem(R.id.limpiar).setVisible(false);
                return true;
            case R.id.eliminar:
                adapter.removeSelections();
                adapter.clearAllSelections();
                adapter.disableMultiDeletionMode();
                toolbar.getMenu().findItem(R.id.eliminar).setVisible(false);
                toolbar.getMenu().findItem(R.id.limpiar).setVisible(false);
                return true;
            case R.id.map:
                confMap();
                return true;
            case R.id.filters:
                new FilterDialogFramgent().show(getSupportFragmentManager(), TAG_FILTER_DIALOG_FRAMGENT);
                return true;
            case R.id.deshacerFiltro:
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
                if (f instanceof PrincipalFragment)
                    ((PrincipalFragment) f).removeFilter();

                mPresenter.detachAdvertsListener();
                mPresenter.attachAdvertsListeners();
                toolbar.getMenu().findItem(R.id.deshacerFiltro).setVisible(false);
                getAnunciosCercanos();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getAnunciosCercanos() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null)
            mPresenter.getLocations(new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 10);
    }

    private void confMap() {
        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);
        fm.beginTransaction().replace(R.id.frmContenido, mapFragment).commit();
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        mGoogleMap = map;
        if (mLastLocation != null)
            posicionarMapa(mLastLocation);
    }

    private void posicionarMapa(Location l) {
        mGoogleMap.clear();
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        LatLng lat = new LatLng(l.getLatitude(), l.getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, Constantes.ZOOM_ANUNCIO_CON_LOCALIZACION));
        mGoogleMap.addMarker(new MarkerOptions().position(lat).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_human_marker)));
    }


    @Override
    public void locationObtained(final Anuncio a, GeoLocation location) {
        int resource = R.drawable.marker_house;
        switch (a.getTipo_vivienda()) {
            case Constantes.CASA:
                resource = R.drawable.marker_house;
                break;
            case Constantes.PISO:
                resource = R.drawable.marker_piso;
                break;
            case Constantes.HABITACION:
                resource = R.drawable.marker_habitacion;
                break;
        }

        if (mGoogleMap != null) {
            Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).icon(BitmapDescriptorFactory.fromResource(resource)));
            marker.setTitle(a.getKey());
            mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    mPresenter.getAdvertClickedFromMap(marker.getTitle());
                    return true;
                }
            });
        } else
            advertHasBeenObtained(a);
    }

    @Override
    public void advertClickedFromMapObtained(Anuncio a) {
        DetallesAnuncioActivity.start(MainActivity.this, a, AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS, mUser);
    }

    @Override
    public void showFilterIcon() {
        toolbar.getMenu().findItem(R.id.filters).setVisible(true);
    }

    @Override
    public void hideFilterIcon() {
        toolbar.getMenu().findItem(R.id.filters).setVisible(false);
    }

    @Override
    public void showMapIcon() {
        toolbar.getMenu().findItem(R.id.map).setVisible(true);
    }

    @Override
    public void hideMapIcon() {
        toolbar.getMenu().findItem(R.id.map).setVisible(false);
    }

    @Override
    public void onItemLongClick() {
        toolbar.getMenu().findItem(R.id.eliminar).setVisible(true);
        toolbar.getMenu().findItem(R.id.limpiar).setVisible(true);
    }

    @Override
    public void desactivarMultiseleccion() {
        toolbar.getMenu().findItem(R.id.eliminar).setVisible(false);
        toolbar.getMenu().findItem(R.id.limpiar).setVisible(false);
    }

    @Override
    public void onItemClick(Anuncio anuncio, int advertType) {
        DetallesAnuncioActivity.start(this, anuncio, advertType, mUser);
    }

    @Override
    public void onBackPressed() {
        //Si el navDrawer está abierto lo cierra
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawers();
        else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
            if (fragment instanceof MessagesFragment || fragment instanceof SupportMapFragment) {
                PrincipalFragment f = (PrincipalFragment) getSupportFragmentManager().findFragmentByTag(TAG_PRINCIPAL_FRAGMENT);
                if (f != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.frmContenido, f).commit();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.frmContenido, new PrincipalFragment(), TAG_PRINCIPAL_FRAGMENT).commit();

                if (fragment instanceof SupportMapFragment) {
                    mPresenter.detachGeoLocationListeners();
                    mGoogleMap = null;
                }
            } else
                super.onBackPressed();
        }
    }

    @Override
    public void userMessageHasBeenObtained(MessagePojo m) {
        if (getSupportFragmentManager().findFragmentById(R.id.frmContenido) instanceof MessagesFragment)
            ((MessagesFragment) getSupportFragmentManager().findFragmentById(R.id.frmContenido)).getmAdapter().addItem(m);
    }

    @Override
    public void filterRequest(String[] tipoVivienda, int minPrice, int maxPrice, int minSize, int maxSize, ArrayList<Prestacion> prestaciones, String provincia, String poblacion) {
        mPresenter.filterRequest(tipoVivienda, minPrice, maxPrice, minSize, maxSize, prestaciones, provincia, poblacion);
    }

    @Override
    public void filteredAdvertsObtained(ArrayList<Anuncio> filteredAdverts) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).loadFilteredAdverts(filteredAdverts);
        toolbar.getMenu().findItem(R.id.deshacerFiltro).setVisible(true);
    }

    @Override
    public void onSubsItemClick(View itemView, Anuncio anuncio) {
        mPresenter.getSolicitantes(itemView, anuncio);
    }

    @Override
    public void onUserSubClick(Usuario u, Anuncio anuncio) {
        VerPerfilActivity.start(this, u, anuncio, mUser);
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).closeSolicitantesDialog();
    }

    @Override
    public void solicitantesObtained(View itemView, ArrayList<Usuario> listaSolicitantes, Anuncio anuncio) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frmContenido);
        if (f instanceof PrincipalFragment)
            ((PrincipalFragment) f).solicitantesObtained(itemView, listaSolicitantes, anuncio);
    }

    @Override
    public void onItemClick(MessagePojo mensaje) {
        ConversationActivity.start(this, mensaje, mUser);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachListeners();
        super.onDestroy();
    }

    @Override
    public void onDismiss() {
        ((FilterDialogFramgent) getSupportFragmentManager().findFragmentByTag(TAG_FILTER_DIALOG_FRAMGENT)).updatePrestaciones();
    }

    public Usuario getmUser() {
        return mUser;
    }

    public static MainPresenter getmPresenter() {
        return mPresenter;
    }


    @Override
    public void onConnected(Bundle bundle) {
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mBuilder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location requests here.
                        getAnunciosCercanos();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, MainActivity.RC_REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_REQUEST_LOCATION:
                if (resultCode == RESULT_OK) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        return;
                    //Se espera a que haya conseguido la localización tras activar el gps, carga los anuncios y desvincula el listener.
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            mPresenter.getLocations(new GeoLocation(location.getLatitude(), location.getLongitude()), 10);
                            mLastLocation = location;
                            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                        }
                    });
                }
                else
                    Toast.makeText(this, "Necesita activar Ubicación para poder ver los anuncios cercanos a usted", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        if(mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        super.onStop();
    }
}
