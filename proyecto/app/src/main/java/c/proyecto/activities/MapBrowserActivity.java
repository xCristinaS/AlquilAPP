package c.proyecto.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import c.proyecto.R;

public class MapBrowserActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_browser);

        confMap();
    }

    private void confMap() {
        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment =  SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);
        fm.beginTransaction().replace(R.id.frmMap, mapFragment).commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
