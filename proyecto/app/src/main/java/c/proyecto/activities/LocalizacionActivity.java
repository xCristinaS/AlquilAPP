package c.proyecto.activities;

import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
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

import c.proyecto.R;
import c.proyecto.adapters.GooglePlacesAutocompleteAdapter;

public class LocalizacionActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds( new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    private AutoCompleteTextView txtDireccion;
    private GoogleApiClient mGoogleApiClient;
    private GooglePlacesAutocompleteAdapter mAdapter;
    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacion);


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
}
