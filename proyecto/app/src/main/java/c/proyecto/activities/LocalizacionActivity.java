package c.proyecto.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.Locale;

import c.proyecto.R;
import c.proyecto.adapters.GooglePlacesAutocompleteAdapter;
import c.proyecto.pojo.Anuncio;

public class LocalizacionActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds( new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    private static final String EXTRA_ANUNCIO = "ExtraAnuncio";
    public static final String EXTRA_ADDRESS = "ExtraAddress";
    public static final int RC_ADDRESS = 233;
    private AutoCompleteTextView txtDireccion;
    private GoogleApiClient mGoogleApiClient;
    private GooglePlacesAutocompleteAdapter mAdapter;
    private GoogleMap mGoogleMap;
    private Anuncio mAnuncio;


    public static void startForResult(Activity activity, Anuncio anuncio){
        Intent intent = new Intent(activity, LocalizacionActivity.class);
        intent.putExtra(EXTRA_ANUNCIO, anuncio);
        activity.startActivityForResult(intent, RC_ADDRESS);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacion);
        mAnuncio = getIntent().getParcelableExtra(EXTRA_ANUNCIO);

        confMap();
        confAutoCompletado();
    }

    private void confMap() {
        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment =  SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);
        fm.beginTransaction().replace(R.id.frmMap, mapFragment).commit();
    }

    private void confAutoCompletado() {
        txtDireccion = (AutoCompleteTextView) findViewById(R.id.txtDireccion);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        mAdapter = new GooglePlacesAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY,null);
        txtDireccion.setAdapter(mAdapter);
        //Click en los items del autocompletado.
        txtDireccion.setOnItemClickListener(mAutocompleteClickListener);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        //Mueve la cámara al lugar pulsado por el usuario.
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);


            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            //Mueve la cámara a la posición seleccionada haciendo el zoom suficiente para que se vea toda la dirección entera
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(place.getViewport(), 10)); // 10 is padding

            places.release();
        }
    };



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_localizacion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.aceptar:
                confirmarCambios();
                break;
        }
        return true;
    }

    private void confirmarCambios() {
        Intent result = new Intent();
        LatLng lat = mGoogleMap.getCameraPosition().target;
        //Se guarda la latitud del punto seleccionado para la localización de la vivienda.
        mAnuncio.setLats(lat);

        //Se devuelve la dirección del lugar seleccionado para su introducción en el EditText pulsado anteriormente.
        result.putExtra(EXTRA_ADDRESS, getAddress(lat.latitude, lat.longitude));
        setResult(RESULT_OK, result);
        finish();
    }

    private Address getAddress(double latitude, double longitude){
        Geocoder geo = new Geocoder(this, Locale.getDefault());
        Address address = null;

        try {
            address = geo.getFromLocation(latitude, longitude, 1).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }
}
