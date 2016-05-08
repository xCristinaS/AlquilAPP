package c.proyecto.activities;

import android.content.Intent;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import c.proyecto.R;
import c.proyecto.fragments.PreferencesFragment;

public class PreferencesActivity extends AppCompatActivity {

    public static final String EXTRA_LIST_PREF_CODES = "prefCodes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        PreferencesFragment frgPreference = new PreferencesFragment();
        getFragmentManager().beginTransaction().replace(R.id.frmContenido, frgPreference).commit();
    }
    
    @Override
    public void onBackPressed() {
        ArrayList<Integer> list = (ArrayList<Integer>) ((PreferencesFragment) getFragmentManager().findFragmentById(R.id.frmContenido)).getmListaCambios();
        setResult(RESULT_OK, new Intent().putIntegerArrayListExtra(EXTRA_LIST_PREF_CODES ,list));
        super.onBackPressed();
    }
}
