package c.proyecto.activities;

import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import c.proyecto.R;
import c.proyecto.fragments.PreferencesFragment;

public class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        PreferencesFragment frgPreference = new PreferencesFragment();
        getFragmentManager().beginTransaction().replace(R.id.frmContenido, frgPreference).commit();
    }
}
