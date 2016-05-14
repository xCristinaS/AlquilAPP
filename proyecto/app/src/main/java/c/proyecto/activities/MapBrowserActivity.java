package c.proyecto.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.adapters.AdvertsRecyclerViewAdapter;
import c.proyecto.mvp_models.AdvertsFirebaseManager;
import c.proyecto.mvp_presenters.MapBrowserPresenter;
import c.proyecto.mvp_views_interfaces.MapBrowserActivityOps;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Usuario;

public class MapBrowserActivity extends AppCompatActivity implements OnMapReadyCallback, MapBrowserActivityOps {


    private static final String EXTRA_USER_LOCATION = "userLocation";
    private static final String EXTRA_USER = "current_user";

    private Location mUserPosition;
    private GoogleMap mGoogleMap;
    private MapBrowserPresenter mPresenter;
    private Usuario mCurrentUser;
    private BroadcastReceiver receiver;
    private ArrayList<Marker> markersList;

    public static void start(Activity activity, Location userPosition, Usuario currentUser) {
        Intent intent = new Intent(activity, MapBrowserActivity.class);
        intent.putExtra(EXTRA_USER_LOCATION, userPosition);
        intent.putExtra(EXTRA_USER, currentUser);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_browser);
        markersList = new ArrayList<>();
        mUserPosition = getIntent().getParcelableExtra(EXTRA_USER_LOCATION);
        mCurrentUser = getIntent().getParcelableExtra(EXTRA_USER);
        mPresenter = MapBrowserPresenter.getPresentador(this);
        mPresenter.setAdvertsManager(new AdvertsFirebaseManager(mPresenter, mCurrentUser));
        confMap();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Anuncio a = intent.getParcelableExtra(MainActivity.EXTRA_ANUNCIO_ELIMINADO);
                for (Marker m: markersList)
                    if (m.getTitle().equals(a.getKey())) {
                        m.remove();
                        markersList.remove(m);
                    }
            }
        };
    }

    private void confMap() {
        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);
        fm.beginTransaction().replace(R.id.frmMap, mapFragment).commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng pos = new LatLng(mUserPosition.getLatitude(), mUserPosition.getLongitude());
        mGoogleMap = googleMap;
        mGoogleMap.addMarker(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_human_marker)));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, Constantes.ZOOM_ANUNCIO_CON_LOCALIZACION));
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        mPresenter.getLocations(new GeoLocation(pos.latitude, pos.longitude), 10);
        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mPresenter.getLocations(new GeoLocation(cameraPosition.target.latitude, cameraPosition.target.longitude), cameraPosition.zoom);
            }
        });
    }

    @Override
    public void advertLocationObtained(Anuncio a, GeoLocation location) {
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

        Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).icon(BitmapDescriptorFactory.fromResource(resource)));
        marker.setTitle(a.getKey());
        markersList.add(marker);
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTitle() != null) // si el titulo del marker es diferente de null, se ha hecho click sobre un anuncio y no sobre el marker de la posición del usuario. 
                    mPresenter.getAdvertClickedFromMap(marker.getTitle());
                return true;
            }
        });
    }

    @Override
    public void advertClickedFromMapObtained(Anuncio a) {
        DetallesAnuncioActivity.start(this, a, AdvertsRecyclerViewAdapter.ADAPTER_TYPE_ADVS, mCurrentUser);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filtro = new IntentFilter(MainActivity.ACTION_ANUNCIO_ELIMINADO);
        registerReceiver(receiver, filtro);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachGeolocationListener();
        super.onDestroy();
    }
}
