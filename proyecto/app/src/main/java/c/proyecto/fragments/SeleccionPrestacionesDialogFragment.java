package c.proyecto.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import c.proyecto.R;
import c.proyecto.pojo.Prestacion;


public class SeleccionPrestacionesDialogFragment extends AppCompatDialogFragment {

    private static final String ARG_PRESTACIONES = "prestaciones_selected";
    private CheckBox cbAptoMascotas;
    private CheckBox cbAireAcondicionado;
    private TextView lblAireAcondicionado;

    public static SeleccionPrestacionesDialogFragment newInstance(ArrayList<Prestacion> prestacionesSelected) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PRESTACIONES, prestacionesSelected);
        SeleccionPrestacionesDialogFragment fragment = new SeleccionPrestacionesDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.dialog_fragment_seleccion_prestaciones, container, false);
        initViews(view);

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return super.onCreateDialog(savedInstanceState);
    }

    private void initViews(View view) {
        cbAptoMascotas = (CheckBox) view.findViewById(R.id.cbAptoMascotas);
        cbAireAcondicionado = (CheckBox) view.findViewById(R.id.cbAireAcondicionado);
        lblAireAcondicionado = (TextView) view.findViewById(R.id.lblAireAcondicionado);
       // lblAireAcondicionado.setText(String.format("%-20s", lblAireAcondicionado.getText().toString()));
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        dialog.setTitle("Prestaciones");
        super.setupDialog(dialog, style);
    }
}
