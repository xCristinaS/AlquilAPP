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
    private CheckBox cbAscensor;
    private CheckBox cbCalefaccion;
    private CheckBox cbLavadora;
    private CheckBox cbParking;
    private CheckBox cbProhibidoFumar;
    private CheckBox cbSecadora;
    private CheckBox cbTren;
    private CheckBox cbTv;
    private CheckBox cbInternet;
    private TextView lblAireAcondicionado;
    private List<Prestacion> mPrestaciones;

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
        mPrestaciones = getArguments().getParcelableArrayList(ARG_PRESTACIONES);
        initViews(view);

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return super.onCreateDialog(savedInstanceState);
    }
    @Override
    public void setupDialog(Dialog dialog, int style) {
        dialog.setTitle("Prestaciones");
        super.setupDialog(dialog, style);
    }

    private void initViews(View view) {
        cbAptoMascotas = (CheckBox) view.findViewById(R.id.cbAptoMascotas);
        cbAireAcondicionado = (CheckBox) view.findViewById(R.id.cbAireAcondicionado);
        cbAscensor = (CheckBox) view.findViewById(R.id.cbAscensor);
        cbCalefaccion = (CheckBox) view.findViewById(R.id.cbCalefaccion);
        cbLavadora = (CheckBox) view.findViewById(R.id.cbLavadora);
        cbParking = (CheckBox) view.findViewById(R.id.cbParking);
        cbProhibidoFumar = (CheckBox) view.findViewById(R.id.cbProhibidoFumar);
        cbSecadora = (CheckBox) view.findViewById(R.id.cbSecadora);
        cbTren = (CheckBox) view.findViewById(R.id.cbTren);
        cbTv = (CheckBox) view.findViewById(R.id.cbTv);
        cbInternet = (CheckBox) view.findViewById(R.id.cbInternet);


        lblAireAcondicionado = (TextView) view.findViewById(R.id.lblAireAcondicionado);
        activarCheckBox();
    }

    private void activarCheckBox() {
        for(Prestacion p : mPrestaciones)
            switch (p.getIdDrawable()){
                case R.drawable.apto_mascotas:
                    cbAptoMascotas.setChecked(true);
                    break;
                case R.drawable.aire_acondicionado:
                    cbAireAcondicionado.setChecked(true);
                    break;
                case R.drawable.ascensor:
                    cbAscensor.setChecked(true);
                    break;
                case R.drawable.calefaccion:
                    cbCalefaccion.setChecked(true);
                    break;
                case R.drawable.lavadora:
                    cbLavadora.setChecked(true);
                    break;
                case R.drawable.parking:
                    cbParking.setChecked(true);
                    break;
                case R.drawable.prohibido_fumar:
                    cbProhibidoFumar.setChecked(true);
                    break;
                case R.drawable.secadora:
                    cbSecadora.setChecked(true);
                    break;
                case R.drawable.tren:
                    cbTren.setChecked(true);
                    break;
                case R.drawable.tv:
                    cbTv.setChecked(true);
                    break;
                case R.drawable.internet:
                    cbInternet.setChecked(true);
                    break;
            }
    }


}
