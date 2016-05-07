package c.proyecto.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.pavelsikun.seekbarpreference.SeekBarPreference;

import c.proyecto.R;

public class PreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
        initPreferences();
    }

    private void initPreferences() {
        SeekBarPreference seek = (SeekBarPreference) findPreference("prefRatio");
        seek.setCurrentValue(seek.getCurrentValue());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        actualizarSummary(findPreference(key));
    }

    private void actualizarSummary(Preference preference){

    }

    @Override
    public void onResume() {
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }


}
