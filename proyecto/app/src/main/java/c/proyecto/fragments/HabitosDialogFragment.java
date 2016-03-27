package c.proyecto.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import c.proyecto.R;


public class HabitosDialogFragment extends AppCompatDialogFragment {

    private static final String ARG_ORDENADO = "ordenado";
    private static final String ARG_FIESTERO = "fiestero";
    private static final String ARG_SOCIABLE = "sociable";
    private static final String ARG_ACTIVO = "activo";
    private SeekBar skOrdenado, skFiestero, skSociable, skActivo;

    public static HabitosDialogFragment newInstance(int ordenado, int fiestero, int sociable, int activo) {

        Bundle args = new Bundle();
        args.putInt(ARG_ORDENADO, ordenado);
        args.putInt(ARG_FIESTERO, fiestero);
        args.putInt(ARG_SOCIABLE, sociable);
        args.putInt(ARG_ACTIVO, activo);

        HabitosDialogFragment fragment = new HabitosDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.dialog_habitos, container, false);

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        skOrdenado = (SeekBar) view.findViewById(R.id.skOrdenado);
        skFiestero = (SeekBar) view.findViewById(R.id.skFiestero);
        skSociable = (SeekBar) view.findViewById(R.id.skSociable);
        skActivo = (SeekBar) view.findViewById(R.id.skActivo);
    }
}
