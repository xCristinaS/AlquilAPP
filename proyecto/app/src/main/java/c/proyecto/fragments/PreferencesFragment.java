package c.proyecto.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.pavelsikun.seekbarpreference.SeekBarPreference;

import java.util.ArrayList;
import java.util.List;

import c.proyecto.R;

public class PreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static int RATIO_CODE = 1;
    private List<Integer> mListaCambios;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
        mListaCambios = new ArrayList<>();
        initPreferences();
    }

    private void initPreferences() {
        SeekBarPreference seek = (SeekBarPreference) findPreference("prefRatio");
        seek.setCurrentValue(seek.getCurrentValue());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        actualizarSummary(findPreference(key));

        if(key.equals(getString(R.string.pref_ratio)))
            if (!mListaCambios.contains(RATIO_CODE))
                mListaCambios.add(RATIO_CODE);



    }

    private void actualizarSummary(Preference preference){

    }

    @Override
    public void onResume() {
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    public List<Integer> getmListaCambios() {
        return mListaCambios;
    }
}
