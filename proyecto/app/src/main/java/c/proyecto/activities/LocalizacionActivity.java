package c.proyecto.activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.adapters.GooglePlacesAutocompleteAdapter;

public class LocalizacionActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private static final String EXTRA_ANUNCIO = "ExtraAnuncio";
    public static final String EXTRA_ADDRESS = "ExtraAddress";
    private static final String EXTRA_SELECTOR_MODE = "ExtraEditable";
    public static final int RC_ADDRESS = 233;

    private TextView lblTitle;
    private AutoCompleteTextView txtDireccion;
    private Toolbar toolbar;
    private ImageView imgLocIcon, imgLimpiarTexto;
    private GoogleApiClient mGoogleApiClient;
    private GooglePlacesAutocompleteAdapter mAdapter;
    private GoogleMap mGoogleMap;
    private LatLng oldLat;
    private boolean mSelectorMode;


    public static void startForResult(Activity activity, LatLng latLng){
        Intent intent = new Intent(activity, LocalizacionActivity.class);
        intent.putExtra(EXTRA_ANUNCIO, latLng);
        activity.startActivityForResult(intent, RC_ADDRESS);
    }
    public static void startForResult(Activity activity){
        activity.startActivityForResult(new Intent(activity, LocalizacionActivity.class), RC_ADDRESS);
    }
    public static void start(Activity activity, LatLng latLng, boolean selectorMode){
        Intent intent = new Intent(activity, LocalizacionActivity.class);
        intent.putExtra(EXTRA_ANUNCIO, latLng);
        intent.putExtra(EXTRA_SELECTOR_MODE, selectorMode);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacion);
        oldLat = getIntent().getParcelableExtra(EXTRA_ANUNCIO);
        mSelectorMode = getIntent().getBooleanExtra(EXTRA_SELECTOR_MODE, true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lblTitle = (TextView) toolbar.findViewById(R.id.lblTitle);
        if(!mSelectorMode)
            lblTitle.setText("Localización");
        setSupportActionBar(toolbar);
        confMap();
        confAutoCompletado();

    }

    private void confMap() {
        imgLocIcon = (ImageView) findViewById(R.id.imgLocIcon);
        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment =  SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);
        fm.beginTransaction().replace(R.id.frmMap, mapFragment).commit();
    }

    private void confAutoCompletado() {
        txtDireccion = (AutoCompleteTextView) toolbar.findViewById(R.id.txtDireccion);
        imgLimpiarTexto = (ImageView) toolbar.findViewById(R.id.imgLimpiarTexto);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        mAdapter = new GooglePlacesAutocompleteAdapter(this, mGoogleApiClient, Constantes.BOUNDS_GREATER_SYDNEY, null);
        txtDireccion.setAdapter(mAdapter);
        //Click en los items del autocompletado.
        txtDireccion.setOnItemClickListener(mAutocompleteClickListener);

        txtDireccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty())
                    imgLimpiarTexto.setVisibility(View.VISIBLE);
                else
                    imgLimpiarTexto.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        imgLimpiarTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDireccion.setText("");
                imgLimpiarTexto.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);

        //Si se entra con un objeto que ya contiene una posición, se cargará esta.
        if(oldLat != null)
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(oldLat, Constantes.ZOOM_ANUNCIO_CON_LOCALIZACION));

        if(!mSelectorMode){
            mGoogleMap.addCircle(new CircleOptions().center(oldLat).radius(Constantes.CIRCLE_RADIUS).fillColor(Constantes.CIRCLE_COLOR).strokeWidth(Constantes.CIRCLE_STROKE_WIDTH));
            imgLocIcon.setVisibility(View.GONE);
            txtDireccion.setVisibility(View.GONE);
        }

        //Mueve la cámara al lugar pulsado por el usuario.
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });
    }



    //Click para los items del autocompletado
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


            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);


        }
    };
    //Respuesta
    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);
            LatLngBounds latBound = place.getViewport();

            //Si el tamaño del sitio no es conocido para Google Maps API se mostrará el punto sin hacer un zoom exacto
            if(latBound == null)
                latBound = LatLngBounds.builder().include(place.getLatLng()).build();

            //Mueve la cámara a la posición seleccionada haciendo el zoom escogido
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latBound, 10)); // 10 is padding
            hideKeyboard();
            places.release();
        }
    };



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_localizacion, menu);
        menu.findItem(R.id.nav_aceptar).setVisible(mSelectorMode);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_aceptar:
                confirmarCambios();
                break;
        }
        return true;
    }

    private void confirmarCambios() {
        Intent result = new Intent();
        LatLng lat = mGoogleMap.getCameraPosition().target;
        Address address = getAddress(lat.latitude, lat.longitude);

        if(address == null)
            Toast.makeText(this, "Esta localización no es válida", Toast.LENGTH_SHORT).show();
        else{
            //Se devuelve la dirección del lugar seleccionado para su introducción en el EditText pulsado anteriormente.
            result.putExtra(EXTRA_ADDRESS, address);
            setResult(RESULT_OK, result);
            finish();
        }

    }

    private Address getAddress(double latitude, double longitude){
        Geocoder geo = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        Address address = null;

        try {
            addresses = geo.getFromLocation(latitude, longitude, 1);
            if(addresses.size() > 0)
                address = addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null)
            view = new View(this);

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
