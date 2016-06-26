package c.proyecto.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.pavelsikun.seekbarpreference.SeekBarPreference;

import java.util.ArrayList;
import java.util.List;

import c.proyecto.R;

public class PreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static int RATIO_CODE = 1, EDAD_CODE = 2;
    private List<Integer> mListaCambios;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
        mListaCambios = new ArrayList<>();
        initPreferences();
    }

    private void initPreferences() {
        SeekBarPreference seek = (SeekBarPreference) findPreference(getString(R.string.pref_ratio));
        seek.setCurrentValue(seek.getCurrentValue());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        actualizarSummary(findPreference(key));

        if(key.equals(getString(R.string.pref_ratio))) {
            if (!mListaCambios.contains(RATIO_CODE))
                mListaCambios.add(RATIO_CODE);

        }else if(key.equals(getString(R.string.pref_edad)))
            if(!mListaCambios.contains(EDAD_CODE))
                mListaCambios.add(EDAD_CODE);


    }

    private void actualizarSummary(Preference preference){

    }

    public List<Integer> getmListaCambios() {
        return mListaCambios;
    }

    @Override
    public void onResume() {
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onStop() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }
}
